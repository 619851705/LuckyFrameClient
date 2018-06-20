package luckyclient.driven;

import java.util.regex.Pattern;

public class SubString {
	
	/**
	 * ��ȡָ���ַ������м��ֶ�
	 * @param str
	 * @param startstr
	 * @param endstr
	 * @return
	 */
	public static String subCentreStr(String str,String startstr,String endstr){
		String getstr=str.substring(str.indexOf(startstr)+startstr.length(), str.indexOf(endstr));
		return getstr;
	}
	
	/**
	 * ��ȡ�ַ�����ָ���ַ���ʼ
	 * @param str
	 * @param startstr
	 * @return
	 */
	public static String subStartStr(String str,String startstr){
		String getstr=str.substring(str.indexOf(startstr)+startstr.length());
		return getstr;
	}
	
	/**
	 * ��ȡ�ַ�����ָ���ַ�����
	 * @param str
	 * @param startstr
	 * @return
	 */
	public static String subEndStr(String str,String endstr){
		String getstr=str.substring(0,str.indexOf(endstr));
		return getstr;
	}
	
	/**
	 * ͨ���ַ���λ�ý�ȡָ���ַ������м��ֶ�
	 * @param str
	 * @param startstr
	 * @param endstr
	 * @return
	 */
	public static String subCentreNum(String str,String startnum,String endnum){
		String getstr="";
		if(isInteger(startnum)&&isInteger(endnum)){
			int start=Integer.valueOf(startnum);
			int end=Integer.valueOf(endnum);
			if(start>end){
				getstr="��ȡ�ַ�����ʼλ�����ֲ��ܴ��ڽ���λ������";
			}else if(start<0||end<0){
				getstr="��ȡ�ַ���λ�õ����ֲ���С��0";
			}else if(start>str.length()||end>str.length()){
				getstr="��ȡ�ַ���λ�õ����ֲ��ܴ����ַ�������ĳ��ȡ�"+str.length()+"��";
			}else{
				getstr=str.substring(start,end);
			}
		}else{
			getstr="";
		}

		return getstr;
	}
	
	/**
	 * ͨ���ַ���λ�ý�ȡ�ַ�����ָ���ַ���ʼ
	 * @param str
	 * @param startstr
	 * @return
	 */
	public static String subStartNum(String str,String startnum){
		String getstr="";
		if(isInteger(startnum)){
			int start=Integer.valueOf(startnum);
			if(start<0){
				getstr="��ȡ�ַ���λ�õ����ֲ���С��0";
			}else if(start>str.length()){
				getstr="��ȡ�ַ���λ�õ����ֲ��ܴ����ַ�������ĳ��ȡ�"+str.length()+"��";
			}else{
				getstr=str.substring(start);
			}
		}else{
			getstr="";
		}

		return getstr;
	}
	
	/**
	 * ��ȡ�ַ�����ָ���ַ�����
	 * @param str
	 * @param startstr
	 * @return
	 */
	public static String subEndNum(String str,String endnum){
		String getstr="";
		if(isInteger(endnum)){
			int end=Integer.valueOf(endnum);
			if(end<0){
				getstr="��ȡ�ַ���λ�õ����ֲ���С��0";
			}else if(end>str.length()){
				getstr="��ȡ�ַ���λ�õ����ֲ��ܴ����ַ�������ĳ��ȡ�"+str.length()+"��";
			}else{
				getstr=str.substring(0,end);
			}
		}else{
			getstr="";
		}

		return getstr;
	}
	
	private static boolean isInteger(String str) {  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
  }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
