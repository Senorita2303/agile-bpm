package com.dstz.base.interceptor;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;

/**
 * Custom Processors
 *
 */
public interface MybatisPlusInterceptorCustomizer {

	/**
	 * Custom interception processing
	 *
	 * @param mybatisPlusInterceptor Mybatis interceptor
	 */
	void customize(MybatisPlusInterceptor mybatisPlusInterceptor);

}
