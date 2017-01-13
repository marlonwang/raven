/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeUtils {
	private static final Map<String, Method> METHODS = new ConcurrentHashMap();

	public static void register(String name, Type type) {
		Class clazz = getClass(type);
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			METHODS.put(name + "." + m.getName(), m);
		}
	}

	public static Method getMethod(String service) {
		return ((Method) METHODS.get(service));
	}

	public static Type[] getParameterTypes(String service) {
		Method m = (Method) METHODS.get(service);
		if (m == null) {
			return null;
		}

		return m.getGenericParameterTypes();
	}

	public static Class<?> getClass(Type type) {
		if (type.getClass() == Class.class) {
			return ((Class) type);
		}
		if (type instanceof GenericArrayType) {
			return GenericArrayType.class;
		}
		if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		}

		return Object.class;
	}

	private static Constructor getAccessConstrutor(Class<?> clazz) {
		Constructor c = clazz.getDeclaredConstructors()[0];
		c.setAccessible(true);
		return c;
	}

	public static void set(Field field, Object obj, Object val) {
		try {
			field.set(obj, val);
		} catch (Exception e) {
			throw new UnsupportedOperationException("set val error:"
					+ field.getName(), e);
		}
	}

	public static Map<String, Field> getFields(Type type) {
		Map fields = new TreeMap();
		for (Class cls = getClass(type); cls != null; cls = cls.getSuperclass()) {
			Field[] fs = cls.getDeclaredFields();
			for (int i = 0; i < fs.length; ++i) {
				Field field = fs[i];
				int mod = fs[i].getModifiers();
				if ((Modifier.isTransient(mod)) || (Modifier.isStatic(mod)))
					continue;
				field.setAccessible(true);
				if ((fields.get(field.getName()) != null)
						|| (field.getName().startsWith("this$")))
					continue;
				fields.put(field.getName(), field);
			}

		}

		return fields;
	}

	public static Collection<?> createCollection(Class<?> clazz, int size) {
		if (clazz.isInterface()) {
			if (clazz == Set.class) {
				return new HashSet(size);
			}

			if ((clazz == List.class) || (clazz == Collection.class)) {
				return new ArrayList(size);
			}

			throw new UnsupportedOperationException("unknow interface:" + clazz);
		}
		if (clazz == AbstractCollection.class) {
			return new ArrayList(size);
		}

		if (clazz.isAssignableFrom(HashSet.class)) {
			return new HashSet();
		}
		if (clazz.isAssignableFrom(LinkedHashSet.class)) {
			return new LinkedHashSet();
		}
		if (clazz.isAssignableFrom(ArrayList.class)) {
			return new ArrayList();
		}

		try {
			return ((Collection) clazz.newInstance());
		} catch (Exception e) {
			throw new UnsupportedOperationException("unknow class: "
					+ clazz.getName(), e);
		}
	}

	public static Map<Object, Object> createMap(Type type, int size) {
		if (type == Map.class) {
			return new LinkedHashMap();
		}

		if (type == HashMap.class) {
			return new HashMap();
		}

		if (type == Properties.class) {
			return new Properties();
		}

		if (type == Hashtable.class) {
			return new Hashtable();
		}

		if (type == IdentityHashMap.class) {
			return new IdentityHashMap();
		}

		if ((type == SortedMap.class) || (type == TreeMap.class)) {
			return new TreeMap();
		}

		if ((type == ConcurrentMap.class) || (type == ConcurrentHashMap.class)) {
			return new ConcurrentHashMap();
		}

		if (type == LinkedHashMap.class) {
			return new LinkedHashMap();
		}

		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;

			return createMap(parameterizedType.getRawType(), size);
		}

		if (type instanceof Class) {
			Class clazz = (Class) type;
			if (clazz.isInterface()) {
				throw new UnsupportedOperationException("unsupport type "+ type);
			}

			try {
				return ((Map) clazz.newInstance());
			} catch (Exception e) {
				throw new UnsupportedOperationException("unsupport type "+ type, e);
			}
		}

		throw new UnsupportedOperationException("unsupport type " + type);
	}
}