package com.dstz.springboot.autoconfigure.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.client.ConfigRestClientInfoTransform;
import com.dstz.base.common.client.RestClientInfoTransform;
import com.dstz.base.common.client.RestClientInfoTransformComposite;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ab rest template auto configuration
 *
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AbRestClientInfoTransformProperties.class)
public class AbRestClientAutoConfiguration {

	@Bean
	public RestTemplateCustomizer abRestTemplateCustomizer() {
		ClientHttpRequestInterceptor clientHttpRequestInterceptor = (request, body, execution) -> {
			RestClientInfoTransformComposite transformComposite = RestClientInfoTransformComposite.getInstance();
			transformComposite.getHeaders().forEach((k, v) -> request.getHeaders().add(k, v));
			String cookiesString = transformComposite.getCookiesString();
			if (StrUtil.isNotEmpty(cookiesString)) {
				request.getHeaders().add(HttpHeaders.COOKIE, cookiesString);
			}
			return execution.execute(request, body);
		};
		return restTemplate -> restTemplate.getInterceptors().add(clientHttpRequestInterceptor);
	}

	@Bean
	public RestClientInfoTransform configRestClientInfoTransform(
			AbRestClientInfoTransformProperties restClientInfoTransformProperties) {
		return new ConfigRestClientInfoTransform(
				CollUtil.map(restClientInfoTransformProperties.getCookies(),
						cookie -> Pair.of(cookie.getName(), cookie.getValue()), false),
				CollUtil.map(restClientInfoTransformProperties.getHeaders(),
						header -> Pair.of(header.getName(), header.getValue()), false));
	}

	/**
	 * ab http rest template
	 *
	 * @param restTemplateBuilder rest template builder
	 * @return rest template
	 */
	@Bean
	public RestTemplate abHttpRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

}
