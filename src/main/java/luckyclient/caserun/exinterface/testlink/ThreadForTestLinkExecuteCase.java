package luckyclient.caserun.exinterface.testlink;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import luckyclient.caserun.exinterface.TestControl;
import luckyclient.dblog.LogOperation;
import luckyclient.publicclass.DBOperation;
import luckyclient.publicclass.InvokeMethod;
import luckyclient.testlinkapi.InterfaceAnalyticTestLinkCase;
/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * �˲��Կ����Ҫ����testlink���ֲ��ܣ��������������Լ����������֣����κ����ʻ�ӭ��ϵ�������ۡ�
 * QQ:24163551 seagull1985
 * =================================================================
 * @ClassName: ThreadForExecuteCase 
 * @Description: �̳߳ط�ʽִ������
 * @author�� seagull
 * @date 2015��4��13�� ����9:29:40  
 * 
 */
public class ThreadForTestLinkExecuteCase extends Thread{
	private String projectname;
	private String caseid;
	private TestCase testcaseob;
	private String tastid;
	
	public ThreadForTestLinkExecuteCase(String projectname,String caseid,TestCase testcaseob,String tastid){
		this.projectname = projectname;
		this.caseid = caseid;
		this.testcaseob = testcaseob;
		this.tastid = tastid;
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
		System.out.println(caseid);
		caselog.AddCaseDetail(tastid, testcaseob.getFullExternalId(), testcaseob.getVersion().toString(), testcaseob.getName(), 4);       //���뿪ʼִ�е�����
	    for(int i=0;i<testcaseob.getSteps().size();i++){
	    	Map<String,String> casescript = InterfaceAnalyticTestLinkCase.AnalyticCaseStep(testcaseob, i+1,tastid);    //�������������еĽű�
	    	try{
		    	packagename = casescript.get("PackageName").toString();
		    	functionname = casescript.get("FunctionName").toString();
	    	}catch(Exception e){
	    		k=0;
	    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getFullExternalId()+"�����������Ƿ�����ʧ�ܣ����飡");
				caselog.CaseLogDetail(tastid, caseid, "�����������Ƿ�����ʧ�ܣ����飡","error",String.valueOf(i+1),"");
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
		    				caselog.CaseLogDetail(tastid, caseid, "�������һ�������������˳���2�����ϵı���Ŷ���Ҵ�����������","error",String.valueOf(i+1),"");
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
				    		caselog.CaseLogDetail(tastid, caseid, "����������"+packagename+" ��������"+functionname
				    				+" ��"+(j+1)+"��������"+ParameterValues,"info",String.valueOf(i+1),"");
				    		getParameterValues[j] = ParameterValues;
		    			}else{
		    				luckyclient.publicclass.LogUtil.APP.error("û���ҵ���Ҫ�ı���Ŷ�������°ɣ���һ�����������ǣ�"+uservariable+"����"
		    						+ "�������������ǣ�"+uservariable1+"�����������������ǣ�"+uservariable2);
		    				caselog.CaseLogDetail(tastid, caseid, "û���ҵ���Ҫ�ı���Ŷ�������°ɣ��ڶ����������ǣ�"+uservariable+"����"
		    						+ "�������������ǣ�"+uservariable1,"error",String.valueOf(i+1),"");
		    			}

		    		}else{
			    		String ParameterValues1 = casescript.get("FunctionParams"+(j+1));
				    	if(ParameterValues1.indexOf("&quot;")>-1 || ParameterValues1.indexOf("&#39;")>-1){         //ҳ��ת���ַ�ת��
				    		ParameterValues1 = ParameterValues1.replaceAll("&quot;", "\"");
				    		ParameterValues1 = ParameterValues1.replaceAll("&#39;", "\'");
				    	}
			    		luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getFullExternalId()+"����������"+packagename+" ��������"+functionname
			    				+" ��"+(j+1)+"��������"+ParameterValues1);
			    		caselog.CaseLogDetail(tastid, caseid,"����������"+packagename+" ��������"+functionname
			    				+" ��"+(j+1)+"��������"+ParameterValues1,"info",String.valueOf(i+1),"");
			    		getParameterValues[j] = ParameterValues1;
		    		}
		    	}
	    	}else{
	    		getParameterValues = null;
	    	}
	    	//���ö�̬������ִ�в�������
		    try{
		    	luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getFullExternalId()+"��ʼ���÷�����"+functionname+" .....");
		    	caselog.CaseLogDetail(tastid, caseid,"��ʼ���÷�����"+functionname+" .....","info",String.valueOf(i+1),"");
		    	if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("$=")>-1){                             //��Ԥ�ڽ��ǰ�����ַ��ж��Ƿ���Ҫ�ѽ���������
		    		String ExpectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
		    		String temptestnote = InvokeMethod.CallCase(packagename,functionname,getParameterValues);
		    		variable.put(ExpectedResultVariable, temptestnote);
		    	}else if(expectedresults.length()>2 && expectedresults.substring(0, 2).indexOf("%=")>-1){                     //��Ԥ�ڽ������Խ����ģ��ƥ��
			    	testnote = InvokeMethod.CallCase(packagename,functionname,getParameterValues);
			    	if(testnote.indexOf(expectedresults.substring(2))>-1){
			    		setresult = 0;
			    		luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getFullExternalId()+"ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���");
			    		caselog.CaseLogDetail(tastid, caseid,"ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���","info",String.valueOf(i+1),"");
			    	}else{
			    		setresult = 1;
			    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getFullExternalId()+"��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
			    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getFullExternalId()+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote);
			    		caselog.CaseLogDetail(tastid, caseid,"��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�"+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote,"error",String.valueOf(i+1),"");
			    		testnote = "������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
			    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
			    	}
		    	}else{                                                                                                                    //��Ԥ�ڽ������Խ������ȷƥ��
			    	testnote = InvokeMethod.CallCase(packagename,functionname,getParameterValues);
			    	if(expectedresults.equals(testnote)){
			    		setresult = 0;
			    		luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getFullExternalId()+"ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���");
			    		caselog.CaseLogDetail(tastid, caseid,"ִ�н���ǣ�"+testnote+"����Ԥ�ڽ��ƥ��ɹ���","info",String.valueOf(i+1),"");
			    	}else{
			    		setresult = 1;
			    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getFullExternalId()+"��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
			    		luckyclient.publicclass.LogUtil.APP.error("������"+testcaseob.getFullExternalId()+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote);
			    		caselog.CaseLogDetail(tastid, caseid,"��"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�"+"Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote,"error",String.valueOf(i+1),"");
			    		testnote = "������"+(i+1)+"��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����"+expectedresults+"      ���Խ����"+testnote;
			    		break;        //ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
			    	}
		    	}
		    	int waitsec = Integer.parseInt(casescript.get("StepWait").toString());   //��ȡ�����ȴ�ʱ��
		    	if(waitsec!=0){
		    		Thread.sleep(waitsec*1000);
		    	}
		    }catch(Exception e){
		    	luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getFullExternalId()+"���÷������̳�����������"+functionname+" �����¼��ű����������Լ�������");
		    	caselog.CaseLogDetail(tastid, caseid,"���÷������̳�����������"+functionname+" �����¼��ű����������Լ�������","error",String.valueOf(i+1),"");
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
		    	//luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getFullExternalId()+"��ʼ���ò�������ִ�н�� .....");
		    	//caselog.CaseLogDetail(tastid, caseid,"��ʼ���ò�������ִ�н�� .....","info","SETCASERESULT...");
		    	//TCResult = TestCaseApi.setTCResult(projectname,caseid, testnote, testcaseob.getVersion(),setresult);
		    	caselog.UpdateCaseDetail(tastid, caseid, setresult);
	     }else{
	    	 luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getFullExternalId()+"����ִ�н��Ϊ��������ο�������־��������������ԭ��.....");    //�����������ǵ��÷�������ȫ����������Ϊ����
	    	 caselog.CaseLogDetail(tastid, caseid,"����ִ�н��Ϊ��������ο�������־��������������ԭ��.....","error","SETCASERESULT...","");
		     //TCResult = TestCaseApi.setTCResult(projectname,caseid, testnote, testcaseob.getVersion(),2);
	    	 setresult = 2;
		     caselog.UpdateCaseDetail(tastid, caseid, setresult);
	     }
    	if(setresult==0){
    		luckyclient.publicclass.LogUtil.APP.info("������"+testcaseob.getFullExternalId()+"ִ�н���ɹ�......");
    		caselog.CaseLogDetail(tastid, caseid,"��������ִ��ȫ���ɹ�......","info","ending","");
    		luckyclient.publicclass.LogUtil.APP.info("******************************�ָ���*************************************");
    	}else if(setresult==1){
    		luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getFullExternalId()+"ִ�н��ʧ��......");
    		caselog.CaseLogDetail(tastid, caseid,"����ִ�н��ʧ��......","error","ending","");
    		luckyclient.publicclass.LogUtil.APP.info("******************************�ָ���*************************************");
    	}else{
    		luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getFullExternalId()+"ִ�н������......");
    		caselog.CaseLogDetail(tastid, caseid,"����ִ�н������......","error","ending","");
    		luckyclient.publicclass.LogUtil.APP.info("******************************�ָ���*************************************");
    	}
	    }catch(Exception e){
	    	luckyclient.publicclass.LogUtil.ERROR.error("������"+testcaseob.getFullExternalId()+"����ִ�н�����̳���......");
	    	caselog.CaseLogDetail(tastid, caseid,"����ִ�н�����̳���......","error","ending","");
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
