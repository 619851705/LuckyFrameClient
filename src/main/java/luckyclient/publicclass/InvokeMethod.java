package luckyclient.publicclass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * =================================================================
 * ����һ�������Ƶ�������������������κ�δ�������ǰ���¶Գ����������޸ĺ�������ҵ��;��Ҳ������Գ�������޸ĺ����κ���ʽ�κ�Ŀ�ĵ��ٷ�����
 * �˲��Կ����Ҫ����testlink���ֲ��ܣ��������������Լ����������֣����κ����ʻ�ӭ��ϵ�������ۡ�
 * QQ:24163551 seagull1985
 * =================================================================
 * @ClassName: InvokeMethod 
 * @Description: ��̬���÷���
 * @author�� seagull
 * @date 2014��6��24�� ����9:29:40  
 * 
 */
public class InvokeMethod {

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static String CallCase(String packagename,String functionname,Object[] getParameterValues){
		try{
		Object server = Class.forName(packagename).newInstance();   //���÷Ǿ�̬�����õ�
		Class[] getParameterTypes = null;
		if(getParameterValues!=null){
			int paramscount = getParameterValues.length;
			//��ֵ���飬��������
			getParameterTypes  = new Class[paramscount];
			for(int i=0;i<paramscount;i++){			
				getParameterTypes[i]=String.class;
			}
		}
		Method method = getMethod(server.getClass().getMethods(), functionname,getParameterTypes);
		if (method==null){
			throw new Exception("�ͻ��˱���libĿ¼��û���ڰ���Ϊ��"+packagename+"�����ҵ������õķ�����"
					+functionname+"��,���鷽�������Լ����������Ƿ�һ�£�");
		}
		Object str=method.invoke(server,getParameterValues);
		if(str==null){
			return  "���ؽ����null";
		}else{
			return str.toString();
		}
	}catch(Throwable e){
		luckyclient.publicclass.LogUtil.ERROR.error(e.getMessage(), e);
		return "�����쳣����鿴������־��";
	}
	//	return str==null?"���ؽ����null":str;
	}
	
	public static Method getMethod(Method[] methods, String methodName, Class[] parameterTypes)
	{
		for (int i = 0; i < methods.length; i++)
		{
			if (!methods[i].getName().equals(methodName))
				continue;
			if (compareParameterTypes(parameterTypes, methods[i].getParameterTypes()))
				return methods[i];
		}
		return null;
	}
	
	public static boolean compareParameterTypes(Class[] parameterTypes, Class[] orgParameterTypes)
	{
		// parameterTypes ���棬int->Integer
		// orgParameterTypes��ԭʼ��������
		if (parameterTypes == null && orgParameterTypes == null)
			return true;
		if (parameterTypes == null && orgParameterTypes != null)
		{
			if (orgParameterTypes.length == 0)
				return true;
			else
				return false;
		}
		if (parameterTypes != null && orgParameterTypes == null)
		{
			if (parameterTypes.length == 0)
				return true;
			else
				return false;
		}
		if (parameterTypes.length != orgParameterTypes.length)
			return false;
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
