package luckyclient.caserun.exwebdriver.ex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import luckyclient.dblog.LogOperation;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * Ϊ���������ߵ��Ͷ��ɹ���LuckyFrame�ؼ���Ȩ��Ϣ�Ͻ��۸�
 * ���κ����ʻ�ӭ��ϵ�������ۡ� QQ:1573584944  seagull1985
 * =================================================================
 * @ClassName: AnalyticCase 
 * @Description: ���������������������ֵĽű�
 * @author�� seagull
 * @date 2016��9��18�� 
 * 
 */
public class WebDriverAnalyticCase {
	//private static String splitFlag = "\\|";

	/**
	 * @param args
	 */
	@SuppressWarnings("finally")
	public static Map<String,String> analyticCaseStep(ProjectCase projectcase,ProjectCasesteps step,String taskid,LogOperation caselog){
		String time = "0";
		Map<String,String> params = new HashMap<String,String>();

		String resultstr = null;
		try {
		if(null!=step.getPath()&&step.getPath().indexOf("=")>-1){
			String property = step.getPath().substring(0, step.getPath().indexOf("="));
			String propertyValue = step.getPath().substring(step.getPath().indexOf("=")+1, step.getPath().length());

			params.put("property", property.trim().toLowerCase());   //set����
			params.put("property_value", propertyValue.trim());   //set����ֵ
			luckyclient.publicclass.LogUtil.APP.info("�������Խ��������property:"+property.trim()+";  property_value:"+propertyValue.trim());		
		}
		params.put("operation", step.getOperation().toLowerCase());   //set��������
		if(null!=step.getParameters()&&!"".equals(step.getParameters())){
			params.put("operation_value", step.getParameters());   //set����ֵ
		}
		luckyclient.publicclass.LogUtil.APP.info("����������������operation:"+step.getOperation().toLowerCase()+";  operation_value:"+step.getParameters());
		resultstr = step.getExpectedresult();   //��ȡԤ�ڽ���ַ���

		//setԤ�ڽ��
		if(null!=resultstr&&"".equals(resultstr)){
			params.put("ExpectedResults", "");
		}else if(null!=resultstr){
			String expectedResults = subComment(resultstr);

			//����check�ֶ�
			if(expectedResults.indexOf("check(")>-1){
				params.put("checkproperty", expectedResults.substring(expectedResults.indexOf("check(")+6, expectedResults.indexOf("=")));
				params.put("checkproperty_value", expectedResults.substring(expectedResults.indexOf("=")+1, expectedResults.lastIndexOf(")")));
			}			
			params.put("ExpectedResults", expectedResults);
			luckyclient.publicclass.LogUtil.APP.info("Ԥ�ڽ��������ExpectedResults:"+expectedResults);
		}
		
		//set waitʱ��
		if(null!=step.getAction()&&step.getAction().toLowerCase().indexOf("*wait")>-1){                    //��Ӳ���֮��ȴ�ʱ��
			String action=step.getAction();
			time=action.substring(0, action.toLowerCase().lastIndexOf("*wait"));
        }
		
		params.put("StepWait", time);
		luckyclient.publicclass.LogUtil.APP.info("������ţ�"+projectcase.getSign()+" �����ţ�"+step.getStepnum()+" �����Զ�����������ű���ɣ�");
		if(null!=caselog){
		  caselog.caseLogDetail(taskid, projectcase.getSign(),"�����ţ�"+step.getStepnum()+" �����Զ�����������ű���ɣ�","info",String.valueOf(step.getStepnum()),"");
		}
		}catch(Exception e) {
			luckyclient.publicclass.LogUtil.ERROR.error("������ţ�"+projectcase.getSign()+" �����ţ�"+step.getStepnum()+" �����Զ�����������ű�����");
			if(null!=caselog){
			  caselog.caseLogDetail(taskid, projectcase.getSign(),"�����ţ�"+step.getStepnum()+" �����Զ�����������ű�����","error",String.valueOf(step.getStepnum()),"");
			}
			luckyclient.publicclass.LogUtil.ERROR.error(e,e);
			params.put("exception","������ţ�"+projectcase.getSign()+"|�����쳣,��������Ϊ�ջ��������ű�����");
			return params;
     }
		return params;
	}
	
	private static String subComment(String htmlStr) throws InterruptedException{
    	String regExScript = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // ����script��������ʽ
        String regExStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // ����style��������ʽ
        String regExHtml = "<[^>]+>"; // ����HTML��ǩ��������ʽ
        String regExSpace = "\t|\r|\n";//����ո�س����з�
        
        String scriptstr = null;
        if (htmlStr!=null) {
            Pattern pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
            Matcher mScript = pScript.matcher(htmlStr);
            htmlStr = mScript.replaceAll(""); // ����script��ǩ
       
            Pattern pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
            Matcher mStyle = pStyle.matcher(htmlStr);
            htmlStr = mStyle.replaceAll(""); // ����style��ǩ
       
            Pattern pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
            Matcher mHtml = pHtml.matcher(htmlStr);
            htmlStr = mHtml.replaceAll(""); // ����html��ǩ
       
            Pattern pSpace = Pattern.compile(regExSpace, Pattern.CASE_INSENSITIVE);
            Matcher mSpace = pSpace.matcher(htmlStr);
            htmlStr = mSpace.replaceAll(""); // ���˿ո�س���ǩ
            
        }
        if(htmlStr.indexOf("/*")>-1&&htmlStr.indexOf("*/")>-1){
    		String commentstr = htmlStr.substring(htmlStr.trim().indexOf("/*"),htmlStr.indexOf("*/")+2);
    		scriptstr = htmlStr.replace(commentstr, "");     //ȥע��
        }else{
        	scriptstr = htmlStr;
        }
        
        scriptstr = trimInnerSpaceStr(scriptstr);          //ȥ���ַ���ǰ��Ŀո�
        scriptstr = scriptstr.replaceAll("&nbsp;", " ");  //�滻�ո�ת��
        scriptstr = scriptstr.replaceAll("&quot;", "\""); //ת��˫����
        scriptstr = scriptstr.replaceAll("&#39;", "\'");  //ת�嵥����
        scriptstr = scriptstr.replaceAll("&amp;", "&");  //ת�����ӷ�
        scriptstr = scriptstr.replaceAll("&lt;", "<");  
        scriptstr = scriptstr.replaceAll("&gt;", ">");  
        
		return scriptstr;
	}

	/***
     * ȥ���ַ���ǰ��Ŀո��м�Ŀո���
     * @param str
     * @return
     */
	public static String trimInnerSpaceStr(String str) {
		str = str.trim();
		while (str.startsWith(" ")) {
			str = str.substring(1, str.length()).trim();
		}
		while (str.startsWith("&nbsp;")) {
			str = str.substring(6, str.length()).trim();
		}
		while (str.endsWith(" ")) {
			str = str.substring(0, str.length() - 1).trim();
		}
		while (str.endsWith("&nbsp;")) {
			str = str.substring(0, str.length() - 6).trim();
		}
		return str;
	}

    public static void main(String[] args){
		// TODO Auto-generated method stub
	}
    
}
