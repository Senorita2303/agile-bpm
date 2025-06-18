package com.dstz.auth.api;

import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.web.api.WebSessionAttributesApi;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Web session attribute interface implementation
 *
 */
@Component
public class WebSessionAttributesApiImpl implements WebSessionAttributesApi {

	private static final Logger logger = LoggerFactory.getLogger(WebSessionAttributesApiImpl.class);

	@Autowired
	private TokenStore tokenStore;

	@Override
	public void setValue(String name, Serializable value) {
		Map<String, Serializable> values = ImmutableMap.of(name, value);
		setValues(values);
	}

	@Override
	public void setValues(Map<String, ? extends Serializable> values) {
		OAuth2Authentication oauth2Authentication = getOauth2Authentication();
		if (oauth2Authentication == null) {
			return;
		}
		AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) oauth2Authentication.getUserAuthentication();
		Map<String, Serializable> originDetails = CastUtils.cast(authenticationToken.getDetails());

		Map<String, Serializable> newDetails = new LinkedHashMap<>();
		if (originDetails != null) {
			newDetails.putAll(originDetails);
		}

		// Update Value
		for (Map.Entry<String, ? extends Serializable> entry : values.entrySet()) {
			if (entry.getValue() == null) {
				newDetails.remove(entry.getKey());
			} else {
				newDetails.put(entry.getKey(), entry.getValue());
			}
		}
		authenticationToken.setDetails(newDetails);

		// Storage
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(((OAuth2AuthenticationDetails)oauth2Authentication.getDetails()).getTokenValue());
		tokenStore.storeAccessToken(accessToken, oauth2Authentication);
	}

	@Override
	public Serializable getValue(String name) {
		OAuth2Authentication oauth2Authentication = getOauth2Authentication();
		if (oauth2Authentication == null) {
			return null;
		}
		Object details = oauth2Authentication.getUserAuthentication().getDetails();
		if (details == null) {
			return null;
		}
		return CastUtils.<Map<String, Serializable>>cast(details).get(name);
	}

	private OAuth2Authentication getOauth2Authentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof OAuth2Authentication)) {
			logger.info("Currently not implemented with OAuth2, tenant switching is not possible");
			return null;
		}
		return (OAuth2Authentication) authentication;
	}
}
