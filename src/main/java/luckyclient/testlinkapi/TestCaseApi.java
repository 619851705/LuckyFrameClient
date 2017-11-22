package luckyclient.testlinkapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseStepAction;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import luckyclient.caserun.exinterface.TestControl;
import luckyclient.dblog.LogOperation;
import luckyclient.publicclass.remoterInterface.HttpRequest;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * �˲��Կ����Ҫ����testlink���ֲ��ܣ��������������Լ����������֣����κ����ʻ�ӭ��ϵ�������ۡ�
 * QQ:24163551 seagull1985
 * =================================================================
 * @ClassName: TestCaseApi 
 * @Description: ���η�װ���ڲ����������ֵ�API 
 * @author�� seagull
 * @date 2014��6��24�� ����9:29:40  
 * 
 */
public class TestCaseApi extends TestLinkBaseApi {

	/**
	 * ��������ִ�н��
	 * @param testCaseExternalId
	 * @param testSuiteName
	 * @param note
	 */

	public static String setTCResult (String projectname,String testCaseExternalId,String note,Integer version,Integer setresult){
/*
		  ExecutionStatus status = null;
		  ReportTCResultResponse result = null;
		try     {		    
			    //ͨ��������Ż�ȡ����ID
			    TestCase testcaseob = new TestCase();
			    testcaseob = api.getTestCaseByExternalId(testCaseExternalId, version);
			    Integer testcaseid = testcaseob.getId();

	    	    //�ܹ���Ŀ�����Լ��ƻ����ƻ�ȡ�ƻ�ID
	    	    TestPlan testplanob = new TestPlan();
	    	    //testplanob = api.getTestPlanByName(TestPlanName(projectname), projectname);
	    	    testplanob = api.getTestPlanByName(LogOperation.GetTestPlanName(TestControl.TASTID), projectname);
	    	    Integer planid = testplanob.getId();
	    	    
	    	    //ͨ�����Լƻ�ID��ȡ���һ�εĹ��������Լ�ID
	    	    Build lastbuild = new Build(); 
	    	    try{
	    	    	lastbuild = api.getLatestBuildForTestPlan(planid);
	    	    }catch( TestLinkAPIException te){
	    	    	JavaBaseTest.LogUtil.APP.info("���Լƻ� "+testplanob.getName()+" ��û���ҵ����԰汾���޷����ò��Խ�������ڽ���Ϊ�㴴��һ�����԰汾��");
	    	    	TestBuildApi.createBuild(projectname);
	    	    	lastbuild = api.getLatestBuildForTestPlan(planid);
	    	    }finally{
		    	    Integer buildId = lastbuild.getId();
		    	    String buildName = lastbuild.getName();
		    	    
		    	    //�Զ���������δ֪�ô�
		    	    Map<String, String> key = new HashMap<String, String>();
		    	    key.put("", "");
		    	    
		    	   //��ע�Լ��ӱ�ע�г�ʼ��ִ�н��
		    	    if(setresult == 0) {
		    	    	status = ExecutionStatus.PASSED;
		    	    }
		    	    else if(setresult == 1) {
		    	    	status = ExecutionStatus.FAILED;
		    	    }else{
		    	    	status = ExecutionStatus.BLOCKED;
		    	    }
		    	    
		    	    result = new ReportTCResultResponse();
		    	    
		    	    //��ȡִ�н��
		    	    result = api.setTestCaseExecutionResult(testcaseid, 1, planid, 
		    				status, buildId, buildName, note, true, "", 
		    				PLATFORMID, PLATFORMNAME, key, true);		    	    
	    	    }
	    	    return result.getMessage().toString();  
	    } catch( TestLinkAPIException te) {
	            te.printStackTrace( System.err );
	            JavaBaseTest.LogUtil.ERROR.error("��������ִ�н���������飡    ������ţ�"+testCaseExternalId+" ��ע��"+note+" �����汾�ţ�"+version);
	            JavaBaseTest.LogUtil.ERROR.error(te,te);
	            return "������"+testCaseExternalId+" ���ý����������ԭ��";
	    }*/
		return "ֱ�ӷ��سɹ�����ʱ������testlink�еĲ��Խ����Success!";
	} 
	
	/**
	 * ������������Լƻ���
	 * @param testCaseExternalID
	 * @param version
	 */
	
	public String addTCToTP(String projectname,String testCaseExternalID,Integer version){
        Object result = null;
		try{
		    //�ܹ���Ŀ�����Լ��ƻ����ƻ�ȡ�ƻ�ID
		    TestPlan testplanob = new TestPlan();
		    //testplanob = api.getTestPlanByName(TestPlanName(projectname), projectname);
		    testplanob = api.getTestPlanByName(LogOperation.GetTestPlanName(TestControl.TASKID), projectname);
		    Integer planid = testplanob.getId();
		    
		    Map<String, Object> params = new HashMap<String, Object>();			
		    params.put("devKey", TESTLINK_DEVKEY);
		    params.put("testprojectid", ProjectID(projectname));
		    params.put("testplanid", planid);
		    params.put("testcaseexternalid", testCaseExternalID);
		    params.put("version", version);
			
			try {
				result = api.executeXmlRpcCall("tl.addTestCaseToTestPlan", params);
			} catch (XmlRpcException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            	
		}catch( TestLinkAPIException te) {
            te.printStackTrace( System.err );
            System.exit(-1);
    }
		@SuppressWarnings("rawtypes")
		Map item = (Map)result;		
		return item.get("status").toString();
	}
	
	
	/**
	 * ��ȡ���������Ĳ���
	 * @param testCaseExternalID
	 * @param version
	 */
	public static List<TestCaseStep> getTestCaseSteps(String testCaseExternalId,Integer version){
	    TestCase testcaseob = new TestCase();
	    testcaseob = api.getTestCaseByExternalId(testCaseExternalId, version);
	   return testcaseob.getSteps();
	}
	
	/**
	 * �Ӽƻ���ȡ������
	 */
	public static TestCase[] getplantestcase(String projectname,String taskid,String testplan){
		TestCase[] testCases = null;
		try {
		//�ܹ���Ŀ�����Լ��ƻ����ƻ�ȡ�ƻ�ID
			Integer planid;
			TestPlan tp;

			if ("888888".equals(taskid)) {
				tp = api.getTestPlanByName(testplan, projectname);
			} else if (taskid.indexOf("NULL") > -1 || LogOperation.GetTestPlanName(taskid).indexOf("NULL") > -1) {
				tp = api.getTestPlanByName(TestPlanName(projectname), projectname);
			} else {
				tp = api.getTestPlanByName(LogOperation.GetTestPlanName(taskid), projectname);				
			}
			planid = tp.getId();
	    //������ϸȫ����Ϣ
//	    TestCaseDetails detail = TestCaseDetails.FULL;
	    
	    //��ȡ��ǰ���Լƻ��еĲ���ģ��   
	    testCases = api.getTestCasesForTestPlan(
	    		planid, null, null, null, null, null, null, null, 
				ExecutionType.AUTOMATED, true, TestCaseDetails.FULL);
	    
	    //ð�����򣬰�������ִ�����ȼ�������������Խ�����ȼ�Խ��
	    for(int i=0;i<testCases.length;i++)
	    {
	     for(int j=0;j<testCases.length-i-1;j++)
	     {
	      TestCase temp=null;
	      if(testCases[j].getExecutionOrder()>testCases[j+1].getExecutionOrder())
	      {
	       temp=testCases[j];
	       testCases[j]=testCases[j+1];
	       testCases[j+1]=temp;
	      }
	     }
	    }
	    
		if(testCases.length>0){
			luckyclient.publicclass.LogUtil.APP.info("��Ŀ��"+projectname+" ���Լƻ���"+tp.getName()+" ��ȡ�Զ�������������ɣ�");		   
		}else{
			luckyclient.publicclass.LogUtil.ERROR.error("��Ŀ��"+projectname+" ���Լƻ���"+tp.getName()+" û����Ӷ�Ӧ��ִ���������������������ִ�У�");
		}
		
		}catch( TestLinkAPIException te) {
            te.printStackTrace( System.err );
            luckyclient.publicclass.LogUtil.ERROR.error("��Ŀ��"+projectname+" ���Լƻ���"+TestPlanName(projectname)+" ��ȡ�Զ������������쳣��");
            System.exit(-1);
    }
		 return testCases;

	}
	
	public static TestCase getTestCaseByExternalId(String TestCaseExternalId,int version){
		return api.getTestCaseByExternalId(TestCaseExternalId, version);
	}
	
	/**
	 * ����testlink������ָ�������Ԥ�ڽ��
	 */
	public static String setTestLinkExpectedResults(String TestCaseExternalId, int version, int steps, String expectedResults) {
		String results = "���ý��ʧ��";
		try {
			TestCase tc = api.getTestCaseByExternalId(TestCaseExternalId, version);
			
			tc.getSteps().get(steps - 1).setExpectedResults(expectedResults);
			api.createTestCaseSteps(tc.getId(), TestCaseExternalId, tc.getVersion(), TestCaseStepAction.UPDATE, tc.getSteps());
			results = "���ý���ɹ�";
		} catch (TestLinkAPIException te) {
			te.printStackTrace(System.err);
			results = te.getMessage().toString();
			return results;
		}
		return results;

	}
	
	/**
	 * ����ϵͳ������ָ�������Ԥ�ڽ��
	 */
	public static String setExpectedResults(String TestCaseSign, int steps, String expectedResults) {
		String results = "���ý��ʧ��";
		String params="";
		try {
			expectedResults = expectedResults.replace("%", "BBFFHH");
			expectedResults = expectedResults.replace("=", "DHDHDH");
			expectedResults = expectedResults.replace("&", "ANDAND");
			params="caseno="+TestCaseSign;
			params+="&stepnum="+steps;
			params+="&expectedresults="+expectedResults;
			results=HttpRequest.sendPost("/projectCasesteps/cUpdateStepExpectedResults.do", params);
		} catch (TestLinkAPIException te) {
			te.printStackTrace(System.err);
			results = te.getMessage().toString();
			return results;
		}
		return results;

	}

	/**
	 * ��ȡָ�����������Լ������ű�����־�е�ִ�в��Խ��
	 * casestatus˵��  pass:0    fail:1   lock:2   unexcute:4
	 */
	public static String getLogdetail_Runresult(String taskname,String caseno,int casestatus){
		int taskid = LogOperation.gettaskexcute_taskid(taskname);
		return LogOperation.getlogdetail_testresult(taskid, caseno,casestatus);
	}
	
	/**
	 * ��������������ϵͳ�Դ���������   �ӿ��Զ�������
	 * @throws InterruptedException 
	 */
	public static void copyCaseToSysForInt(String projectname,String testplan,int projectid) throws InterruptedException{		
		TestCase[] testcases=getplantestcase(projectname,"888888",testplan);
		int casecount=1;
		for(TestCase cases:testcases){
			System.out.println("׼����ʼ����"+casecount+"������..."+cases.getFullExternalId());
			TestCase suitecase=api.getTestCaseByExternalId(cases.getFullExternalId(), cases.getVersion());
			List<Integer> suiteid=new ArrayList<Integer>();
			suiteid.add(suitecase.getTestSuiteId());
			TestSuite suite[]=api.getTestSuiteByID(suiteid);
			
			String params="";

			params="name="+cases.getName().replace("%", "BBFFHH");
			params+="&projectid="+projectid;
			params+="&modulename="+suite[0].getName();
			params+="&casetype=0";    //0 �ӿ�  1 UI
			
			String caseid=HttpRequest.sendPost("/projectCase/cpostcase.do", params);
			System.out.println("�Ѿ��ɹ�����������ID��"+caseid);
			Thread.sleep(500);
			int k=1;
			for(TestCaseStep step:cases.getSteps()){
				String stepsparams="";
				String resultstr = null;
				String stepsstr = step.getActions();    //��ȡactions�ַ���
				String scriptstr = InterfaceAnalyticTestLinkCase.SubComment(stepsstr);

				if(scriptstr.substring(scriptstr.length()-6, scriptstr.length()).indexOf("*Wait;")>-1){
					String action="";
					action = scriptstr.substring(scriptstr.lastIndexOf("|")+1,scriptstr.lastIndexOf("*Wait;")+5);
		        	stepsparams="action="+action+"&";
		        	scriptstr = scriptstr.substring(0, scriptstr.lastIndexOf("|")+1);
		        }
				if(scriptstr.substring(scriptstr.length()-6, scriptstr.length()).indexOf("*wait;")>-1){
					String action="";
					action = scriptstr.substring(scriptstr.lastIndexOf("|")+1,scriptstr.lastIndexOf("*wait;")+5);
		        	stepsparams="action="+action+"&";
		        	scriptstr = scriptstr.substring(0, scriptstr.lastIndexOf("|")+1);
		        }
				resultstr = InterfaceAnalyticTestLinkCase.SubComment(step.getExpectedResults());   //��ȡԤ�ڽ���ַ���
				stepsparams+="expectedresult="+resultstr.replace("%", "BBFFHH");
				stepsparams+="&caseid="+caseid;
				stepsparams+="&stepnum="+k;
				stepsparams+="&projectid="+projectid;
				stepsparams+="&steptype=0";
				String temp[]=scriptstr.split("\\|",-1);
				String param="";
				for(int i=0;i<temp.length;i++){
					if(i==0){
						String packagenage = temp[i].substring(0, temp[i].indexOf("#"));
						String functionname = temp[i].substring(temp[i].indexOf("#")+1, temp[i].indexOf(";"));
						stepsparams+="&path="+packagenage.trim();   //set����
						stepsparams+="&operation="+functionname.trim();   //set��������
					}else if(temp[i].equals("")){
						continue;
					}else{
						param+=temp[i]+"|";
					}
				}
				stepsparams+="&parameters="+param.replace("%", "BBFFHH");   //set��������
				String stepid=HttpRequest.sendPost("/projectCasesteps/cpoststep.do", stepsparams);
				System.out.println("�Ѿ��ɹ������������裬����ID:"+caseid+"  ����ID:"+stepid);
				k++;
				Thread.sleep(500);
			}
			System.out.println("��"+casecount+"������������ɣ�"+cases.getFullExternalId());
			casecount++;
		}
		System.out.println(testplan+"�е������Ѿ�ȫ��������ϣ�");
	}
	
	/**
	 * ��������������ϵͳ�Դ���������   UI�Զ�������
	 * @throws InterruptedException 
	 */
	public static void copyCaseToSysForUI(String projectname,String testplan,int projectid) throws InterruptedException{		
		TestCase[] testcases=getplantestcase(projectname,"888888",testplan);
		int casecount=1;
		for(TestCase cases:testcases){
			System.out.println("׼����ʼ����"+casecount+"������..."+cases.getFullExternalId());
			TestCase suitecase=api.getTestCaseByExternalId(cases.getFullExternalId(), cases.getVersion());
			List<Integer> suiteid=new ArrayList<Integer>();
			suiteid.add(suitecase.getTestSuiteId());
			TestSuite suite[]=api.getTestSuiteByID(suiteid);
			
			String params="";

			params="name="+cases.getName().replace("%", "BBFFHH");
			params+="&projectid="+projectid;
			params+="&modulename="+suite[0].getName();;
			params+="&casetype=1";    //0 �ӿ�  1 UI
			
			String caseid=HttpRequest.sendPost("/projectCase/cpostcase.do", params);
			System.out.println("�Ѿ��ɹ�����������ID��"+caseid);
			Thread.sleep(500);
			int k=1;
			for(TestCaseStep step:cases.getSteps()){
				String stepsparams="";
				String resultstr = null;
				String stepsstr = step.getActions();    //��ȡactions�ַ���
				String scriptstr = InterfaceAnalyticTestLinkCase.SubComment(stepsstr);

				if(scriptstr.substring(scriptstr.length()-6, scriptstr.length()).indexOf("*Wait;")>-1){
					String action="";
					action = scriptstr.substring(scriptstr.lastIndexOf("|")+1,scriptstr.lastIndexOf("*Wait;")+5);
		        	stepsparams="action="+action+"&";
		        	scriptstr = scriptstr.substring(0, scriptstr.lastIndexOf("|")+1);
		        }
				if(scriptstr.substring(scriptstr.length()-6, scriptstr.length()).indexOf("*wait;")>-1){
					String action="";
					action = scriptstr.substring(scriptstr.lastIndexOf("|")+1,scriptstr.lastIndexOf("*wait;")+5);
		        	stepsparams="action="+action+"&";
		        	scriptstr = scriptstr.substring(0, scriptstr.lastIndexOf("|")+1);
		        }
				resultstr = InterfaceAnalyticTestLinkCase.SubComment(step.getExpectedResults());   //��ȡԤ�ڽ���ַ���
				stepsparams+="expectedresult="+resultstr.replace("%", "BBFFHH");
				stepsparams+="&caseid="+caseid;
				stepsparams+="&stepnum="+k;
				stepsparams+="&projectid="+projectid;
				stepsparams+="&steptype=1";
				String temp[]=scriptstr.split("\\|",-1);
				for(int i=0;i<temp.length;i++){
					if(i==0&&temp[i].indexOf("=")>-1&&(temp.length>2||!"".equals(temp[1]))){
						stepsparams+="&path="+temp[i].replace("=", "DHDHDH");   //set����					
					}else if(temp[i].equals("")){
						continue;
					}else{
						String operation = null;
						String operation_value = null;
						if(temp[i].indexOf("(")>-1&&temp[i].indexOf(")")>-1){
							operation = temp[i].substring(0, temp[i].indexOf("("));
							operation_value = temp[i].substring(temp[i].indexOf("(")+1, temp[i].lastIndexOf(")"));
						}else{
							operation = temp[i];
						}
						stepsparams+="&operation="+operation.toLowerCase();   //set��������
						if(null!=operation_value){
							stepsparams+="&parameters="+operation_value.replace("%", "BBFFHH");   //set��������
						}						
					}
				}
				String stepid=HttpRequest.sendPost("/projectCasesteps/cpoststep.do", stepsparams);
				System.out.println("�Ѿ��ɹ������������裬����ID:"+caseid+"  ����ID:"+stepid);
				k++;
				Thread.sleep(500);
			}
			System.out.println("��"+casecount+"������������ɣ�"+cases.getFullExternalId());
			casecount++;
		}
		System.out.println(testplan+"�е������Ѿ�ȫ��������ϣ�");
	}
	
	/**
	 * ��ȡָ�����������Լ������ű�����־�е�ִ��Ԥ�ڽ��
	 * casestatus˵��  pass:0    fail:1   lock:2   unexcute:4
	 */
	public static String getLogdetail_Expectresult(String taskname,String caseno,int casestatus){
		int taskid = LogOperation.gettaskexcute_taskid(taskname);
		return LogOperation.getlogdetail_expectresult(taskid, caseno,casestatus);
	}
	
	public static void main(String[] args){

	}
}
