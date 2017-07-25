package luckyclient.jenkinsapi;

import luckyclient.dblog.LogOperation;

public class BuildingInitialization {
	
	public static String BooleanBuildingOver(String[] buildname) throws InterruptedException{
		String buildresult = "Status:true"+" ��Ŀȫ�������ɹ���";
		int k;
		for(int i=0;i<300;i++){
			k=0;
			for(int j=0;j<buildname.length;j++){
				String result = JenkinsBuilding.BuildingResult(buildname[i]);
				if(result.indexOf("alt=\"Failed\"")>-1){
					buildresult = "��Ŀ"+buildname[i]+"����ʧ�ܣ��Զ��������˳���";
					luckyclient.publicclass.LogUtil.APP.error("��Ŀ"+buildname[i]+"����ʧ�ܣ��Զ��������˳���");
					break;
				}else if(result.indexOf("alt=\"Success\"")>-1){
					k++;
				}
			}
			if(buildresult.indexOf("Status:true")<=-1){
				break;
			}
			luckyclient.publicclass.LogUtil.APP.info("���ڼ�鹹���е���Ŀ(ÿ6����һ��)��������Ҫ������Ŀ"+buildname.length+"����Ŀǰ�ɹ�"+k+"��");
			if(k==buildname.length){
				break;
			}			
			Thread.sleep(6000);
		}
		return buildresult;
	}

	@SuppressWarnings("finally")
	public static String BuildingRun(String tastid) throws InterruptedException{
		String result = "Status:true"+" ��ǰ����û���ҵ���Ҫ��������Ŀ��";
		try{
		String[] buildname = LogOperation.GetBuildName(tastid);
		
		if(buildname!=null){
			luckyclient.publicclass.LogUtil.APP.info("׼�������õĲ�����Ŀ���й��������Եȡ�������");
			for(int i=0;i<buildname.length;i++){
				JenkinsBuilding.sendBuilding(buildname[i], 0);
			}
			Thread.sleep(10000);  //�ȴ��������
			result = BooleanBuildingOver(buildname);
		}else{
			luckyclient.publicclass.LogUtil.APP.info("��ǰ����û���ҵ���Ҫ��������Ŀ��");
		}
		}catch(Exception e){
			luckyclient.publicclass.LogUtil.APP.error("��Ŀ���������г����쳣");
			luckyclient.publicclass.LogUtil.APP.error(e);
			result = "��Ŀ���������г����쳣";
		}finally{
			return result;
		}

	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
