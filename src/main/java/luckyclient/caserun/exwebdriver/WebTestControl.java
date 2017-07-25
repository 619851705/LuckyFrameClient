package luckyclient.caserun.exwebdriver;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import luckyclient.caserun.exinterface.TestControl;
import luckyclient.caserun.exwebdriver.ex.WebCaseExecution;
import luckyclient.caserun.exwebdriver.extestlink.WebCaseExecutionTestLink;
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

public class WebTestControl{
	
	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * ����̨ģʽ���ȼƻ�ִ������ 
	 */
	
	public  static void ManualExecutionPlan(String planname){
		DbLink.exetype = 1;   //������־�����ݿ�
		String taskid = "888888";
		WebDriver wd = null;
		try {
			wd = WebDriverInitialization.setWebDriverForLocal();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogOperation caselog = new LogOperation(); // ��ʼ��д��������Լ���־ģ��
		List<ProjectCase> testCases=GetServerAPI.getCasesbyplanname(planname);
		luckyclient.publicclass.LogUtil.APP.info("��ǰ�ƻ��ж�ȡ�������� "+testCases.size()+" ��");
		int i=0;
		for(ProjectCase testcase:testCases){
			List<ProjectCasesteps> steps=GetServerAPI.getStepsbycaseid(testcase.getId());
			if(steps.size()==0){
				continue;
			}
			i++;
			luckyclient.publicclass.LogUtil.APP.info("��ʼִ�е�"+i+"����������"+testcase.getSign()+"��......");
			try {
				WebCaseExecution.CaseExcution(testcase,steps,taskid,wd,caselog);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				luckyclient.publicclass.LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
				e.printStackTrace();
			}
			luckyclient.publicclass.LogUtil.APP.info("��ǰ��������"+testcase.getSign()+"��ִ�����......������һ��");
		}
		luckyclient.publicclass.LogUtil.APP.info("��ǰ��Ŀ���Լƻ��е������Ѿ�ȫ��ִ�����...");
        //�ر������
        wd.quit();
	}
	
	public static void TaskExecutionPlan(String taskid,TestTaskexcute task) throws InterruptedException {
		DbLink.exetype = 0; // ��¼��־�����ݿ�
		TestControl.TASKID = taskid;
		String restartstatus = RestartServerInitialization.RestartServerRun(taskid);
		String buildstatus = BuildingInitialization.BuildingRun(taskid);
		String projectname=task.getTestJob().getPlanproj();
		task=GetServerAPI.cgetTaskbyid(Integer.valueOf(taskid));
		String jobname = task.getTestJob().getTaskName();
		int drivertype = LogOperation.Querydrivertype(taskid);
		// �ж��Ƿ�Ҫ�Զ�����TOMCAT
		if (restartstatus.indexOf("Status:true") > -1) {
			// �ж��Ƿ񹹽��Ƿ�ɹ�
			if (buildstatus.indexOf("Status:true") > -1) {
				WebDriver wd = null;
				try {
					wd = WebDriverInitialization.setWebDriverForTask(taskid,drivertype);
				} catch (IOException e1) {
					luckyclient.publicclass.LogUtil.APP.error("��ʼ��WebDriver����", e1);
					e1.printStackTrace();
				}
				LogOperation caselog = new LogOperation(); // ��ʼ��д��������Լ���־ģ��
				int[] tastcount=null;
				if(task.getTestJob().getProjecttype()==0){
					TestBuildApi.GetBuild(projectname);
					TestCase[] testCases = TestCaseApi.getplantestcase(projectname, taskid,"");
					luckyclient.publicclass.LogUtil.APP.info("��ǰ�ƻ��ж�ȡ�������� " + testCases.length + " ��");
					LogOperation.UpdateTastStatus(taskid,testCases.length);
					
					for (TestCase testcase : testCases) {
						if (testcase.getSteps().size() == 0) {
							continue;
						}
						luckyclient.publicclass.LogUtil.APP.info("��ʼִ����������" + testcase.getFullExternalId() + "��......");
						try {
							WebCaseExecutionTestLink.CaseExcution(projectname, testcase, taskid, wd, caselog);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							luckyclient.publicclass.LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
							e.printStackTrace();
						}
						luckyclient.publicclass.LogUtil.APP.info("��ǰ��������" + testcase.getFullExternalId() + "��ִ�����......������һ��");
					}
					tastcount = LogOperation.UpdateTastdetail(taskid, testCases.length);
				}else if(task.getTestJob().getProjecttype()==1){
					List<ProjectCase> cases=GetServerAPI.getCasesbyplanid(task.getTestJob().getPlanid());
					luckyclient.publicclass.LogUtil.APP.info("��ǰ�ƻ��ж�ȡ�������� " + cases.size() + " ��");
					LogOperation.UpdateTastStatus(taskid,cases.size());
					
					for (ProjectCase testcase : cases) {
						List<ProjectCasesteps> steps=GetServerAPI.getStepsbycaseid(testcase.getId());
						if (steps.size() == 0) {
							continue;
						}
						luckyclient.publicclass.LogUtil.APP.info("��ʼִ����������" + testcase.getSign() + "��......");
						try {
							WebCaseExecution.CaseExcution(testcase, steps, taskid, wd, caselog);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							luckyclient.publicclass.LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
							e.printStackTrace();
						}
						luckyclient.publicclass.LogUtil.APP.info("��ǰ��������" + testcase.getSign() + "��ִ�����......������һ��");
					}
					tastcount = LogOperation.UpdateTastdetail(taskid, cases.size());
				}
				String testtime = LogOperation.GetTestTime(taskid);
				luckyclient.publicclass.LogUtil.APP.info("��ǰ��Ŀ��" + projectname + "�����Լƻ��е������Ѿ�ȫ��ִ�����...");
				MailSendInitialization.SendMailInitialization(HtmlMail.HtmlSubjectFormat(jobname),
						HtmlMail.HtmlContentFormat(tastcount, taskid, buildstatus, restartstatus, testtime,jobname), taskid);
				// �ر������
				wd.quit();
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			PropertyConfigurator.configure(System.getProperty("user.dir")
					+ "\\log4j.conf");
			//ManualExecutionPlan("automation test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
