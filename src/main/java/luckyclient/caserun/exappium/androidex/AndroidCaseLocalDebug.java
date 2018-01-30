package luckyclient.caserun.exappium.androidex;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import luckyclient.dblog.DbLink;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.planapi.entity.PublicCaseParams;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸� ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944 seagull1985
 * =================================================================
 * 
 * @author seagull
 * @date 2018��1��29��
 * 
 */
public class AndroidCaseLocalDebug {

	public static void oneCasedebug(AndroidDriver<AndroidElement> androiddriver, String testCaseExternalId) {
		// ����¼��־�����ݿ�
		DbLink.exetype = 1;
		LogOperation caselog = new LogOperation();

		try {
			ProjectCase testcase = GetServerAPI.cgetCaseBysign(testCaseExternalId);
			List<PublicCaseParams> pcplist = GetServerAPI
					.cgetParamsByProjectid(String.valueOf(testcase.getProjectid()));
			luckyclient.publicclass.LogUtil.APP.info("��ʼִ����������" + testCaseExternalId + "��......");
			List<ProjectCasesteps> steps = GetServerAPI.getStepsbycaseid(testcase.getId());
			AndroidCaseExecution.caseExcution(testcase, steps, "888888", androiddriver, caselog, pcplist);

			luckyclient.publicclass.LogUtil.APP.info("��ǰ��������" + testcase.getSign() + "��ִ�����......������һ��");
		} catch (Exception e) {
			luckyclient.publicclass.LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
			e.printStackTrace();
		}
		// �ر�APP�Լ�appium�Ự
		androiddriver.closeApp();
		androiddriver.close();
	}

	/**
	 * @param ��Ŀ��
	 * @param �������
	 * @param �����汾��
	 *            ������testlink�����ú������������������������е���
	 */
	public static void moreCaseDebug(AndroidDriver<AndroidElement> androiddriver, String projectname,
			Map<String, Integer> addtestcase) {
		System.out.println(addtestcase.size());
		@SuppressWarnings("rawtypes")
		Iterator it = addtestcase.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			String testCaseExternalId = (String) entry.getKey();
			Integer version = (Integer) entry.getValue();
			try {
				luckyclient.publicclass.LogUtil.APP
						.info("��ʼ���÷�������Ŀ����" + projectname + "��������ţ�" + testCaseExternalId + "�������汾��" + version);
				oneCasedebug(androiddriver, testCaseExternalId);
			} catch (Exception e) {
				continue;
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
