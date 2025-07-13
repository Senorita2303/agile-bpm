package com.dstz.auth.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.constats.StrPool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Cross-domain address checking
 *
 */
public class RefererCsrfChecker {
    private List<Pattern> ingores = new ArrayList<Pattern>();

    public void setIngores(List<String> urls) {
        if (CollectionUtil.isEmpty(urls)){ return;}
        for (String url : urls) {
            // Prevent empty configuration errors
            if(StrUtil.isEmpty(url)){continue;}

            Pattern regex = Pattern.compile(url, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE |
                    Pattern.DOTALL | Pattern.MULTILINE);
            ingores.add(regex);
        }
    }


    /**
     * Determines whether the current URL is among the ignored addresses.
     *
     * @param requestUrl
     * @return
     */
    public boolean isIngores(String requestUrl) {
        // It will jump to index.html again, so just ignore it
        if(StrPool.SLASH.equals(requestUrl)){return true;}

        for (Pattern pattern : ingores) {
            Matcher regexMatcher = pattern.matcher(requestUrl);
            if (regexMatcher.find()) {
                return true;
            }
        }
        return false;
    }
}
