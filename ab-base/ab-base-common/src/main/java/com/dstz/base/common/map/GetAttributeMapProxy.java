package com.dstz.base.common.map;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapProxy;
import cn.hutool.core.util.ReflectUtil;
import com.dstz.base.common.utils.CastUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Get the MapProxy property for proxy getAttribute(String key, Class&lt;T&gt; toClass);
 *
 */
public class GetAttributeMapProxy extends MapProxy {

	private static final long serialVersionUID = -7468987711284643178L;

	private final Function<Map<String, Object>, Object> loadFunc;

	private volatile boolean lazyLoaded;

	public GetAttributeMapProxy(Map<?, ?> map) {
		super(map);
		this.lazyLoaded = true;
		this.loadFunc = null;
	}

	/**
	 * Structure
	 *
	 * @param map Proxy Map
	 */
	public GetAttributeMapProxy(Map<?, ?> map, Function<Map<String, Object>, Object> loadFunc) {
		super(map);
		this.loadFunc = loadFunc;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) {
		// Compatible with Map
		if (Map.class.isAssignableFrom(method.getDeclaringClass())) {
			doLazyLoad();
			return ReflectUtil.invoke(this, method, args);
		}

		Object value;

		// Get property method
		if ("getAttrValue".equals(method.getName()) && method.getParameterCount() == 2) {
			value = Convert.convert(CastUtils.<Class<?>>cast(args[1]), get(args[0]));
		} else {
			value = super.invoke(proxy, method, args);
		}

		if (value == null && !lazyLoaded) {
			doLazyLoad();
			// After loading, get the value again
			value = invoke(proxy, method, args);
		}

		return value;
	}

	private synchronized void doLazyLoad() {
		if (lazyLoaded) {
			return;
		}
		lazyLoaded = true;
		Object value = loadFunc.apply(CastUtils.cast(this));
		if (Objects.nonNull(value)) {
			if (value instanceof Map) {
				super.putAll(CastUtils.cast(value));
			} else {
				throw new UnsupportedOperationException("Lazy loading function only supports return value Map");
			}
		}
	}
}
