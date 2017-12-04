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
 * @date 2014��6��24�� ����9:29:40  
 * 
 */
public class InterfaceAnalyticTestLinkCase{
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
		if(scriptstr.substring(scriptstr.length()-6, scriptstr.length()).indexOf("*Wait;")>-1){                    //��Ӳ���֮��ȴ�ʱ��
        	time = scriptstr.substring(scriptstr.lastIndexOf("|")+1,scriptstr.lastIndexOf("*Wait;"));
        	scriptstr = scriptstr.substring(0, scriptstr.lastIndexOf("|")+1);
        }
		resultstr = testcasesteps.get(ordersteps-1).getExpectedResults();   //��ȡԤ�ڽ���ַ���
		String[] temp=scriptstr.split(splitFlag,-1);
		for(int i=0;i<temp.length;i++){
			if(i==0){
				String packagenage = temp[i].substring(0, temp[i].indexOf("#"));
				String functionname = temp[i].substring(temp[i].indexOf("#")+1, temp[i].indexOf(";"));
//				String functionname = temp[i].substring(0, temp[i].indexOf(";"));
				params.put("PackageName", packagenage.trim());   //set����
				params.put("FunctionName", functionname.trim());   //set��������
			}else if("".equals(temp[i])){
				continue;
			}else{
				params.put("FunctionParams"+i, temp[i]);   //set��N���������
			}
		}
		//setԤ�ڽ��
		if("".equals(resultstr)){
			params.put("ExpectedResults", "");
		}else{
			params.put("ExpectedResults", subComment(resultstr));
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
			params.put("FunctionName","������ţ�"+testcase.getFullExternalId()+"|�����쳣,��������Ϊ�ջ��������ű�����");
			return params;
     }
		return params;
	}
	
	public static String subComment(String htmlStr) throws InterruptedException{
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
    public static String trimInnerSpaceStr(String str){
        str = str.trim();
        while(str.startsWith(" ")){
        str = str.substring(1,str.length()).trim();
        }
        while(str.startsWith("&nbsp;")){
        str = str.substring(6,str.length()).trim();
        }
        while(str.endsWith(" ")){
        str = str.substring(0,str.length()-1).trim();
        }
        while(str.endsWith("&nbsp;")){
            str = str.substring(0,str.length()-6).trim();
            }
        return str;
    } 
    

    public static void main(String[] args){
		// TODO Auto-generated method stub

	}
}
