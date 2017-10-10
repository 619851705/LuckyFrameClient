package luckyclient.caserun.exwebdriver.ex;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import luckyclient.caserun.exinterface.TestCaseExecution;
import luckyclient.caserun.exwebdriver.BaseWebDrive;
import luckyclient.caserun.exwebdriver.EncapsulateOperation;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.publicclass.DBOperation;

public class WebCaseExecution extends TestCaseExecution{
	static Map<String, String> variable = new HashMap<String, String>();

	public static void CaseExcution(ProjectCase testcase, List<ProjectCasesteps> steps,String taskid, WebDriver wd,LogOperation caselog)
			throws InterruptedException {
		int setresult = 0; // 0:�ɹ� 1:ʧ�� 2:���� ����������
		String casenote = "��ע��ʼ��";
		String imagname = "";
		
		caselog.AddCaseDetail(taskid, testcase.getSign(), "1", testcase.getName(), 4);       //���뿪ʼִ�е�����
		
		for (ProjectCasesteps step : steps) {
			Map<String, String> params = WebDriverAnalyticCase.AnalyticCaseStep(testcase, step, taskid);
			
			if(params.get("exception")!=null&&params.get("exception").toString().indexOf("�����쳣")>-1){
				setresult = 2;
				break;
			}
			
			String result = WebCaseExecution.runStep(params, wd, taskid, testcase.getSign(), step.getStepnum(), caselog);

			String expectedResults = params.get("ExpectedResults").toString();

			if (result.indexOf("����") < 0 && result.indexOf("ʧ��") < 0) { // ���н������
				int waitsec = Integer.parseInt(params.get("StepWait").toString()); // ��ȡ�����ȴ�ʱ��
				if (waitsec != 0) {
					luckyclient.publicclass.LogUtil.APP.info("�������ߡ�"+waitsec+"����");
					Thread.sleep(waitsec * 1000);
				}
				
				if (!expectedResults.equals("")) { // ��Ԥ�ڽ��
					// �жϴ���
					luckyclient.publicclass.LogUtil.APP.info("expectedResults=��"+expectedResults+"��");
					if (expectedResults.length() > 2 && expectedResults.substring(0, 2).indexOf("$=") > -1) {
						String ExpectedResultVariable = expectedResults.substring(2);
						variable.put(ExpectedResultVariable, result);
						continue;
					}

					// �ж�Ԥ�ڽ��-���ģʽ
					if (params.get("checkproperty") != null && params.get("checkproperty_value") != null) {
						String checkproperty = params.get("checkproperty").toString();
						String checkproperty_value = params.get("checkproperty_value").toString();

						WebElement we = isElementExist(wd, checkproperty, checkproperty_value);
						if (null != we) {
							luckyclient.publicclass.LogUtil.APP.info("������" + testcase.getSign() + " ��" + step.getStepnum()
									+ "�����ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ�гɹ���");
							caselog.CaseLogDetail(taskid, testcase.getSign(), "�ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ�гɹ���",
									"info", String.valueOf(step.getStepnum()),"");
							continue;
						} else {
							casenote = "��" + step.getStepnum() + "����û���ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж���ִ��ʧ�ܣ�";
							setresult = 1;
							java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-hhmmss");
							imagname = timeformat.format(new Date());
							BaseWebDrive.WebScreenShot(wd,imagname);
							luckyclient.publicclass.LogUtil.APP.error("������" + testcase.getSign() + " ��" + step.getStepnum()
									+ "����û���ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ��ʧ�ܣ�");
							caselog.CaseLogDetail(taskid, testcase.getSign(), "�ڵ�ǰҳ����û���ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ��ʧ�ܣ�"
									+ "checkproperty��"+checkproperty+"��  checkproperty_value��"+checkproperty_value+"��","error", String.valueOf(step.getStepnum()),imagname);
							break;
						}

					}else{
						// ģ��ƥ��Ԥ�ڽ��ģʽ
						if (expectedResults.length()>2 && expectedResults.substring(0, 2).indexOf("%=")>-1) {
							if(result.indexOf(expectedResults.substring(2))>-1){
								luckyclient.publicclass.LogUtil.APP.info("������" + testcase.getSign() + " ��" + step.getStepnum()
								+ "����ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н����"+result);
						        caselog.CaseLogDetail(taskid, testcase.getSign(), "����ģ��ƥ��Ԥ�ڽ���ɹ���",
								"info", String.valueOf(step.getStepnum()),"");
						        continue;
							}else{
								casenote = "��" + step.getStepnum() + "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�";
								setresult = 1;
								java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-hhmmss");
								imagname = timeformat.format(new Date());
								BaseWebDrive.WebScreenShot(wd,imagname);
								luckyclient.publicclass.LogUtil.APP.error("������" + testcase.getSign() + " ��" + step.getStepnum()
								+ "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�ִ�н����"+result);
						        caselog.CaseLogDetail(taskid, testcase.getSign(), "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�ִ�н����"+result,
								"error", String.valueOf(step.getStepnum()),imagname);
								break;
							}
						}else if(expectedResults.equals(result)) {    // ֱ��ƥ��Ԥ�ڽ��ģʽ
							luckyclient.publicclass.LogUtil.APP.info("������" + testcase.getSign() + " ��" + step.getStepnum()
							+ "����ֱ��ƥ��Ԥ�ڽ���ɹ���ִ�н����"+result);
					        caselog.CaseLogDetail(taskid, testcase.getSign(), "����ֱ��ƥ��Ԥ�ڽ���ɹ���",
							"info", String.valueOf(step.getStepnum()),"");
					        continue;
						} else {
							casenote = "��" + step.getStepnum() + "����ֱ��ƥ��Ԥ�ڽ��ʧ�ܣ�";
							setresult = 1;
							java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-hhmmss");
							imagname = timeformat.format(new Date());
							BaseWebDrive.WebScreenShot(wd,imagname);
							luckyclient.publicclass.LogUtil.APP.error("������" + testcase.getSign() + " ��" + step.getStepnum()
							+ "����ֱ��ƥ��Ԥ�ڽ��ʧ�ܣ�ִ�н����"+result);
					        caselog.CaseLogDetail(taskid, testcase.getSign(), "����ֱ��ƥ��Ԥ�ڽ��ʧ�ܣ�ִ�н����"+result,
							"error", String.valueOf(step.getStepnum()),imagname);
							break;
						}
					}
				}

			} else {
				casenote = result;
				setresult = 2;
				java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-hhmmss");
				imagname = timeformat.format(new Date());
				BaseWebDrive.WebScreenShot(wd,imagname);
				luckyclient.publicclass.LogUtil.APP.error("������" + testcase.getSign() + " ��" + step.getStepnum()	+ "����"+result);
		        caselog.CaseLogDetail(taskid, testcase.getSign(), "��ǰ������ִ�й����н���|��λԪ��|��������ʧ�ܣ�"+result,
				"error", String.valueOf(step.getStepnum()),imagname);
				break;
			}

		}

		variable.clear();
		caselog.UpdateCaseDetail(taskid, testcase.getSign(), setresult);
		if(setresult==0){
			luckyclient.publicclass.LogUtil.APP.info("������"+testcase.getSign()+"��ȫ������ִ�н���ɹ�...");
	        caselog.CaseLogDetail(taskid, testcase.getSign(), "����ȫ������ִ�н���ɹ�","info", "ending","");
		}else{
			luckyclient.publicclass.LogUtil.APP.error("������"+testcase.getSign()+"������ִ�й�����ʧ�ܻ�������...��鿴����ԭ��"+casenote);
	        caselog.CaseLogDetail(taskid, testcase.getSign(), "����ִ�й�����ʧ�ܻ�������"+casenote,"error", "ending","");
		}
		//LogOperation.UpdateTastdetail(taskid, 0);
	}

	private static String runStep(Map<String, String> params, WebDriver wd,String taskid,String casenum,int stepno,LogOperation caselog) {
		String result = "";
		String property;
		String property_value;
		String operation;
		String operation_value;

		try {
			property = params.get("property");
			property_value = params.get("property_value");
			operation = params.get("operation");
			operation_value = params.get("operation_value");

			// ����ֵ����
			if (property_value != null && property_value.indexOf("@") > -1 && property_value.indexOf("[@") < 0 
					&& property_value.indexOf("@@") < 0) {
				property_value = SettingParameter(property_value);
				// �жϴ����Ƿ��������
				if (property_value.indexOf("Set parameter error") > -1) {
					caselog.CaseLogDetail(taskid, casenum, "��ǰ������������쳣���Ƕ���Ϊ�գ�---"+property,
							"error", String.valueOf(stepno),"");
					return "�����ι��̳���" + property_value;
				}
			}else if(property_value != null && (property_value.indexOf("&quot;")>-1 
					|| property_value.indexOf("&#39;")>-1 || property_value.indexOf("@@")>-1)){
				property_value = property_value.replaceAll("&quot;", "\"");
				property_value = property_value.replaceAll("&#39;", "\'");
				property_value = property_value.replaceAll("@@", "@");
			}
			
			if (operation_value != null && operation_value.indexOf("@") > -1 && operation_value.indexOf("@@") < 0) {
				operation_value = SettingParameter(operation_value);
				if (operation_value.indexOf("Set parameter error") > -1) {
					return "�����ι��̳���" + property_value;
				}
			}else if(operation_value != null && (operation_value.indexOf("&quot;")>-1 
					|| operation_value.indexOf("&#39;")>-1 || operation_value.indexOf("@@")>-1)){
				operation_value = operation_value.replaceAll("&quot;", "\"");
				operation_value = operation_value.replaceAll("&#39;", "\'");
				operation_value = operation_value.replaceAll("@@", "@");
			}
			
			luckyclient.publicclass.LogUtil.APP.info("���ν�������������ɣ��ȴ����ж������......");
			caselog.CaseLogDetail(taskid, casenum, "�������:"+operation+"; ����ֵ:"+operation_value,"info", String.valueOf(stepno),"");

		} catch (Exception e) {
			e.printStackTrace();
			luckyclient.publicclass.LogUtil.APP.error("���ν������������׳��쳣��---"+e.getMessage());
			return "��������ʧ��!";
		}

		try {		
			//���ýӿ�����
			if(null != operation&&null != operation_value&&"runcase".equals(operation)){
				String temp[]=operation_value.split(",",-1);
				String ex = TestCaseExecution.OneCaseExecuteForWebDriver(temp[0],taskid);
				if(ex.indexOf("CallCase���ó���")<=-1&&ex.indexOf("������������")<=-1&&ex.indexOf("ƥ��ʧ��")<=-1){
					return ex;
				}else{
					return "���ýӿ���������ʧ��";
				}
			}
			
			WebElement we = null;

			if (null != property && null != property_value) { // ҳ��Ԫ�ز�
				we = isElementExist(wd, property, property_value);
				// �жϴ�Ԫ���Ƿ����
				if (null==we) {
					luckyclient.publicclass.LogUtil.APP.error("��λ����ʧ�ܣ�isElementExistΪnull!");
					return "isElementExist��λԪ�ع���ʧ�ܣ�";
				}

				if (operation.indexOf("select") > -1) {
					result = EncapsulateOperation.SelectOperation(we, operation, operation_value);
				} else if (operation.indexOf("get") > -1){
					result = EncapsulateOperation.GetOperation(wd, we, operation);
				} else if (operation.indexOf("mouse") > -1){
					result = EncapsulateOperation.ActionWeOperation(wd, we, operation, operation_value, property, property_value);
				} else {
					result = EncapsulateOperation.ObjectOperation(wd, we, operation, operation_value, property, property_value);
				}
			} else if (null==property && null != operation) { // Driver�����				
				// ���������¼�
				if (operation.indexOf("alert") > -1){
					result = EncapsulateOperation.AlertOperation(wd, operation);
				}else if(operation.indexOf("mouse") > -1){
					result = EncapsulateOperation.ActionOperation(wd, operation, operation_value);
				}else{
					result = EncapsulateOperation.DriverOperation(wd, operation, operation_value);
				} 				
			}else{
				luckyclient.publicclass.LogUtil.APP.error("Ԫ�ز�������ʧ�ܣ�");
				result =  "Ԫ�ز�������ʧ�ܣ�";
			}
		} catch (Exception e) {
			luckyclient.publicclass.LogUtil.APP.error("Ԫ�ض�λ���̻��ǲ�������ʧ�ܻ��쳣��"+e.getMessage());
			return "Ԫ�ض�λ���̻��ǲ�������ʧ�ܻ��쳣��" + e.getMessage();
		}
		caselog.CaseLogDetail(taskid, casenum, result,"info", String.valueOf(stepno),"");
		
		if(result.indexOf("��ȡ����ֵ�ǡ�")>-1&&result.indexOf("��")>-1){
			result = result.substring(7, result.length()-1);
		}
		return result;

	}

	private static String SettingParameter(String parameter) {
		int keyexistidentity = 0;
		if (parameter.indexOf("&quot;") > -1 || parameter.indexOf("&#39;") > -1) { // ҳ��ת���ַ�ת��
			parameter = parameter.replaceAll("&quot;", "\"");
			parameter = parameter.replaceAll("&#39;", "\'");
		}
		//��������ַ����д�@�����
		if(parameter.indexOf("\\@")>-1){
			return parameter.replace("\\@", "@");
		}
		
		// ȡ�������������ñ�������
		int sumvariable = DBOperation.sumString(parameter, "@");
		String uservariable = null;
		String uservariable1 = null;
		String uservariable2 = null;

		if (sumvariable == 1) {
			uservariable = parameter.substring(parameter.indexOf("@") + 1);
		} else if (sumvariable == 2) { // �������������õڶ�������
			uservariable = parameter.substring(parameter.indexOf("@") + 1, parameter.lastIndexOf("@"));
			uservariable1 = parameter.substring(parameter.lastIndexOf("@") + 1);
		} else if (sumvariable == 3) {
			String temp = parameter.substring(parameter.indexOf("@") + 1, parameter.lastIndexOf("@"));
			uservariable1 = temp.substring(temp.indexOf("@") + 1);
			uservariable2 = parameter.substring(parameter.lastIndexOf("@") + 1);
			uservariable = parameter.substring(parameter.indexOf("@") + 1, parameter.indexOf(uservariable1) - 1);
		} else {
			luckyclient.publicclass.LogUtil.APP.error("�������һ�������������˳���3�����ϵı���Ŷ���Ҵ�����������");
			return "�������һ�������������˳���3�����ϵı���Ŷ���Ҵ�������������Set parameter error��";
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
			String ParameterValues = parameter.replaceAll("@" + uservariable, variable.get(uservariable).toString());
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

			return ParameterValues;
		} else {
			luckyclient.publicclass.LogUtil.APP.error("û���ҵ���Ҫ�ı���Ŷ�������°ɣ���һ�����������ǣ�" + uservariable + "����" + "�������������ǣ�" + uservariable1
					+ "�����������������ǣ�" + uservariable2);
			return "��Set parameter error��û���ҵ���Ҫ�ı���Ŷ�������°ɣ���һ�����������ǣ�" + uservariable + "����" + "�������������ǣ�" + uservariable1
					+ "�����������������ǣ�" + uservariable2;
		}
	}

	public static WebElement isElementExist(WebDriver wd, String property, String property_value) {
		try {
			WebElement we = null;

			// ����WebElement����λ
			switch (property) {
			case "id":
				we = wd.findElement(By.id(property_value));
				break;
			case "name":
				we = wd.findElement(By.name(property_value));
				break;
			case "xpath":
				we = wd.findElement(By.xpath(property_value));
				break;
			case "linktext":
				we = wd.findElement(By.linkText(property_value));
				break;
			case "tagname":
				we = wd.findElement(By.tagName(property_value));
				break;
			case "cssselector":
				we = wd.findElement(By.cssSelector(property_value));
				break;
			default:
				break;
			}

			return we;

		} catch (Exception e) {
			luckyclient.publicclass.LogUtil.APP.error("��ǰ����λʧ�ܣ�"+e.getMessage());
			return null;
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
