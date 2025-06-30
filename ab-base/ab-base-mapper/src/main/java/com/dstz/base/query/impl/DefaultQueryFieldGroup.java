package com.dstz.base.query.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.enums.GlobalApiCodes;
//gitlab.agilebpm.cn/agilebpm-v5/agile-bpm
import com.dstz.base.common.exceptions.ApiException;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.base.query.ConditionType;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Query Grouping
 * <pre>
 * There is an OR condition between groups, which is not supported for adjustment
 * eg: defaultGroup AND (groupA) OR (groupB) OR (groupC)
 * The default condition between fields is AND, which is not supported yet.
 * eg: (groupA) : ( name_ like 'xx' and age_ = '3' )
 * </pre>
 *
 */
public class DefaultQueryFieldGroup {
	public final static String DEFAULT_KEY = "AB_DGL_DK";

	private Map<String, List<QueryField>> fieldGroupMap = null;


	/**
	 * Initialize queryGroup size
	 */
	public DefaultQueryFieldGroup() {
		this.fieldGroupMap = MapUtil.newHashMap(6);
	}

	/**
	 * Only the IN type needs to directly splice the parameters. In this case, use inValue
	 * @param groupKey
	 * @param fieldName
	 * @param condition
	 * @param inValue
	 */
	public void addQueryField(String groupKey, String fieldName, ConditionType condition, Object inValue) {
		if(StrUtil.isEmpty(groupKey)) {
			groupKey = DEFAULT_KEY;
		}

		if(condition == ConditionType.IN || condition == ConditionType.NOT_IN) {
			if(StrUtil.isEmptyIfStr(inValue)) {
				throw new ApiException(GlobalApiCodes.PARAMETER_INVALID);
			}
		}
		List<QueryField> fields = fieldGroupMap.getOrDefault(groupKey, new ArrayList<>(10));
		fields.add(new QueryField(fieldName, condition,  StrUtil.toStringOrNull(inValue)) );
		this.fieldGroupMap.put(groupKey, fields);
	}

	public String getWhereSql() {
		// Default group conditions
		String mainSql = genSql(DEFAULT_KEY);
		StringBuilder orSql = new StringBuilder();//or group sql

		fieldGroupMap.forEach((key, val) -> {
			if(DEFAULT_KEY.equals(key)) {
				return;
			}
			String str = this.genSql(key);

			if(orSql.length()>0) {
				orSql.append(" or ");
			}else {
				orSql.append("( ");
			}

			orSql.append(str);
		});

		if(StrUtil.isNotEmpty(orSql)) {
			orSql.append(" )");
			if(StrUtil.isNotEmpty(mainSql)) {
				orSql.insert(0, "and");
			}
		}

		if(StrUtil.isNotEmpty(mainSql)) {
			orSql.insert(0, mainSql);
		}

		return orSql.toString();

	}

	private String genSql(String groupKey) {
		List<QueryField> fields = fieldGroupMap.get(groupKey);
		if (CollectionUtil.isEmpty(fields)) {
			return StrUtil.EMPTY;
		}

		StringBuilder sql = new StringBuilder("(");

		for (QueryField field : fields) {
			if ( ! CharUtil.equals('(', sql.charAt(sql.length()-1), false)) {
				sql.append(" AND ");
			}
			sql.append(field.getSql());
		}

		sql.append(")");

		return sql.toString();
	}

	public Map<String, List<QueryField>> getFieldGroupMap() {
		return fieldGroupMap;
	}

	public void setFieldGroupMap(Map<String, List<QueryField>> fieldGroupMap) {
		this.fieldGroupMap = fieldGroupMap;
	}


}
