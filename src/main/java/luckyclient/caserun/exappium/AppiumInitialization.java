package luckyclient.caserun.exappium;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import luckyclient.caserun.exappium.androidex.AndroidBaseAppium;
import luckyclient.caserun.exappium.androidex.AndroidCaseExecution;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.planapi.entity.PublicCaseParams;

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
public class AppiumInitialization{
	/**
	 * ��ʼ��AndroidAppium
	 * @throws IOException 
	 */
	public static AndroidDriver<AndroidElement> setAndroidAppium() throws IOException{
		AndroidDriver<AndroidElement> appium = null;
		Properties properties = luckyclient.publicclass.AppiumConfig.getConfiguration();
    	DesiredCapabilities capabilities = new DesiredCapabilities();
    	File directory = new File("");
    	File app=new File(directory.getCanonicalPath()+"//"+properties.getProperty("appname"));
    	capabilities.setCapability("app", app.getAbsolutePath());
    	//�Զ������Է���
    	capabilities.setCapability("automationName", properties.getProperty("automationName"));
    	//�豸����
    	capabilities.setCapability("deviceName", properties.getProperty("deviceName"));
    	//ϵͳ�汾
    	capabilities.setCapability("platformVersion", properties.getProperty("platformVersion"));
    	//ģ�����ϵ�ip��ַ
    	capabilities.setCapability("udid", properties.getProperty("udid"));
    	//AndroidӦ�õİ���
    	capabilities.setCapability("appPackage",properties.getProperty("appPackage"));
    	//������Android Activity
    	capabilities.setCapability("appActivity",properties.getProperty("appActivity"));
    	//֧���������룬���Զ���װUnicode����
    	capabilities.setCapability("unicodeKeyboard", properties.getProperty("unicodeKeyboard")); 
    	//�������뷨��ԭ��״̬
        capabilities.setCapability("resetKeyboard", properties.getProperty("resetKeyboard")); 
        //������ǩ��apk
        capabilities.setCapability("noSign", properties.getProperty("noSign")); 
        //�ȴ���ʱû���յ�����ر�appium
        capabilities.setCapability("newCommandTimeout", properties.getProperty("newCommandTimeout")); 
        appium = new AndroidDriver<AndroidElement>(new URL("http://"+properties.getProperty("appiumsever")+"/wd/hub"), capabilities);
        int waittime=Integer.valueOf(properties.getProperty("implicitlyWait"));
		appium.manage().timeouts().implicitlyWait(waittime, TimeUnit.SECONDS);
		return appium;
	}

	/**
	 * ��ʼ��IOSAppium
	 * @throws IOException 
	 */
	public static IOSDriver<IOSElement> setIosAppium() throws IOException{
		IOSDriver<IOSElement> appium = null;
		Properties properties = luckyclient.publicclass.AppiumConfig.getConfiguration();
    	DesiredCapabilities capabilities = new DesiredCapabilities();
    	File directory = new File("");
    	File app=new File(directory.getCanonicalPath()+"//"+properties.getProperty("appname"));
    	capabilities.setCapability("app", app.getAbsolutePath());
    	//�Զ������Է���
    	capabilities.setCapability("automationName", properties.getProperty("automationName"));
    	//�豸����
    	capabilities.setCapability("deviceName", properties.getProperty("deviceName"));
    	//ϵͳ�汾
    	capabilities.setCapability("platformVersion", properties.getProperty("platformVersion"));
    	//ģ�����ϵ�ip��ַ
    	capabilities.setCapability("udid", properties.getProperty("udid"));
    	//AndroidӦ�õİ���
    	capabilities.setCapability("appPackage",properties.getProperty("appPackage"));
    	//������Android Activity
    	capabilities.setCapability("appActivity",properties.getProperty("appActivity"));
    	//֧���������룬���Զ���װUnicode����
    	capabilities.setCapability("unicodeKeyboard", properties.getProperty("unicodeKeyboard")); 
    	//�������뷨��ԭ��״̬
        capabilities.setCapability("resetKeyboard", properties.getProperty("resetKeyboard")); 
        //������ǩ��apk
        capabilities.setCapability("noSign", properties.getProperty("noSign")); 
        //�ȴ���ʱû���յ�����ر�appium
        capabilities.setCapability("newCommandTimeout", properties.getProperty("newCommandTimeout")); 
        appium = new IOSDriver<IOSElement>(new URL("http://"+properties.getProperty("appiumsever")+"/wd/hub"), capabilities);
        int waittime=Integer.valueOf(properties.getProperty("implicitlyWait"));
		appium.manage().timeouts().implicitlyWait(waittime, TimeUnit.SECONDS);
		return appium;
	}
	
	public static void main(String args[]) throws IOException, InterruptedException{
		AndroidDriver<AndroidElement> ad=setAndroidAppium();
		
		ProjectCase testcase = new ProjectCase();
		testcase.setName("test");
		testcase.setSign("test-1");
		ProjectCasesteps step=new ProjectCasesteps();
		step.setPath("name=����");
		step.setOperation("click");
		List<ProjectCasesteps> steps = new ArrayList<ProjectCasesteps>();
		steps.add(step);
		
		step=new ProjectCasesteps();
		step.setPath("classname=android.widget.EditText");
		step.setOperation("sendkeys");
		step.setParameters("100.01");
		steps.add(step);
		
		step=new ProjectCasesteps();
		step.setPath("name=ȷ��");
		step.setOperation("click");
		steps.add(step);
		LogOperation caselog = new LogOperation();
		List<PublicCaseParams> pcplist= new ArrayList<PublicCaseParams>();
		AndroidCaseExecution.caseExcution(testcase, steps, "888888", ad, caselog, pcplist);
		/*		
		AndroidBaseAppium.swipePageLeft(ad, 2.0, 1);
		Thread.sleep(5000);

    	ad.findElementByAndroidUIAutomator("text(\"����\")").click();
    	//driver.findElementByClassName("android.widget.EditText").click();
    	ad.findElementByClassName("android.widget.EditText").sendKeys("100.01");
    	ad.findElementByAndroidUIAutomator("text(\"ȷ��\")").click();
    	//Thread.sleep(35000);
    	System.out.println("�ύ����");
    	
    	Thread.sleep(10000);
    	System.out.println("��ȡ��ǩ��ҳ���ȡ����");
    	
    	ad.findElementById("com.ys.smartpos:id/signature_cancel").click();
    	System.out.println("ȡ��ǩ��");
    	Thread.sleep(10000);
    	
    	//ȷ����ӡ�̻���
    	ad.findElementByAndroidUIAutomator("text(\"ȷ��\")").click();
    	System.out.println("ȷ����ӡ�̻���");
    	Thread.sleep(10000);
    	System.out.println("��ȡ��Ӧ����ҳ��");
    	
    	
    	
    	String transResult=ad.findElement(By.xpath("//android.widget.ListView/android.widget.LinearLayout[21]/android.widget.TextView[3]")).getText();
    	System.out.println("��ȡ����transResultֵ��"+transResult);
    	String traceNo=ad.findElement(By.xpath("//android.widget.ListView/android.widget.LinearLayout[4]/android.widget.TextView[3]")).getText();
    	System.out.println("��ȡ����ƾ֤�ţ�"+traceNo);
    	String referNum=ad.findElement(By.xpath("//android.widget.ListView/android.widget.LinearLayout[7]/android.widget.TextView[3]")).getText();
    	System.out.println("��ȡ���˲ο��ż������ţ�"+referNum);
    	if(transResult.equals("0")){
    		System.out.println("����transResult����ֵ��ȷ");
    	}else{
    		System.out.println("����transResult����ֵ����");
    	}
    	ad.findElementByAndroidUIAutomator("text(\"ȷ��\")").click();
    	Thread.sleep(10000);*/
	}
}
