/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

import java.lang.reflect.Type;

public class TypeReference<T> {
	public static final Type LIST_STRING = new TypeReference() {
	}.getType();
	private final Type type;

	protected TypeReference() {
		Type superClass = super.getClass().getGenericSuperclass();

		this.type = ((java.lang.reflect.ParameterizedType) superClass)
				.getActualTypeArguments()[0];
	}

	public Type getType() {
		return this.type;
	}
}