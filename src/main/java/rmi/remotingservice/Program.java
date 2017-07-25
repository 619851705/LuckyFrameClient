package rmi.remotingservice;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import rmi.service.RunService;
import rmi.serviceImpl.RunServiceImpl;


public class Program{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        try {
        	RunService runService=new RunServiceImpl();
			//ע��ͨѶ�˿�
			LocateRegistry.createRegistry(6633);
			//ע��ͨѶ·��
			Naming.rebind("rmi://localhost:6633/RunService", runService);
			System.out.println("�����ͻ��˼���...�˿ڣ�6633");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}