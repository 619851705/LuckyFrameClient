package luckyclient.jenkinsapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class JenkinsBuilding {
	/**
     * ��ָ��URL����GET����������
     * ���𹹽�����
     * @param buildname
     *            jenkins�еĹ�������
     * @param param
     *            �ӳٶ�������й���
     * @return URL ������Զ����Դ����Ӧ���
     */
    public static String sendBuilding(String buildname, int param) {
        String result = "";
        BufferedReader in = null;
        try {
        	final String jenkinsurl = "http://10.211.19.19:18080/jenkins/job/";
        	
            String urlString = jenkinsurl+buildname + "/build?delay="+param+"sec";
                      
            URL realUrl = new URL(urlString);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                luckyclient.publicclass.LogUtil.APP.info(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            luckyclient.publicclass.LogUtil.APP.error("���͹�������(GET)ʱ�����쳣��", e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    
	/**
     * ��ָ��URL����GET����������
     * �ж����һ�ι�����û�гɹ�
     * @param buildname
     *            jenkins�еĹ�������
     * @return URL ������Զ����Դ����Ӧ���
     * 
     * alt="Success"  alt="In progress"  alt="Failed"
     */
    public static String BuildingResult(String buildname) {
        String result = "";
        BufferedReader in = null;
        try {
        	final String jenkinsurl = "http://10.211.19.19:18080/jenkins/job/";
        	
            String urlString = jenkinsurl+buildname + "/lastBuild/";
                      
            URL realUrl = new URL(urlString);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                luckyclient.publicclass.LogUtil.APP.info(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            luckyclient.publicclass.LogUtil.APP.error("���͹�������(GET)ʱ�����쳣��", e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//���� GET ����
		//http://10.211.19.19:18080/jenkins/job/72_deploy_settle_check_server/lastBuild/
        String s=JenkinsBuilding.BuildingResult("deploy-abc-b2cApi");
        System.out.println(s);
	}

}
