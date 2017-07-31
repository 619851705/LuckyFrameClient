package luckyclient.caserun.exinterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import luckyclient.caserun.exinterface.AnalyticSteps.InterfaceAnalyticCase;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.publicclass.DBOperation;
import luckyclient.publicclass.InvokeMethod;
import luckyclient.testlinkapi.InterfaceAnalyticTestLinkCase;
import luckyclient.testlinkapi.TestCaseApi;
/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * �˲��Կ����Ҫ����testlink���ֲ��ܣ��������������Լ����������֣����κ����ʻ�ӭ��ϵ�������ۡ�
 * QQ:24163551 seagull1985
 * =================================================================
 * @ClassName: TestCaseDebug 
 * @Description: ����Զ��������ڱ�д�����У��������ű����е���
 * @author�� seagull
 * @date 2014��8��24�� ����9:29:40  
 * 
 */
public class TestCaseDebug{

	/**
	 * @param ��Ŀ��
	 * @param �������
	 * @param �����汾��
	 * ������testlink�����ú�������������������������
	 */
	public static void OneCaseDebug(String projectname,String testCaseExternalId){
		Map<String,String> variable = new HashMap<String,String>();
		String packagename =null;
		String functionname = null;
		String expectedresults = null;
		Integer setresult = null;
		Object[] getParameterValues = null;
    	String testnote = null;
		int k = 0;
		ProjectCase testcaseob = GetServerAPI.cgetCaseBysign(testCaseExternalId);
		List<ProjectCasesteps> steps=GetServerAPI.getStepsbycaseid(testcaseob.getId());
		    //����ѭ���������������в���
		    for(int i=0;i<steps.size();i++){		    	
		    	Map<String,String> casescript = InterfaceAnalyticCase.AnalyticCaseStep(testcaseob, steps.get(i),"888888");    //�������������еĽű�
		    	packagename = casescript.get("PackageName").toString();
		    	functionname = casescript.get("FunctionName").toString();
		    	//�������ƽ��������쳣���ǵ���������������쳣
		    	if(functionname.indexOf("�����쳣")>-1||k==1){
		    		k=0;
		    		testnote = "������"+(i+1)+"��������������";
		    		break;
		    	}
		    	expectedresults = casescript.get("ExpectedResults").toString();    //Ԥ�ڽ��
		    	if(expectedresults.indexOf("&quot;")>-1||expectedresults.indexOf("&#39;")>-1){                             //ҳ��ת���ַ�ת��
		    		expectedresults = expectedresults.replaceAll("&quot;", "\"");
		    		expectedresults = expectedresults.replaceAll("&#39;", "\'");
		    	}
		    	//�жϷ����Ƿ������
		    	if(casescript.size()>4){
			    	//��ȡ������������������
			    	getParameterValues = new Object[casescript.size()-4];    //��ʼ�������������
			    	for(int j=0;j<casescript.size()-4;j++){		    		
			    		if(casescript.get("FunctionParams"+(j+1))==null){
			    			k = 1;
			    			break;
			    		}
			    		if(casescript.get("FunctionParams"+(j+1)).indexOf("@")>-1){                        //������ڴ��Σ����д���
			    			int keyexistidentity = 0;
			    			//ȡ�������������ñ�������
			    			int sumvariable = DBOperation.sumString(casescript.get("FunctionParams"+(j+1)), "@");     
			    			String uservariable = null;
			    			String uservariable1 = null;
			    			String uservariable2 = null;
			    			
			    			if(sumvariable==1){
			    				uservariable = casescript.get("FunctionParams"+(j+1)).substring(
				    					casescript.get("FunctionParams"+(j+1)).indexOf("@")+1);
			    			}else if(sumvariable==2){       //�������������õڶ�������
			    				uservariable = casescript.get("FunctionParams"+(j+1)).substring(casescript.get("FunctionParams"+(j+1)).indexOf("@")+1,
			    						casescript.get("FunctionParams"+(j+1)).lastIndexOf("@"));
			    				uservariable1 = casescript.get("FunctionParams"+(j+1)).substring(
				    					casescript.get("FunctionParams"+(j+1)).lastIndexOf("@")+1);
			    			}else if(sumvariable==3){
			    				String temp = casescript.get("FunctionParams"+(j+1)).substring(casescript.get("FunctionParams"+(j+1)).indexOf("@")+1,
			    						casescript.get("FunctionParams"+(j+1)).lastIndexOf("@"));
		    					uservariable1 = temp.substring(temp.indexOf("@")+1);
		    					uservariable2 = casescript.get("FunctionParams"+(j+1)).substring(
				    					casescript.get("FunctionParams"+(j+1)).lastIndexOf("@")+1);
		    					uservariable = casescript.get("FunctionParams"+(j+1)).substring(casescript.get("FunctionParams"+(j+1)).indexOf("@")+1,
				    					casescript.get("FunctionParams"+(j+1)).indexOf(uservariable1)-1);
		    				}else{
			    				luckyclient.publicclass.LogUtil.APP.error("�������һ�������������˳���3�����ϵı���Ŷ���Ҵ�����������");
			    			}
			    			Iterator keys = variable.keySet().iterator();
			    			String key = null;
			    			while(keys.hasNext()){
			    				key = (String)keys.next();
			    				if(uservariable.equals(key)){
			    					keyexistidentity = 1;
			    					uservariable = key;
						    		break;
			    				}
			    			}
			    			if(sumvariable==2||sumvariable==3){            //����ڶ�������
			    				keys = variable.keySet().iterator();
				    			while(keys.hasNext()){
				    				keyexistidentity = 0;
				    				key = (String)keys.next();
				    				if(uservariable1.equals(key)){
				    					keyexistidentity = 1;
				    					uservariable1 = key;
							    		break;
				    				}
				    			}
			    			}
			    			if(sumvariable==3){            //�������������
			    				keys = variable.keySet().iterator();
				    			while(keys.hasNext()){
				    				keyexistidentity = 0;
				    				key = (String)keys.next();
				    				if(uservariable2.equals(key)){
				    					keyexistidentity = 1;
				    					uservariable2 = key;
							    		break;
				    				}
				    			}
			    			}
			    			if(keyexistidentity == 1){
					    		//ƴװ����������+ԭ���ַ�����
					    		String ParameterValues =casescript.get("FunctionParams"+(j+1)).replaceAll("@"+uservariable, variable.get(uservariable).toString());
					    		//����ڶ�������
					    		if(sumvariable==2||sumvariable==3){
					    			ParameterValues = ParameterValues.replaceAll("@"+uservariable1, variable.get(uservariable1).toString());
					    		}
					    		//�������������
					    		if(sumvariable==3){
					    			ParameterValues = ParameterValues.replaceAll("@"+uservariable2, variable.get(uservariable2).toString());
					    		}
						    	if(ParameterValues.indexOf("&quot;")>-1 || ParameterValues.indexOf("&#39;")>-1){         //ҳ��ת���ַ�ת��
						    		ParameterValues = ParameterValues.replaceAll("&quot;", "\"");
						    		ParameterValues = ParameterValues.replaceAll("&#39;", "\'");
						    	}
					    		luckyclient.publicclass.LogUtil.APP.info("����������"+packagename+" ��������"+functionname
					    				+" ��"+(j+1)+"��������"+ParameterValues);
					    		getParameterValues[j] = ParameterValues;
			    			}else{
			    				luckyclient.publicclass.LogUtil.APP.error("û���ҵ���Ҫ�ı���Ŷ�������°ɣ���һ�����������ǣ�"+uservariable+"����"
			    						+ "�������������ǣ�"+uservariable1+"�����������������ǣ�"+uservariable2);
			    			}

			    		}else{
				    		String ParameterValues1 = casescript.get("FunctionParams"+(j+1));
					    	if(ParameterValues1.indexOf("&quot;")>-1 || ParameterValues1.indexOf("&#39;")>-1){         //ҳ��ת���ַ�ת��
					    		ParameterValues1 = ParameterValues1.replaceAll("&quot;", "\"");
					    		ParameterValues1 = ParameterValues1.replaceAll("&#39;", "\'");
					    	}
				    		luckyclient.publicclass.LogUtil.APP.info("����������"+packagename+" ��������"+functionname
				    				+" ��"+(j+1)+"��������"+ParameterValues1);
				    		getParameterValues[j] = ParameterValues1;
			    		}
			    	}
		    	}else{
		    		getParameterValues = null;
		    	}
		    	//���ö�̬������ִ�в�������
			    try{
			    	luckyclient.publicclass.LogUtil.APP.info("��ʼ���÷�����"+functionname+" .....");
			    	if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("$=")>-1){                             //��Ԥ�ڽ��ǰ�����ַ��ж��Ƿ���Ҫ�ѽ���������
			    		String ExpectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
			    		String temptestnote = InvokeMethod.CallCase(packagename,functionname,getParameterValues);
			    		variable.put(ExpectedResultVariable, temptestnote);
			    	}else if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("%=")>-1){                     //��Ԥ�ڽ������Խ����ģ��ƥ��
				    	testnote = InvokeMethod.CallCase(packagename,functionname,getParameterValues);
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
				    	testnote = InvokeMethod.CallCase(packagename,functionname,getParameterValues);
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
			    	int waitsec = Integer.parseInt(casescript.get("StepWait").toString());   //��ȡ�����ȴ�ʱ��
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
	 * @param ��Ŀ��
	 * @param �������
	 * @param �����汾��
	 * ������testlink�����ú������������������������е���
	 */
	public static void MoreCaseDebug(String projectname,Map<String,Integer> addtestcase){
		System.out.println(addtestcase.size());
		Iterator it=addtestcase.entrySet().iterator();
		while(it.hasNext()){
		    Map.Entry entry=(Map.Entry)it.next();
		    String testCaseExternalId = (String)entry.getKey();
		    Integer version = (Integer)entry.getValue();
		    try{
		    luckyclient.publicclass.LogUtil.APP.info("��ʼ���÷�������Ŀ����"+projectname+"��������ţ�"+testCaseExternalId+"�������汾��"+version); 
		    OneCaseDebug(projectname,testCaseExternalId);
		    }catch(Exception e){
		    	continue;
		    }
		}
	}

}
