package luckyclient.caserun.exinterface;

import luckyclient.caserun.exinterface.testlink.TestLinkCaseExecution;

public class ThreadForBatchCase extends Thread{
	
	private String projectname;
	private String testCaseExternalId;
	private int version;
	private String tastid;
	
	public ThreadForBatchCase(String projectname,String testCaseExternalId,int version,String tastid){
		this.projectname = projectname;
		this.testCaseExternalId = testCaseExternalId;
		this.version = version;
		this.tastid = tastid;
	}
	
	public void run(){		
		 TestCaseExecution.OneCaseExecuteForTast(projectname, testCaseExternalId, version, tastid);
		 TestControl.Debugcount--;        //���̼߳���--�����ڼ���߳��Ƿ�ȫ��ִ����
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
