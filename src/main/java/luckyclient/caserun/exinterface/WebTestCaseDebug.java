package luckyclient.caserun.exinterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckyclient.caserun.exinterface.analyticsteps.InterfaceAnalyticCase;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.planapi.entity.PublicCaseParams;
import luckyclient.publicclass.ChangString;
import luckyclient.publicclass.InvokeMethod;
/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 * @ClassName: WebTestCaseDebug 
 * @Description: �ṩWeb�˵��Խӿ�
 * @author�� seagull
 * @date 2017��9��2�� ����9:29:40  
 * 
 */
public class WebTestCaseDebug{

	/**
	 * @param ִ����
	 * @param �������
	 * ������WEBҳ���ϵ�������ʱ�ṩ�Ľӿ�
	 */
	public static void oneCaseDebug(String sign,String executor){
		Map<String,String> variable = new HashMap<String,String>();
		String packagename =null;
		String functionname = null;
		String expectedresults = null;
		Integer setresult = null;
		Object[] getParameterValues = null;
    	String testnote = null;
		int k = 0;
		ProjectCase testcaseob = GetServerAPI.cgetCaseBysign(sign);
		List<PublicCaseParams> pcplist=GetServerAPI.cgetParamsByProjectid(String.valueOf(testcaseob.getProjectid()));
		// �ѹ����������뵽MAP��
		for (PublicCaseParams pcp : pcplist) {
			variable.put(pcp.getParamsname(), pcp.getParamsvalue());
		}
		List<ProjectCasesteps> steps=GetServerAPI.getStepsbycaseid(testcaseob.getId());
		    //����ѭ���������������в���
		    for(int i=0;i<steps.size();i++){		    	
		    	Map<String,String> casescript = InterfaceAnalyticCase.analyticCaseStep(testcaseob, steps.get(i),"888888",null);    
		    	try{
			    	packagename = casescript.get("PackageName").toString();
			    	packagename = ChangString.changparams(packagename, variable,"��·��");
			    	functionname = casescript.get("FunctionName").toString();
			    	functionname = ChangString.changparams(functionname, variable,"������");
		    	}catch(Exception e){
		    		k=0;
		    		GetServerAPI.cPostDebugLog(sign, executor, "ERROR", "�����������Ƿ�����ʧ�ܣ����飡");
		    		e.printStackTrace();
		    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
		    	}
		    	//�������ƽ��������쳣���ǵ���������������쳣
		    	if(functionname.indexOf("�����쳣")>-1||k==1){
		    		k=0;
		    		testnote = "������"+(i+1)+"��������������";
		    		break;
		    	}
		    	expectedresults = casescript.get("ExpectedResults").toString();    
		    	expectedresults = ChangString.changparams(expectedresults, variable,"Ԥ�ڽ��");
		    	//�жϷ����Ƿ������
		    	if(casescript.size()>4){
			    	//��ȡ������������������
			    	getParameterValues = new Object[casescript.size()-4];    
			    	for(int j=0;j<casescript.size()-4;j++){
			    		if(casescript.get("FunctionParams"+(j+1))==null){
			    			k = 1;
			    			break;
			    		}
			    		
						String parameterValues = casescript.get("FunctionParams" + (j + 1));
						parameterValues = ChangString.changparams(parameterValues, variable,"��������");
	    				GetServerAPI.cPostDebugLog(sign, executor, "INFO", "����������"+packagename+" ��������"+functionname
			    				+" ��"+(j+1)+"��������"+parameterValues);
						getParameterValues[j] = parameterValues;
			    	}
		    	}else{
		    		getParameterValues = null;
		    	}
		    	//���ö�̬������ִ�в�������
			    try{
    				GetServerAPI.cPostDebugLog(sign, executor, "INFO", "��ʼ���÷�����"+functionname+" .....");
			    	if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("$=")>-1){                             
			    		String expectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
			    		String temptestnote = InvokeMethod.callCase(packagename,functionname,getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
			    		variable.put(expectedResultVariable, temptestnote);
			    		GetServerAPI.cPostDebugLog(sign, executor, "INFO", "��ֵ������"+expectedresults.substring(2, expectedresults.length())+"���� "+temptestnote);
			    	}else if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("%=")>-1){                    
				    	testnote = InvokeMethod.callCase(packagename,functionname,getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
				    	if(testnote.indexOf(expectedresults.substring(2))>-1){
				    		setresult = 0;
				    		GetServerAPI.cPostDebugLog(sign, executor, "INFO", "����ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���");
				    	}else{
				    		setresult = 1;
				    		GetServerAPI.cPostDebugLog(sign, executor, "ERROR", "��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�"+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote);
				    		testnote = "������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
				    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
				    	}
			    	}else{                                                                                                                          //��Ԥ�ڽ������Խ������ȷƥ��
				    	testnote = InvokeMethod.callCase(packagename,functionname,getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
				    	if(expectedresults.equals(testnote)){
				    		setresult = 0;
				    		GetServerAPI.cPostDebugLog(sign, executor, "INFO", "����ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���");
				    	}else{
				    		setresult = 1;
				    		GetServerAPI.cPostDebugLog(sign, executor, "ERROR", "��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�"+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote);
				    		testnote = "������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
				    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
				    	}
			    	}
			    	int waitsec = Integer.parseInt(casescript.get("StepWait").toString());   
			    	if(waitsec!=0){
			    		Thread.sleep(waitsec*1000);
			    	}
			    }catch(Exception e){
			    	setresult = 1;
			    	GetServerAPI.cPostDebugLog(sign, executor, "ERROR", "���÷������̳�����������"+functionname+" �����¼��ű����������Լ�������");
					testnote = "CallCase���ó���";
					e.printStackTrace();
	    			break;
			    }			    
		    }
		    variable.clear();               //��մ���MAP
		    //������÷���������δ�����������ò��Խ������
		    if(testnote.indexOf("CallCase���ó���")<=-1&&testnote.indexOf("������������")<=-1){
		    	GetServerAPI.cPostDebugLog(sign, executor, "INFOover", "���� "+sign+"�����ɹ������ɹ����������з�����������鿴ִ�н����");
		     }else{
		    	 GetServerAPI.cPostDebugLog(sign, executor, "ERRORover", "���� "+sign+"�������ǵ��ò����еķ�������");
		     }
		    if(setresult == 0){
		    	GetServerAPI.cPostDebugLog(sign, executor, "INFOover", "���� "+sign+"����ȫ��ִ�гɹ���");
		    }else{
		    	GetServerAPI.cPostDebugLog(sign, executor, "ERRORover", "���� "+sign+"��ִ�й�����ʧ�ܣ����飡");
		    }
	}

	public static void main(String[] args) throws Exception {
	}
}
