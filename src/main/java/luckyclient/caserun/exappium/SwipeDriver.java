package luckyclient.caserun.exappium;

import java.util.HashMap;

import org.openqa.selenium.JavascriptExecutor;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

class SwipeDriver {
	
	/**
	 * @param args
	 * TouchAction  ֧�ֶԴ����Ĳ��ֲ���
	 */
	private static TouchAction SwipeTouchAction(AppiumDriver appium){
		TouchAction swipetouch = new TouchAction(appium);
		return swipetouch;
	}	
	
	/**
	 * @param args
	 * js webview  ֧��4.1��4.4
	 */   
	private static void webview_swipe(AppiumDriver appium, Double sX, Double sY,
            Double eX, Double eY, Double duration) throws Exception {
        JavascriptExecutor js;
        HashMap<String, Double> swipeObject;
        try {
                // ����
                js = (JavascriptExecutor) appium;
                swipeObject = new HashMap<String, Double>();
                swipeObject.put("startX", sX);
                swipeObject.put("startY", sY);
                swipeObject.put("endX", eX);
                swipeObject.put("endY", eY);
                swipeObject.put("duration", duration);
                js.executeScript("mobile: swipe", swipeObject);
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
            throw ex;
        } finally {
            // �ͷű���
        }

    }
    
//	/**
//	 * @param args
//	 * js webview  ֧��4.1��4.4
//	 */   
//    public static void webview_swipe2(AppiumDriver appium, Double sX, Double sY,
//            Double mX, Double mY,Double mX2, Double mY2, Double eX, Double eY, Double duration) throws Exception {
//        JavascriptExecutor js;
//        HashMap<String, Double> swipeObject;
//        try {
//                // ����
//                js = (JavascriptExecutor) appium;
//                swipeObject = new HashMap<String, Double>();
//                swipeObject.put("startX", sX);
//                swipeObject.put("startY", sY);
//                swipeObject.put("middX", mX);
//                swipeObject.put("middY", mY);
//                swipeObject.put("middX2", mX2);
//                swipeObject.put("middY2", mY2);
//                swipeObject.put("endX", eX);
//                swipeObject.put("endY", eY);
//                swipeObject.put("duration", duration);
//                js.executeScript("mobile: swipe", swipeObject);
//        } catch (Exception ex) {
//            // TODO Auto-generated catch block
//            ex.printStackTrace();
//            throw ex;
//        } finally {
//            // �ͷű���
//        }
//
//    }
    
	/**
	 * @param args
	 * ���� ADBֱ�ӻ���  ֧��4.1��4.4
	 */   
	private static void adb_swipe(AppiumDriver appium, Double sX, Double sY,
            Double eX, Double eY) throws Exception {
        int X;
        int Y;
        int sX2;
        int sY2;
        int eX2;
        int eY2;
        try {
                // ����
        	X = appium.manage().window().getSize().getWidth();
            Y = appium.manage().window().getSize().getHeight();

            sX2 = (int) (X * sX);
            sY2 = (int) (Y * sY);
            eX2 = (int) (X * eX);
            eY2 = (int) (Y * eY);
            // logger.info("����11111111");
            Runtime.getRuntime().exec(
                    "adb -s " + "Android" + " shell input swipe " + sX2 + " "
                            + sY2 + " " + eX2 + " " + eY2);
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
            throw ex;
        } finally {
            // �ͷű���
        }

    }
    
	/**
	 * @param args
	 * ��Ļ����¼�
	 */  
	private static void clickScreen(AppiumDriver drivers,int x, int y, int duration) {
    		JavascriptExecutor js = (JavascriptExecutor) drivers;
    		HashMap<String, Integer> tapObject = new HashMap<String, Integer>();
    		tapObject.put("x", x);
    		tapObject.put("y", y);
    		tapObject.put("duration", duration);
    		js.executeScript("mobile: tap", tapObject);
    		}
}
