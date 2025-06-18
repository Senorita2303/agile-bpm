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
 * bean 拷贝
 *
 * @author jeff
 * @since 2022-01-24
 */
public class BeanCopierUtils {

    public static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    private BeanCopierUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * 属性拷贝
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static <T> T copyProperties(Object source, T target) {
        if (source != null && target != null) {
            getBeanCopier(ClassUtil.getClass(source), ClassUtil.getClass(target)).copy(source, target, null);
        }
        return target;
    }

    /**
     * 将一个类转换成另外一个类，可用于copy复制对象场景
     *
     * @param source 源对象
     * @param clazz  目标类
     * @return 目标对象
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
     * 将列表转换为另一个类对象实例列表。
     *
     * @param sourceList  源数据列表
     * @param targetClass 目标类
     * @param <T>         T
     * @return 目标对象列表
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
	 * 将bean转换为Map
	 *
	 * @param bean     bean
	 * @param editable 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父
	 * @return map存放的键值数据
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
     * 重写BeanCopier，兼容高版本JDK
     * <p>
     * 重写原因：<br>
     * 1. JDK 16版本新加了ClassLoader保护，不在允许通过反射调用。<br/>
     * 2. 设置生成名称与BeanCopier在同一包名下 <br/>
     * <b>JEP规范：https://openjdk.java.net/jeps/396</</b>
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
