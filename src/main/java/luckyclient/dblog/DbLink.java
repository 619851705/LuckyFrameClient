package luckyclient.dblog;

import java.util.Properties;

import luckyclient.publicclass.DBOperation;

public class DbLink {
	
	/**
	 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * �˲��Կ����Ҫ����testlink���ֲ��ܣ��������������Լ����������֣����κ����ʻ�ӭ��ϵ�������ۡ�
 * QQ:24163551 seagull1985
	 * =================================================================
	 * @ClassName: DbLogLink 
	 * @Description: ����������־���ݿ����ӵ�ַ
	 * @author�� seagull
	 * @date 2015��4��20�� ����9:29:40  
	 * 
	 */
	public  static DBOperation DbLogLink(){
		Properties properties = luckyclient.publicclass.SysConfig.getConfiguration();
		String url_base = "jdbc:mysql://"+properties.getProperty("mysql.db.ip")+":"+properties.getProperty("mysql.db.port")
		+"/"+properties.getProperty("mysql.db.dbname")+"?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false";
		String username_base = properties.getProperty("mysql.db.username");
		String password_base = properties.getProperty("mysql.db.userpwd");
		return new DBOperation(url_base, username_base, password_base);
	}

	public static int exetype;      //����ִ�����ͣ� 0   �������ģʽ    1   ����̨ģʽ

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
