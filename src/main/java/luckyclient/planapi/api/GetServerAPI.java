package luckyclient.planapi.api;

import java.io.UnsupportedEncodingException;
import java.util.List;

import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;
import luckyclient.planapi.entity.PublicCaseParams;
import luckyclient.planapi.entity.TestTaskexcute;
import luckyclient.publicclass.remoterinterface.HttpRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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
public class GetServerAPI {

	/**
	 * ͨ���ƻ�ID��ȡ������������
	 * @param planid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ProjectCase> getCasesbyplanid(int planid) {
		String result = HttpRequest.loadJSON("/projectPlanCase/cgetcasebyplanid.do?planid=" + planid);
		JSONObject jsonObject = JSONObject.fromObject(result.toString());
		JSONArray jsonarr = JSONArray.fromObject(jsonObject.getString("cases"));
		List<ProjectCase> list = JSONArray.toList(jsonarr, new ProjectCase(), new JsonConfig());
		return list;
	}

	/**
	 * ͨ���ƻ����ƻ�ȡ������������
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ProjectCase> getCasesbyplanname(String name) {
		String result = HttpRequest.loadJSON("/projectPlanCase/cgetcasebyplanname.do?name=" + name);
		JSONObject jsonObject = JSONObject.fromObject(result.toString());
		JSONArray jsonarr = JSONArray.fromObject(jsonObject.getString("cases"));
		List<ProjectCase> list = JSONArray.toList(jsonarr, new ProjectCase(), new JsonConfig());
		return list;
	}

	/**
	 * ͨ������ID��ȡ����Ĳ������
	 * @param caseid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ProjectCasesteps> getStepsbycaseid(int caseid) {
		String result = HttpRequest.loadJSON("/projectCasesteps/cgetStepsByCase.do?caseid=" + caseid);
		JSONObject jsonObject = JSONObject.fromObject(result.toString());
		JSONArray jsonarr = JSONArray.fromObject(jsonObject.getString("steps"));
		List<ProjectCasesteps> list = JSONArray.toList(jsonarr, new ProjectCasesteps(), new JsonConfig());
		return list;
	}

	/**
	 * ͨ��taskid��ȡ����
	 * @param taskid
	 * @return
	 */
	public static TestTaskexcute cgetTaskbyid(int taskid) {
		String result = HttpRequest.loadJSON("/tastExecute/cgettaskbyid.do?taskid=" + taskid);
		JSONObject jsonObject = JSONObject.fromObject(result.toString());
		TestTaskexcute task = (TestTaskexcute) JSONObject.toBean(jsonObject, TestTaskexcute.class);
		return task;
	}

	/**
	 * ͨ��������Ż�ȡ����
	 * @param sign
	 * @return
	 */
	public static ProjectCase cgetCaseBysign(String sign) {
		String result = HttpRequest.loadJSON("/projectCase/cgetcasebysign.do?sign=" + sign);
		JSONObject jsonObject = JSONObject.fromObject(result.toString());
		ProjectCase pc = (ProjectCase) JSONObject.toBean(jsonObject, ProjectCase.class);
		return pc;
	}

	/**
	 * ��ȡ��Ŀ�µ����й�������
	 * @param projectid
	 * @return
	 */
	public static List<PublicCaseParams> cgetParamsByProjectid(String projectid) {
		String result = HttpRequest.loadJSON("/publicCaseParams/cgetParamsByProjectid.do?projectid="+projectid);
		JSONObject jsonObject = JSONObject.fromObject(result.toString());
		JSONArray jsonarr = JSONArray.fromObject(jsonObject.getString("params"));
		@SuppressWarnings("unchecked")
		List<PublicCaseParams> list = JSONArray.toList(jsonarr, new PublicCaseParams(), new JsonConfig());
		return list;
	}
	
	/**
	 * put web��������ݵ������
	 * @param sign
	 * @param executor
	 * @param loglevel
	 * @param detail
	 */
	public static void cPostDebugLog(String sign, String executor, String loglevel, String detail){
		sign = sign.replace("%","BBFFHH");
		sign = sign.replace("=","DHDHDH");
		sign = sign.replace("&","ANDAND");
		detail = detail.replace("%","BBFFHH");
		detail = detail.replace("=","DHDHDH");
		detail = detail.replace("&","ANDAND");
		String params = "";
		params = "sign=" + sign;
		params += "&executor=" + executor;
		params += "&executor=" + executor;
		params += "&loglevel=" + loglevel;
		params += "&detail=" + detail;

		String logid = HttpRequest.sendPost("/projectCasesteps/cPostDebugLog.do", params);
		System.out.println("�Ѿ��ɹ�д����ʱ��־�⣬ID��" + logid);
	}

	/**
	 * ������ϵͳԶ�̵��õ�����������ִ��
	 * @param jobid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String cRunJobForId(String jobid) throws UnsupportedEncodingException {
		String result = HttpRequest.loadJSON("/testJobs/runJobForInterface.do?jobid=" + jobid);
		return new String(result.getBytes("GBK"), "UTF-8");
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		getCasesbyplanid(79);
	}

}
