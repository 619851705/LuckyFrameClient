package luckyclient.publicclass;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luckyclient.planapi.entity.ProjectProtocolTemplate;
import luckyclient.planapi.entity.ProjectTemplateParams;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * �˲��Կ����Ҫ����testlink���ֲ��ܣ��������������Լ����������֣����κ����ʻ�ӭ��ϵ�������ۡ� QQ:24163551 seagull1985
 * =================================================================
 * 
 * @ClassName: InvokeMethod
 * @Description: ��̬���÷��� @author�� seagull
 * @date 2014��6��24�� ����9:29:40
 * 
 */
public class InvokeMethod {

	/**
	 * @param args
	 * @throws Throwable
	 */
	public static String CallCase(String packagename, String functionname, Object[] getParameterValues, int steptype,
			String action) {
		String result = "�����쳣����鿴������־��";
		try {
			if (steptype == 0) {
				Object server = Class.forName(packagename).newInstance(); // ���÷Ǿ�̬�����õ�
				Class[] getParameterTypes = null;
				if (getParameterValues != null) {
					int paramscount = getParameterValues.length;
					// ��ֵ���飬��������
					getParameterTypes = new Class[paramscount];
					for (int i = 0; i < paramscount; i++) {
						getParameterTypes[i] = String.class;
					}
				}
				Method method = getMethod(server.getClass().getMethods(), functionname, getParameterTypes);
				if (method == null) {
					throw new Exception(
							"�ͻ��˱���libĿ¼��û���ڰ���Ϊ��" + packagename + "�����ҵ������õķ�����" + functionname + "��,���鷽�������Լ����������Ƿ�һ�£�");
				}
				Object str = method.invoke(server, getParameterValues);
				if (str == null) {
					result = "���ؽ����null";
				} else {
					result = str.toString();
				}
			} else if (steptype == 2) {
				String templateidstr = action.substring(1, action.indexOf("��"));
				String templatenamestr = action.substring(action.indexOf("��") + 1);
				luckyclient.publicclass.LogUtil.APP
						.info("����ʹ��ģ��" + templatenamestr + " ID:" + templateidstr + " ����HTTP����");

				String httpppt = HttpRequest
						.loadJSON("/projectprotocolTemplate/cgetPTemplateById.do?templateid=" + templateidstr);
				JSONObject jsonpptObject = JSONObject.fromObject(httpppt.toString());
				ProjectProtocolTemplate ppt = (ProjectProtocolTemplate) JSONObject.toBean(jsonpptObject,
						ProjectProtocolTemplate.class);

				String httpptp = HttpRequest
						.loadJSON("/projectTemplateParams/cgetParamsByTemplate.do?templateid=" + templateidstr);
				JSONObject jsonptpObject = JSONObject.fromObject(httpptp.toString());
				JSONArray jsonarr = JSONArray.fromObject(jsonptpObject.getString("params"));
				List<ProjectTemplateParams> paramslist = JSONArray.toList(jsonarr, new ProjectTemplateParams(),
						new JsonConfig());
                //�����������
				if(null!=getParameterValues){
					for (Object obp : getParameterValues) {
						String paramob = obp.toString();
						String key = paramob.substring(0, paramob.indexOf("#"));
						String value = paramob.substring(paramob.indexOf("#") + 1);
						for (int i=0;i<paramslist.size();i++) {
							ProjectTemplateParams ptp = paramslist.get(i);
							if(ptp.getParamname().equals(key)){
								ptp.setParam(value);
								paramslist.set(i, ptp);
							}
						}
					}
				}
				Map<String, Object> params = new HashMap<String, Object>();
				for (ProjectTemplateParams ptp : paramslist) {
					params.put(ptp.getParamname().replaceAll("&quot;", "\""), ptp.getParam().replaceAll("&quot;", "\""));
				}
				
				if (functionname.toLowerCase().equals("httpurlpost")) {
					result = HttpClientHelper.sendHttpURLPost(packagename, params,
							ppt.getContentencoding().toLowerCase(),ppt.getConnecttimeout());
				} else if (functionname.toLowerCase().equals("urlpost")) {
					result = HttpClientHelper.sendURLPost(packagename, params,
							ppt.getContentencoding().toLowerCase(),ppt.getConnecttimeout());
				} else if (functionname.toLowerCase().equals("getandsavefile")) {
					String fileSavePath = System.getProperty("user.dir")+"\\HTTPSaveFile\\";
					HttpClientHelper.sendGetAndSaveFile(packagename, params,fileSavePath,ppt.getConnecttimeout());
					result = "�����ļ��ɹ�����ǰ���ͻ���·��:"+fileSavePath+" �鿴������";
				} else if (functionname.toLowerCase().equals("httpurlget")) {
					result = HttpClientHelper.sendHttpURLGet(packagename, params,
							ppt.getContentencoding().toLowerCase(),ppt.getConnecttimeout());
				} else if (functionname.toLowerCase().equals("urlget")) {
					result = HttpClientHelper.sendURLGet(packagename, params,
							ppt.getContentencoding().toLowerCase(),ppt.getConnecttimeout());
				} else if (functionname.toLowerCase().equals("httpclientpost")) {
					result = HttpClientHelper.httpClientPost(packagename, params,
							ppt.getContentencoding().toLowerCase());
				} else if (functionname.toLowerCase().equals("httpclientget")) {
					result = HttpClientHelper.httpClientGet(packagename, params,
							ppt.getContentencoding().toLowerCase());
				} else {
					luckyclient.publicclass.LogUtil.APP.error("����HTTP���������쳣����⵽�Ĳ��������ǣ�" + functionname);
					result = "�����쳣����鿴������־��";
				}
			} else if (steptype == 3) {
				String templateidstr = action.substring(1, action.indexOf("��"));
				String templatenamestr = action.substring(action.indexOf("��") + 1);
				luckyclient.publicclass.LogUtil.APP
						.info("����ʹ��ģ��" + templatenamestr + " ID:" + templateidstr + " ����SOCKET����");

				String httpppt = HttpRequest
						.loadJSON("/projectprotocolTemplate/cgetPTemplateById.do?templateid=" + templateidstr);
				JSONObject jsonpptObject = JSONObject.fromObject(httpppt.toString());
				ProjectProtocolTemplate ppt = (ProjectProtocolTemplate) JSONObject.toBean(jsonpptObject,
						ProjectProtocolTemplate.class);

				String httpptp = HttpRequest
						.loadJSON("/projectTemplateParams/cgetParamsByTemplate.do?templateid=" + templateidstr);
				JSONObject jsonptpObject = JSONObject.fromObject(httpptp.toString());
				JSONArray jsonarr = JSONArray.fromObject(jsonptpObject.getString("params"));
				List<ProjectTemplateParams> paramslist = JSONArray.toList(jsonarr, new ProjectTemplateParams(),
						new JsonConfig());
                //�����������
				if(null!=getParameterValues){
					for (Object obp : getParameterValues) {
						String paramob = obp.toString();
						String key = paramob.substring(0, action.indexOf("#"));
						String value = paramob.substring(action.indexOf("#") + 1);
						for (int i=0;i<paramslist.size();i++) {
							ProjectTemplateParams ptp = paramslist.get(i);
							if(ptp.getParamname().equals(key)){
								ptp.setParam(value);
								paramslist.set(i, ptp);
							}
						}
					}
				}
				Map<String, Object> params = new HashMap<String, Object>();
				for (ProjectTemplateParams ptp : paramslist) {
					params.put(ptp.getParamname(), ptp.getParam());
				}
				
				if (functionname.toLowerCase().equals("socketpost")) {
					result = HttpClientHelper.sendSocketPost(packagename, params,
							ppt.getContentencoding().toLowerCase());
				} else if (functionname.toLowerCase().equals("socketget")) {
					result = HttpClientHelper.sendSocketGet(packagename, params,
							ppt.getContentencoding().toLowerCase());
				} else {
					luckyclient.publicclass.LogUtil.APP.error("����SOCKET���������쳣����⵽�Ĳ��������ǣ�" + functionname);
					result = "�����쳣����鿴������־��";
				}
				
			}
		} catch (Throwable e) {
			luckyclient.publicclass.LogUtil.APP.error(e.getMessage(), e);
			return "�����쳣����鿴������־��";
		}
		return result;
	}

	public static Method getMethod(Method[] methods, String methodName, Class[] parameterTypes) {
		for (int i = 0; i < methods.length; i++) {
			if (!methods[i].getName().equals(methodName))
				continue;
			if (compareParameterTypes(parameterTypes, methods[i].getParameterTypes()))
				return methods[i];
		}
		return null;
	}

	public static boolean compareParameterTypes(Class[] parameterTypes, Class[] orgParameterTypes) {
		// parameterTypes ���棬int->Integer
		// orgParameterTypes��ԭʼ��������
		if (parameterTypes == null && orgParameterTypes == null)
			return true;
		if (parameterTypes == null && orgParameterTypes != null) {
			if (orgParameterTypes.length == 0)
				return true;
			else
				return false;
		}
		if (parameterTypes != null && orgParameterTypes == null) {
			if (parameterTypes.length == 0)
				return true;
			else
				return false;
		}
		if (parameterTypes.length != orgParameterTypes.length)
			return false;
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
