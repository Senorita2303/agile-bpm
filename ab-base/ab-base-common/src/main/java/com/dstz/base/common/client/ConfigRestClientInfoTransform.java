package com.dstz.base.common.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.dstz.base.common.utils.AbRequestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Configuration-based client request information forwarding
 *
 */
public class ConfigRestClientInfoTransform implements RestClientInfoTransform {

	/**
	 * Reference Cookie
	 */
	private static final String REF_COOKIE = "$cookie.";

	/**
	 * Reference header information
	 */
	private static final String REF_HEADER = "$header.";

	/**
	 * Reference Parameters
	 */
	private static final String REF_PARAM = "$param";

	private final List<Pair<String, String>> cookies;
	private final List<Pair<String, String>> headers;

	public ConfigRestClientInfoTransform(List<Pair<String, String>> cookies, List<Pair<String, String>> headers) {
		this.cookies = upgradeDefinitionNames(cookies, REF_COOKIE);
		this.headers = upgradeDefinitionNames(headers, REF_HEADER);
	}

	/**
	 * Upgrade definition name, upgrade the definition to the latest acquisition
	 *
	 * @param definitions  Definition List
	 * @param appendPrefix Append prefix
	 * @return New List
	 */
	private List<Pair<String, String>> upgradeDefinitionNames(List<Pair<String, String>> definitions, String appendPrefix) {
		if (CollUtil.isEmpty(definitions)) {
			return Collections.emptyList();
		}
		List<Pair<String, String>> newDefinitions = new ArrayList<>(CollUtil.size(definitions));
		for (Pair<String, String> definition : definitions) {
			Pair<String, String> newDefinition = definition;
			if (StrUtil.isEmpty(definition.getValue()) && !StrUtil.startWith(newDefinition.getKey(), appendPrefix)) {
				newDefinition = Pair.of(newDefinition.getKey(), appendPrefix + definition.getKey());
			}
			newDefinitions.add(newDefinition);
		}
		return newDefinitions;
	}

	@Override
	public Map<String, String> getCookies() {
		return getValueMap(cookies, URLEncodeUtil::encodeQuery);
	}

	@Override
	public Map<String, String> getHeaders() {
		return getValueMap(headers, null);
	}

	private Map<String, String> getValueMap(List<Pair<String, String>> pairList, Function<String, String> valueEncodeFn) {
		if (CollUtil.isEmpty(pairList)) {
			return Collections.emptyMap();
		}

		HttpServletRequest request = AbRequestUtils.getHttpServletRequest();
		Map<String, Cookie> cookieMap = request == null ? Collections.emptyMap() : ServletUtil.readCookieMap(request);

		Map<String, String> returnMap = MapUtil.newHashMap(pairList.size());
		for (Pair<String, String> definition : pairList) {
			String value;
			if (StrUtil.isNotEmpty(definition.getValue())) {
				value = getValueByName(request, cookieMap, definition.getValue());
			} else {
				value = request == null ? null : getValueByName(request, cookieMap, definition.getKey());
			}
			// Encode the obtained value
			if (value != null && valueEncodeFn != null) {
				value = valueEncodeFn.apply(value);
			}
			returnMap.put(definition.getKey(), value);
		}
		return returnMap;
	}

	private String getValueByName(HttpServletRequest request, Map<String, Cookie> cookieMap, String name) {
		String value;
		if (StrUtil.startWith(name, REF_COOKIE)) {
			Cookie cookie = cookieMap.get(StrUtil.removePrefix(name, REF_COOKIE));
			value = cookie == null ? null : cookie.getValue();
		} else if (StrUtil.startWith(name, REF_HEADER)) {
			value = request == null ? null : request.getHeader(StrUtil.removePrefix(name, REF_HEADER));
		} else if (StrUtil.startWith(name, REF_PARAM)) {
			value = request == null ? null : request.getParameter(StrUtil.removePrefix(name, REF_PARAM));
		} else {
			value = name;
		}
		return value;
	}
}
