package com.dstz.sys.groovy;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.async.ContextCleanTaskDecorator;
import com.dstz.base.common.async.ContextDuplicationTaskDecorator;
import com.dstz.base.common.identityconvert.SysIdentity;
import com.dstz.base.common.script.ScriptLog;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.common.utils.TaskDecoratorUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.datasource.DataSourceLoading;
import com.dstz.base.utils.AbDataSourceUtils;
import com.dstz.groovy.script.api.IScript;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.core.entity.SysProperties;
import com.dstz.sys.core.manager.SysPropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @Name SysScript
 * @description: Some default scripts in the system
 */
@Component
public class SysScript implements IScript {

    protected Logger LOG = LoggerFactory.getLogger(getClass());
 
    @Autowired
    private DataSourceLoading dataSourceLoading;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private  SysPropertiesManager sysPropertiesManager;
 

    /**
     * Get system properties
     * @param key code
     * @return value
     */
    public String getProperty(String key) {
        SysProperties properties = sysPropertiesManager.selectOne(Wrappers.lambdaQuery(SysProperties.class).eq(SysProperties::getCode,key));
        if (properties!=null){
            return properties.getValue();
        }
        return null;
    }


    public IUser getCurrentUser() {
        IUser user = UserContextUtils.getValidUser();
        return user;
    }

    public String getCurrentGroupName() {
        IGroup iGroup =UserContextUtils.getGroup().get();
        if (iGroup!= null) {
            return iGroup.getGroupName();
        } else {
            return "";
        }
    }

    public String getCurrentUserName() {
        return UserContextUtils.getValidUser().getFullName();
    }

    public Integer executeUpdateSql(String sql, Object ... params) {
        Integer result = jdbcTemplate.update(sql, params);
        return result ;
    }

    public Integer executeUpdateSqlOnDataSource(String dataSourceKey,String sql, Object ... params) {
        JdbcOperations jdbcOperations = AbDataSourceUtils.getByDsAlias(dataSourceKey).getJdbcOperations();
        return jdbcOperations.update(sql, params);
    }


    public Integer executeIntegerSql(String sql, Object ... params) {
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, params);
        return result ;
    }

    public Integer executeIntegerSqlOnDataSource(String dataSourceKey,String sql, Object ... params) {
        JdbcOperations jdbcOperations = AbDataSourceUtils.getByDsAlias(dataSourceKey).getJdbcOperations();
        return jdbcOperations.queryForObject(sql, Integer.class, params);
    }

    /**
     * Get candidates through a sql
     * @param sql
     * @param type
     * @param params
     * @return
     */
    public Set<SysIdentity> getIdentityBySqlAndType(String sql, String type, Object... params) {
        List<SysIdentity> list =jdbcTemplate.query(sql, new RowMapperResultSetExtractor<>(((rs, rowNum) -> new SysIdentity(rs.getString(1), rs.getString(2), type))), params);
        return transitionIdentitys(list);
    }

    private Set<SysIdentity> transitionIdentitys(List<SysIdentity> list) {
        Set<SysIdentity> set = new HashSet<>();

        for(SysIdentity identity : list) {
            // If the candidate list returned by SQL is incomplete, it will not be added
            if(StrUtil.isEmpty(identity.getType()) || StrUtil.isEmpty(identity.getName())
                    || StrUtil.isEmpty(identity.getId())) {

                LOG.debug("Failed to obtain user candidates through sql. The identity information returned by sql is incomplete. Please check{}", JsonUtils.toJSONString(identity));
                continue;
            }
            set.add(identity);
        }

        return set;
    }

    /**
     * Get candidates through a SQL, specify the data source, specify the type
     * @param dataSourceKey
     * @param sql
     * @param type
     * @param params
     * @return
     */
    public Set<SysIdentity> getIdentityBySqlOnDataSource(String dataSourceKey,String sql,String type, Object ... params) {
        JdbcOperations jdbcOperations = AbDataSourceUtils.getByDsAlias(dataSourceKey).getJdbcOperations();
        List<SysIdentity> list = jdbcOperations.query(sql, new RowMapperResultSetExtractor<>(((rs, rowNum) -> new SysIdentity(rs.getString(1), rs.getString(2), type))), params);
        return transitionIdentitys(list);
    }


    // Enable thread logging
    public void openScriptLog(Boolean isOpen) {
    	ScriptLog.setConfigOpen(isOpen);
    }



    /**
     * The new thread executes some business logic eg: sysScript.newThead(cust::save)  or sysScript.newThead( ()->{do any thing} )
     * @param target
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void newThead(Runnable target) throws InterruptedException, ExecutionException {
		Runnable runnable = TaskDecoratorUtils.decorate(target, ContextCleanTaskDecorator.INSTANCE, ContextDuplicationTaskDecorator.INSTANCE);
    	ThreadUtil.execAsync(runnable).get();
    }

}
