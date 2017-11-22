package luckyclient.publicclass.remoterInterface;

public class InterfaceObject {
	/**
	 * �ӿ���
	 */
	String interfaceClass;
	/**
	 * �ӿڷ���
	 */
	String interfaceMethod;
	/**
	 * Զ�̵��õ�ַ
	 */
	String remoteUrl;
	/**
	 * �ӿڲ������ͺ�ֵ��json��������ṹ���ж����˳��
	 */
	String params;
	/**
	 * �ӿ�rpcЭ������
	 */
	String testProtocolType;

	public String getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(String interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public String getInterfaceMethod() {
		return interfaceMethod;
	}

	public void setInterfaceMethod(String interfaceMethod) {
		this.interfaceMethod = interfaceMethod;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getTestProtocolType() {
		return testProtocolType;
	}

	public void setTestProtocolType(String testProtocolType) {
		this.testProtocolType = testProtocolType;
	}

	
	 /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	 
	@Override
	public String toString() {
		return "InterfaceObject [interfaceClass=" + interfaceClass
				+ ", interfaceMethod=" + interfaceMethod + ", remoteUrl="
				+ remoteUrl + ", params=" + params + ", testProtocolType="
				+ testProtocolType + "]";
	}

	
}
