package com.dstz.base.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.query.AbPage;
import com.dstz.base.query.AbQueryFilter;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Intercepts(
	    {
	        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
	    }
	)
public class AbQueryFilterInterceptor implements Interceptor {
	@SuppressWarnings("unchecked")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		if(ArrayUtil.isEmpty(args) || args.length < 2) {
			 return invocation.proceed();
		}
		
		Object param = args[1];
		AbPage page = null;
		// Paging, parameter conversion
		Boolean isAbQueryFilterBoolean = param instanceof AbQueryFilter;
		if(isAbQueryFilterBoolean) {
			AbQueryFilter queryFilter = (AbQueryFilter) param;
			// MybatisPlugs paging plug-in custom parameters need to be declared, mybatisplugs conflicts with its own page plug-in, so this is the only way to get around it
			page = queryFilter.getPage();
	        // No paging, no Page is passed
	        if (page == null) {
	            page = new AbPage();
	            page.setParams(queryFilter.generateQuerySql());
	        } else {
	            // If there is paging, convert the paging into MAP and pass it in to bypass the inconsistent processing logic of mp
	            page.setParams(queryFilter.generateQuerySql());
	            page.put("AB_PAGE_INFO", page);
	        }
			// Convert queryFilter to Map Param
			if (CollUtil.isNotEmpty(queryFilter.getSelectColumnNames())) {
				page.put("SELECT_COLUMN_NAMES", CollUtil.join(queryFilter.getSelectColumnNames(), StrUtil.COMMA));
			}
			
			args[1] = page;
		}
		
		Object object = invocation.proceed();
		 if(isAbQueryFilterBoolean) {
			 // Encapsulated as abPageList and returned
			 return new PageListDTO<>(page.getSize(), page.getCurrent(), page.getTotal(),(List) object);
		 }
		 
		 return object;
	}

}
