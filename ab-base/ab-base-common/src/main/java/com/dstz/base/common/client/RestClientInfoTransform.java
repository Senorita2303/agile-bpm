package com.dstz.base.common.client;

import java.util.Collections;
import java.util.Map;

/**
 * REST client requests information exchange, used to pass token and information
 *
 */
public interface RestClientInfoTransform {

	/**
	 * Get Cookies
	 *
	 * @return cookie
	 */
	default Map<String, String> getCookies() {
		return Collections.emptyMap();
	}

	/**
	 * Get header information
	 *
	 * @return Header Information
	 */
	default Map<String, String> getHeaders() {
		return Collections.emptyMap();
	}

}
