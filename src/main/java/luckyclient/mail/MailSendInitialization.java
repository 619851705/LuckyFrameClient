package luckyclient.mail;

import java.util.Properties;

import luckyclient.dblog.LogOperation;

public class MailSendInitialization {
	
	public static void SendMailInitialization(String subject,String content,String taskid){
		String[] addresses = LogOperation.GetEmailAddress(taskid);
		Properties properties = luckyclient.publicclass.SysConfig.getConfiguration();
		if(addresses!=null){
			 luckyclient.publicclass.LogUtil.APP.info("׼�������Խ�������ʼ�֪ͨ�����Եȡ�������");
			 //�������Ҫ�������ʼ�   
		      MailSenderInfo mailInfo = new MailSenderInfo(); 
		         //�������Ҫ�������ʼ�   
		      SimpleMailSender sms = new SimpleMailSender();   
		      mailInfo.setMailServerHost(properties.getProperty("mail.smtp.ip"));    
		      mailInfo.setMailServerPort(properties.getProperty("mail.smtp.port"));    
		      mailInfo.setValidate(true);    
		      mailInfo.setUserName(properties.getProperty("mail.smtp.username"));    
		      mailInfo.setPassword(properties.getProperty("mail.smtp.password"));//������������    
		      mailInfo.setFromAddress(properties.getProperty("mail.smtp.username"));    
		      mailInfo.setSubject(subject);    //����
		      mailInfo.setContent(content);     //����
		      mailInfo.setToAddresses(addresses);
			  sms.sendHtmlMail(mailInfo);
			  String addressesmail = "";
			  for(int i=0;i<addresses.length;i++){
				  addressesmail =  addressesmail+addresses[i]+";";
			  }
			  luckyclient.publicclass.LogUtil.APP.info("��"+addressesmail+"�Ĳ��Խ��֪ͨ�ʼ�������ɣ�");
		}else{
			luckyclient.publicclass.LogUtil.APP.info("��ǰ������Ҫ�����ʼ�֪ͨ��");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] taskcount = {79,78,1,0,0};
		String test = HtmlMail.HtmlContentFormat(taskcount, "220", "Status:true ��ǰ����û���ҵ���Ҫ��������Ŀ��", "Status:true ��ǰ����û���ҵ���Ҫ������TOMCAT���", "0Сʱ1��1��","testtask");
		MailSendInitialization.SendMailInitialization("test", test, "220");
	}

}
