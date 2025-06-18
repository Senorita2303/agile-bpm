package com.dstz.sys.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.MapWrapper;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.utils.ThreadMapUtil;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.enums.AbDbType;
import com.dstz.base.utils.AbDataSourceUtils;
import com.dstz.groovy.script.api.IGroovyScriptEngine;
import com.dstz.org.api.GroupApi;
import com.dstz.org.api.enums.GroupGradeConstant;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Name SystemVariableApiImpl
 * @description: System constant acquisition implementation class
 */
@Service
public class SystemVariableApiImpl implements SystemVariableApi, InitializingBean {

    private final GroupApi groupApi;
    private final Map<String, Supplier<Object>> variableLoaderMap = new HashMap<>();
    private final static String patternString = "\\$\\{([^}]+)}";
    private final static Pattern pattern = Pattern.compile(patternString);

    public SystemVariableApiImpl(GroupApi groupApi) {
        this.groupApi = groupApi;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //Current user information
        variableLoaderMap.put("currentUserId", () -> UserContextUtils.getUser().map(IUser::getUserId).orElse(null));
        variableLoaderMap.put("currentUserName", () -> UserContextUtils.getUser().map(IUser::getFullName).orElse(null));
        variableLoaderMap.put("currentUserAccount", () -> UserContextUtils.getUser().map(IUser::getUsername).orElse(null));
        variableLoaderMap.put("currentOrgId", () -> UserContextUtils.getGroup().map(IGroup::getGroupId).orElse(null));
        variableLoaderMap.put("currentOrgName", () -> UserContextUtils.getGroup().map(IGroup::getGroupName).orElse(null));
        variableLoaderMap.put("currentOrgCode", () -> UserContextUtils.getGroup().map(IGroup::getGroupCode).orElse(null));
        // Date format string
        variableLoaderMap.put("currentDateTime", Date::new);
        variableLoaderMap.put("curDate", () -> DateUtil.formatDate(new Date()));
        variableLoaderMap.put("curDateTime", () -> DateUtil.formatDateTime(new Date()));
        variableLoaderMap.put("curTime", () -> DateUtil.formatTime(new Date()));

        //Get the company information of the current user
        variableLoaderMap.put("currentCompanyId", this::getCompanyId);
        variableLoaderMap.put("currentCompanyName", this::getCompanyName);
        variableLoaderMap.put("currentCompanyCode", this::getCompanyCode);

        //Current database type
        variableLoaderMap.put("dbType", () -> AbDataSourceUtils.getCurrentDataSource().getDbType().getDb());
    }


    @Override
    public Map<String, Object> getVariableMap() {
        return new MapWrapper<String, Object>(MapUtil.newHashMap()) {
            @Override
            public Object get(Object key) {
                return getRaw().computeIfAbsent((String) key, k -> {
                    Supplier<Object> fn = variableLoaderMap.get(k);
                    if (fn != null) {
                        return fn.get();
                    }
                    return null;
                });
            }
        };
    }

    private String getCompanyId() {
        IGroup group = getCompany();
        return group == null ? null : group.getGroupId();
    }

    private String getCompanyName() {
        IGroup group = getCompany();
        return group == null ? null : group.getGroupName();
    }

    private String getCompanyCode() {
        IGroup group = getCompany();
        return group == null ? null : group.getGroupCode();
    }

    private IGroup getCompany() {
        //Get from the thread variable, return if it is available, get if it is not available
        if (ObjectUtil.isNotNull(ThreadMapUtil.get("currentCompany"))) {
            return (IGroup) ThreadMapUtil.get("currentCompany");
        }
        // Get the current organization
        IGroup currentOrg = UserContextUtils.getGroup().orElse(null);
        if (currentOrg == null) {
            return null;
        }
        // If the current organization is a company, return directly
        if (Integer.valueOf(GroupGradeConstant.COMPANY.getKey()).equals(currentOrg.getGroupLevel())) {
            ThreadMapUtil.put("currentCompany", currentOrg);
            return currentOrg;
        }
        //Recursively find the company where the current organization is located
        IGroup company = getParentGroupByType(currentOrg);
        if (company != null) {
            ThreadMapUtil.put("currentCompany", company);
        }
        return company;
    }

    private IGroup getParentGroupByType(IGroup group) {
        IGroup parentGroup = null;
        groupApi.getByGroupId(GroupType.ORG.getType(), group.getParentId());
        if (parentGroup == null) {
            return null;
        }
        if (Integer.valueOf(GroupGradeConstant.COMPANY.getKey()).equals(parentGroup.getGroupLevel())) {
            return parentGroup;
        }
        return getParentGroupByType(parentGroup);
    }


    @Override
    public boolean containScript(String value) {
        if (value == null) {
            return false;
        }
        return (value.startsWith("#{") || value.startsWith("${")) && value.endsWith("}");
    }


    @Override
    public Object getVariableValue(Object val) {
        if (!(val instanceof String)) {
            return val;
        }
        String value = val.toString();
        if (!containScript(value)) {
            return value;
        }

        // The constants are enclosed in ${}.
        if (value.startsWith("${") && value.endsWith("}")) {
            return getVariableMap().get(value.substring(2, value.length() - 1));
        }

        // The script is surrounded by #{}
        if (value.startsWith("#{") && value.endsWith("}")) {
            IGroovyScriptEngine engine = SpringUtil.getBean(IGroovyScriptEngine.class);
            String script = value.substring(2, value.length() - 1);
            // Automatic assembly
            if (!script.contains("return ")) {
                script = "return " + script;
            }
            return engine.evaluate(script, new HashMap<>());
        }
        return value;
    }

    @Override
    public Map<String, Object> getVariableMap(String str) {
        Matcher matcher = pattern.matcher(str);
        Map<String, Object> resultMap = MapUtil.newHashMap();

        while (matcher.find()) {
            String match = matcher.group(1);
            if (variableLoaderMap.containsKey(match)) {
                Object result = variableLoaderMap.get(match).get();
                if (result != null) {
                    resultMap.put(match, result);
                }
            }
        }
        return resultMap;
    }
}
