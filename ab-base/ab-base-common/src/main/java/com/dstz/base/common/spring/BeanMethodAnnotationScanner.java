package com.dstz.base.common.spring;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.utils.CastUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * Spring bean method annotation scanning
 *
 */
public class BeanMethodAnnotationScanner<T extends Annotation> {

	private static final Logger logger = LoggerFactory.getLogger(BeanMethodAnnotationScanner.class);

	private final ConfigurableListableBeanFactory beanFactory;

	private final Class<T> annotationClass;

	public BeanMethodAnnotationScanner(ConfigurableListableBeanFactory beanFactory, Class<T> annotationClass) {
		this.beanFactory = beanFactory;
		this.annotationClass = annotationClass;
	}

	public Stream<MethodAnnotationMetadata<T>> scanStream() {
		Iterator<String> beanNamesIterator = beanFactory.getBeanNamesIterator();
		IteratorImpl<List<MethodAnnotationMetadata<T>>> iterator = new IteratorImpl<>(beanNamesIterator);
		return StreamUtil.of(iterator).flatMap(List::stream);
	}

	private class IteratorImpl<A extends List<MethodAnnotationMetadata<T>>> implements Iterator<A> {

		private final Iterator<String> beanNameIterator;

		public IteratorImpl(Iterator<String> beanNameIterator) {
			this.beanNameIterator = beanNameIterator;
		}

		@Override
		public boolean hasNext() {
			return beanNameIterator.hasNext();
		}

		@Override
		public A next() {
			final String beanName = beanNameIterator.next();
			if (!ScopedProxyUtils.isScopedTarget(beanName)) {
				Class<?> type = null;
				try {
					type = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
				} catch (Throwable ex) {
					// An unresolvable bean type, probably from a lazy bean - let's ignore it.
					if (logger.isDebugEnabled()) {
						logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
					}
				}
				if (type != null) {
					if (ScopedObject.class.isAssignableFrom(type)) {
						try {
							Class<?> targetClass = AutoProxyUtils.determineTargetClass(
									beanFactory, ScopedProxyUtils.getTargetBeanName(beanName));
							if (targetClass != null) {
								type = targetClass;
							}
						} catch (Throwable ex) {
							// An invalid scoped proxy arrangement - let's ignore it.
							if (logger.isDebugEnabled()) {
								logger.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
							}
						}
					}
					try {
						if(!excludeType(type)) {
							return processBean(beanName, type);
						}
					} catch (Throwable ex) {
						throw new BeanInitializationException("Failed to process @EventListener " +
								"annotation on bean with name '" + beanName + "'", ex);
					}
				}
			}
			return CastUtils.cast(Collections.emptyList());
		}

		private boolean excludeType(Class<?> typeClass){
			return StrUtil.startWithAny(typeClass.getName(), "java.", "org.springframework.");
		}

		private A processBean(String beanName, Class<?> type) {
			Map<Method, T> annotatedMethods = MethodIntrospector.selectMethods(type, (MethodIntrospector.MetadataLookup<T>) method -> AnnotatedElementUtils.findMergedAnnotation(method, annotationClass));
			if (CollUtil.isNotEmpty(annotatedMethods)) {
				List<MethodAnnotationMetadata<T>> annotationMetadataList = new ArrayList<>(annotatedMethods.size());
				for (Map.Entry<Method, T> entry : annotatedMethods.entrySet()) {
					annotationMetadataList.add(new MethodAnnotationMetadata<>(beanName, type, entry.getValue(), entry.getKey()));
				}
				return CastUtils.cast(annotationMetadataList);
			}
			return CastUtils.cast(Collections.emptyList());
		}
	}

	public static class MethodAnnotationMetadata<T> {

		private final String beanName;

		private final Class<?> typeClass;

		private final T annotation;

		private final Method method;

		public MethodAnnotationMetadata(String beanName, Class<?> typeClass, T annotation, Method method) {
			this.beanName = beanName;
			this.typeClass = typeClass;
			this.annotation = annotation;
			this.method = method;
		}

		public Class<?> getTypeClass() {
			return typeClass;
		}

		public String getBeanName() {
			return beanName;
		}

		public T getAnnotation() {
			return annotation;
		}

		public Method getMethod() {
			return method;
		}
	}
}
