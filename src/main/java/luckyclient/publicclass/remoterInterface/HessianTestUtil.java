package luckyclient.publicclass.remoterInterface;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.caucho.hessian.client.HessianProxyFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HessianTestUtil {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final int connect_time_out = 30 * 1000;
	private static final Map<String, Class<?>> base_type = new HashMap<String, Class<?>>();
	static {
		base_type.put("long.class", long.class);
		base_type.put("long.class", long.class);
		base_type.put("float.class", float.class);
		base_type.put("boolean.class", boolean.class);
		base_type.put("char.class", char.class);
		base_type.put("byte.class", byte.class);
		// base_type.put("void.class", void.class);
		base_type.put("short.class", short.class);
		base_type.put("long", long.class);
		base_type.put("float", float.class);
		base_type.put("boolean", boolean.class);
		base_type.put("char", char.class);
		base_type.put("byte", byte.class);
		// base_type.put("void", void.class);
		base_type.put("short", short.class);

	}
	
	public static Map<String, Object> startInvoke(InterfaceObject object)
			throws Exception {
		// ��ȡ����������б����������ͣ�����ֵ������ת��InterfaceParamObject����
		InterfaceParamObject[] paramContent = mapper
				.readValue(object.getParams(), InterfaceParamObject[].class);
		Map<String, Object> resultMap = hessianInvoke(object.getRemoteUrl(),
				object.getInterfaceClass(), object.getInterfaceMethod(),
				paramContent);
		return resultMap;

	}
	
	private static Map<String, Object> hessianInvoke(String remoteUrl,
			String className, String methodName,
			InterfaceParamObject[] interfaceParamArray) throws Exception {
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setReadTimeout(connect_time_out);
		// factory.setConnectTimeout(connect_time_out);
		// factory.setReadTimeout(read_time_out);
		Object InterfaceObject = null;
		Class<?> allRequstParasClass[] = new Class[interfaceParamArray.length];
		Object[] allRequstParas = new Object[interfaceParamArray.length];
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			InterfaceObject = factory.create(Class.forName(className),remoteUrl);
		} catch (Exception ex) {
			throw ex;
		}
		
		// ��ȡ��ȫ���������������Ա�ֵ�����ȡ����ʱʹ��
				try {
					for (int index = 0; index < interfaceParamArray.length; index++) {
						String tempParaClassName = interfaceParamArray[index]
								.getClassname();
						if (isBaseTypeForArray(tempParaClassName)) {
							// ����ǻ����������ͣ���ômapperת���ٻ�ȡ�����;ͻ������⣬����ֵ����ֿ�
							allRequstParasClass[index] = base_type
									.get(tempParaClassName);
							allRequstParas[index] = mapper.readValue(
									interfaceParamArray[index].getValue(),
									base_type.get(tempParaClassName));
						} else {
							allRequstParasClass[index] = Class
									.forName(tempParaClassName);
							allRequstParas[index] = mapper.readValue(
									interfaceParamArray[index].getValue(),
									allRequstParasClass[index]);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
				
				try {
					Method method = InterfaceObject.getClass().getMethod(methodName,
							allRequstParasClass);
					Class<?> returnClass = method.getReturnType();
					Object result = method.invoke(InterfaceObject, allRequstParas);

					if (returnClass == void.class) {
						resultMap.put("������������Ϊvoid", "ִ�н�����û�з���ֵ");
					} else if (isBaseTypeForArray(returnClass.getName())) {
						resultMap.put(returnClass.getName(), result);
					} else {
						resultMap = objectToMap(result);
					}
				} catch (NoSuchMethodException e) {
					resultMap.put("�޷���ȡ����", methodName);
					e.printStackTrace();
					throw e;
				} catch (SecurityException e) {
					resultMap.put("�޷���ȡ����", methodName);
					e.printStackTrace();
					throw e;
				} catch (IllegalAccessException e) {
					resultMap.put("�޷�������Ȩ��", methodName);
					e.printStackTrace();
					throw e;
				} catch (IllegalArgumentException e) {
					resultMap.put("���������Ƿ�", methodName);
					e.printStackTrace();
					throw e;
				} catch (InvocationTargetException e) {
					resultMap.put("ִ�з���ʱ����", methodName);
					resultMap.put("������Ϣ", e.getCause().getMessage());
					e.printStackTrace();
					throw e;
				} catch (Throwable t) {
					resultMap.put("δ֪��������", t.getCause().getMessage());
					t.printStackTrace();
					throw t;
				}
				return resultMap;
			}

			public static Map<String, Object> objectToMap(Object obj) throws Exception {
				Map<String, Object> map = new HashMap<String, Object>();
				String ObjClass = obj.getClass().getName();
				if (isBaseTypeForArray(ObjClass)) {
					map.put(ObjClass, obj.toString());
				} else {
					// Field[] allField = obj.getClass().getDeclaredFields();
					Field[] allField = getAllFields(obj);
					for (Field field : allField) {
						field.setAccessible(true);
						map.put(field.getName(), field.get(obj));
					}

				}
				return map;
			}

			private static Field[] getAllFields(Object object) {
				Class<?> clazz = object.getClass();
				List<Field> fieldList = new ArrayList<>();
				while (clazz != null) {
					fieldList.addAll(
							new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
					clazz = clazz.getSuperclass();
				}
				Field[] fields = new Field[fieldList.size()];
				fieldList.toArray(fields);
				return fields;
			}

			private static boolean isBaseTypeForArray(String ObjClassName) {

				boolean isBase = false;
				if (ObjClassName != null) {
					isBase = base_type.get(ObjClassName) != null ? true : false;
				}
				return isBase;
			}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
