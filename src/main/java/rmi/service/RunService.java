package rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rmi.model.RunBatchCaseEntity;
import rmi.model.RunCaseEntity;
import rmi.model.RunTaskEntity;

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
//��ΪԶ�̶�����õĽӿڣ�����̳�Remote��
public interface RunService extends Remote {
    public String runtask(RunTaskEntity task,String loadpath) throws RemoteException;
    public String runcase(RunCaseEntity onecase,String loadpath) throws RemoteException;
    public String runbatchcase(RunBatchCaseEntity batchcase,String loadpath) throws RemoteException;
    public String getlogdetail(String storeName) throws RemoteException;
    public byte[] getlogimg(String imgName) throws RemoteException;
    public String uploadjar(byte[] fileContent,String name,String loadpath) throws RemoteException;
    public String webdebugcase(String sign,String executor,String loadpath) throws RemoteException;
    public String getClientStatus() throws RemoteException;
}
