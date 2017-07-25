package luckyclient.caserun.exwebdriver.ex;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebDriver;

import luckyclient.caserun.exinterface.TestControl;
import luckyclient.caserun.exwebdriver.WebDriverInitialization;
import luckyclient.dblog.DbLink;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;

public class WebOneCaseExecute{
	
	@SuppressWarnings("static-access")
	public static void OneCaseExecuteForTast(String projectname,String testCaseExternalId,int version,String taskid){
		DbLink.exetype = 0;   //��¼��־�����ݿ�
		TestControl.TASKID = taskid;
		int drivertype = LogOperation.Querydrivertype(taskid);
		WebDriver wd = null;
		try {
			wd = WebDriverInitialization.setWebDriverForTask(taskid,drivertype);
		} catch (IOException e1) {
			luckyclient.publicclass.LogUtil.APP.error("��ʼ��WebDriver����", e1);
			e1.printStackTrace();
		}
		LogOperation caselog = new LogOperation(); // ��ʼ��д��������Լ���־ģ��
		caselog.DeleteCaseDetail(testCaseExternalId, taskid);   //ɾ���ɵ�����
		caselog.DeleteCaseLogDetail(testCaseExternalId, taskid);    //ɾ���ɵ���־
		ProjectCase testcase = GetServerAPI.cgetCaseBysign(testCaseExternalId);
		luckyclient.publicclass.LogUtil.APP.info("��ʼִ����������"+testCaseExternalId+"��......");
		try {
			List<ProjectCasesteps> steps=GetServerAPI.getStepsbycaseid(testcase.getId());
			WebCaseExecution.CaseExcution(testcase, steps, taskid,wd,caselog);
			luckyclient.publicclass.LogUtil.APP.info("��ǰ��������"+testcase.getSign()+"��ִ�����......������һ��");
		} catch (InterruptedException e) {
			luckyclient.publicclass.LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
			e.printStackTrace();
		}
        //�ر������
        wd.quit();
	}

}
