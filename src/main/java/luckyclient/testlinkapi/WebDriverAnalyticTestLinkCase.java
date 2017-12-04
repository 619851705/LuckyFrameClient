package luckyclient.testlinkapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import luckyclient.dblog.LogOperation;
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
public class WebDriverAnalyticTestLinkCase {
	private static String splitFlag = "\\|";

	/**
	 * @param args
	 */
	@SuppressWarnings("finally")
	public static Map<String,String> analyticCaseStep(TestCase testcase,Integer ordersteps,String tastid,LogOperation caselog){
		String time = "0";
		Map<String,String> params = new HashMap<String,String>();

		String resultstr = null;
		try {	
		List<TestCaseStep> testcasesteps = (List<TestCaseStep>) testcase.getSteps();
		String stepsstr = testcasesteps.get(ordersteps-1).getActions();    //��ȡactions�ַ���
		String scriptstr = subComment(stepsstr);
		if(scriptstr.substring(scriptstr.length()-6, scriptstr.length()).toLowerCase().indexOf("*wait;")>-1){                    //��Ӳ���֮��ȴ�ʱ��
        	time = scriptstr.substring(scriptstr.lastIndexOf("|")+1,scriptstr.toLowerCase().lastIndexOf("*wait;"));
        	scriptstr = scriptstr.substring(0, scriptstr.lastIndexOf("|")+1);
        }
		resultstr = testcasesteps.get(ordersteps-1).getExpectedResults();   //��ȡԤ�ڽ���ַ���
		String[] temp=scriptstr.split(splitFlag,-1);
		for(int i=0;i<temp.length;i++){
			if(i==0&&temp[i].indexOf("=")>-1&&(temp.length>2||!"".equals(temp[1]))){
				String property = temp[i].substring(0, temp[i].indexOf("="));
				String propertyValue = temp[i].substring(temp[i].indexOf("=")+1, temp[i].length());

				params.put("property", property.trim().toLowerCase());   //set����
				params.put("property_value", propertyValue.trim());   //set����ֵ
				luckyclient.publicclass.LogUtil.APP.info("�������Խ��������property:"+property.trim()+";  property_value:"+propertyValue.trim());
			}else if("".equals(temp[i])){
				continue;
			}else{
				String operation = null;
				String operationValue = null;
				if(temp[i].indexOf("(")>-1&&temp[i].indexOf(")")>-1){
					operation = temp[i].substring(0, temp[i].indexOf("("));
					operationValue = temp[i].substring(temp[i].indexOf("(")+1, temp[i].lastIndexOf(")"));
				}else{
					operation = temp[i];
				}
				params.put("operation", operation.toLowerCase());   //set��������
				params.put("operation_value", operationValue);   //set����ֵ
				luckyclient.publicclass.LogUtil.APP.info("����������������operation:"+operation+";  operation_value:"+operationValue);
			}
		}
		//setԤ�ڽ��
		if(resultstr.equals("")){
			params.put("ExpectedResults", "");
		}else{
			String expectedResults = subComment(resultstr);

			//����check�ֶ�
			if(expectedResults.indexOf("check(")>-1){
				params.put("checkproperty", expectedResults.substring(expectedResults.indexOf("check(")+6, expectedResults.indexOf("=")));
				params.put("checkproperty_value", expectedResults.substring(expectedResults.indexOf("=")+1, expectedResults.indexOf(")")));
			}			
			params.put("ExpectedResults", expectedResults);
			luckyclient.publicclass.LogUtil.APP.info("Ԥ�ڽ��������ExpectedResults:"+expectedResults);
		}
		params.put("StepWait", time);
		luckyclient.publicclass.LogUtil.APP.info("������ţ�"+testcase.getFullExternalId()+" �����ţ�"+ordersteps+" �����Զ�����������ű���ɣ�");
		if(null!=caselog){
			caselog.caseLogDetail(tastid, testcase.getFullExternalId(),"�����ţ�"+ordersteps+" �����Զ�����������ű���ɣ�","info",String.valueOf(ordersteps),"");
		}
		}catch(Exception e) {
			luckyclient.publicclass.LogUtil.ERROR.error("������ţ�"+testcase.getFullExternalId()+" �����ţ�"+ordersteps+" �����Զ�����������ű�����");
			if(null!=caselog){
			  caselog.caseLogDetail(tastid, testcase.getFullExternalId(),"�����ţ�"+ordersteps+" �����Զ�����������ű�����","error",String.valueOf(ordersteps),"");
			}
			luckyclient.publicclass.LogUtil.ERROR.error(e,e);
			params.put("exception","������ţ�"+testcase.getFullExternalId()+"|�����쳣,��������Ϊ�ջ��������ű�����");
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
/*		Thread.sleep(20000);
		System.out.println(test.stopServer());*/

	}
    
}
