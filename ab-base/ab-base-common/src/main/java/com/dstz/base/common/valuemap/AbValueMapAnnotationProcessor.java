package com.dstz.base.common.valuemap;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.*;
import com.dstz.base.common.async.ContextCleanTaskDecorator;
import com.dstz.base.common.async.ContextDuplicationTaskDecorator;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.utils.TaskDecoratorUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.function.SingletonSupplier;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Annotation Processor
 *
 */
public class AbValueMapAnnotationProcessor {

	private static final Logger logger = LoggerFactory.getLogger(AbValueMapAnnotationProcessor.class);

	private static final SingletonSupplier<AbValueMapAnnotationProcessor> INSTANCE_SUPPLIER = SingletonSupplier.of(AbValueMapAnnotationProcessor::new);

	/**
	 * Class Annotation Field Cache
	 */
	private final SimpleCache<Class<?>, List<BeanPropertyWriter>> classAnnotationFieldCache = new SimpleCache<>();

	private final SimpleCache<Class<?>, Map<String, PropertyDescriptor>> classPropertyDesCache = new SimpleCache<>();

	private volatile ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public static AbValueMapAnnotationProcessor getInstance() {
		return INSTANCE_SUPPLIER.obtain();
	}


	/**
	 * Get the thread task pool
	 *
	 * @return Thread Task Pool
	 */
	private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		if (threadPoolTaskExecutor == null) {
			synchronized (this) {
				if (threadPoolTaskExecutor == null) {
					ThreadPoolTaskExecutor localThreadPoolTaskExecutor = new ThreadPoolTaskExecutor();
					localThreadPoolTaskExecutor.setKeepAliveSeconds(30);
					localThreadPoolTaskExecutor.setMaxPoolSize(Math.max(Math.round(Runtime.getRuntime().availableProcessors() / 0.7f), 10));
					localThreadPoolTaskExecutor.setTaskDecorator(TaskDecoratorUtils.buildChainTaskDecorator(ContextCleanTaskDecorator.INSTANCE, ContextDuplicationTaskDecorator.INSTANCE));
					localThreadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
					localThreadPoolTaskExecutor.setThreadFactory(new NamedThreadFactory("ab-valuemap-", true));
					localThreadPoolTaskExecutor.initialize();
					this.threadPoolTaskExecutor = localThreadPoolTaskExecutor;
				}
			}
		}
		return threadPoolTaskExecutor;
	}


	private Map<Object, Object> loadMappedValue(Collection<Object> mapKeys, AbValueMap abValueMap) {
		return ObjectUtil.defaultIfNull(AbValueMapLoaderProvider.findAbValueMapLoader(abValueMap).loading(abValueMap, mapKeys), Collections.emptyMap());
	}

	public void prepareContainerMapValue(SerializerProvider serializerProvider, Object value) {
		Map<PropertyWriter, Collection<Object>> fieldGroupValues = Collections.emptyMap();
		if (value instanceof Iterable) {
			Iterable<Object> iterable = CastUtils.cast(value);
			fieldGroupValues = extractFieldGroupValues(StreamUtil.of(iterable, true), serializerProvider);
		} else if (value instanceof Map) {
			Map<Object, Object> map = CastUtils.cast(value);
			fieldGroupValues = extractFieldGroupValues(map.entrySet().parallelStream(), serializerProvider);
		} else if (ArrayUtil.isArray(value)) {
			fieldGroupValues = extractFieldGroupValues(Arrays.stream((Object[]) value).parallel(), serializerProvider);
		}
		if (MapUtil.isNotEmpty(fieldGroupValues)) {
			Map<String, Map<Object, Object>> fieldValueMap;
			if (fieldGroupValues.size() > 1) {
				// Use multi-threaded parallel loading to improve speed
				fieldValueMap = new ConcurrentHashMap<>((int) (fieldGroupValues.size() / MapUtil.DEFAULT_LOAD_FACTOR + 1));
				CountDownLatch countDownLatch = new CountDownLatch(fieldGroupValues.size());
				for (Map.Entry<PropertyWriter, Collection<Object>> entry : fieldGroupValues.entrySet()) {
					final AbValueMap abValueMap = entry.getKey().getAnnotation(AbValueMap.class);
					getThreadPoolTaskExecutor().submit(() -> {
						try {
							fieldValueMap.put(entry.getKey().getName(), loadMappedValue(entry.getValue(), abValueMap));
						} finally {
							countDownLatch.countDown();
						}
					});
				}
				// Wait for all threads to complete
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
					ExceptionUtil.wrapAndThrow(e);
				}
			} else {
				Map.Entry<PropertyWriter, Collection<Object>> entry = CollUtil.getFirst(fieldGroupValues.entrySet());
				AbValueMap abValueMap = entry.getKey().getAnnotation(AbValueMap.class);
				fieldValueMap = ImmutableMap.of(entry.getKey().getName(), loadMappedValue(entry.getValue(), abValueMap));
			}
			pushStack(serializerProvider, fieldValueMap);
		} else {
			pushStack(serializerProvider, null);
		}
	}

	private void pushStack(SerializerProvider serializerProvider, Map<String, Map<Object, Object>> fieldValueMap) {
		LinkedList<Map<String, Map<Object, Object>>> stack = CastUtils.cast(serializerProvider.getAttribute(AbValueMap.class));
		if (stack == null) {
			serializerProvider.setAttribute(AbValueMap.class, stack = new LinkedList<>());
		}
		stack.push(fieldValueMap);
	}

	private LinkedList<Map<String, Map<Object, Object>>> stack(SerializerProvider serializerProvider) {
		return CastUtils.cast(serializerProvider.getAttribute(AbValueMap.class));
	}

	public void cleanContainerMapValue(SerializerProvider serializerProvider) {
		LinkedList<?> stack = stack(serializerProvider);
		if (CollUtil.isNotEmpty(stack)) {
			stack.poll();
		}
	}

	public void serialFieldMapped(Object pojo, JsonGenerator gen, SerializerProvider serializerProvider, PropertyWriter writer) throws Exception {
		// Skip the unannotated fields
		if (writer.getAnnotation(AbValueMap.class) == null) {
			return;
		}
		AbValueMap abValueMap = writer.getAnnotation(AbValueMap.class);
		if (abValueMap == null) {
			return;
		}
		Object mappedObject = getPropertyMappedValue(pojo, serializerProvider, abValueMap, (BeanPropertyWriter) writer);
		if (mappedObject == null) {
			return;
		}
		writeMappedFieldValue(gen, writer.getName(), abValueMap, mappedObject);
	}

	private Object getPropertyMappedValue(Object pojo, SerializerProvider serializerProvider, AbValueMap abValueMap, BeanPropertyWriter propertyWriter) throws Exception {
		Object fieldValue = propertyWriter.get(pojo);
		// Skip if field value is empty
		if (fieldValue == null) {
			return null;
		}
		Map<Object, Object> valueMappedMap;
		Map<String, Map<Object, Object>> filedGroupValueMap = CollUtil.getFirst(stack(serializerProvider));
		// The upper source is not a container
		if (filedGroupValueMap == null) {
			valueMappedMap = loadMappedValue(Collections.singleton(fieldValue), abValueMap);
		} else {
			valueMappedMap = filedGroupValueMap.get(propertyWriter.getName());
		}
		return CollUtil.isEmpty(valueMappedMap) ? null : valueMappedMap.get(fieldValue);
	}

	private void writeMappedFieldValue(JsonGenerator gen, String fieldNamePrefix, AbValueMap abValueMap, Object mappedObject) throws IOException {
		// Map type data processing
		if (mappedObject instanceof Map) {
			Map<String, Object> mappedMap = CastUtils.cast(mappedObject);
			if (ArrayUtil.isEmpty(abValueMap.attrMap())) {
				for (Map.Entry<String, Object> entry : mappedMap.entrySet()) {
					gen.writeFieldName(fieldNamePrefix + StrUtil.upperFirst(entry.getKey()));
					gen.writeObject(entry.getValue());
				}
			} else {
				for (AbValueMap.AttrMap attrMap : abValueMap.attrMap()) {
					gen.writeFieldName(StrUtil.isNotEmpty(attrMap.targetName()) ? attrMap.targetName() : fieldNamePrefix + StrUtil.upperFirst(attrMap.originName()));
					gen.writeObject(mappedMap.get(attrMap.originName()));
				}
			}
		} else {
			Class<?> mappedObjectClass = AopUtils.getTargetClass(mappedObject);
			if (ArrayUtil.isEmpty(abValueMap.attrMap())) {
				for (PropertyDescriptor propertyDescriptor : getPropertyDescriptorMap(mappedObjectClass).values()) {
					gen.writeFieldName(fieldNamePrefix + StrUtil.upperFirst(propertyDescriptor.getName()));
					gen.writeObject(ReflectUtil.invoke(mappedObject, propertyDescriptor.getReadMethod()));
				}
			} else {
				Map<String, PropertyDescriptor> propertyDescriptorMap = getPropertyDescriptorMap(mappedObjectClass);
				for (AbValueMap.AttrMap attrMap : abValueMap.attrMap()) {
					PropertyDescriptor propertyDescriptor = propertyDescriptorMap.get(attrMap.originName());
					if (propertyDescriptor != null) {
						gen.writeFieldName(StrUtil.isNotEmpty(attrMap.targetName()) ? attrMap.targetName() : fieldNamePrefix + StrUtil.upperFirst(attrMap.originName()));
						gen.writeObject(ReflectUtil.invoke(mappedObject, propertyDescriptor.getReadMethod()));
					}
				}
			}
		}
	}

	private Map<String, PropertyDescriptor> getPropertyDescriptorMap(Class<?> clazz) {
		return classPropertyDesCache.get(clazz, () -> {
			Map<String, PropertyDescriptor> propertyDescriptorMap = BeanUtil.getPropertyDescriptorMap(clazz, false);
			Map<String, PropertyDescriptor> filterPropertyDescriptorMap = MapUtil.newHashMap(propertyDescriptorMap.size());
			propertyDescriptorMap.forEach((k, v) -> {
				Method readMethod = v.getReadMethod();
				if (readMethod != null && !Class.class.isAssignableFrom(readMethod.getReturnType())) {
					filterPropertyDescriptorMap.put(k, v);
				}
			});
			return filterPropertyDescriptorMap;
		});
	}


	private Map<PropertyWriter, Collection<Object>> extractFieldGroupValues(Stream<?> stream, SerializerProvider serializerProvider) {
		Map<PropertyWriter, Collection<Object>> fieldGroupValues = MapUtil.newConcurrentHashMap();
		stream.forEach(obj -> {
			Class<Object> typeClass = ClassUtil.getClass(obj);
			for (BeanPropertyWriter propertyWriter : getAnnotationFields(serializerProvider, typeClass)) {
				try {
					Object fieldValue = propertyWriter.get(obj);
					if (fieldValue != null) {
						fieldGroupValues.computeIfAbsent(propertyWriter, k -> new ConcurrentHashSet<>()).add(fieldValue);
					}
				} catch (Exception e) {
					throw new UndeclaredThrowableException(e);
				}
			}
		});
		return fieldGroupValues;
	}

	private List<BeanPropertyWriter> getAnnotationFields(SerializerProvider serializerProvider, Class<?> typeClass) {
		if (typeClass == null) {
			return Collections.emptyList();
		}
		return classAnnotationFieldCache.get(typeClass, () -> {
			try {
				JsonSerializer<Object> typedValueSerializer = serializerProvider.findTypedValueSerializer(typeClass, true, null);
				if (typedValueSerializer instanceof BeanSerializer) {
					return StreamUtil.of(IterUtil.asIterable(typedValueSerializer.properties()))
					                 .filter(propertyWriter -> propertyWriter instanceof BeanPropertyWriter && Objects.nonNull(propertyWriter.getAnnotation(AbValueMap.class)))
					                 .map(BeanPropertyWriter.class::cast)
					                 .collect(Collectors.toList());
				}
			} catch (JsonMappingException ex) {
				logger.error(ex.getMessage(), ex);
			}
			return Collections.emptyList();
		});
	}
}
