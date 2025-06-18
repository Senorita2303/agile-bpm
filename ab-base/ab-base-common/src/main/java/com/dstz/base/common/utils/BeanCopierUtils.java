package com.dstz.base.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean copy
 *
 */
public class BeanCopierUtils {

    public static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    private BeanCopierUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * Attribute copy
     *
     * @param source Source object
     * @param target Target object
     */
    public static <T> T copyProperties(Object source, T target) {
        if (source != null && target != null) {
            getBeanCopier(ClassUtil.getClass(source), ClassUtil.getClass(target)).copy(source, target, null);
        }
        return target;
    }

    /**
     * Convert a class into another class, which can be used to copy object scenes
     *
     * @param source Source object
     * @param clazz  Target class
     * @return Target object
     */
    public static <T> T transformBean(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        T target = ReflectUtil.newInstance(clazz);
        copyProperties(source, target);
        return target;
    }

    /**
     * Convert a list to a list of instances of another class object.
     *
     * @param sourceList  Source data list
     * @param targetClass Target class
     * @param <T>         T
     * @return Target object list
     */
    public static <T> List<T> transformList(Collection<?> sourceList, Class<T> targetClass) {
        if (CollUtil.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        BeanCopier beanCopier = getBeanCopier(ClassUtil.getClass(CollUtil.getFirst(sourceList)), targetClass);
        List<T> list = new ArrayList<>(sourceList.size());
        for (Object sourceObject : sourceList) {
            T targetObject = ReflectUtil.newInstance(targetClass);
            beanCopier.copy(sourceObject, targetObject, null);
            list.add(targetObject);
        }
        return list;
    }

	/**
	 * Convert beans to Map
	 *
	 * @param bean     bean
	 * @param editable The restricted class or interface must be the target object's implementation interface or parent class, which is used to restrict the properties of the copy. For example, if I only want to copy some properties of a class from its parent class, I can set editable to parent.
	 * @return Map stores key-value data
	 */
	public static Map<String, Object> transformMap(Object bean, Class<?> editable) {
		PropertyDescriptor[] propertyDescriptors;
		if(editable != null) {
			 propertyDescriptors = BeanUtil.getPropertyDescriptors(editable);
		} else {
			Class<?> targetClass = AopUtils.getTargetClass(bean);
			propertyDescriptors = BeanUtil.getPropertyDescriptors(targetClass);
		}
		if (ArrayUtil.isEmpty(propertyDescriptors)) {
			return new HashMap<>(0);
		}
		Map<String, Object> dataMap = MapUtil.newHashMap(propertyDescriptors.length);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method readMethod = propertyDescriptor.getReadMethod();
			if (readMethod != null) {
				dataMap.put(propertyDescriptor.getName(), ReflectUtil.invoke(bean, readMethod));
			}
		}
		return dataMap;
	}

    private static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass) {
        final String beanKey = sourceClass.getName() + "->" + targetClass.getName();
        return BEAN_COPIER_MAP.computeIfAbsent(beanKey, k -> new AbBeanCopierGenerator(sourceClass, targetClass).create());
    }

    /**
     * Rewrite BeanCopier to be compatible with higher JDK versions
     * <p>
     * Reason for rewriting:<br>
     * 1. JDK 16 adds ClassLoader protection, and reflection calls are no longer allowed.<br/>
     * 2. Set the generated name to be in the same package as BeanCopier <br/>
     * <b>JEP specification: https://openjdk.java.net/jeps/396</</b>
     * </p>
     */
    private static class AbBeanCopierGenerator extends BeanCopier.Generator {
        public AbBeanCopierGenerator(Class<?> source, Class<?> target) {
	        setSource(source);
			setTarget(target);
			setNamePrefix(target.getName());
            setContextClass(target);
        }
    }
}
