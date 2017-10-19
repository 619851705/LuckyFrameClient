package luckyclient.caserun.exinterface;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import luckyclient.caserun.exinterface.testlink.ThreadForTestLinkExecuteCase;
import luckyclient.dblog.DbLink;
import luckyclient.dblog.LogOperation;
import luckyclient.jenkinsapi.BuildingInitialization;
import luckyclient.jenkinsapi.RestartServerInitialization;
import luckyclient.mail.HtmlMail;
import luckyclient.mail.MailSendInitialization;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.planapi.entity.TestTaskexcute;
import luckyclient.testlinkapi.TestBuildApi;
import luckyclient.testlinkapi.TestCaseApi;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * �˲��Կ����Ҫ����testlink���ֲ��ܣ��������������Լ����������֣����κ����ʻ�ӭ��ϵ�������ۡ� QQ:24163551 seagull1985
 * =================================================================
 * 
 * @ClassName: TestControl
 * @Description: ����ɨ��ָ����Ŀ�������ű��������ýű��еķ��� @author�� ������
 * @date 2014��8��24�� ����9:29:40
 * 
 */
public class TestControl {
	public static String TASKID = "NULL";
	public static int Debugcount = 0;

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 *             ����̨ģʽ���ȼƻ�ִ��testlink����
	 */

	public static void ManualExecutionTestLinkPlan(String projectname, String testplan) throws Exception {
		DbLink.exetype = 1;
		int threadcount = 10;
		// �����̳߳أ����߳�ִ������
		ThreadPoolExecutor threadExecute = new ThreadPoolExecutor(threadcount, 20, 3, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

		TestBuildApi.GetBuild(projectname);
		TestCase[] testCases = TestCaseApi.getplantestcase(projectname, "NULL", testplan);
		String taskid = "888888";
		for (TestCase testcase : testCases) {
			if (testcase.getSteps().size() == 0) {
				continue;
			}
			Debugcount++; // ���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
			threadExecute
					.execute(new ThreadForTestLinkExecuteCase(projectname, testcase.getFullExternalId(), testcase, taskid));
			// new ThreadForExecuteCase(projectname,caseid,testcaseob).run();
		}
		// ���̼߳��������ڼ���߳��Ƿ�ȫ��ִ����
		int i = 0;
		while (Debugcount != 0) {
			i++;
			if (i > 600) {
				break;
			}
			Thread.sleep(6000);
		}
		luckyclient.publicclass.LogUtil.APP.info("�ף�û����һ�������ҷ�����������Ѿ�ȫ��ִ����ϣ���ȥ������û��ʧ�ܵ������ɣ�");
		threadExecute.shutdown();
	}

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 *             ����̨ģʽ���ȼƻ�ִ������
	 */

	public static void ManualExecutionPlan(String planname) throws Exception {
		DbLink.exetype = 1;
		int threadcount = 10;
		// �����̳߳أ����߳�ִ������
		ThreadPoolExecutor threadExecute = new ThreadPoolExecutor(threadcount, 20, 3, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

		List<ProjectCase> testCases = GetServerAPI.getCasesbyplanname(planname);
		String taskid = "888888";
		for (ProjectCase testcase : testCases) {
			List<ProjectCasesteps> steps = GetServerAPI.getStepsbycaseid(testcase.getId());
			if (steps.size() == 0) {
				continue;
			}
			Debugcount++; // ���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
			threadExecute
					.execute(new ThreadForExecuteCase(testcase, steps,taskid));
		}
		// ���̼߳��������ڼ���߳��Ƿ�ȫ��ִ����
		int i = 0;
		while (Debugcount != 0) {
			i++;
			if (i > 600) {
				break;
			}
			Thread.sleep(6000);
		}
		luckyclient.publicclass.LogUtil.APP.info("�ף�û����һ�������ҷ�����������Ѿ�ȫ��ִ����ϣ���ȥ������û��ʧ�ܵ������ɣ�");
		threadExecute.shutdown();
	}
	
	/**
	 * @param args
	 * @throws ClassNotFoundException
	 *             �ƻ�����ģʽ���ȼƻ�ִ������
	 */

	protected static void TastExecutionPlan(String taskid,TestTaskexcute task) throws Exception {
		DbLink.exetype = 0;
		TestControl.TASKID = taskid;
		String restartstatus = RestartServerInitialization.RestartServerRun(taskid);
		String buildstatus = BuildingInitialization.BuildingRun(taskid);
		String jobname = task.getTestJob().getTaskName();
		String projectname=task.getTestJob().getPlanproj();
		int timeout = task.getTestJob().getTimeout();
		// �ж��Ƿ�Ҫ�Զ�����TOMCAT
		if (restartstatus.indexOf("Status:true") > -1) {
			// �ж��Ƿ񹹽��Ƿ�ɹ�
			if (buildstatus.indexOf("Status:true") > -1) {
				int threadcount = task.getTestJob().getThreadCount();
				// �����̳߳أ����߳�ִ������
				ThreadPoolExecutor threadExecute = new ThreadPoolExecutor(threadcount, 20, 3, TimeUnit.SECONDS,
						new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
				
				int[] tastcount=null;
				if(task.getTestJob().getProjecttype()==1){
					TestBuildApi.GetBuild(projectname);
					TestCase[] testCases= TestCaseApi.getplantestcase(projectname, taskid, "");
					LogOperation.UpdateTastStatus(taskid, testCases.length);
					for (TestCase testcase : testCases) {
						if (testcase.getSteps().size() == 0) {
							continue;
						}
						Debugcount++; // ���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
						threadExecute.execute(
								new ThreadForTestLinkExecuteCase(projectname, testcase.getFullExternalId(), testcase, taskid));
					}
					// ���̼߳��������ڼ���߳��Ƿ�ȫ��ִ����
					int i = 0;
					while (Debugcount != 0) {
						i++;
						if (i > timeout * 10) {
							break;
						}
						Thread.sleep(6000);
					}
					tastcount = LogOperation.UpdateTastdetail(taskid, testCases.length);
					
				}else{
					 List<ProjectCase> cases=GetServerAPI.getCasesbyplanid(task.getTestJob().getPlanid());
					 LogOperation.UpdateTastStatus(taskid, cases.size());
						for (int j=0;j<cases.size();j++) {
							ProjectCase projectcase =cases.get(j);
							List<ProjectCasesteps> steps=GetServerAPI.getStepsbycaseid(projectcase.getId());
							if (steps.size()== 0) {
								continue;
							}
							Debugcount++; // ���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
							threadExecute.execute(
									new ThreadForExecuteCase(projectcase, steps,taskid));
						}
						// ���̼߳��������ڼ���߳��Ƿ�ȫ��ִ����
						int i = 0;
						while (Debugcount != 0) {
							i++;
							if (i > timeout * 10) {
								break;
							}
							Thread.sleep(6000);
						}
						tastcount = LogOperation.UpdateTastdetail(taskid, cases.size());
						
				}

				String testtime = LogOperation.GetTestTime(taskid);
				MailSendInitialization.SendMailInitialization(HtmlMail.HtmlSubjectFormat(jobname),
						HtmlMail.HtmlContentFormat(tastcount, taskid, buildstatus, restartstatus, testtime,jobname), taskid);
				threadExecute.shutdown();
				luckyclient.publicclass.LogUtil.APP.info("�ף�û����һ�������ҷ�����������Ѿ�ȫ��ִ����ϣ���ȥ������û��ʧ�ܵ������ɣ�");
			} else {
				luckyclient.publicclass.LogUtil.APP.error("��Ŀ����ʧ�ܣ��Զ��������Զ��˳�����ǰ��JENKINS�м����Ŀ���������");
				MailSendInitialization.SendMailInitialization(jobname,
						"������Ŀ������ʧ�ܣ��Զ��������Զ��˳�����ǰȥJENKINS�鿴���������", taskid);
			}
		} else {
			luckyclient.publicclass.LogUtil.APP.error("��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������");
			MailSendInitialization.SendMailInitialization(jobname,
					"��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������", taskid);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ManualExecutionPlan("test");
	}

}
