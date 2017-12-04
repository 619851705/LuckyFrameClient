package luckyclient.caserun.exappium;

import io.appium.java_client.AppiumDriver;

import java.io.IOException;

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
public class InputWord {
	/**
	 * @param args
	 * @throws IOException
	 * appium��֧���������� �ο���robotium����js��ʽΪԪ��ֱ������value������
	 * ����Selenium��Webdriverִ��js����ʵ����������
	 */
    public static void sendChinese(AppiumDriver appium,String preferences,String value){
        org.openqa.selenium.JavascriptExecutor jse = (org.openqa.selenium.JavascriptExecutor) appium;
        jse.executeScript("document.getElementByName('"+preferences+"').value='"+value+"'");
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
