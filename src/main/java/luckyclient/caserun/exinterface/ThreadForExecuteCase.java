package luckyclient.caserun.exinterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckyclient.caserun.exinterface.analyticsteps.InterfaceAnalyticCase;
import luckyclient.dblog.LogOperation;
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
 * @ClassName: ThreadForExecuteCase
 * @Description: �̳߳ط�ʽִ������ 
 * @author�� seagull
 * @date 2017��7��13�� ����9:29:40
 * 
 */
public class ThreadForExecuteCase extends Thread {
	private String caseid;
	private ProjectCase testcaseob;
	private String taskid;
	private List<ProjectCasesteps> steps;
	private List<PublicCaseParams> pcplist;
	private LogOperation caselog;

	public ThreadForExecuteCase(ProjectCase projectcase, List<ProjectCasesteps> steps, String taskid,
			List<PublicCaseParams> pcplist,LogOperation caselog) {
		this.caseid = projectcase.getSign();
		this.testcaseob = projectcase;
		this.taskid = taskid;
		this.steps = steps;
		this.pcplist = pcplist;
		this.caselog = caselog;
	}
	
	@Override
	public void run() {
		Map<String, String> variable = new HashMap<String, String>(0);
		// �ѹ����������뵽MAP��
		for (PublicCaseParams pcp : pcplist) {
			variable.put(pcp.getParamsname(), pcp.getParamsvalue());
		}
		String functionname = null;
		String packagename = null;
		String expectedresults = null;
		Integer setresult = null;
		Object[] getParameterValues = null;
		String testnote = null;
		int k = 0;
		// ����ѭ�������������������в���
		// ���뿪ʼִ�е�����
		caselog.addCaseDetail(taskid, caseid, "1", testcaseob.getName(), 4); 
		for (int i = 0; i < steps.size(); i++) {
			// �������������еĽű�
			Map<String, String> casescript = InterfaceAnalyticCase.analyticCaseStep(testcaseob, steps.get(i), taskid,
					caselog); 
			try {
				packagename = casescript.get("PackageName").toString();
				packagename = ChangString.changparams(packagename, variable,"��·��");
				functionname = casescript.get("FunctionName").toString();
				functionname = ChangString.changparams(functionname, variable,"������");
			} catch (Exception e) {
				k = 0;
				luckyclient.publicclass.LogUtil.APP.error("������" + testcaseob.getSign() + "�����������Ƿ�����ʧ�ܣ����飡");
				caselog.caseLogDetail(taskid, caseid, "�����������Ƿ�����ʧ�ܣ����飡", "error", String.valueOf(i + 1), "");
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
					caselog.caseLogDetail(taskid, caseid,
							"����������" + packagename + " ��������" + functionname + " ��" + (j + 1) + "��������" + parameterValues,
							"info", String.valueOf(i + 1), "");
					getParameterValues[j] = parameterValues;
				}
			} else {
				getParameterValues = null;
			}
			// ���ö�̬������ִ�в�������
			try {
				luckyclient.publicclass.LogUtil.APP
						.info("������" + testcaseob.getSign() + "��ʼ���÷�����" + functionname + " .....");
				caselog.caseLogDetail(taskid, caseid, "��ʼ���÷�����" + functionname + " .....", "info",
						String.valueOf(i + 1), "");
				// ��Ԥ�ڽ��ǰ�����ַ��ж��Ƿ���Ҫ�ѽ���������
				if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("$=") > -1) { 
					String expectedResultVariable = casescript.get("ExpectedResults").toString().substring(2);
					String temptestnote = InvokeMethod.callCase(packagename, functionname, getParameterValues,
							steps.get(i).getSteptype(), steps.get(i).getAction());
					// ���������빫��������ͻ�����������������Զ��滻
					variable.put(expectedResultVariable, temptestnote);					
				} else if (expectedresults.length() > 2 && expectedresults.substring(0, 2).indexOf("%=") > -1) {
					// ��Ԥ�ڽ������Խ����ģ��ƥ��
					testnote = InvokeMethod.callCase(packagename, functionname, getParameterValues,
							steps.get(i).getSteptype(), steps.get(i).getAction());
					if (testnote.indexOf(expectedresults.substring(2)) > -1) {
						setresult = 0;
						luckyclient.publicclass.LogUtil.APP
								.info("������" + testcaseob.getSign() + "ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���");
						caselog.caseLogDetail(taskid, caseid, "ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���", "info",
								String.valueOf(i + 1), "");
					} else {
						setresult = 1;
						luckyclient.publicclass.LogUtil.APP
								.error("������" + testcaseob.getSign() + "��" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
						luckyclient.publicclass.LogUtil.APP.error(
								"������" + testcaseob.getSign() + "Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote);
						caselog.caseLogDetail(taskid, caseid, "��" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�" + "Ԥ�ڽ����"
								+ expectedresults + "      ���Խ����" + testnote, "error", String.valueOf(i + 1), "");
						testnote = "������" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�";
						break; // ĳһ����ʧ�ܺ󣬴���������Ϊʧ���˳�
					}
				} else { // ��Ԥ�ڽ������Խ������ȷƥ��
					testnote = InvokeMethod.callCase(packagename, functionname, getParameterValues,
							steps.get(i).getSteptype(), steps.get(i).getAction());
					if (expectedresults.equals(testnote)) {
						setresult = 0;
						luckyclient.publicclass.LogUtil.APP
								.info("������" + testcaseob.getSign() + "ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���");
						caselog.caseLogDetail(taskid, caseid, "ִ�н���ǣ�" + testnote + "����Ԥ�ڽ��ƥ��ɹ���", "info",
								String.valueOf(i + 1), "");
					} else {
						setresult = 1;
						luckyclient.publicclass.LogUtil.APP
								.error("������" + testcaseob.getSign() + "��" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�");
						luckyclient.publicclass.LogUtil.APP.error(
								"������" + testcaseob.getSign() + "Ԥ�ڽ����" + expectedresults + "      ���Խ����" + testnote);
						caselog.caseLogDetail(taskid, caseid, "��" + (i + 1) + "��ִ�н����Ԥ�ڽ��ƥ��ʧ�ܣ�" + "Ԥ�ڽ����"
								+ expectedresults + "      ���Խ����" + testnote, "error", String.valueOf(i + 1), "");
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
				luckyclient.publicclass.LogUtil.ERROR
						.error("������" + testcaseob.getSign() + "���÷������̳�����������" + functionname + " �����¼��ű����������Լ�������");
				caselog.caseLogDetail(taskid, caseid, "���÷������̳�����������" + functionname + " �����¼��ű����������Լ�������", "error",
						String.valueOf(i + 1), "");
				luckyclient.publicclass.LogUtil.ERROR.error(e, e);
				testnote = "CallCase���ó������÷������̳�����������" + functionname + " �����¼��ű����������Լ�������";
				setresult = 1;
				e.printStackTrace();
				break;
			}
		}
		// ������÷���������δ�����������ò��Խ������
		try {
			// �ɹ���ʧ�ܵ������ߴ�����
			if (testnote.indexOf("CallCase���ó���") <0 && testnote.indexOf("������������") <0) { 
				caselog.updateCaseDetail(taskid, caseid, setresult);
			} else {
				// �����������ǵ��÷�������ȫ����������Ϊ����
				luckyclient.publicclass.LogUtil.ERROR
						.error("������" + testcaseob.getSign() + "����ִ�н��Ϊ��������ο�������־��������������ԭ��....."); 
				caselog.caseLogDetail(taskid, caseid, "����ִ�н��Ϊ��������ο�������־��������������ԭ��.....", "error", "SETCASERESULT...",
						"");
				setresult = 2;
				caselog.updateCaseDetail(taskid, caseid, setresult);
			}
			if (setresult == 0) {
				luckyclient.publicclass.LogUtil.APP.info("������" + testcaseob.getSign() + "ִ�н���ɹ�......");
				caselog.caseLogDetail(taskid, caseid, "��������ִ��ȫ���ɹ�......", "info", "ending", "");
				luckyclient.publicclass.LogUtil.APP
						.info("*********������"+testcaseob.getSign()+"��ִ�����,���Խ�����ɹ�*********");
			} else if (setresult == 1) {
				luckyclient.publicclass.LogUtil.ERROR.error("������" + testcaseob.getSign() + "ִ�н��ʧ��......");
				caselog.caseLogDetail(taskid, caseid, "����ִ�н��ʧ��......", "error", "ending", "");
				luckyclient.publicclass.LogUtil.APP
						.info("*********������"+testcaseob.getSign()+"��ִ�����,���Խ����ʧ��*********");
			} else {
				luckyclient.publicclass.LogUtil.ERROR.error("������" + testcaseob.getSign() + "ִ�н������......");
				caselog.caseLogDetail(taskid, caseid, "����ִ�н������......", "error", "ending", "");
				luckyclient.publicclass.LogUtil.APP
						.info("*********������"+testcaseob.getSign()+"��ִ�����,���Խ��������*********");
			}
		} catch (Exception e) {
			luckyclient.publicclass.LogUtil.ERROR.error("������" + testcaseob.getSign() + "����ִ�н�����̳���......");
			caselog.caseLogDetail(taskid, caseid, "����ִ�н�����̳���......", "error", "ending", "");
			luckyclient.publicclass.LogUtil.ERROR.error(e, e);
			e.printStackTrace();
		} finally {
			variable.clear(); // һ��������������ձ����洢�ռ�
			TestControl.Debugcount--; // ���̼߳���--�����ڼ���߳��Ƿ�ȫ��ִ����
		}
	}

	public static void main(String[] args) {
	}

}
