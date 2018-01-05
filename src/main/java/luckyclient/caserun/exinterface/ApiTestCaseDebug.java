package luckyclient.caserun.exinterface;

import java.util.HashMap;
import java.util.Iterator;
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
 * @ClassName: TestCaseDebug 
 * @Description: ����Զ��������ڱ�д�����У��������ű����е���
 * @author�� seagull
 * @date 2014��8��24�� ����9:29:40  
 * 
 */
public class ApiTestCaseDebug{

	/**
	 * �����ڱ�����������������
	 * @param projectname
	 * @param testCaseExternalId
	 */
	public static void oneCaseDebug(String projectname,String testCaseExternalId){
		Map<String,String> variable = new HashMap<String,String>(0);
		String packagename =null;
		String functionname = null;
		String expectedresults = null;
		Integer setresult = null;
		Object[] getParameterValues = null;
    	String testnote = null;
		int k = 0;
		ProjectCase testcaseob = GetServerAPI.cgetCaseBysign(testCaseExternalId);
		List<PublicCaseParams> pcplist=GetServerAPI.cgetParamsByProjectid(String.valueOf(testcaseob.getProjectid()));
		// �ѹ����������뵽MAP��
		for (PublicCaseParams pcp : pcplist) {
			variable.put(pcp.getParamsname(), pcp.getParamsvalue());
		}
		List<ProjectCasesteps> steps=GetServerAPI.getStepsbycaseid(testcaseob.getId());
		if(steps.size()==0){
			setresult=2;
			luckyclient.publicclass.LogUtil.APP.error("������δ�ҵ����裬���飡");
			testnote="������δ�ҵ����裬���飡";
		}
		    //����ѭ���������������в���
		    for(int i=0;i<steps.size();i++){		    	
		    	Map<String,String> casescript = InterfaceAnalyticCase.analyticCaseStep(testcaseob, steps.get(i),"888888",null); 
				try {
					packagename = casescript.get("PackageName").toString();
					packagename = ChangString.changparams(packagename, variable,"��·��");
					functionname = casescript.get("FunctionName").toString();
					functionname = ChangString.changparams(functionname, variable,"������");
				} catch (Exception e) {
					k = 0;
					luckyclient.publicclass.LogUtil.APP.error("������" + testcaseob.getSign() + "�����������Ƿ�����ʧ�ܣ����飡");
					e.printStackTrace();
					break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
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
			    	//��ȡ�����������������У���ʼ�������������
			    	getParameterValues = new Object[casescript.size()-4];    
			    	for(int j=0;j<casescript.size()-4;j++){		    		
			    		if(casescript.get("FunctionParams"+(j+1))==null){
			    			k = 1;
			    			break;
			    		}
						String parameterValues = casescript.get("FunctionParams" + (j + 1));
						parameterValues = ChangString.changparams(parameterValues, variable,"��������");
						luckyclient.publicclass.LogUtil.APP.info("������" + testcaseob.getSign() + "����������" + packagename
								+ " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues);
						getParameterValues[j] = parameterValues;
			    	}
		    	}else{
		    		getParameterValues = null;
		    	}
		    	//���ö�̬������ִ�в�������
			    try{
			    	luckyclient.publicclass.LogUtil.APP.info("��ʼ���÷�����"+functionname+" .....");
			    	//��Ԥ�ڽ��ǰ�����ַ��ж��Ƿ���Ҫ�ѽ���������
			    	if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("$=")>-1){                             
			    		String expectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
			    		String temptestnote = InvokeMethod.callCase(packagename,functionname,getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
			    		variable.put(expectedResultVariable, temptestnote);
			    		//��Ԥ�ڽ������Խ����ģ��ƥ��
			    	}else if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("%=")>-1){                     
				    	testnote = InvokeMethod.callCase(packagename,functionname,getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
				    	if(testnote.indexOf(expectedresults.substring(2))>-1){
				    		setresult = 0;
				    		luckyclient.publicclass.LogUtil.APP.info("����ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���");
				    	}else{
				    		setresult = 1;
				    		luckyclient.publicclass.LogUtil.APP.error("������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
				    		luckyclient.publicclass.LogUtil.APP.error("Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote);
				    		testnote = "������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
				    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
				    	}
			    	}else{                                                                                                                          //��Ԥ�ڽ������Խ������ȷƥ��
				    	testnote = InvokeMethod.callCase(packagename,functionname,getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
				    	if(expectedresults.equals(testnote)){
				    		setresult = 0;
				    		luckyclient.publicclass.LogUtil.APP.info("����ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���");
				    	}else{
				    		setresult = 1;
				    		luckyclient.publicclass.LogUtil.APP.error("������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
				    		luckyclient.publicclass.LogUtil.APP.error("Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote);
				    		testnote = "������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
				    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
				    	}
			    	}
			    	//��ȡ�����ȴ�ʱ��
			    	int waitsec = Integer.parseInt(casescript.get("StepWait").toString());   
			    	if(waitsec!=0){
			    		Thread.sleep(waitsec*1000);
			    	}
			    }catch(Exception e){
			    	setresult = 1;
			    	luckyclient.publicclass.LogUtil.APP.error("���÷������̳�����������"+functionname+" �����¼��ű����������Լ�������");
					luckyclient.publicclass.LogUtil.APP.error(e.getMessage(),e);
					testnote = "CallCase���ó���";
					e.printStackTrace();
	    			break;
			    }			    
		    }
		    variable.clear();               //��մ���MAP
		    //������÷���������δ�����������ò��Խ������
		    if(testnote.indexOf("CallCase���ó���")<=-1&&testnote.indexOf("������������")<=-1){
		    	luckyclient.publicclass.LogUtil.APP.info("���� "+testCaseExternalId+"�����ɹ������ɹ����������з�����������鿴ִ�н����");    	
		     }else{
		    	 luckyclient.publicclass.LogUtil.APP.error("���� "+testCaseExternalId+"�������ǵ��ò����еķ�������"); 
		     }
		    if(setresult == 0){
		    	luckyclient.publicclass.LogUtil.APP.info("���� "+testCaseExternalId+"����ȫ��ִ�гɹ���"); 
		    }else{
		    	luckyclient.publicclass.LogUtil.APP.error("���� "+testCaseExternalId+"��ִ�й�����ʧ�ܣ�������־��"); 
		    }
	}
	
	/**
	 * �����ڱ����������������е���
	 * @param projectname
	 * @param addtestcase
	 */
	public static void moreCaseDebug(String projectname,Map<String,Integer> addtestcase){
		System.out.println(addtestcase.size());
		@SuppressWarnings("rawtypes")
		Iterator it=addtestcase.entrySet().iterator();
		while(it.hasNext()){
		    @SuppressWarnings("rawtypes")
			Map.Entry entry=(Map.Entry)it.next();
		    String testCaseExternalId = (String)entry.getKey();
		    Integer version = (Integer)entry.getValue();
		    try{
		    luckyclient.publicclass.LogUtil.APP.info("��ʼ���÷�������Ŀ����"+projectname+"��������ţ�"+testCaseExternalId+"�������汾��"+version); 
		    oneCaseDebug(projectname,testCaseExternalId);
		    }catch(Exception e){
		    	continue;
		    }
		}
	}

	public static void main(String[] args) throws Exception {

	}
}
