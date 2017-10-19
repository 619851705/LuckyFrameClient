package luckyclient.caserun.exwebdriver;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import luckyclient.caserun.exwebdriver.ex.WebCaseExecution;
import luckyclient.dblog.DbLink;
import luckyclient.dblog.LogOperation;
import luckyclient.planapi.api.GetServerAPI;
import luckyclient.planapi.entity.ProjectCase;
import luckyclient.planapi.entity.ProjectCasesteps;

public class CaseLocalDebug{

	
	public static void OneCasedebug(WebDriver wd,String testCaseExternalId){
		DbLink.exetype = 1;   //����¼��־�����ݿ�
		LogOperation caselog = new LogOperation(); // ��ʼ��д��������Լ���־ģ��
		try {
			ProjectCase testcase = GetServerAPI.cgetCaseBysign(testCaseExternalId);
			luckyclient.publicclass.LogUtil.APP.info("��ʼִ����������"+testCaseExternalId+"��......");
			List<ProjectCasesteps> steps=GetServerAPI.getStepsbycaseid(testcase.getId());
			WebCaseExecution.CaseExcution(testcase,steps, "888888",wd,caselog);
			luckyclient.publicclass.LogUtil.APP.info("��ǰ��������"+testcase.getSign()+"��ִ�����......������һ��");
		} catch (Exception e) {
			luckyclient.publicclass.LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
			e.printStackTrace();
		}
        //�ر������
        wd.quit();
	}
	
	/**
	 * @param ��Ŀ��
	 * @param �������
	 * @param �����汾��
	 * ������testlink�����ú������������������������е���
	 */
	public static void MoreCaseDebug(WebDriver wd,String projectname,Map<String,Integer> addtestcase){
		System.out.println(addtestcase.size());
		@SuppressWarnings("rawtypes")
		Iterator it=addtestcase.entrySet().iterator();
		while(it.hasNext()){
		    @SuppressWarnings("rawtypes")
			Map.Entry entry=(Map.Entry)it.next();
		    String testCaseExternalId = (String)entry.getKey();
		    Integer version = (Integer)entry.getValue();
		    try{
		    luckyclient.publicclass.LogUtil.APP.info("��ʼ���÷�������Ŀ����"+projectname+"��������ţ�"+testCaseExternalId+"�������汾��"+version); 
		    OneCasedebug(wd,testCaseExternalId);
		    }catch(Exception e){
		    	continue;
		    }
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		

	}

}
