
package com.dstz.auth.utils;


import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.auth.authentication.api.constant.AuthStatusCode;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.property.PropertyEnum;

import java.util.HashMap;
import java.util.Map;

import static com.dstz.auth.authentication.api.constant.AuthCacheKeyConstant.USER_LOGIN_COUNT;

/**
 * Login Tools
 *
 */
public class LoginUtils {

    /**
     * Login failure: Number of authentication failures Storage cache
     * Number of failures <= system configuration Update cache Delete old cache Add new cache
     */


    public static String chckLoginFailedTimes(String userName, String ip) {
        ICache cache = SpringUtil.getBean(ICache.class);
        String loginKey = String.format("%s%s", ip, userName);
        int maxCount = PropertyEnum.LOGIN_COUNT.getPropertyValue(int.class);
        String lockTimeDesc = PropertyEnum.LOGIN_FILED_LOCK_TIME_DESC.getPropertyValue(String.class);

        // By default, the login attempt verification is not enabled. If the configured number of attempts is 0, the password attempt number will not be verified.
        if (maxCount == 0) {
            return StrPool.EMPTY;
        }

        Map<String, Object> map = getArgMap(userName, ip, userName);
        Integer failedCount = getFailedCount(userName, ip, userName);

        if (failedCount < maxCount) {
            map.put(loginKey, String.valueOf(failedCount + 1));
            cache.put(AbCacheRegionConstant.LOGIN_CACHE_REGION, USER_LOGIN_COUNT + userName, map);
        } else {
            throw new BusinessMessage(AuthStatusCode.DISABLE_LOGIN.formatDefaultMessage(lockTimeDesc));
        }
        return String.format("The account password is incorrect and has failed %d times. If it fails %d times in a row, you will be prohibited from logging in to %s", failedCount + 1, maxCount, lockTimeDesc);
    }


    public static Integer getFailedCount(String userName, String ip, String name) {
        String loginKey = String.format("%s%s", ip, name);
        int count = 0;
        Map<String, Object> map = getArgMap(userName, ip, name);
        if (!map.isEmpty()) {
            for (String key : map.keySet()) {
                if (key.equals(loginKey)) {
                    String value = String.valueOf(map.get(key));
                    int i = Integer.parseInt(value);
                    count = i;
                }
            }
        }
        return count;
    }

    /*Check whether the current user's data exists in the cache*/

    public static Map<String, Object> getArgMap(String userName, String ip, String name) {
        ICache cache = SpringUtil.getBean(ICache.class);
        Map<String, Object> mapObj = new HashMap();
        if (StrUtil.isNotEmpty(ip) || StrUtil.isNotEmpty(name)) {
            if (cache.exists(AbCacheRegionConstant.LOGIN_CACHE_REGION, USER_LOGIN_COUNT + userName)) {
                // Failed to obtain
                mapObj = (Map<String, Object>) cache.getIfPresent(AbCacheRegionConstant.LOGIN_CACHE_REGION, USER_LOGIN_COUNT + userName);
            }
        }
        return mapObj;
    }

    public static Boolean queryCaptchaSwitch() {
        return PropertyEnum.LOGIN_CAPTCHA_KEY.getPropertyValue(Boolean.class);
    }

    public static Boolean queryGetBackPwdSwitch() {
        return PropertyEnum.IS_OPEN_RESET_PWD_BY_EMAIL.getPropertyValue(Boolean.class);
    }


    public static Boolean queryIsResetPwd() {
        return PropertyEnum.LOGIN_RESET_PWD.getPropertyValue(Boolean.class);
    }

}

