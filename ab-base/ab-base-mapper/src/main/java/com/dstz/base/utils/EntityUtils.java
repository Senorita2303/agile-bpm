package com.dstz.base.utils;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Map;

/**
 * Entity manipulation tools
 *
 */
public class EntityUtils {


	/**
	 * Get column names
	 *
	 * @param func lambda function
	 * @param <T>  Entity
	 * @return Column Name
	 */
	public static <T> String getColumnName(SFunction<T, ?> func) {
		LambdaMeta lambdaMeta = LambdaUtils.extract(func);
		String fieldName = PropertyNamer.methodToProperty(lambdaMeta.getImplMethodName());
		Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(lambdaMeta.getInstantiatedClass());
		ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(fieldName));
		return columnCache.getColumn();
	}


}
