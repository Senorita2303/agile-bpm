package com.dstz.springboot.autoconfigure.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Client information transmission configuration
 *
 */
@ConfigurationProperties(prefix = "ab.rest-client-info-transform")
public class AbRestClientInfoTransformProperties {

	/**
	 * Cookie
	 */
	private List<NameValue> cookies;

	/**
	 * header
	 */
	private List<NameValue> headers;

	public List<NameValue> getCookies() {
		return cookies;
	}

	public void setCookies(List<NameValue> cookies) {
		this.cookies = cookies;
	}

	public List<NameValue> getHeaders() {
		return headers;
	}

	public void setHeaders(List<NameValue> headers) {
		this.headers = headers;
	}

	public static final class NameValue {
		/**
		 * Property Name
		 */
		private String name;
		/**
		 * Attribute value, if empty, it is taken from the current request
		 */
		private String value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

}
