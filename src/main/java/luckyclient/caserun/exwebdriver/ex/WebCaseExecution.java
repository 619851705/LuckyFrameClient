package luckyclient.caserun.exwebdriver.ex;

import java.util.Date;
import java.util.HashMap;
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
import luckyclient.planapi.entity.PublicCaseParams;
import luckyclient.publicclass.ChangString;

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
public class WebCaseExecution extends TestCaseExecution{
	static Map<String, String> variable = new HashMap<String, String>();

	public static void caseExcution(ProjectCase testcase, List<ProjectCasesteps> steps,String taskid, WebDriver wd,LogOperation caselog,List<PublicCaseParams> pcplist)
			throws InterruptedException {
		// 0:�ɹ� 1:ʧ�� 2:���� ����������
		int setresult = 0; 
		String casenote = "��ע��ʼ��";
		String imagname = "";
		// �ѹ����������뵽MAP��
		for (PublicCaseParams pcp : pcplist) {
			variable.put(pcp.getParamsname(), pcp.getParamsvalue());
		}
		//���뿪ʼִ�е�����
		caselog.addCaseDetail(taskid, testcase.getSign(), "1", testcase.getName(), 4);       
		
		for (ProjectCasesteps step : steps) {
			Map<String, String> params = WebDriverAnalyticCase.analyticCaseStep(testcase, step, taskid,caselog);
			
			if(params.get("exception")!=null&&params.get("exception").toString().indexOf("�����쳣")>-1){
				setresult = 2;
				break;
			}
			
			String result = WebCaseExecution.runStep(params, wd, taskid, testcase.getSign(), step.getStepnum(), caselog);

			String expectedResults = params.get("ExpectedResults").toString();
			expectedResults=ChangString.changparams(expectedResults, variable,"Ԥ�ڽ��");
			// ���н������
			if (result.indexOf("����") < 0 && result.indexOf("ʧ��") < 0) { 
				// ��ȡ�����ȴ�ʱ��
				int waitsec = Integer.parseInt(params.get("StepWait").toString()); 
				if (waitsec != 0) {
					luckyclient.publicclass.LogUtil.APP.info("�������ߡ�"+waitsec+"����");
					Thread.sleep(waitsec * 1000);
				}
				// ��Ԥ�ڽ��
				if (!"".equals(expectedResults)) { 
					// �жϴ���
					luckyclient.publicclass.LogUtil.APP.info("expectedResults=��"+expectedResults+"��");
					if (expectedResults.length() > 2 && expectedResults.substring(0, 2).indexOf("$=") > -1) {
						String expectedResultVariable = expectedResults.substring(2);
						variable.put(expectedResultVariable, result);
						continue;
					}

					// �ж�Ԥ�ڽ��-���ģʽ
					if (params.get("checkproperty") != null && params.get("checkproperty_value") != null) {
						String checkproperty = params.get("checkproperty").toString();
						String checkPropertyValue = params.get("checkproperty_value").toString();

						WebElement we = isElementExist(wd, checkproperty, checkPropertyValue);
						if (null != we) {
							luckyclient.publicclass.LogUtil.APP.info("������" + testcase.getSign() + " ��" + step.getStepnum()
									+ "�����ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ�гɹ���");
							caselog.caseLogDetail(taskid, testcase.getSign(), "�ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ�гɹ���",
									"info", String.valueOf(step.getStepnum()),"");
							continue;
						} else {
							casenote = "��" + step.getStepnum() + "����û���ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж���ִ��ʧ�ܣ�";
							setresult = 1;
							java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-hhmmss");
							imagname = timeformat.format(new Date());
							BaseWebDrive.webScreenShot(wd,imagname);
							luckyclient.publicclass.LogUtil.APP.error("������" + testcase.getSign() + " ��" + step.getStepnum()
									+ "����û���ڵ�ǰҳ�����ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ��ʧ�ܣ�");
							caselog.caseLogDetail(taskid, testcase.getSign(), "�ڵ�ǰҳ����û���ҵ�Ԥ�ڽ���ж��󡣵�ǰ����ִ��ʧ�ܣ�"
									+ "checkproperty��"+checkproperty+"��  checkproperty_value��"+checkPropertyValue+"��","error", String.valueOf(step.getStepnum()),imagname);
							break;
						}

					}else{
						// ģ��ƥ��Ԥ�ڽ��ģʽ
						if (expectedResults.length()>2 && expectedResults.substring(0, 2).indexOf("%=")>-1) {
							if(result.indexOf(expectedResults.substring(2))>-1){
								luckyclient.publicclass.LogUtil.APP.info("������" + testcase.getSign() + " ��" + step.getStepnum()
								+ "����ģ��ƥ��Ԥ�ڽ���ɹ���ִ�н����"+result);
						        caselog.caseLogDetail(taskid, testcase.getSign(), "����ģ��ƥ��Ԥ�ڽ���ɹ���",
								"info", String.valueOf(step.getStepnum()),"");
						        continue;
							}else{
								casenote = "��" + step.getStepnum() + "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�";
								setresult = 1;
								java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-hhmmss");
								imagname = timeformat.format(new Date());
								BaseWebDrive.webScreenShot(wd,imagname);
								luckyclient.publicclass.LogUtil.APP.error("������" + testcase.getSign() + " ��" + step.getStepnum()
								+ "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�ִ�н����"+result);
						        caselog.caseLogDetail(taskid, testcase.getSign(), "����ģ��ƥ��Ԥ�ڽ��ʧ�ܣ�ִ�н����"+result,
								"error", String.valueOf(step.getStepnum()),imagname);
								break;
							}
							// ֱ��ƥ��Ԥ�ڽ��ģʽ
						}else if(expectedResults.equals(result)) {    
							luckyclient.publicclass.LogUtil.APP.info("������" + testcase.getSign() + " ��" + step.getStepnum()
							+ "����ֱ��ƥ��Ԥ�ڽ���ɹ���ִ�н����"+result);
					        caselog.caseLogDetail(taskid, testcase.getSign(), "����ֱ��ƥ��Ԥ�ڽ���ɹ���",
							"info", String.valueOf(step.getStepnum()),"");
					        continue;
						} else {
							casenote = "��" + step.getStepnum() + "����ֱ��ƥ��Ԥ�ڽ��ʧ�ܣ�";
							setresult = 1;
							java.text.DateFormat timeformat = new java.text.SimpleDateFormat("MMdd-hhmmss");
							imagname = timeformat.format(new Date());
							BaseWebDrive.webScreenShot(wd,imagname);
							luckyclient.publicclass.LogUtil.APP.error("������" + testcase.getSign() + " ��" + step.getStepnum()
							+ "����ֱ��ƥ��Ԥ�ڽ��ʧ�ܣ�ִ�н����"+result);
					        caselog.caseLogDetail(taskid, testcase.getSign(), "����ֱ��ƥ��Ԥ�ڽ��ʧ�ܣ�ִ�н����"+result,
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
				BaseWebDrive.webScreenShot(wd,imagname);
				luckyclient.publicclass.LogUtil.APP.error("������" + testcase.getSign() + " ��" + step.getStepnum()	+ "����"+result);
		        caselog.caseLogDetail(taskid, testcase.getSign(), "��ǰ������ִ�й����н���|��λԪ��|��������ʧ�ܣ�"+result,
				"error", String.valueOf(step.getStepnum()),imagname);
				break;
			}

		}

		variable.clear();
		caselog.updateCaseDetail(taskid, testcase.getSign(), setresult);
		if(setresult==0){
			luckyclient.publicclass.LogUtil.APP.info("������"+testcase.getSign()+"��ȫ������ִ�н���ɹ�...");
	        caselog.caseLogDetail(taskid, testcase.getSign(), "����ȫ������ִ�н���ɹ�","info", "ending","");
		}else{
			luckyclient.publicclass.LogUtil.APP.error("������"+testcase.getSign()+"������ִ�й�����ʧ�ܻ�������...��鿴����ԭ��"+casenote);
	        caselog.caseLogDetail(taskid, testcase.getSign(), "����ִ�й�����ʧ�ܻ�������"+casenote,"error", "ending","");
		}
		//LogOperation.UpdateTastdetail(taskid, 0);
	}

	private static String runStep(Map<String, String> params, WebDriver wd,String taskid,String casenum,int stepno,LogOperation caselog) {
		String result = "";
		String property;
		String propertyValue;
		String operation;
		String operationValue;

		try {
			property = params.get("property");
			propertyValue = params.get("property_value");
			operation = params.get("operation");
			operationValue = params.get("operation_value");

			// ����ֵ����
			property = ChangString.changparams(property, variable,"��λ��ʽ");
			propertyValue=ChangString.changparams(propertyValue, variable,"��λ·��");
			operation=ChangString.changparams(operation, variable,"����");
			operationValue=ChangString.changparams(operationValue, variable,"��������");
			
			luckyclient.publicclass.LogUtil.APP.info("���ν�������������ɣ��ȴ����ж������......");
			caselog.caseLogDetail(taskid, casenum, "�������:"+operation+"; ����ֵ:"+operationValue,"info", String.valueOf(stepno),"");
		} catch (Exception e) {
			e.printStackTrace();
			luckyclient.publicclass.LogUtil.APP.error("���ν������������׳��쳣��---"+e.getMessage());
			return "��������ʧ��!";
		}

		try {		
			//���ýӿ�����
			if(null != operation&&null != operationValue&&"runcase".equals(operation)){
				String[] temp=operationValue.split(",",-1);
				String ex = TestCaseExecution.oneCaseExecuteForWebDriver(temp[0],taskid,caselog);
				if(ex.indexOf("CallCase���ó���")<=-1&&ex.indexOf("������������")<=-1&&ex.indexOf("ƥ��ʧ��")<=-1){
					return ex;
				}else{
					return "���ýӿ���������ʧ��";
				}
			}
			
			WebElement we = null;
			// ҳ��Ԫ�ز�
			if (null != property && null != propertyValue) { 
				we = isElementExist(wd, property, propertyValue);
				// �жϴ�Ԫ���Ƿ����
				if (null==we) {
					luckyclient.publicclass.LogUtil.APP.error("��λ����ʧ�ܣ�isElementExistΪnull!");
					return "isElementExist��λԪ�ع���ʧ�ܣ�";
				}

				if (operation.indexOf("select") > -1) {
					result = EncapsulateOperation.selectOperation(we, operation, operationValue);
				} else if (operation.indexOf("get") > -1){
					result = EncapsulateOperation.getOperation(wd, we, operation,operationValue);
				} else if (operation.indexOf("mouse") > -1){
					result = EncapsulateOperation.actionWeOperation(wd, we, operation, operationValue, property, propertyValue);
				} else {
					result = EncapsulateOperation.objectOperation(wd, we, operation, operationValue, property, propertyValue);
				}
				// Driver�����
			} else if (null==property && null != operation) { 				
				// ���������¼�
				if (operation.indexOf("alert") > -1){
					result = EncapsulateOperation.alertOperation(wd, operation);
				}else if(operation.indexOf("mouse") > -1){
					result = EncapsulateOperation.actionOperation(wd, operation, operationValue);
				}else{
					result = EncapsulateOperation.driverOperation(wd, operation, operationValue);
				} 				
			}else{
				luckyclient.publicclass.LogUtil.APP.error("Ԫ�ز�������ʧ�ܣ�");
				result =  "Ԫ�ز�������ʧ�ܣ�";
			}
		} catch (Exception e) {
			luckyclient.publicclass.LogUtil.APP.error("Ԫ�ض�λ���̻��ǲ�������ʧ�ܻ��쳣��"+e.getMessage());
			return "Ԫ�ض�λ���̻��ǲ�������ʧ�ܻ��쳣��" + e.getMessage();
		}
		caselog.caseLogDetail(taskid, casenum, result,"info", String.valueOf(stepno),"");
		
		if(result.indexOf("��ȡ����ֵ�ǡ�")>-1&&result.indexOf("��")>-1){
			result = result.substring(7, result.length()-1);
		}
		return result;

	}

	public static WebElement isElementExist(WebDriver wd, String property, String propertyValue) {
		try {
			WebElement we = null;
			property=property.toLowerCase();
			// ����WebElement����λ
			switch (property) {
			case "id":
				we = wd.findElement(By.id(propertyValue));
				break;
			case "name":
				we = wd.findElement(By.name(propertyValue));
				break;
			case "xpath":
				we = wd.findElement(By.xpath(propertyValue));
				break;
			case "linktext":
				we = wd.findElement(By.linkText(propertyValue));
				break;
			case "tagname":
				we = wd.findElement(By.tagName(propertyValue));
				break;
			case "cssselector":
				we = wd.findElement(By.cssSelector(propertyValue));
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
