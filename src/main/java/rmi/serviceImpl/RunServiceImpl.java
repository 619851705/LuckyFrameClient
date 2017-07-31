package rmi.serviceImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import rmi.model.RunBatchCaseEntity;
import rmi.model.RunCaseEntity;
import rmi.model.RunTaskEntity;
import rmi.service.RunService;



//��ΪԶ�̶����ʵ���࣬��̳�UnicastRemoteObject
public class RunServiceImpl extends UnicastRemoteObject implements RunService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RunServiceImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String runtask(RunTaskEntity task) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("��������ģʽ���Գ���...������Ŀ��"+task.getProjectname()+"  ����ID��"+task.getTaskid());
		try{
			Runtime run = Runtime.getRuntime();
			run.exec("cmd.exe /k start " + "task.cmd" +" "+ task.getTaskid(), null,new File(System.getProperty("user.dir")+"\\"));
			
		} catch (Exception e) {		
			e.printStackTrace();
			return "��������ģʽ���Գ����쳣������";
		} 
		return "��������ģʽ���Գ�������";
	}
	
	@Override
	public String runcase(RunCaseEntity onecase) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("����������ģʽ���Գ���...������Ŀ��"+onecase.getProjectname()+"  ����ID��"+onecase.getTaskid());
		System.out.println("����������ţ�"+onecase.getTestCaseExternalId()+"  �����汾��"+onecase.getVersion());
		try{
			Runtime run = Runtime.getRuntime();
			StringBuffer sb=new StringBuffer();
			sb.append(onecase.getTaskid()).append(" ");
			sb.append(onecase.getTestCaseExternalId()).append(" ");
			sb.append(onecase.getVersion());
			run.exec("cmd.exe /k start " + "task_onecase.cmd" + " " +sb.toString(), null,new File(System.getProperty("user.dir")+"\\"));			
		} catch (Exception e) {		
			e.printStackTrace();
			return "����������ģʽ���Գ����쳣������";
		} 
		return "����������ģʽ���Գ�������";
	}
	
	@Override
	public String runbatchcase(RunBatchCaseEntity batchcase) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("������������ģʽ���Գ���...������Ŀ��"+batchcase.getProjectname()+"  ����ID��"+batchcase.getTaskid());
		System.out.println("��������������"+batchcase.getBatchcase());
		try{
			Runtime run = Runtime.getRuntime();
			StringBuffer sb=new StringBuffer();
			sb.append(batchcase.getTaskid()).append(" ");
			sb.append(batchcase.getBatchcase());
			System.out.println(sb.toString());
			run.exec("cmd.exe /k start " + "task_batch.cmd" + " " +sb.toString(), null,new File(System.getProperty("user.dir")+"\\"));		
		} catch (Exception e) {		
			e.printStackTrace();
			return "������������ģʽ���Գ����쳣������";
		} 
		return "������������ģʽ���Գ�������";
	}
	
	/**
	 * ��ȡ�ͻ�����־
	 * 
	 * @param request
	 * @param response
	 * @param storeName
	 * @param contentType
	 * @param realName
	 * @throws Exception
	 */
	public String getlogdetail(String storeName) throws RemoteException{
		BufferedReader bos = null;
		String ctxPath = System.getProperty("user.dir")+"\\log\\";
		String downLoadPath = ctxPath + storeName;

		String str = "";
		InputStreamReader isr=null;
		try {
			isr = new InputStreamReader(new FileInputStream(downLoadPath), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "��ȡ��־·����������ͻ�����־·���Ƿ����!downLoadPath: "+downLoadPath;
		}
		bos = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		try {
			while ((str = bos.readLine()) != null)
			{
				sb.append(str).append("\n");
			}
			bos.close();
			System.out.println("����˶�ȡ������־�ɹ�!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "�ͻ���תBufferedReaderʧ�ܣ�����ԭ��";
		}
		return sb.toString();
	}
	
	/**
	 * ��ȡ�ͻ��˽�ͼ
	 * 
	 * @param request
	 * @param response
	 * @param storeName
	 * @param contentType
	 * @param realName
	 * @throws Exception
	 */
	public byte[] getlogimg(String imgName) throws RemoteException{
		String ctxPath = System.getProperty("user.dir")+"\\log\\ScreenShot\\";
		String downLoadPath = ctxPath+imgName;
        byte[] b = null;
        try {
            File file = new File(downLoadPath);
            b = new byte[(int) file.length()];
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            is.read(b);
            is.close();
            System.out.println("����˻�ȡ����ͼƬ��"+downLoadPath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
	}
	
	/**
	 * �ϴ�JAR��
	 * 
	 * @param request
	 * @param response
	 * @param storeName
	 * @param contentType
	 * @param realName
	 * @throws Exception
	 */
	public String uploadjar(byte[] fileContent,String name) throws RemoteException{
		String path = System.getProperty("user.dir")+"\\lib\\";
		String pathName = path + name;
		File file = new File(pathName);
        try {
            if (file.exists()){
            	file.deleteOnExit();
            }
            file.createNewFile();
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            os.write(fileContent);
            os.flush();
            os.close();
            System.out.println("������ϴ�JAR��("+name+")�����ؿͻ���libĿ¼�ɹ�!");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "�ͻ���δ�ҵ���ȷ·�����ļ����ϴ�ʧ�ܣ�";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "�ͻ���IOException";
        }
         return "�ϴ�"+name+"���ͻ��˳ɹ���";
	}
	
	
	public static void main(String[] args) throws RemoteException {
	}
}
