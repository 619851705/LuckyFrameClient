package luckyclient.caserun.exwebdriver.ex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
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
public class WebDriverAnalyticCase {
	private static String splitFlag = "\\|";

	/**
	 * @param args
	 */
	@SuppressWarnings("finally")
	public static Map<String,String> AnalyticCaseStep(ProjectCase projectcase,ProjectCasesteps step,String taskid){
		String time = "0";
		Map<String,String> params = new HashMap<String,String>();
		LogOperation caselog = new LogOperation();        //��ʼ��д��������Լ���־ģ�� 
		String resultstr = null;
		try {
		if(null!=step.getPath()&&step.getPath().indexOf("=")>-1){
			String property = step.getPath().substring(0, step.getPath().indexOf("="));
			String property_value = step.getPath().substring(step.getPath().indexOf("=")+1, step.getPath().length());

			params.put("property", property.trim().toLowerCase());   //set����
			params.put("property_value", property_value.trim());   //set����ֵ
			luckyclient.publicclass.LogUtil.APP.info("�������Խ��������property:"+property.trim()+";  property_value:"+property_value.trim());		
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
			String ExpectedResults = SubComment(resultstr);

			//����check�ֶ�
			if(ExpectedResults.indexOf("check(")>-1){
				params.put("checkproperty", ExpectedResults.substring(ExpectedResults.indexOf("check(")+6, ExpectedResults.indexOf("=")));
				params.put("checkproperty_value", ExpectedResults.substring(ExpectedResults.indexOf("=")+1, ExpectedResults.lastIndexOf(")")));
			}			
			params.put("ExpectedResults", ExpectedResults);
			luckyclient.publicclass.LogUtil.APP.info("Ԥ�ڽ��������ExpectedResults:"+ExpectedResults);
		}
		
		//set waitʱ��
		if(null!=step.getAction()&&step.getAction().toLowerCase().indexOf("*wait")>-1){                    //��Ӳ���֮��ȴ�ʱ��
			String action=step.getAction();
			time=action.substring(0, action.toLowerCase().lastIndexOf("*wait"));
        }
		
		params.put("StepWait", time);
		luckyclient.publicclass.LogUtil.APP.info("������ţ�"+projectcase.getSign()+" �����ţ�"+step.getStepnum()+" �����Զ�����������ű���ɣ�");
		caselog.CaseLogDetail(taskid, projectcase.getSign(),"�����ţ�"+step.getStepnum()+" �����Զ�����������ű���ɣ�","info",String.valueOf(step.getStepnum()),"");
		}catch(Exception e) {
			luckyclient.publicclass.LogUtil.ERROR.error("������ţ�"+projectcase.getSign()+" �����ţ�"+step.getStepnum()+" �����Զ�����������ű�����");
			caselog.CaseLogDetail(taskid, projectcase.getSign(),"�����ţ�"+step.getStepnum()+" �����Զ�����������ű�����","error",String.valueOf(step.getStepnum()),"");
			luckyclient.publicclass.LogUtil.ERROR.error(e,e);
			params.put("exception","������ţ�"+projectcase.getSign()+"|�����쳣,��������Ϊ�ջ��������ű�����");
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
	}
    
}
