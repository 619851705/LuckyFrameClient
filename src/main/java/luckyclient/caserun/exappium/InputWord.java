package luckyclient.caserun.exappium;

import io.appium.java_client.AppiumDriver;

import java.io.IOException;

public class InputWord {
	/**
	 * @param args
	 * @throws IOException
	 * appium��֧���������� �ο���robotium����js��ʽΪԪ��ֱ������value������
	 * ����Selenium��Webdriverִ��js����ʵ����������
	 */
    public static void SendChinese(AppiumDriver appium,String preferences,String value){
        org.openqa.selenium.JavascriptExecutor jse = (org.openqa.selenium.JavascriptExecutor) appium;
        jse.executeScript("document.getElementByName('"+preferences+"').value='"+value+"'");
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
