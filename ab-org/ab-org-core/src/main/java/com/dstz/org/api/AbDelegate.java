package com.dstz.org.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;

import java.beans.PropertyDescriptor;

abstract class AbDelegate<T> {

	protected transient final T delegate;

	public AbDelegate(T delegate) {
		this.delegate = delegate;
	}

	public <R> R getAttrValue(String attrName, Class<R> tClass) {
		PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(getClass(), attrName);
		if (propertyDescriptor != null && propertyDescriptor.getReadMethod() != null) {
			return Convert.convert(tClass, ReflectUtil.invoke(this, propertyDescriptor.getReadMethod()));
		}
		propertyDescriptor = BeanUtil.getPropertyDescriptor(ClassUtil.getClass(delegate), attrName);
		Assert.isTrue(propertyDescriptor != null && propertyDescriptor.getReadMethod() != null, () -> new IllegalArgumentException(String.format("%s属性%s不存在", ClassUtil.getClass(delegate).getName(), attrName)));
		return Convert.convert(tClass, ReflectUtil.invoke(delegate, propertyDescriptor.getReadMethod()));
	}
}
