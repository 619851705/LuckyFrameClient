package luckyclient.publicclass.remoterInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.client.HessianProxyFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InterfaceTestUtil {
	private static Logger log = LoggerFactory
			.getLogger(InterfaceTestUtil.class);
	private static final ObjectMapper map = new ObjectMapper();
	private static final int read_time_out = 30 * 1000;
	private static final Map<String, Class<?>> base_type = new HashMap<String, Class<?>>();
	static {
		base_type.put("long", long.class);
		base_type.put("double", double.class);
		base_type.put("float", float.class);
		base_type.put("bool", boolean.class);
		base_type.put("char", char.class);
		base_type.put("byte", byte.class);
		base_type.put("void", void.class);
		base_type.put("short", short.class);
	}

	public static Map<String, Object> doTest(InterfaceObject object)
			throws Exception {
		// 获取带请求参数列表将（参数类型，参数值）数组转成InterfaceParamObject数组
		InterfaceParamObject[] paramContent = map.readValue(object.getParams(),
				InterfaceParamObject[].class);
		Object result = hessian(object.getRemoteUrl(),
				object.getInterfaceClass(), object.getInterfaceMethod(),
				paramContent);

		if (result != null) {
			log.info("返回结果对象是{},返回原始结果是{}", result.getClass(),
					result.toString());
			return objectToMap(result);
		}
		log.info("返回结果对象是null");
		return null;

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
		for (Object key : map.keySet()) {
			System.out.println("KEY:" + key + ", VALUE:" + map.get(key));
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

	public static String genJsonStr(Object object)
			throws JsonProcessingException {
		return map.writeValueAsString(object);
	}

	private static Object hessian(String remoteUrl, String className,
			String methodName, InterfaceParamObject[] paramContent)
			throws Exception {
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setReadTimeout(read_time_out);
		Object interfaceObj = null;
		Class<?> interfaceClassName = null;

		try {
			interfaceClassName = Class.forName(className);
			interfaceObj = factory.create(interfaceClassName, remoteUrl);
			log.info("调用MyHessianProxyFactory返回的hessian代理对象为：{}",
					interfaceObj.getClass());
		} catch (Exception ex) {
			log.info("程序在反射获取接口的hesian代理对象时出现问题。异常信息：{}", ex.getMessage());
		}
		Object[] allRequstParas = new Object[paramContent.length];
		for (int i = 0; i < paramContent.length; i++) {
			InterfaceParamObject paramObject = paramContent[i];
			Class<?> paramClass = null;

			try {
				String paramClassName = paramObject.getClassname();

				paramClass = getBaseTypeClassByName(paramClassName);
				if (paramClass == null) {
					paramClass = Class.forName(paramObject.getClassname());
				}
			} catch (Exception ex) {
				log.info("程序在反射获取接口的参数类类型时出现问题。异常信息{}", ex.getMessage());
			}
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Object requestParamObject = null;
			if (paramObject.getValue() != null
					&& paramObject.getValue().trim().length() > 0) {
				try {

					requestParamObject = objectMapper
							.readValue(paramObject.getValue(), paramClass);
				} catch (Exception ex) {
					log.info("根据Json字符串参数转成类%s对象时发生异常，对象预期值为%s",
							requestParamObject.getClass(),
							paramObject.getValue());
				}
			} else {
				requestParamObject = paramClass.getInterfaces();
			}
			allRequstParas[i] = requestParamObject;
		}

		if (interfaceObj != null && allRequstParas.length > 0) {
			return getMethod(methodName, interfaceObj, allRequstParas);
		}
		log.info("没有请求参数，返回空对象");
		return null;
	}

	private static Object getMethod(String methodName, Object o,
			Object[] allRequstParas) {
		int totalRequestParam = allRequstParas.length;

		Class<?> allRequstParasClass[] = null;
		if (allRequstParas != null) {// 存在
			int len = allRequstParas.length;
			allRequstParasClass = new Class[len];
			for (int i = 0; i < len; ++i) {
				allRequstParasClass[i] = allRequstParas[i].getClass();
			}
		}
		Object result = new Object[totalRequestParam];
		Method method = null;
		try {
			// 根据方法名以及方法参数列表获取方法对象
			method = o.getClass().getDeclaredMethod(methodName,
					allRequstParasClass);
		} catch (NoSuchMethodException ex) {
			log.error(String.format("获取方法对象发生找不到方法，方法的类为%s,异常信息:%s",
					o.getClass(), ex.getMessage()));
		} catch (SecurityException ex) {
			log.error(String.format("获取方法对象发生发生安全异常，方法的类为%s,异常信息:%s",
					o.getClass(), ex.getMessage()));
		}
		try {
			result = method.invoke(o, allRequstParas);
		} catch (Exception ex) {
			log.error(String.format("调用方法对象执行调用发生异常，方法的类为%s,异常信息:%s",
					o.getClass(), ex.getMessage()));
		}
		return result;
	}

	/**
	 * 1、转换基本数据类型为包装类型<br>
	 * 2、…<br>
	 * 
	 * @param className
	 * @return
	 * @see
	 */
	private static Class<?> getBaseTypeClassByName(String className) {
		return base_type.get(className);
	}

	private static Boolean isBaseType(String className) {
		Boolean istrue = false;
		for (String key : base_type.keySet()) {
			istrue = className.equals(key) ? true : false;
		}
		return istrue;
	}
}