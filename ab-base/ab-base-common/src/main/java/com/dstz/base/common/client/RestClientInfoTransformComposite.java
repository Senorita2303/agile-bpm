package com.dstz.base.common.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.constats.StrPool;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * REST request information forwarding combination
 *
 */
public class RestClientInfoTransformComposite implements RestClientInfoTransform {

	private final Collection<RestClientInfoTransform> transforms;

	public RestClientInfoTransformComposite(Collection<RestClientInfoTransform> transforms) {
		this.transforms = transforms;
	}

	@Override
	public Map<String, String> getCookies() {
		Map<String, String> mergeCookies = MapUtil.newHashMap();
		for (RestClientInfoTransform transform : transforms) {
			Map<String, String> cookies = transform.getCookies();
			if (MapUtil.isNotEmpty(cookies)) {
				mergeCookies.putAll(cookies);
			}
		}
		return mergeCookies;
	}

	@Override
	public Map<String, String> getHeaders() {
		Map<String, String> mergeHeaders = MapUtil.newHashMap();
		for (RestClientInfoTransform transform : transforms) {
			Map<String, String> headers = transform.getHeaders();
			if (MapUtil.isNotEmpty(headers)) {
				mergeHeaders.putAll(headers);
			}
		}
		return mergeHeaders;
	}

	/**
	 * Get cookies string
	 *
	 * @return cookie string
	 */
	public String getCookiesString() {
		return getCookies().entrySet().stream().map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue())).collect(Collectors.joining(StrPool.SEMICOLON));
	}

	/**
	 * Get an instance
	 *
	 * @return Examples
	 */
	public static RestClientInfoTransformComposite getInstance() {
		return Holder.INSTANCE;
	}

	private static class Holder {
		static final RestClientInfoTransformComposite INSTANCE = new RestClientInfoTransformComposite(SpringUtil.getBeansOfType(RestClientInfoTransform.class).values());
	}
}
