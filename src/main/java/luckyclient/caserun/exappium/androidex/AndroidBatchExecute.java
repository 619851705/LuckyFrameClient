package luckyclient.caserun.exappium.androidex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import luckyclient.caserun.exappium.AppiumInitialization;
import luckyclient.caserun.exinterface.TestControl;
import luckyclient.dblog.DbLink;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.planapi.entity.PublicCaseParams;
import luckyclient.planapi.entity.TestTaskexcute;
import luckyclient.testlinkapi.TestBuildApi;

/**
 * =================================================================
 * ����һ�������Ƶ��������������������κ�δ��������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ�������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 * 
 * @author�� seagull
 * 
 * @date 2018��1��26�� ����15:29:40
 * 
 */
public class AndroidBatchExecute {

	public static void batchCaseExecuteForTast(String projectname, String taskid, String batchcase) throws IOException {
		// ��¼��־�����ݿ�
		DbLink.exetype = 0;
		TestControl.TASKID = taskid;
		AndroidDriver<AndroidElement> ad = null;
		try {
			ad = AppiumInitialization.setAndroidAppium();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		LogOperation caselog = new LogOperation();
		TestBuildApi.getBuild(projectname);
		TestTaskexcute task = GetServerAPI.cgetTaskbyid(Integer.valueOf(taskid));
		List<PublicCaseParams> pcplist = GetServerAPI
				.cgetParamsByProjectid(task.getTestJob().getProjectid().toString());
		// ִ��ȫ���ǳɹ�״̬����
		if (batchcase.indexOf("ALLFAIL") > -1) {
			String casemore = caselog.unSucCaseUpdate(taskid);
			String[] temp = casemore.split("\\#", -1);
			for (int i = 0; i < temp.length; i++) {
				String testCaseExternalId = temp[i].substring(0, temp[i].indexOf("%"));
				ProjectCase testcase = GetServerAPI.cgetCaseBysign(testCaseExternalId);
				List<ProjectCasesteps> steps = GetServerAPI.getStepsbycaseid(testcase.getId());
				// ɾ���ɵ�����
				LogOperation.deleteCaseDetail(testCaseExternalId, taskid);
				// ɾ���ɵ���־
				LogOperation.deleteCaseLogDetail(testCaseExternalId, taskid);
				try {
					AndroidCaseExecution.caseExcution(testcase, steps, taskid, ad, caselog, pcplist);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					luckyclient.publicclass.LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
					e.printStackTrace();
				}
			}
		} else { // ����ִ������
			String[] temp = batchcase.split("\\#", -1);
			for (int i = 0; i < temp.length; i++) {
				String testCaseExternalId = temp[i].substring(0, temp[i].indexOf("%"));
				// int version =
				// Integer.parseInt(temp[i].substring(temp[i].indexOf("%")+1,temp[i].length()));
				ProjectCase testcase = GetServerAPI.cgetCaseBysign(testCaseExternalId);
				List<ProjectCasesteps> steps = GetServerAPI.getStepsbycaseid(testcase.getId());
				try {
					AndroidCaseExecution.caseExcution(testcase, steps, taskid, ad, caselog, pcplist);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					luckyclient.publicclass.LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
					e.printStackTrace();
				}
			}
		}
		ad.closeApp();
		ad.close();
	}

}