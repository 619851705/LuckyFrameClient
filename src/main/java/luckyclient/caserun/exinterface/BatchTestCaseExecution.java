package luckyclient.caserun.exinterface;

import luckyclient.caserun.exinterface.TestControl;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.TestTaskexcute;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BatchTestCaseExecution {
	
	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * �����̳߳أ����߳�ִ������
	 */
	
	public static void BatchCaseExecuteForTast(String projectname,String taskid,String batchcase) throws Exception{
		TestTaskexcute task=GetServerAPI.cgetTaskbyid(Integer.valueOf(taskid));
		int threadcount = task.getTestJob().getThreadCount();
		ThreadPoolExecutor	threadExecute	= new ThreadPoolExecutor(threadcount, 30, 3, TimeUnit.SECONDS,
	            new ArrayBlockingQueue<Runnable>(1000),
	            new ThreadPoolExecutor.CallerRunsPolicy());
		if(batchcase.indexOf("ALLFAIL")>-1){    //ִ��ȫ���ǳɹ�״̬����
			LogOperation caselog = new LogOperation();        //��ʼ��д��������Լ���־ģ�� 
			String casemore = caselog.UnSucCaseUpdate(taskid);
			String temp[]=casemore.split("\\#",-1);
			for(int i=0;i<temp.length;i++){
  			   String testCaseExternalId = temp[i].substring(0, temp[i].indexOf("%"));
			   int version = Integer.parseInt(temp[i].substring(temp[i].indexOf("%")+1,temp[i].length()-1));
			   TestControl.Debugcount++;   //���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
			   threadExecute.execute(new ThreadForBatchCase(projectname,testCaseExternalId,version,taskid));
			}			
		}else{                                           //����ִ������
			String temp[]=batchcase.split("\\#",-1);
			for(int i=0;i<temp.length;i++){
				String testCaseExternalId = temp[i].substring(0, temp[i].indexOf("%"));
				int version = Integer.parseInt(temp[i].substring(temp[i].indexOf("%")+1,temp[i].length()));
				TestControl.Debugcount++;   //���̼߳���++�����ڼ���߳��Ƿ�ȫ��ִ����
				threadExecute.execute(new ThreadForBatchCase(projectname,testCaseExternalId,version,taskid));
			}
		}
		//���̼߳��������ڼ���߳��Ƿ�ȫ��ִ����
		int i=0;
		while(TestControl.Debugcount!=0){
			i++;
			if(i>600){
				break;
			}
			Thread.sleep(6000);
		}
		threadExecute.shutdown();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		BatchTestCaseExecution.BatchCaseExecuteForTast("������Ŀ", "35", "ALLFAIL");
	}

}
