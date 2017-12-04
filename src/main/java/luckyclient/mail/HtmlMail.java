package luckyclient.mail;

import java.util.HashMap;
import java.util.Map;

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
public class HtmlMail {
	static FreemarkerEmailTemplate  fet=new FreemarkerEmailTemplate();
	
	public static String htmlContentFormat(int[] taskcount,String taskid,String buildstatus,String restartstatus,String time,String jobname){		
		Map<Object, Object> parameters = new HashMap<Object, Object>();
		parameters.put("buildstatus", buildstatus);
		parameters.put("restartstatus", restartstatus);
		parameters.put("taskcount", taskcount);
		parameters.put("time", time);
		parameters.put("taskid", taskid);
		parameters.put("jobname", jobname);
		return fet.getText("task-body", parameters);
	}
	
	public static String htmlSubjectFormat(String jobname){
		Map<Object, Object> parameters = new HashMap<Object, Object>();
		parameters.put("jobname", jobname);
		return fet.getText("task-title", parameters);
	}

}
