package luckyclient.caserun.exwebdriver.extestlink;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import luckyclient.caserun.exinterface.TestControl;
import luckyclient.caserun.exwebdriver.WebDriverInitialization;
import luckyclient.dblog.DbLink;
import luckyclient.dblog.LogOperation;
import luckyclient.testlinkapi.TestBuildApi;
import luckyclient.testlinkapi.TestCaseApi;

public class WebOneCaseExecuteTestLink{
	
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
		TestBuildApi.GetBuild(projectname);
		TestCase testcase = TestCaseApi.getTestCaseByExternalId(testCaseExternalId, version);
		luckyclient.publicclass.LogUtil.APP.info("��ʼִ����������"+testCaseExternalId+"��......");
		try {
			WebCaseExecutionTestLink.CaseExcution(projectname,testcase, taskid,wd,caselog);
			luckyclient.publicclass.LogUtil.APP.info("��ǰ��������"+testcase.getFullExternalId()+"��ִ�����......������һ��");
		} catch (InterruptedException e) {
			luckyclient.publicclass.LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
			e.printStackTrace();
		}
        //�ر������
        wd.quit();
	}

}
