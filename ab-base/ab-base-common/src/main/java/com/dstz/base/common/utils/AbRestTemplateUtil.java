package com.dstz.base.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * http client
 *
 */
public class AbRestTemplateUtil {

	/**
	 * Microservice service calling protocol
	 */
	public static final String LB_PROTOCOL = "lb://";

	/**
	 * HTTP service call protocol
	 */
	public static final String HTTP_PROTOCOL = "http://";
	
	private static final class RestTemplateBeanHolder {

		/**
		 * Ordinary rest template
		 */
		static final RestTemplate REST_TEMPLATE;

		/**
		 * Microservices
		 */
		static final RestTemplate SERVICE_REST_TEMPLATE;

		static Class<?> getLoadBalancerInterceptorClass(){
			try {
				return ClassUtils.forName("org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor", Thread.currentThread().getContextClassLoader());
			} catch (Exception ignored) {
				return null;
			}
		}

		static {
			Collection<RestTemplate> restTemplates = SpringUtil.getBeansOfType(RestTemplate.class).values();
			if (CollUtil.isEmpty(restTemplates)) {
				throw new NoSuchBeanDefinitionException(RestTemplate.class);
			}
			final Class<?> loadBalancerInterceptorClass = getLoadBalancerInterceptorClass();
			RestTemplate[] restTemplateArray = new RestTemplate[2];
			for (RestTemplate template : restTemplates) {
				boolean existsLoadBalancerInterceptorFilter = !Objects.isNull(loadBalancerInterceptorClass) && ObjectUtil.defaultIfNull(template.getInterceptors(), Collections.<ClientHttpRequestInterceptor>emptyList())
				                                                                                                         .stream()
				                                                                                                         .anyMatch(clientHttpRequestInterceptor -> ClassUtils.isAssignableValue(loadBalancerInterceptorClass, clientHttpRequestInterceptor));
				if (existsLoadBalancerInterceptorFilter) {
					if (restTemplateArray[1] == null) {
						restTemplateArray[1] = template;
					}
				} else {
					if (restTemplateArray[0] == null) {
						restTemplateArray[0] = template;
					}
				}
				if (restTemplateArray[0] != null && restTemplateArray[1] != null) {
					break;
				}
			}
			REST_TEMPLATE = restTemplateArray[0];
			SERVICE_REST_TEMPLATE = restTemplateArray[1];
		}
	}

	/**
	 * Get rest template
	 *
	 * @param isService Is it microservices?
	 * @return rest template
	 */
	public static RestTemplate getRestTemplate(boolean isService) {
		RestTemplate restTemplate = isService ? RestTemplateBeanHolder.SERVICE_REST_TEMPLATE : RestTemplateBeanHolder.REST_TEMPLATE;
		Assert.notNull(restTemplate, () -> new NoSuchBeanDefinitionException(RestTemplate.class, "isService: " + isService));
		return restTemplate;
	}

	/**
	 * Get RestTemplate based on the address
	 *
	 * @param url Call address
	 * @return rest template
	 */
	public static RestTemplate getRestTemplate(String url) {
		return getRestTemplate(StrUtil.startWith(url, LB_PROTOCOL));
	}

	/**
	 * Identify the real call address based on the URL
	 *
	 * @param url Original address
	 * @return Real call address
	 */
	public static String tellUrl(String url) {
		if (StrUtil.startWith(url, LB_PROTOCOL)) {
			return HTTP_PROTOCOL + StrUtil.removePrefix(LB_PROTOCOL, url);
		}
		return url;
	}
}
