package com.dstz.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.auth.utils.IngoreChecker;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XSS security filter.
 * <pre>
 *  This feature is to prevent XSS attacks.
 *  If there is a Xss attack:
 *  	1.When submitting the form, the platform will go to a prompt page.
 *  	2.AJAX submission method, pop-up prompt information.
 *  You can configure certain URLs that do not need to be checked.
 * </pre>
 *
 */
public class XssFilter extends IngoreChecker implements Filter {

	private Pattern regex = Pattern.compile("<(\\S*?)[^>]*>.*?</\\1>|<[^>]+>",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.MULTILINE);

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		// Whether the page is ignored.
		boolean isIngoreUrl = isIngores(req);
		if (isIngoreUrl) {
			chain.doFilter(request, response);
		} else {
			// Check for XSS attacks.
			boolean hasXss = checkXss(req);
			if (hasXss) {
				response.getWriter().print(JsonUtils.toJSONString(
						ApiResponse.fail(GlobalApiCodes.PARAMETER_INVALID.getCode(), "检测到提交内容含HTML代码，被拦截！")));
			} else {
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig config) {
	}

	/**
	 * Determine whether the input has XSS injection issues.
	 *
	 * @param request
	 * @return
	 */
	private boolean checkXss(HttpServletRequest request) {
		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String key = params.nextElement().toString();
			String[] vals = request.getParameterValues(key);
			String val = StrUtil.join("", vals);
			if (StrUtil.isEmpty(val))
				continue;

			Matcher regexMatcher = regex.matcher(val);
			if (regexMatcher.find()) {
				return true;
			}
		}
		return false;
	}

}
