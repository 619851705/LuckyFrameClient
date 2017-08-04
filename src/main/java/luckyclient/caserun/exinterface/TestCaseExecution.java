package luckyclient.caserun.exinterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import luckyclient.caserun.exinterface.TestControl;
import luckyclient.caserun.exinterface.AnalyticSteps.InterfaceAnalyticCase;
import luckyclient.dblog.DbLink;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.publicclass.DBOperation;
import luckyclient.publicclass.InvokeMethod;

public class TestCaseExecution {
	/**
	 * @param ��Ŀ��
	 * @param �������
	 * @param �����汾��
	 * ���ڵ����������ԣ���ͨ����־���д��־��UTP�ϣ�����UTP�ϵ�����������
	 */
	@SuppressWarnings("static-access")
	public static void OneCaseExecuteForTast(String projectname, String testCaseExternalId, int version,
			String taskid) {
		Map<String, String> variable = new HashMap<String, String>();
		TestControl.TASKID = taskid;
		DbLink.exetype = 0;
		LogOperation caselog = new LogOperation(); // ��ʼ��д��������Լ���־ģ��
		String packagename = null;
		String functionname = null;
		String expectedresults = null;
		Integer setresult = null;
		Object[] getParameterValues = null;
		String testnote = null;
		int k = 0;
		caselog.DeleteCaseLogDetail(testCaseExternalId, taskid); // ɾ���ɵ���־
		ProjectCase testcaseob = GetServerAPI.cgetCaseBysign(testCaseExternalId);
		List<ProjectCasesteps> steps = GetServerAPI.getStepsbycaseid(testcaseob.getId());
		// ����ѭ���������������в���
		for (int i = 0; i < steps.size(); i++) {
			Map<String, String> casescript = InterfaceAnalyticCase.AnalyticCaseStep(testcaseob, steps.get(i), taskid); // �������������еĽű�
			packagename = casescript.get("PackageName").toString();
			functionname = casescript.get("FunctionName").toString();
			// �������ƽ��������쳣���ǵ���������������쳣
			if (functionname.indexOf("�����쳣") > -1 || k == 1) {
				k = 0;
				testnote = "������" + (i + 1) + "��������������";
				break;
			}
			expectedresults = casescript.get("ExpectedResults").toString(); // Ԥ�ڽ��
			if (expectedresults.indexOf("&quot;") > -1 || expectedresults.indexOf("&#39;") > -1) { // ҳ��ת���ַ�ת��
				expectedresults = expectedresults.replaceAll("&quot;", "\"");
				expectedresults = expectedresults.replaceAll("&#39;", "\'");
			}
			// �жϷ����Ƿ������
			if (casescript.size() > 4) {
				// ��ȡ������������������
				getParameterValues = new Object[casescript.size() - 4]; // ��ʼ�������������
				for (int j = 0; j < casescript.size() - 4; j++) {
					if (casescript.get("FunctionParams" + (j + 1)) == null) {
						k = 1;
						break;
					}
					if (casescript.get("FunctionParams" + (j + 1)).indexOf("@") > -1
							&&casescript.get("FunctionParams"+(j+1)).indexOf("@@")<0) { // ������ڴ��Σ����д���
						int keyexistidentity = 0;
						// ȡ�������������ñ�������
						int sumvariable = DBOperation.sumString(casescript.get("FunctionParams" + (j + 1)), "@");
						String uservariable = null;
						String uservariable1 = null;
						String uservariable2 = null;

						if (sumvariable == 1) {
							uservariable = casescript.get("FunctionParams" + (j + 1))
									.substring(casescript.get("FunctionParams" + (j + 1)).indexOf("@") + 1);
						} else if (sumvariable == 2) { // �������������õڶ�������
							uservariable = casescript.get("FunctionParams" + (j + 1)).substring(
									casescript.get("FunctionParams" + (j + 1)).indexOf("@") + 1,
									casescript.get("FunctionParams" + (j + 1)).lastIndexOf("@"));
							uservariable1 = casescript.get("FunctionParams" + (j + 1))
									.substring(casescript.get("FunctionParams" + (j + 1)).lastIndexOf("@") + 1);
						} else if (sumvariable == 3) {
							String temp = casescript.get("FunctionParams" + (j + 1)).substring(
									casescript.get("FunctionParams" + (j + 1)).indexOf("@") + 1,
									casescript.get("FunctionParams" + (j + 1)).lastIndexOf("@"));
							uservariable1 = temp.substring(temp.indexOf("@") + 1);
							uservariable2 = casescript.get("FunctionParams" + (j + 1))
									.substring(casescript.get("FunctionParams" + (j + 1)).lastIndexOf("@") + 1);
							uservariable = casescript.get("FunctionParams" + (j + 1)).substring(
									casescript.get("FunctionParams" + (j + 1)).indexOf("@") + 1,
									casescript.get("FunctionParams" + (j + 1)).indexOf(uservariable1) - 1);
						} else {
							luckyclient.publicclass.LogUtil.APP.error("�������һ�������������˳���3�����ϵı���Ŷ���Ҵ�����������");
							caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "�������һ�������������˳���2�����ϵı���Ŷ���Ҵ�����������",
									"error", String.valueOf(i + 1));
						}
						Iterator keys = variable.keySet().iterator();
						String key = null;
						while (keys.hasNext()) {
							key = (String) keys.next();
							if (uservariable.indexOf(key)>-1) {
								keyexistidentity = 1;
								uservariable = key;
								break;
							}
						}
						if (sumvariable == 2 || sumvariable == 3) { // ����ڶ�������
							keys = variable.keySet().iterator();
							while (keys.hasNext()) {
								keyexistidentity = 0;
								key = (String) keys.next();
								if (uservariable.indexOf(key)>-1) {
									keyexistidentity = 1;
									uservariable1 = key;
									break;
								}
							}
						}
						if (sumvariable == 3) { // �������������
							keys = variable.keySet().iterator();
							while (keys.hasNext()) {
								keyexistidentity = 0;
								key = (String) keys.next();
								if (uservariable.indexOf(key)>-1) {
									keyexistidentity = 1;
									uservariable2 = key;
									break;
								}
							}
						}
						if (keyexistidentity == 1) {
							// ƴװ����������+ԭ���ַ�����
							String ParameterValues = casescript.get("FunctionParams" + (j + 1))
									.replaceAll("@" + uservariable, variable.get(uservariable).toString());
							// ����ڶ�������
							if (sumvariable == 2 || sumvariable == 3) {
								ParameterValues = ParameterValues.replaceAll("@" + uservariable1,
										variable.get(uservariable1).toString());
							}
							// �������������
							if (sumvariable == 3) {
								ParameterValues = ParameterValues.replaceAll("@" + uservariable2,
										variable.get(uservariable2).toString());
							}
							if (ParameterValues.indexOf("&quot;") > -1 || ParameterValues.indexOf("&#39;") > -1) { // ҳ��ת���ַ�ת��
								ParameterValues = ParameterValues.replaceAll("&quot;", "\"");
								ParameterValues = ParameterValues.replaceAll("&#39;", "\'");
							}
							luckyclient.publicclass.LogUtil.APP.info("����������" + packagename + " ��������" + functionname
									+ " ��" + (j + 1) + "��������" + ParameterValues);
							caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "����������" + packagename + " ��������"
									+ functionname + " ��" + (j + 1) + "��������" + ParameterValues, "info",
									String.valueOf(i + 1));
							getParameterValues[j] = ParameterValues;
						} else {
							luckyclient.publicclass.LogUtil.APP.error("û���ҵ���Ҫ�ı���Ŷ�������°ɣ���һ�����������ǣ�" + uservariable + "����"
									+ "�������������ǣ�" + uservariable1 + "�����������������ǣ�" + uservariable2);
							caselog.UpdateCaseLogDetail(testCaseExternalId,
									taskid, "û���ҵ���Ҫ�ı���Ŷ�������°ɣ��ڶ����������ǣ�" + uservariable + "����" + "�������������ǣ�"
											+ uservariable1 + "�����������������ǣ�" + uservariable2,
									"error", String.valueOf(i + 1));
						}

					} else {
						String ParameterValues1 = casescript.get("FunctionParams" + (j + 1));
						if (ParameterValues1.indexOf("&quot;") > -1 || ParameterValues1.indexOf("&#39;") > -1 || ParameterValues1.indexOf("@@")>-1) { // ҳ��ת���ַ�ת��
							ParameterValues1 = ParameterValues1.replaceAll("&quot;", "\"");
							ParameterValues1 = ParameterValues1.replaceAll("&#39;", "\'");
							ParameterValues1 = ParameterValues1.replaceAll("@@", "@");
						}
						luckyclient.publicclass.LogUtil.APP.info("����������" + packagename + " ��������" + functionname + " ��"
								+ (j + 1) + "��������" + ParameterValues1);
						caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "����������" + packagename + " ��������"
								+ functionname + " ��" + (j + 1) + "��������" + ParameterValues1, "info",
								String.valueOf(i + 1));
						getParameterValues[j] = ParameterValues1;
					}
				}
			} else {
				getParameterValues = null;
			}
			// ���ö�̬������ִ�в�������
			try {
				luckyclient.publicclass.LogUtil.APP.info("��ʼ���÷�����" + functionname + " .....");
				caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "��ʼ���÷�����" + functionname + " .....", "info",
						String.valueOf(i + 1));
				if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("$=") > -1) { // ��Ԥ�ڽ��ǰ�����ַ��ж��Ƿ���Ҫ�ѽ���������
					String ExpectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
					String temptestnote = InvokeMethod.CallCase(packagename, functionname, getParameterValues);
					variable.put(ExpectedResultVariable, temptestnote);
				} else if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("%=") > -1) { // ��Ԥ�ڽ������Խ����ģ��ƥ��
					testnote = InvokeMethod.CallCase(packagename, functionname, getParameterValues);
					if (testnote.indexOf(expectedresults.substring(2)) > -1) {
						setresult = 0;
						luckyclient.publicclass.LogUtil.APP.info("����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���");
						caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���",
								"info", String.valueOf(i + 1));
					} else {
						setresult = 1;
						luckyclient.publicclass.LogUtil.APP.error("������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
						caselog.UpdateCaseLogDetail(testCaseExternalId, taskid,
								"������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote,
								"error", String.valueOf(i + 1));
						luckyclient.publicclass.LogUtil.APP.error("Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote);
						testnote = "������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
						break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
					}
				} else { // ��Ԥ�ڽ������Խ������ȷƥ��
					testnote = InvokeMethod.CallCase(packagename, functionname, getParameterValues);
					if (expectedresults.equals(testnote)) {
						setresult = 0;
						luckyclient.publicclass.LogUtil.APP.info("����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���");
						caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���",
								"info", String.valueOf(i + 1));
					} else {
						setresult = 1;
						luckyclient.publicclass.LogUtil.APP.error("������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
						caselog.UpdateCaseLogDetail(testCaseExternalId, taskid,
								"������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote,
								"error", String.valueOf(i + 1));
						luckyclient.publicclass.LogUtil.APP.error("Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote);
						testnote = "������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "      ���Խ����"
								+ testnote;
						break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
					}
				}
				int waitsec = Integer.parseInt(casescript.get("StepWait").toString()); // ��ȡ�����ȴ�ʱ��
				if (waitsec != 0) {
					Thread.sleep(waitsec * 1000);
				}
			} catch (Exception e) {
				luckyclient.publicclass.LogUtil.APP.error("���÷������̳�����������" + functionname + " �����¼��ű����������Լ�������");
				caselog.UpdateCaseLogDetail(testCaseExternalId, taskid,
						"���÷������̳�����������" + functionname + " �����¼��ű����������Լ�������", "error", String.valueOf(i + 1));
				luckyclient.publicclass.LogUtil.APP.error(e, e);
				testnote = "CallCase���ó���";
				setresult = 1;
				e.printStackTrace();
				break;
			}
		}
		variable.clear(); // ��մ���MAP
		// ������÷���������δ�����������ò��Խ������
		if (testnote.indexOf("CallCase���ó���") <= -1 && testnote.indexOf("������������") <= -1) {
			luckyclient.publicclass.LogUtil.APP.info("���� " + testCaseExternalId + "�����ɹ������ɹ����������з�����������鿴ִ�н����");
			caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "�����ɹ������ɹ����������з�����������鿴ִ�н����", "info",
					"SETCASERESULT...");
			// TCResult =
			// TestCaseApi.setTCResult(projectname,testCaseExternalId, testnote,
			// version,setresult);
			caselog.UpdateCaseDetail(taskid, testCaseExternalId, setresult);
		} else {
			setresult = 1;
			luckyclient.publicclass.LogUtil.APP.error("���� " + testCaseExternalId + "�������ǵ��ò����еķ�������");
			caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "�������ǵ��ò����еķ�������", "error", "SETCASERESULT...");
			// TCResult =
			// TestCaseApi.setTCResult(projectname,testCaseExternalId, testnote,
			// version,2);
			caselog.UpdateCaseDetail(taskid, testCaseExternalId, 2);
		}
		if (0 == setresult) {
			luckyclient.publicclass.LogUtil.APP.info("���� " + testCaseExternalId + "����ȫ��ִ�гɹ���");
			caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "����ȫ��ִ�гɹ���", "info", "EXECUTECASESUC...");
		} else {
			luckyclient.publicclass.LogUtil.APP.error("���� " + testCaseExternalId + "��ִ�й�����ʧ�ܣ�������־��");
			caselog.UpdateCaseLogDetail(testCaseExternalId, taskid, "��ִ�й�����ʧ�ܣ�������־��", "error", "EXECUTECASESUC...");
		}
		LogOperation.UpdateTastdetail(taskid, 0);
	}

	/**
	 * @param ��Ŀ��
	 * @param �������
	 * @param �����汾��
	 *            ������UI�Ĳ��Թ����У���Ҫ���ýӿڵĲ�������
	 */
	@SuppressWarnings("static-access")
	protected static String OneCaseExecuteForWebDriver(String testCaseExternalId, String taskid) {
		Map<String, String> variable = new HashMap<String, String>();
		String packagename = null;
		String functionname = null;
		String expectedresults = null;
		Integer setresult = null;
		Object[] getParameterValues = null;
		String testnote = null;
		int k = 0;
		ProjectCase testcaseob = GetServerAPI.cgetCaseBysign(testCaseExternalId);
		List<ProjectCasesteps> steps = GetServerAPI.getStepsbycaseid(testcaseob.getId());
		// ����ѭ���������������в���
		for (int i = 0; i < steps.size(); i++) {
			Map<String, String> casescript = InterfaceAnalyticCase.AnalyticCaseStep(testcaseob, steps.get(i), taskid);
			packagename = casescript.get("PackageName").toString();
			functionname = casescript.get("FunctionName").toString();
			// �������ƽ��������쳣���ǵ���������������쳣
			if (functionname.indexOf("�����쳣") > -1 || k == 1) {
				k = 0;
				testnote = "������" + (i + 1) + "��������������";
				break;
			}
			expectedresults = casescript.get("ExpectedResults").toString(); // Ԥ�ڽ��
			if (expectedresults.indexOf("&quot;") > -1 || expectedresults.indexOf("&#39;") > -1) { // ҳ��ת���ַ�ת��
				expectedresults = expectedresults.replaceAll("&quot;", "\"");
				expectedresults = expectedresults.replaceAll("&#39;", "\'");
			}
			// �жϷ����Ƿ������
			if (casescript.size() > 4) {
				// ��ȡ������������������
				getParameterValues = new Object[casescript.size() - 4]; // ��ʼ�������������
				for (int j = 0; j < casescript.size() - 4; j++) {
					if (casescript.get("FunctionParams" + (j + 1)) == null) {
						k = 1;
						break;
					}
					if (casescript.get("FunctionParams" + (j + 1)).indexOf("@") > -1
							&&casescript.get("FunctionParams"+(j+1)).indexOf("@@")<0) { // ������ڴ��Σ����д���
						int keyexistidentity = 0;
						// ȡ�������������ñ�������
						int sumvariable = DBOperation.sumString(casescript.get("FunctionParams" + (j + 1)), "@");
						String uservariable = null;
						String uservariable1 = null;
						String uservariable2 = null;

						if (sumvariable == 1) {
							uservariable = casescript.get("FunctionParams" + (j + 1))
									.substring(casescript.get("FunctionParams" + (j + 1)).indexOf("@") + 1);
						} else if (sumvariable == 2) { // �������������õڶ�������
							uservariable = casescript.get("FunctionParams" + (j + 1)).substring(
									casescript.get("FunctionParams" + (j + 1)).indexOf("@") + 1,
									casescript.get("FunctionParams" + (j + 1)).lastIndexOf("@"));
							uservariable1 = casescript.get("FunctionParams" + (j + 1))
									.substring(casescript.get("FunctionParams" + (j + 1)).lastIndexOf("@") + 1);
						} else if (sumvariable == 3) {
							String temp = casescript.get("FunctionParams" + (j + 1)).substring(
									casescript.get("FunctionParams" + (j + 1)).indexOf("@") + 1,
									casescript.get("FunctionParams" + (j + 1)).lastIndexOf("@"));
							uservariable1 = temp.substring(temp.indexOf("@") + 1);
							uservariable2 = casescript.get("FunctionParams" + (j + 1))
									.substring(casescript.get("FunctionParams" + (j + 1)).lastIndexOf("@") + 1);
							uservariable = casescript.get("FunctionParams" + (j + 1)).substring(
									casescript.get("FunctionParams" + (j + 1)).indexOf("@") + 1,
									casescript.get("FunctionParams" + (j + 1)).indexOf(uservariable1) - 1);
						} else {
							luckyclient.publicclass.LogUtil.APP.error("�������һ�������������˳���3�����ϵı���Ŷ���Ҵ�����������");
						}
						Iterator keys = variable.keySet().iterator();
						String key = null;
						while (keys.hasNext()) {
							key = (String) keys.next();
							if (uservariable.indexOf(key) > -1) {
								keyexistidentity = 1;
								uservariable = key;
								break;
							}
						}
						if (sumvariable == 2 || sumvariable == 3) { // ����ڶ�������
							keys = variable.keySet().iterator();
							while (keys.hasNext()) {
								keyexistidentity = 0;
								key = (String) keys.next();
								if (uservariable1.indexOf(key) > -1) {
									keyexistidentity = 1;
									uservariable1 = key;
									break;
								}
							}
						}
						if (sumvariable == 3) { // �������������
							keys = variable.keySet().iterator();
							while (keys.hasNext()) {
								keyexistidentity = 0;
								key = (String) keys.next();
								if (uservariable2.indexOf(key) > -1) {
									keyexistidentity = 1;
									uservariable2 = key;
									break;
								}
							}
						}
						if (keyexistidentity == 1) {
							// ƴװ����������+ԭ���ַ�����
							String ParameterValues = casescript.get("FunctionParams" + (j + 1))
									.replaceAll("@" + uservariable, variable.get(uservariable).toString());
							// ����ڶ�������
							if (sumvariable == 2 || sumvariable == 3) {
								ParameterValues = ParameterValues.replaceAll("@" + uservariable1,
										variable.get(uservariable1).toString());
							}
							// �������������
							if (sumvariable == 3) {
								ParameterValues = ParameterValues.replaceAll("@" + uservariable2,
										variable.get(uservariable2).toString());
							}
							if (ParameterValues.indexOf("&quot;") > -1 || ParameterValues.indexOf("&#39;") > -1) { // ҳ��ת���ַ�ת��
								ParameterValues = ParameterValues.replaceAll("&quot;", "\"");
								ParameterValues = ParameterValues.replaceAll("&#39;", "\'");
							}
							luckyclient.publicclass.LogUtil.APP.info("����������" + packagename + " ��������" + functionname
									+ " ��" + (j + 1) + "��������" + ParameterValues);
							getParameterValues[j] = ParameterValues;
						} else {
							luckyclient.publicclass.LogUtil.APP.error("û���ҵ���Ҫ�ı���Ŷ�������°ɣ���һ�����������ǣ�" + uservariable + "����"
									+ "�������������ǣ�" + uservariable1 + "�����������������ǣ�" + uservariable2);
						}

					} else {
						String ParameterValues1 = casescript.get("FunctionParams" + (j + 1));
						if (ParameterValues1.indexOf("&quot;") > -1 || ParameterValues1.indexOf("&#39;") > -1 || ParameterValues1.indexOf("@@")>-1) { // ҳ��ת���ַ�ת��
							ParameterValues1 = ParameterValues1.replaceAll("&quot;", "\"");
							ParameterValues1 = ParameterValues1.replaceAll("&#39;", "\'");
							ParameterValues1 = ParameterValues1.replaceAll("@@", "@");
						}
						luckyclient.publicclass.LogUtil.APP.info("����������" + packagename + " ��������" + functionname + " ��"
								+ (j + 1) + "��������" + ParameterValues1);
						getParameterValues[j] = ParameterValues1;
					}
				}
			} else {
				getParameterValues = null;
			}
			// ���ö�̬������ִ�в�������
			try {
				luckyclient.publicclass.LogUtil.APP.info("��ʼ���÷�����" + functionname + " .....");
				if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("$=") > -1) { // ��Ԥ�ڽ��ǰ�����ַ��ж��Ƿ���Ҫ�ѽ���������
					String ExpectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
					String temptestnote = InvokeMethod.CallCase(packagename, functionname, getParameterValues);
					variable.put(ExpectedResultVariable, temptestnote);
				} else if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("%=") > -1) { // ��Ԥ�ڽ������Խ����ģ��ƥ��
					testnote = InvokeMethod.CallCase(packagename, functionname, getParameterValues);
					if (testnote.indexOf(expectedresults.substring(2)) > -1) {
						setresult = 0;
						luckyclient.publicclass.LogUtil.APP.info("����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���");
					} else {
						setresult = 1;
						luckyclient.publicclass.LogUtil.APP.error("������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
						luckyclient.publicclass.LogUtil.APP.error("Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote);
						testnote = "������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
						break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
					}
				} else { // ��Ԥ�ڽ������Խ������ȷƥ��
					testnote = InvokeMethod.CallCase(packagename, functionname, getParameterValues);
					if ("".equals(expectedresults) || testnote.equals(expectedresults)) {
						setresult = 0;
						luckyclient.publicclass.LogUtil.APP.info("����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���");
					} else {
						setresult = 1;
						luckyclient.publicclass.LogUtil.APP.error("������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
						luckyclient.publicclass.LogUtil.APP.error("Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote);
						testnote = "������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "      ���Խ����"
								+ testnote;
						break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
					}
				}
				int waitsec = Integer.parseInt(casescript.get("StepWait").toString()); // ��ȡ�����ȴ�ʱ��
				if (waitsec != 0) {
					Thread.sleep(waitsec * 1000);
				}
			} catch (Exception e) {
				luckyclient.publicclass.LogUtil.APP.error("���÷������̳�����������" + functionname + " �����¼��ű����������Լ�������");
				luckyclient.publicclass.LogUtil.APP.error(e, e);
				testnote = "CallCase���ó���";
				setresult = 1;
				e.printStackTrace();
				break;
			}
		}
		variable.clear(); // ��մ���MAP
		if (0 == setresult) {
			luckyclient.publicclass.LogUtil.APP.info("���� " + testcaseob.getSign() + "����ȫ��ִ�гɹ���");
		} else {
			luckyclient.publicclass.LogUtil.APP.error("���� " + testcaseob.getSign() + "��ִ�й�����ʧ�ܣ�������־��");
		}
		return testnote;
	}

}
