package luckyclient.caserun.exwebdriver;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import luckyclient.caserun.exwebdriver.ocr.Ocr;


public class EncapsulateOperation {

	public static String SelectOperation(WebElement we,String operation,String operation_value) throws Exception{
		String result = "";
		// �����������
		Select select = new Select(we);

		// �����������¼�
		switch (operation) {
		case "selectbyvisibletext":
			select.selectByVisibleText(operation_value);
			luckyclient.publicclass.LogUtil.APP.info("���������ͨ��VisibleText����ѡ��...��VisibleText����ֵ:" + operation_value + "��");
			break;
		case "selectbyvalue":
			select.selectByValue(operation_value);
			luckyclient.publicclass.LogUtil.APP.info("���������ͨ��Value����ѡ��...��Value����ֵ:" + operation_value + "��");
			break;
		case "selectbyindex":
			select.selectByIndex(Integer.valueOf(operation_value));
			luckyclient.publicclass.LogUtil.APP.info("���������ͨ��Index����ѡ��...��Index����ֵ:" + operation_value + "��");
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
	
	public static String GetOperation(WebDriver wd,WebElement we,String operation) throws Exception{
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
		case "getcaptcha":
			result = "��ȡ����ֵ�ǡ�"+Ocr.getCAPTCHA(wd, we)+"��";
			luckyclient.publicclass.LogUtil.APP.info("getcaptcha��ȡ��֤��...����֤��ֵ:" + result + "��");
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String ActionWeOperation(WebDriver wd,WebElement we,String operation,String operation_value,String property,String property_value) throws Exception{
		String result = "";
		Actions action = new Actions(wd);
		// action����
		switch (operation) {
		case "mouselkclick":  //���������
			action.click(we).perform();
			result = "mouselkclick�������������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("mouselkclick�������������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��");
			break;
		case "mouserkclick":
			action.contextClick(we).perform();
			result = "mouserkclick����Ҽ��������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("mouserkclick����Ҽ��������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��");
			break;
		case "mousedclick":
			action.doubleClick(we).perform();
			result = "mousedclick���˫������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("mousedclick���˫������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��");
			break;
		case "mouseclickhold":
			action.clickAndHold(we).perform();
			result = "mouseclickhold�����������ͷ�...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("mouseclickhold�����������ͷ�...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��");
			break;
		case "mousedrag":
			String temp[]=operation_value.split(",",-1);
			action.dragAndDropBy(we, Integer.valueOf(temp[0]), Integer.valueOf(temp[1])).perform();
			result = "mousedrag����ƶ��������������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��  ����x��"+Integer.valueOf(temp[0])
			+" ����y��"+Integer.valueOf(temp[1]);
			luckyclient.publicclass.LogUtil.APP.info("mousedrag����ƶ��������������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��  ����x��"+Integer.valueOf(temp[0])
			+" ����y��"+Integer.valueOf(temp[1]));
			break;
		case "mouseto":
			String temp1[]=operation_value.split(",",-1);
			action.moveToElement(we, Integer.valueOf(temp1[0]), Integer.valueOf(temp1[1])).perform();
			result = "mouseto����ƶ��������������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��  ����x��"+Integer.valueOf(temp1[0])
			+" ����y��"+Integer.valueOf(temp1[1]);
			luckyclient.publicclass.LogUtil.APP.info("mouseto����ƶ��������������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��  ����x��"+Integer.valueOf(temp1[0])
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
	
	public static String ActionOperation(WebDriver wd,String operation,String operation_value) throws Exception{
		String result = "";
		Actions action = new Actions(wd);
		// action����
		switch (operation) {
		case "mouselkclick":  //���������
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
			String temp1[]=operation_value.split(",",-1);
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
			switch (operation_value) {
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
	
	public static String ObjectOperation(WebDriver wd,WebElement we,String operation,String operation_value,String property,String property_value) throws Exception{
		String result = "";
		// ����WebElement�������
		switch (operation) {
		case "click":
			we.click();
			result = "click�������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("click�������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��");
			break;
		case "sendkeys":
			we.sendKeys(operation_value);
			result = "sendKeys��������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"; ����ֵ:"+operation_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("sendkeys��������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"; ����ֵ:"+operation_value+"��");
			break;
		case "clear":
			we.clear();
			result = "clear��������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("clear��������...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��");
			break; // ��������
		case "gotoframe":
			wd.switchTo().frame(we);
			luckyclient.publicclass.LogUtil.APP.info("gotoframe�л�Frame...������λ����:"+property+"; ��λ����ֵ:"+property_value+"��");
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
			jse.executeScript(operation_value,we);
			result = "ִ��JS...��"+operation_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("ִ��JS...��"+operation_value+"��");
			break;
		default:
			break;
		}
		return result;
	}
	
	public static String AlertOperation(WebDriver wd,String operation) throws Exception{
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
	
	public static String DriverOperation(WebDriver wd,String operation,String operation_value) throws Exception{
		String result = "";
		// ����ҳ��������
		switch (operation) {
		case "open":
			operation_value = "http://"+operation_value;
			wd.get(operation_value);
			result = "Openҳ��...��"+operation_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("Openҳ��...��"+operation_value+"��");
			break;
		case "exjs":
			JavascriptExecutor jse = (JavascriptExecutor)wd;
			jse.executeScript(operation_value);
			result = "ִ��JS...��"+operation_value+"��";
			luckyclient.publicclass.LogUtil.APP.info("ִ��JS...��"+operation_value+"��");
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
			wd.switchTo().window(operation_value);
			luckyclient.publicclass.LogUtil.APP.info("gotowindow�л����ָ������...");
			break;
		case "wait":
			try {
				wd.wait(Integer.valueOf(operation_value) * 1000);
				result = "��ǰ��������ȴ���"+operation_value+"����...";
				luckyclient.publicclass.LogUtil.APP.info("��ǰ��������ȴ���"+operation_value+"����...");
				break;
			} catch (NumberFormatException | InterruptedException e) {
				luckyclient.publicclass.LogUtil.APP.error("�ȴ�ʱ��ת������ ��");
				e.printStackTrace();
				result = "�ȴ�ʱ��ת���������������";
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