package luckyclient.caserun.exappium;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

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
public class PublicAppium extends BaseAppium{
	/**
	 * ��ʼ��appium
	 */
	@SuppressWarnings("rawtypes")
	public static AppiumDriver setUpAppium(String apkname,String version,String apppackage,String appactivity){
		AppiumDriver appium = null;
		//"MobilePayment.apk"
		 File app=new File(System.getProperty("user.dir")+"//"+apkname);    
         DesiredCapabilities capabilities = new DesiredCapabilities();
         capabilities.setCapability("deviceName","Android");
         capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
          //"4.2.2"
         capabilities.setCapability(CapabilityType.VERSION, version);    
         capabilities.setCapability(CapabilityType.PLATFORM, "Android");
         capabilities.setCapability("unicodeKeyboard", "True");
         capabilities.setCapability("resetKeyboard", "True");
         capabilities.setCapability("app", app.getAbsolutePath());
         //"com.ysepay.mobileportal.activity"
         capabilities.setCapability("app-package", apppackage);   
         //"com.ysepay.mobileportal.IndexActivity"  
         capabilities.setCapability("app-activity", appactivity);         
         try {
       	 appium = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		appium.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
		return appium;
	}

}
