package com.dstz.base.common.events;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.dstz.base.common.enums.ErrorLogLeve;
import com.dstz.base.common.utils.AbRequestUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.org.api.model.IUser;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

public class AbErrorLogEvent extends ApplicationEvent {
	private static final long serialVersionUID = -3131555628978461408L;

	private AbErrorLogEvent(AbErrorLog abErrorLog) {
		super(abErrorLog);
	}

	/**
	 * Construct an exception information event using exception information, current user information, and current request information
	 *
	 * @param e Exception information
	 * @return Abnormal log events
	 */
	public static AbErrorLogEvent createErrorLog(Throwable e, ErrorLogLeve errorLogLeve) {
		return new AbErrorLogEvent(new AbErrorLog(e, errorLogLeve));
	}

	/**
	 * User-defined error messages
	 *
	 * @param abErrorLog Abnormal log information
	 * @return Abnormal log events
	 */
	public static AbErrorLogEvent createErrorLog(AbErrorLog abErrorLog) {
		return new AbErrorLogEvent(abErrorLog);
	}

	public AbErrorLog getErrorLog() {
		return (AbErrorLog) getSource();
	}

	public static class AbErrorLog {

		/**
		 * Operator information, the default is the current user
		 */
		private IUser operatorUser;

		/**
		 * Error operation
		 */
		private String action;
		/**
		 * Error exception
		 */
		private Throwable exception;
		/**
		 * Parameter information
		 */
		private Map paramMap;

		/**
		 * Parameter information
		 */
		private Map heardMap;

		/**
		 * Request address
		 */
		private String ip;

		/**
		 * Mistake
		 */
		private ErrorLogLeve errorLogLeve;

		public AbErrorLog() {
		}

		public AbErrorLog(Throwable exception, ErrorLogLeve errorLogLeve) {
			this.exception = exception;
			this.errorLogLeve = errorLogLeve;
			this.operatorUser = UserContextUtils.getUser().orElse(null);
			HttpServletRequest request = AbRequestUtils.getHttpServletRequest();
			if (request != null) {
				setRequestInfo(request);
			}
		}

		public AbErrorLog(Exception exception, String action, Map paramMap, ErrorLogLeve errorLogLeve) {
			this.action = action;
			this.exception = exception;
			this.operatorUser = UserContextUtils.getUser().orElse(null);
			this.paramMap = paramMap;
			this.errorLogLeve = errorLogLeve;
			HttpServletRequest request = AbRequestUtils.getHttpServletRequest();
			if (request != null) {
				setRequestInfo(request);
			}
		}

		public ErrorLogLeve getErrorLogLeve() {
			return errorLogLeve;
		}

		public void setErrorLogLeve(ErrorLogLeve errorLogLeve) {
			this.errorLogLeve = errorLogLeve;
		}

		private void setRequestInfo(HttpServletRequest request) {
			this.ip = ServletUtil.getClientIP(request);
			MultiValueMap<String, String> headerValueMap = new LinkedMultiValueMap<>();

			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				Enumeration<String> headerValue = request.getHeaders(headerName);
				while (headerValue.hasMoreElements()) {
					headerValueMap.add(headerName, headerValue.nextElement());
				}
			}

			this.heardMap = headerValueMap;
			if (CollUtil.isEmpty(paramMap)) {
				paramMap = ServletUtil.getParamMap(request);
			}

			if (StrUtil.isEmpty(action)) {
				action = request.getServletPath();
			}
		}

		public Throwable getException() {
			return exception;
		}

		public void setException(Throwable exception) {
			this.exception = exception;
		}

		public IUser getOperatorUser() {
			if (operatorUser != null) {
				return operatorUser;
			}
			return UserContextUtils.getUser().orElse(null);
		}

		public void setOperatorUser(IUser operatorUser) {
			this.operatorUser = operatorUser;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public Map getParamMap() {
			return paramMap;
		}

		public void setParamMap(Map paramMap) {
			this.paramMap = paramMap;
		}

		public Map getHeardMap() {
			return heardMap;
		}

		public void setHeardMap(Map heardMap) {
			this.heardMap = heardMap;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
	}

}
