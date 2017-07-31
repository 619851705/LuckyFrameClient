package luckyclient.caserun.exinterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import luckyclient.caserun.exinterface.AnalyticSteps.InterfaceAnalyticCase;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.publicclass.DBOperation;
import luckyclient.publicclass.InvokeMethod;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * �˲��Կ����Ҫ����testlink���ֲ��ܣ��������������Լ����������֣����κ����ʻ�ӭ��ϵ�������ۡ�
 * QQ:24163551 seagull1985
 * =================================================================
 * @ClassName: ThreadForExecuteCase 
 * @Description: �̳߳ط�ʽִ������
 * @author�� seagull
 * @date 2017��7��13�� ����9:29:40  
 * 
 */
public class ThreadForExecuteCase extends Thread{
	private String caseid;
	private ProjectCase testcaseob;
	private String taskid;
	private List<ProjectCasesteps> steps;
	
	public ThreadForExecuteCase(ProjectCase projectcase,List<ProjectCasesteps> steps,String taskid){
		this.caseid = String.valueOf(projectcase.getId());
		this.testcaseob = projectcase;
		this.taskid = taskid;
		this.steps = steps;
	}
	
	public void run(){
		Map<String,String> variable = new HashMap<String,String>();
		LogOperation caselog = new LogOperation();        //��ʼ��д��������Լ���־ģ�� 
		String functionname = null;
		String packagename =null;
		String expectedresults = null;
		Integer setresult = null;
		Object[] getParameterValues = null;
    	String testnote = null;
		int k = 0;
		 //����ѭ�������������������в���
		System.out.println(testcaseob.getSign());
		caselog.AddCaseDetail(taskid, testcaseob.getSign(), "1", testcaseob.getName(), 4);       //���뿪ʼִ�е�����
	    for(int i=0;i<steps.size();i++){
	    	Map<String,String> casescript = InterfaceAnalyticCase.AnalyticCaseStep(testcaseob, steps.get(i),taskid);    //�������������еĽű�
	    	try{
		    	packagename = casescript.get("PackageName").toString();
		    	functionname = casescript.get("FunctionName").toString();
	    	}catch(Exception e){
	    		k=0;
	    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getSign()+"�����������Ƿ�����ʧ�ܣ����飡");
				caselog.CaseLogDetail(taskid, caseid, "�����������Ƿ�����ʧ�ܣ����飡","error",String.valueOf(i+1),"");
	    		e.printStackTrace();
	    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
	    	}
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
		    				caselog.CaseLogDetail(taskid, caseid, "�������һ�������������˳���2�����ϵı���Ŷ���Ҵ�����������","error",String.valueOf(i+1),"");
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
				    		caselog.CaseLogDetail(taskid, caseid, "����������"+packagename+" ��������"+functionname
				    				+" ��"+(j+1)+"��������"+ParameterValues,"info",String.valueOf(i+1),"");
				    		getParameterValues[j] = ParameterValues;
		    			}else{
		    				luckyclient.publicclass.LogUtil.APP.error("û���ҵ���Ҫ�ı���Ŷ�������°ɣ���һ�����������ǣ�"+uservariable+"����"
		    						+ "�������������ǣ�"+uservariable1+"�����������������ǣ�"+uservariable2);
		    				caselog.CaseLogDetail(taskid, caseid, "û���ҵ���Ҫ�ı���Ŷ�������°ɣ��ڶ����������ǣ�"+uservariable+"����"
		    						+ "�������������ǣ�"+uservariable1,"error",String.valueOf(i+1),"");
		    			}

		    		}else{
			    		String ParameterValues1 = casescript.get("FunctionParams"+(j+1));
				    	if(ParameterValues1.indexOf("&quot;")>-1 || ParameterValues1.indexOf("&#39;")>-1){         //ҳ��ת���ַ�ת��
				    		ParameterValues1 = ParameterValues1.replaceAll("&quot;", "\"");
				    		ParameterValues1 = ParameterValues1.replaceAll("&#39;", "\'");
				    	}
			    		luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getSign()+"����������"+packagename+" ��������"+functionname
			    				+" ��"+(j+1)+"��������"+ParameterValues1);
			    		caselog.CaseLogDetail(taskid, caseid,"����������"+packagename+" ��������"+functionname
			    				+" ��"+(j+1)+"��������"+ParameterValues1,"info",String.valueOf(i+1),"");
			    		getParameterValues[j] = ParameterValues1;
		    		}
		    	}
	    	}else{
	    		getParameterValues = null;
	    	}
	    	//���ö�̬������ִ�в�������
		    try{
		    	luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getSign()+"��ʼ���÷�����"+functionname+" .....");
		    	caselog.CaseLogDetail(taskid, caseid,"��ʼ���÷�����"+functionname+" .....","info",String.valueOf(i+1),"");
		    	if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("$=")>-1){                             //��Ԥ�ڽ��ǰ�����ַ��ж��Ƿ���Ҫ�ѽ���������
		    		String ExpectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
		    		String temptestnote = InvokeMethod.CallCase(packagename,functionname,getParameterValues);
		    		variable.put(ExpectedResultVariable, temptestnote);
		    	}else if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("%=")>-1){                     //��Ԥ�ڽ������Խ����ģ��ƥ��
			    	testnote = InvokeMethod.CallCase(packagename,functionname,getParameterValues);
			    	if(testnote.indexOf(expectedresults.substring(2))>-1){
			    		setresult = 0;
			    		luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getSign()+"ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���");
			    		caselog.CaseLogDetail(taskid, caseid,"ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���","info",String.valueOf(i+1),"");
			    	}else{
			    		setresult = 1;
			    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getSign()+"��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
			    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getSign()+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote);
			    		caselog.CaseLogDetail(taskid, caseid,"��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�"+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote,"error",String.valueOf(i+1),"");
			    		testnote = "������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
			    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
			    	}
		    	}else{                                                                                                                    //��Ԥ�ڽ������Խ������ȷƥ��
			    	testnote = InvokeMethod.CallCase(packagename,functionname,getParameterValues);
			    	if(expectedresults.equals(testnote)){
			    		setresult = 0;
			    		luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getSign()+"ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���");
			    		caselog.CaseLogDetail(taskid, caseid,"ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���","info",String.valueOf(i+1),"");
			    	}else{
			    		setresult = 1;
			    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getSign()+"��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
			    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getSign()+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote);
			    		caselog.CaseLogDetail(taskid, caseid,"��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�"+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote,"error",String.valueOf(i+1),"");
			    		testnote = "������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote;
			    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
			    	}
		    	}
		    	int waitsec = Integer.parseInt(casescript.get("StepWait").toString());   //��ȡ�����ȴ�ʱ��
		    	if(waitsec!=0){
		    		Thread.sleep(waitsec*1000);
		    	}
		    }catch(Exception e){
		    	luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getSign()+"���÷������̳�����������"+functionname+" �����¼��ű����������Լ�������");
		    	caselog.CaseLogDetail(taskid, caseid,"���÷������̳�����������"+functionname+" �����¼��ű����������Լ�������","error",String.valueOf(i+1),"");
				luckyclient.publicclass.LogUtil.ERROR.error(e,e);
				testnote = "CallCase���ó������÷������̳�����������"+functionname+" �����¼��ű����������Լ�������";
				setresult = 1;
				e.printStackTrace();
    			break;
		    }			    
	    }
	    //������÷���������δ�����������ò��Խ������
	    try{
	    if(testnote.indexOf("CallCase���ó���")<=-1&&testnote.indexOf("������������")<=-1){                //�ɹ���ʧ�ܵ������ߴ�����
		    	caselog.UpdateCaseDetail(taskid, caseid, setresult);
	     }else{
	    	 luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getSign()+"����ִ�н��Ϊ��������ο�������־��������������ԭ��.....");    //�����������ǵ��÷�������ȫ����������Ϊ����
	    	 caselog.CaseLogDetail(taskid, caseid,"����ִ�н��Ϊ��������ο�������־��������������ԭ��.....","error","SETCASERESULT...","");
	    	 setresult = 2;
		     caselog.UpdateCaseDetail(taskid, caseid, setresult);
	     }
    	if(setresult==0){
    		luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getSign()+"ִ�н���ɹ�......");
    		caselog.CaseLogDetail(taskid, caseid,"��������ִ��ȫ���ɹ�......","info","ending","");
    		luckyclient.publicclass.LogUtil.APP.info("******************************�ָ���*************************************");
    	}else if(setresult==1){
    		luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getSign()+"ִ�н��ʧ��......");
    		caselog.CaseLogDetail(taskid, caseid,"����ִ�н��ʧ��......","error","ending","");
    		luckyclient.publicclass.LogUtil.APP.info("******************************�ָ���*************************************");
    	}else{
    		luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getSign()+"ִ�н������......");
    		caselog.CaseLogDetail(taskid, caseid,"����ִ�н������......","error","ending","");
    		luckyclient.publicclass.LogUtil.APP.info("******************************�ָ���*************************************");
    	}
	    }catch(Exception e){
	    	luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getSign()+"����ִ�н�����̳���......");
	    	caselog.CaseLogDetail(taskid, caseid,"����ִ�н�����̳���......","error","ending","");
			luckyclient.publicclass.LogUtil.ERROR.error(e,e);
			e.printStackTrace();
	    }finally{
	    	variable.clear();                     //һ��������������ձ����洢�ռ�
	    	TestControl.Debugcount--;        //���̼߳���--�����ڼ���߳��Ƿ�ȫ��ִ����
	    }
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
