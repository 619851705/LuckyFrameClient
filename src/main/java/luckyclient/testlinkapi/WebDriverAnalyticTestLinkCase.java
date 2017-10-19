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
 * �˲��Կ����Ҫ����testlink���ֲ��ܣ��������������Լ����������֣����κ����ʻ�ӭ��ϵ�������ۡ�
 * QQ:24163551 seagull1985
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
	public static Map<String,String> AnalyticCaseStep(TestCase testcase,Integer ordersteps,String tastid,LogOperation caselog){
		String time = "0";
		Map<String,String> params = new HashMap<String,String>();

		String resultstr = null;
		try {	
		List<TestCaseStep> testcasesteps = (List<TestCaseStep>) testcase.getSteps();
		String stepsstr = testcasesteps.get(ordersteps-1).getActions();    //��ȡactions�ַ���
		String scriptstr = SubComment(stepsstr);
		if(scriptstr.substring(scriptstr.length()-6, scriptstr.length()).toLowerCase().indexOf("*wait;")>-1){                    //��Ӳ���֮��ȴ�ʱ��
        	time = scriptstr.substring(scriptstr.lastIndexOf("|")+1,scriptstr.toLowerCase().lastIndexOf("*wait;"));
        	scriptstr = scriptstr.substring(0, scriptstr.lastIndexOf("|")+1);
        }
		resultstr = testcasesteps.get(ordersteps-1).getExpectedResults();   //��ȡԤ�ڽ���ַ���
		String temp[]=scriptstr.split(splitFlag,-1);
		for(int i=0;i<temp.length;i++){
			if(i==0&&temp[i].indexOf("=")>-1&&(temp.length>2||!"".equals(temp[1]))){
				String property = temp[i].substring(0, temp[i].indexOf("="));
				String property_value = temp[i].substring(temp[i].indexOf("=")+1, temp[i].length());

				params.put("property", property.trim().toLowerCase());   //set����
				params.put("property_value", property_value.trim());   //set����ֵ
				luckyclient.publicclass.LogUtil.APP.info("�������Խ��������property:"+property.trim()+";  property_value:"+property_value.trim());
			}else if("".equals(temp[i])){
				continue;
			}else{
				String operation = null;
				String operation_value = null;
				if(temp[i].indexOf("(")>-1&&temp[i].indexOf(")")>-1){
					operation = temp[i].substring(0, temp[i].indexOf("("));
					operation_value = temp[i].substring(temp[i].indexOf("(")+1, temp[i].lastIndexOf(")"));
				}else{
					operation = temp[i];
				}
				params.put("operation", operation.toLowerCase());   //set��������
				params.put("operation_value", operation_value);   //set����ֵ
				luckyclient.publicclass.LogUtil.APP.info("����������������operation:"+operation+";  operation_value:"+operation_value);
			}
		}
		//setԤ�ڽ��
		if(resultstr.equals("")){
			params.put("ExpectedResults", "");
		}else{
			String ExpectedResults = SubComment(resultstr);

			//����check�ֶ�
			if(ExpectedResults.indexOf("check(")>-1){
				params.put("checkproperty", ExpectedResults.substring(ExpectedResults.indexOf("check(")+6, ExpectedResults.indexOf("=")));
				params.put("checkproperty_value", ExpectedResults.substring(ExpectedResults.indexOf("=")+1, ExpectedResults.indexOf(")")));
			}			
			params.put("ExpectedResults", ExpectedResults);
			luckyclient.publicclass.LogUtil.APP.info("Ԥ�ڽ��������ExpectedResults:"+ExpectedResults);
		}
		params.put("StepWait", time);
		luckyclient.publicclass.LogUtil.APP.info("������ţ�"+testcase.getFullExternalId()+" �����ţ�"+ordersteps+" �����Զ�����������ű���ɣ�");
		if(null!=caselog){
			caselog.CaseLogDetail(tastid, testcase.getFullExternalId(),"�����ţ�"+ordersteps+" �����Զ�����������ű���ɣ�","info",String.valueOf(ordersteps),"");
		}
		}catch(Exception e) {
			luckyclient.publicclass.LogUtil.ERROR.error("������ţ�"+testcase.getFullExternalId()+" �����ţ�"+ordersteps+" �����Զ�����������ű�����");
			if(null!=caselog){
			  caselog.CaseLogDetail(tastid, testcase.getFullExternalId(),"�����ţ�"+ordersteps+" �����Զ�����������ű�����","error",String.valueOf(ordersteps),"");
			}
			luckyclient.publicclass.LogUtil.ERROR.error(e,e);
			params.put("exception","������ţ�"+testcase.getFullExternalId()+"|�����쳣,��������Ϊ�ջ��������ű�����");
     }finally{
 		return params;
     }
	}
	
	private static String SubComment(String htmlStr) throws InterruptedException{
    	String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // ����script��������ʽ
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // ����style��������ʽ
        String regEx_html = "<[^>]+>"; // ����HTML��ǩ��������ʽ
        String regEx_space = "\t|\r|\n";//����ո�س����з�
        
        String scriptstr = null;
        if (htmlStr!=null) {
            Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            Matcher m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // ����script��ǩ
       
            Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // ����style��ǩ
       
            Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // ����html��ǩ
       
            Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
            Matcher m_space = p_space.matcher(htmlStr);
            htmlStr = m_space.replaceAll(""); // ���˿ո�س���ǩ
            
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
