package luckyclient.caserun.exwebdriver;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import luckyclient.caserun.exwebdriver.ocr.Ocr;

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
public class EncapsulateOperation {

	public static String selectOperation(WebElement we,String operation,String operationValue) throws Exception{
		String result = "";
		// �����������
		Select select = new Select(we);

		// �����������¼�
		switch (operation) {
		case "selectbyvisibletext":
			select.selectByVisibleText(operationValue);
			luckyclient.publicclass.LogUtil.APP.info("���������ͨ��VisibleText����ѡ��...��VisibleText����ֵ:" + operationValue + "��");
			break;
		case "selectbyvalue":
			select.selectByValue(operationValue);
			luckyclient.publicclass.LogUtil.APP.info("���������ͨ��Value����ѡ��...��Value����ֵ:" + operationValue + "��");
			break;
		case "selectbyindex":
			select.selectByIndex(Integer.valueOf(operationValue));
			luckyclient.publicclass.LogUtil.APP.info("���������ͨ��Index����ѡ��...��Index����ֵ:" + operationValue + "��");
			break;
		case "isselect":
			result = "��ȡ����ֵ�ǡ�"+we.isSelected()+"��";
			luckyclient.publicclass.LogUtil.APP.info("�ж϶����Ƿ��Ѿ���ѡ��...�����ֵ:" + we.isSelected() + "��");
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String getOperation(WebDriver wd,WebElement we,String operation,String value) throws Exception{
		String result = "";
		// ��ȡ������
		switch (operation) {
		case "gettext":
			result = "��ȡ����ֵ�ǡ�"+we.getText()+"��";
			luckyclient.publicclass.LogUtil.APP.info("getText��ȡ����text����...��text����ֵ:" + result + "��");
			break; // ��ȡ���������
		case "gettagname":
			result = "��ȡ����ֵ�ǡ�"+we.getTagName()+"��";
			luckyclient.publicclass.LogUtil.APP.info("getTagName��ȡ����tagname����...��tagname����ֵ:" + result + "��");
			break;
		case "getattribute":
			result = "��ȡ����ֵ�ǡ�"+we.getAttribute(value)+"��";
			luckyclient.publicclass.LogUtil.APP.info("getAttribute��ȡ����"+value+"������...��"+value+"����ֵ:" + result + "��");
			break;
		case "getcssvalue":
			result = "��ȡ����ֵ�ǡ�"+we.getCssValue(value)+"��";
			luckyclient.publicclass.LogUtil.APP.info("getCssValue��ȡ����"+value+"������...��"+value+"����ֵ:" + result + "��");
			break;
		case "getcaptcha":
			result = "��ȡ����ֵ�ǡ�"+Ocr.getCAPTCHA(wd, we)+"��";
			luckyclient.publicclass.LogUtil.APP.info("getcaptcha��ȡ��֤��...����֤��ֵ:" + result + "��");
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String actionWeOperation(WebDriver wd,WebElement we,String operation,String operationValue,String property,String propertyValue) throws Exception{
		String result = "";
		Actions action = new Actions(wd);
		// action����
		switch (operation) {
		//���������
		case "mouselkclick":
			action.click(we).perform();
			result = "mouselkclick�������������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("mouselkclick�������������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��");
			break;
		case "mouserkclick":
			action.contextClick(we).perform();
			result = "mouserkclick����Ҽ��������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("mouserkclick����Ҽ��������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��");
			break;
		case "mousedclick":
			action.doubleClick(we).perform();
			result = "mousedclick���˫������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("mousedclick���˫������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��");
			break;
		case "mouseclickhold":
			action.clickAndHold(we).perform();
			result = "mouseclickhold�����������ͷ�...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("mouseclickhold�����������ͷ�...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��");
			break;
		case "mousedrag":
			String[] temp=operationValue.split(",",-1);
			action.dragAndDropBy(we, Integer.valueOf(temp[0]), Integer.valueOf(temp[1])).perform();
			result = "mousedrag����ƶ��������������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��  ����x��"+Integer.valueOf(temp[0])
			+" ����y��"+Integer.valueOf(temp[1]);
			luckyclient.publicclass.LogUtil.APP.info("mousedrag����ƶ��������������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��  ����x��"+Integer.valueOf(temp[0])
			+" ����y��"+Integer.valueOf(temp[1]));
			break;
		case "mouseto":
			String[] temp1=operationValue.split(",",-1);
			action.moveToElement(we, Integer.valueOf(temp1[0]), Integer.valueOf(temp1[1])).perform();
			result = "mouseto����ƶ��������������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��  ����x��"+Integer.valueOf(temp1[0])
			+" ����y��"+Integer.valueOf(temp1[1]);
			luckyclient.publicclass.LogUtil.APP.info("mouseto����ƶ��������������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��  ����x��"+Integer.valueOf(temp1[0])
			+" ����y��"+Integer.valueOf(temp1[1]));
			break;
		case "mouserelease":
			action.release(we).perform();
			result = "mouserelease����ͷ�...";
			luckyclient.publicclass.LogUtil.APP.info("mouserelease����ͷ�...");
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String actionOperation(WebDriver wd,String operation,String operationValue) throws Exception{
		String result = "";
		Actions action = new Actions(wd);
		// action����
		switch (operation) {
		//���������
		case "mouselkclick": 
			action.click().perform();
			result = "mouselkclick�����������ǰλ��...";
			luckyclient.publicclass.LogUtil.APP.info("mouselkclick�����������ǰλ��...");
			break;
		case "mouserkclick":
			action.contextClick().perform();
			result = "mouserkclick����Ҽ������ǰλ��...";
			luckyclient.publicclass.LogUtil.APP.info("mouserkclick����Ҽ������ǰλ��...");
			break;
		case "mousedclick":
			action.doubleClick().perform();
			result = "mousedclick���˫����ǰλ��...";
			luckyclient.publicclass.LogUtil.APP.info("mousedclick���˫����ǰλ��...");
			break;
		case "mouseclickhold":
			action.clickAndHold().perform();
			result = "mouseclickhold�������ǰλ�ú��ͷ�...";
			luckyclient.publicclass.LogUtil.APP.info("mouseclickhold�������ǰλ�ú��ͷ�...");
			break;
		case "mouseto":
			String[] temp1=operationValue.split(",",-1);
			action.moveByOffset(Integer.valueOf(temp1[0]), Integer.valueOf(temp1[1])).perform();
			result = "mouseto����ƶ��������������...����x��"+Integer.valueOf(temp1[0])
			+" ����y��"+Integer.valueOf(temp1[1]);
			luckyclient.publicclass.LogUtil.APP.info("mouseto����ƶ��������������... ����x��"+Integer.valueOf(temp1[0])
			+" ����y��"+Integer.valueOf(temp1[1]));
			break;
		case "mouserelease":
			action.release().perform();
			result = "mouserelease����ͷ�...";
			luckyclient.publicclass.LogUtil.APP.info("mouserelease����ͷ�...");
			break;
		case "mousekey":
			switch (operationValue) {
			case "tab":
				action.sendKeys(Keys.TAB).perform();
				result = "���̲���TAB��...";
				luckyclient.publicclass.LogUtil.APP.info("���̲���TAB��...");
				break;
			case "space":
				action.sendKeys(Keys.SPACE).perform();
				result = "���̲���SPACE��...";
				luckyclient.publicclass.LogUtil.APP.info("���̲���SPACE��...");
				break;
			case "ctrl":
				action.sendKeys(Keys.CONTROL).perform();
				result = "���̲���CONTROL��...";
				luckyclient.publicclass.LogUtil.APP.info("���̲���CONTROL��...");
				break;
			case "shift":
				action.sendKeys(Keys.SHIFT).perform();
				result = "���̲���SHIFT��...";
				luckyclient.publicclass.LogUtil.APP.info("���̲���SHIFT��...");
				break;
			case "enter":
				action.sendKeys(Keys.ENTER).perform();
				result = "���̲���SHIFT��...";
				luckyclient.publicclass.LogUtil.APP.info("���̲���SHIFT��...");
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String objectOperation(WebDriver wd,WebElement we,String operation,String operationValue,String property,String propertyValue) throws Exception{
		String result = "";
		// ����WebElement�������
		switch (operation) {
		case "click":
			we.click();
			result = "click�������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("click�������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��");
			break;
		case "sendkeys":
			we.sendKeys(operationValue);
			result = "sendKeys��������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"; ����ֵ:"+operationValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("sendkeys��������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"; ����ֵ:"+operationValue+"��");
			break;
		case "clear":
			we.clear();
			result = "clear��������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("clear��������...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��");
			break; // ��������
		case "gotoframe":
			wd.switchTo().frame(we);
			luckyclient.publicclass.LogUtil.APP.info("gotoframe�л�Frame...������λ����:"+property+"; ��λ����ֵ:"+propertyValue+"��");
			break;
		case "isenabled":
			result = "��ȡ����ֵ�ǡ�"+we.isEnabled()+"��";
			luckyclient.publicclass.LogUtil.APP.info("��ǰ�����ж��Ƿ���ò���ֵΪ��"+we.isEnabled()+"��");
			break;
		case "isdisplayed":
			result = "��ȡ����ֵ�ǡ�"+we.isDisplayed()+"��";
			luckyclient.publicclass.LogUtil.APP.info("��ǰ�����ж��Ƿ�ɼ�����ֵΪ��"+we.isDisplayed()+"��");
			break;
		case "exjsob":
			JavascriptExecutor jse = (JavascriptExecutor)wd;
			jse.executeScript(operationValue,we);
			result = "ִ��JS...��"+operationValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("ִ��JS...��"+operationValue+"��");
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String alertOperation(WebDriver wd,String operation) throws Exception{
		String result = "";
		Alert alert = wd.switchTo().alert();
		switch (operation) {
		case "alertaccept":
			alert.accept();
			luckyclient.publicclass.LogUtil.APP.info("�����������ͬ��...");
			break;
		case "alertdismiss":
			alert.dismiss();
			luckyclient.publicclass.LogUtil.APP.info("�����������ȡ��...");
			break;
		case "alertgettext":
			result = "��ȡ����ֵ�ǡ�"+alert.getText()+"��";
			luckyclient.publicclass.LogUtil.APP.info("���������ͨ��getText��ȡ����text����...��Text����ֵ:" + alert.getText() + "��");
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String driverOperation(WebDriver wd,String operation,String operationValue) throws Exception{
		String result = "";
		// ����ҳ��������
		switch (operation) {
		case "open":
			wd.get(operationValue);
			result = "Openҳ��...��"+operationValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("Openҳ��...��"+operationValue+"��");
			break;
		case "exjs":
			JavascriptExecutor jse = (JavascriptExecutor)wd;
			jse.executeScript(operationValue);
			result = "ִ��JS...��"+operationValue+"��";
			luckyclient.publicclass.LogUtil.APP.info("ִ��JS...��"+operationValue+"��");
			break;
		case "gotodefaultcontent":
			wd.switchTo().defaultContent();
			luckyclient.publicclass.LogUtil.APP.info("gotodefaultcontent�л���Ĭ��ҳ��λ��...");
			break;
		case "gettitle":
			result = "��ȡ����ֵ�ǡ�"+wd.getTitle()+"��";
			luckyclient.publicclass.LogUtil.APP.info("��ȡҳ��Title...��"+result+"��");
			break;
		case "getwindowhandle":
			result = "��ȡ����ֵ�ǡ�"+wd.getWindowHandle()+"��";
			luckyclient.publicclass.LogUtil.APP.info("getWindowHandle��ȡ���ھ��...�����ֵ:" + result + "��");
			break;
		case "gotowindow":
			wd.switchTo().window(operationValue);
			luckyclient.publicclass.LogUtil.APP.info("gotowindow�л����ָ������...");
			break;
		case "wait":
			try {
				wd.wait(Integer.valueOf(operationValue) * 1000);
				result = "��ǰ��������ȴ���"+operationValue+"����...";
				luckyclient.publicclass.LogUtil.APP.info("��ǰ��������ȴ���"+operationValue+"����...");
				break;
			} catch (NumberFormatException | InterruptedException e) {
				luckyclient.publicclass.LogUtil.APP.error("�ȴ�ʱ��ת������ ��");
				e.printStackTrace();
				result = "�ȴ�ʱ��ת�������������";
				break;
			}
		default:
			break;
		}
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
