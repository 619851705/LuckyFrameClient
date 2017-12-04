package luckyclient.caserun.exinterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckyclient.caserun.exinterface.analyticsteps.InterfaceAnalyticCase;
import luckyclient.dblog.DbLink;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.planapi.entity.PublicCaseParams;
import luckyclient.publicclass.ChangString;
import luckyclient.publicclass.InvokeMethod;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 * 
 * @author�� seagull
 * @date 2017��12��1�� ����9:29:40
 * 
 */
public class TestCaseExecution {
	/**
	 * @param ��Ŀ��
	 * @param �������
	 * @param �����汾��
	 * ���ڵ����������ԣ���ͨ����־���д��־��UTP�ϣ�����UTP�ϵ�����������
	 */
	@SuppressWarnings("static-access")
	public static void oneCaseExecuteForTast(String projectname, String testCaseExternalId, int version,
			String taskid) {
		Map<String, String> variable = new HashMap<String, String>(0);
		TestControl.TASKID = taskid;
		DbLink.exetype = 0;
		// ��ʼ��д��������Լ���־ģ��
		LogOperation caselog = new LogOperation(); 
		String packagename = null;
		String functionname = null;
		String expectedresults = null;
		Integer setresult = null;
		Object[] getParameterValues = null;
		String testnote = null;
		int k = 0;
		// ɾ���ɵ���־
		LogOperation.deleteCaseLogDetail(testCaseExternalId, taskid); 
		ProjectCase testcaseob = GetServerAPI.cgetCaseBysign(testCaseExternalId);
		List<PublicCaseParams> pcplist=GetServerAPI.cgetParamsByProjectid(String.valueOf(testcaseob.getProjectid()));
		// �ѹ����������뵽MAP��
		for (PublicCaseParams pcp : pcplist) {
			variable.put(pcp.getParamsname(), pcp.getParamsvalue());
		}
		List<ProjectCasesteps> steps = GetServerAPI.getStepsbycaseid(testcaseob.getId());
		// ����ѭ���������������в���
		for (int i = 0; i < steps.size(); i++) {
			Map<String, String> casescript = InterfaceAnalyticCase.analyticCaseStep(testcaseob, steps.get(i), taskid,caselog);
			try {
				packagename = casescript.get("PackageName").toString();
				packagename = ChangString.changparams(packagename, variable,"��·��");
				functionname = casescript.get("FunctionName").toString();
				functionname = ChangString.changparams(functionname, variable,"������");
			} catch (Exception e) {
				k = 0;
				luckyclient.publicclass.LogUtil.APP.error("������" + testcaseob.getSign() + "�����������Ƿ�����ʧ�ܣ����飡");
				caselog.caseLogDetail(taskid, testcaseob.getSign(), "�����������Ƿ�����ʧ�ܣ����飡", "error", String.valueOf(i + 1), "");
				e.printStackTrace();
				break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
			}
			// �������ƽ��������쳣���ǵ���������������쳣
			if (functionname.indexOf("�����쳣") > -1 || k == 1) {
				k = 0;
				testnote = "������" + (i + 1) + "��������������";
				break;
			}
			expectedresults = casescript.get("ExpectedResults").toString(); 
			expectedresults = ChangString.changparams(expectedresults, variable,"Ԥ�ڽ��");
			// �жϷ����Ƿ������
			if (casescript.size() > 4) {
				// ��ȡ�����������������У���ʼ�������������
				getParameterValues = new Object[casescript.size() - 4]; 
				for (int j = 0; j < casescript.size() - 4; j++) {
					if (casescript.get("FunctionParams" + (j + 1)) == null) {
						k = 1;
						break;
					}
					
					String parameterValues = casescript.get("FunctionParams" + (j + 1));
					parameterValues = ChangString.changparams(parameterValues, variable,"��������");
					luckyclient.publicclass.LogUtil.APP.info("������" + testcaseob.getSign() + "����������" + packagename
							+ " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues);
					caselog.caseLogDetail(taskid, testcaseob.getSign(),
							"����������" + packagename + " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues,
							"info", String.valueOf(i + 1), "");
					getParameterValues[j] = parameterValues;
				}
			} else {
				getParameterValues = null;
			}
			// ���ö�̬������ִ�в�������
			try {
				luckyclient.publicclass.LogUtil.APP.info("��ʼ���÷�����" + functionname + " .....");
				LogOperation.updateCaseLogDetail(testCaseExternalId, taskid, "��ʼ���÷�����" + functionname + " .....", "info",
						String.valueOf(i + 1));
				// ��Ԥ�ڽ��ǰ�����ַ��ж��Ƿ���Ҫ�ѽ���������
				if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("$=") > -1) { 
					String expectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
					String temptestnote = InvokeMethod.callCase(packagename, functionname, getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
					variable.put(expectedResultVariable, temptestnote);
				} else if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("%=") > -1) { 
					testnote = InvokeMethod.callCase(packagename, functionname, getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
					if (testnote.indexOf(expectedresults.substring(2)) > -1) {
						setresult = 0;
						luckyclient.publicclass.LogUtil.APP.info("����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���");
						LogOperation.updateCaseLogDetail(testCaseExternalId, taskid, "����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���",
								"info", String.valueOf(i + 1));
					} else {
						setresult = 1;
						luckyclient.publicclass.LogUtil.APP.error("������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
						LogOperation.updateCaseLogDetail(testCaseExternalId, taskid,
								"������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote,
								"error", String.valueOf(i + 1));
						luckyclient.publicclass.LogUtil.APP.error("Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote);
						testnote = "������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
						break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
					}
				} else { // ��Ԥ�ڽ������Խ������ȷƥ��
					testnote = InvokeMethod.callCase(packagename, functionname, getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
					if (expectedresults.equals(testnote)) {
						setresult = 0;
						luckyclient.publicclass.LogUtil.APP.info("����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���");
						LogOperation.updateCaseLogDetail(testCaseExternalId, taskid, "����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���",
								"info", String.valueOf(i + 1));
					} else {
						setresult = 1;
						luckyclient.publicclass.LogUtil.APP.error("������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
						LogOperation.updateCaseLogDetail(testCaseExternalId, taskid,
								"������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote,
								"error", String.valueOf(i + 1));
						luckyclient.publicclass.LogUtil.APP.error("Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote);
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append("������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "      ���Խ����");
						stringBuilder.append(testnote);
						testnote = stringBuilder.toString();
						break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
					}
				}
				// ��ȡ�����ȴ�ʱ��
				int waitsec = Integer.parseInt(casescript.get("StepWait").toString()); 
				if (waitsec != 0) {
					Thread.sleep(waitsec * 1000);
				}
			} catch (Exception e) {
				luckyclient.publicclass.LogUtil.APP.error("���÷������̳�����������" + functionname + " �����¼��ű����������Լ�������");
				LogOperation.updateCaseLogDetail(testCaseExternalId, taskid,
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
			LogOperation.updateCaseLogDetail(testCaseExternalId, taskid, "�����ɹ������ɹ����������з�����������鿴ִ�н����", "info",
					"SETCASERESULT...");
			// TCResult =
			// TestCaseApi.setTCResult(projectname,testCaseExternalId, testnote,
			// version,setresult);
			caselog.updateCaseDetail(taskid, testCaseExternalId, setresult);
		} else {
			setresult = 1;
			luckyclient.publicclass.LogUtil.APP.error("���� " + testCaseExternalId + "�������ǵ��ò����еķ�������");
			LogOperation.updateCaseLogDetail(testCaseExternalId, taskid, "�������ǵ��ò����еķ�������", "error", "SETCASERESULT...");
			// TCResult =
			// TestCaseApi.setTCResult(projectname,testCaseExternalId, testnote,
			// version,2);
			caselog.updateCaseDetail(taskid, testCaseExternalId, 2);
		}
		if (0 == setresult) {
			luckyclient.publicclass.LogUtil.APP.info("���� " + testCaseExternalId + "����ȫ��ִ�гɹ���");
			LogOperation.updateCaseLogDetail(testCaseExternalId, taskid, "����ȫ��ִ�гɹ���", "info", "EXECUTECASESUC...");
		} else {
			luckyclient.publicclass.LogUtil.APP.error("���� " + testCaseExternalId + "��ִ�й�����ʧ�ܣ�������־��");
			LogOperation.updateCaseLogDetail(testCaseExternalId, taskid, "��ִ�й�����ʧ�ܣ�������־��", "error", "EXECUTECASESUC...");
		}
		LogOperation.updateTastdetail(taskid, 0);
	}

	/**
	 * @param ��Ŀ��
	 * @param �������
	 * @param �����汾��
	 *            ������UI�Ĳ��Թ����У���Ҫ���ýӿڵĲ�������
	 */
	protected static String oneCaseExecuteForWebDriver(String testCaseExternalId, String taskid,LogOperation caselog) {
		Map<String, String> variable = new HashMap<String, String>(0);
		String packagename = null;
		String functionname = null;
		String expectedresults = null;
		Integer setresult = null;
		Object[] getParameterValues = null;
		String testnote = null;
		int k = 0;
		ProjectCase testcaseob = GetServerAPI.cgetCaseBysign(testCaseExternalId);
		List<PublicCaseParams> pcplist=GetServerAPI.cgetParamsByProjectid(String.valueOf(testcaseob.getProjectid()));
		// �ѹ����������뵽MAP��
		for (PublicCaseParams pcp : pcplist) {
			variable.put(pcp.getParamsname(), pcp.getParamsvalue());
		}
		List<ProjectCasesteps> steps = GetServerAPI.getStepsbycaseid(testcaseob.getId());
		// ����ѭ���������������в���
		for (int i = 0; i < steps.size(); i++) {
			Map<String, String> casescript = InterfaceAnalyticCase.analyticCaseStep(testcaseob, steps.get(i), taskid,caselog);
			try {
				packagename = casescript.get("PackageName").toString();
				packagename = ChangString.changparams(packagename, variable,"��·��");
				functionname = casescript.get("FunctionName").toString();
				functionname = ChangString.changparams(functionname, variable,"������");
			} catch (Exception e) {
				k = 0;
				luckyclient.publicclass.LogUtil.APP.error("������" + testcaseob.getSign() + "�����������Ƿ�����ʧ�ܣ����飡");
				caselog.caseLogDetail(taskid, testcaseob.getSign(), "�����������Ƿ�����ʧ�ܣ����飡", "error", String.valueOf(i + 1), "");
				e.printStackTrace();
				break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
			}
			// �������ƽ��������쳣���ǵ���������������쳣
			if (functionname.indexOf("�����쳣") > -1 || k == 1) {
				k = 0;
				testnote = "������" + (i + 1) + "��������������";
				break;
			}
			expectedresults = casescript.get("ExpectedResults").toString(); 
			expectedresults = ChangString.changparams(expectedresults, variable,"Ԥ�ڽ��");
			// �жϷ����Ƿ������
			if (casescript.size() > 4) {
				// ��ȡ������������������
				getParameterValues = new Object[casescript.size() - 4]; 
				for (int j = 0; j < casescript.size() - 4; j++) {
					if (casescript.get("FunctionParams" + (j + 1)) == null) {
						k = 1;
						break;
					}
					
					String parameterValues = casescript.get("FunctionParams" + (j + 1));
					parameterValues = ChangString.changparams(parameterValues, variable,"��������");
					luckyclient.publicclass.LogUtil.APP.info("������" + testcaseob.getSign() + "����������" + packagename
							+ " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues);
					caselog.caseLogDetail(taskid, testcaseob.getSign(),
							"����������" + packagename + " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues,
							"info", String.valueOf(i + 1), "");
					getParameterValues[j] = parameterValues;

				}
			} else {
				getParameterValues = null;
			}
			// ���ö�̬������ִ�в�������
			try {
				luckyclient.publicclass.LogUtil.APP.info("��ʼ���÷�����" + functionname + " .....");
				// ��Ԥ�ڽ��ǰ�����ַ��ж��Ƿ���Ҫ�ѽ���������
				if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("$=") > -1) { 
					String expectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
					String temptestnote = InvokeMethod.callCase(packagename, functionname, getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
					variable.put(expectedResultVariable, temptestnote);
				} else if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("%=") > -1) { 
					testnote = InvokeMethod.callCase(packagename, functionname, getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
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
					testnote = InvokeMethod.callCase(packagename, functionname, getParameterValues,steps.get(i).getSteptype(),steps.get(i).getAction());
					if ("".equals(expectedresults) || testnote.equals(expectedresults)) {
						setresult = 0;
						luckyclient.publicclass.LogUtil.APP.info("����ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���");
					} else {
						setresult = 1;
						luckyclient.publicclass.LogUtil.APP.error("������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
						luckyclient.publicclass.LogUtil.APP.error("Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote);
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append("������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�Ԥ�ڽ����" + expectedresults + "      ���Խ����");
						stringBuilder.append(testnote);
						testnote = stringBuilder.toString();
						break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
					}
				}
				int waitsec = Integer.parseInt(casescript.get("StepWait").toString()); 
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
