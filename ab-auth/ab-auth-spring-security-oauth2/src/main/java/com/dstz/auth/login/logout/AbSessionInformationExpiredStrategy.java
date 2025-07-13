package com.dstz.auth.login.logout;

import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Session expiration policy
 *
 */
public class AbSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        byte[] body = JsonUtils.toJSONString(ApiResponse.fail(GlobalApiCodes.INTERNAL_ERROR.getCode(),"Multiple logins were forced offline")).getBytes();

        HttpServletResponse response = event.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(body, NumberPool.INTEGER_ZERO, body.length);
    }
}
