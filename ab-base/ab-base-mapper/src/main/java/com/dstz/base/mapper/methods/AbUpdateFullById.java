package com.dstz.base.mapper.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Update based on ID integrity, without checking if the field is empty
 *
 */
public class AbUpdateFullById extends AbstractMethod {

	private static final long serialVersionUID = -8602998669480218957L;

	public AbUpdateFullById() {
		super("updateFullById");
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		final String additional = optlockVersion(tableInfo) + tableInfo.getLogicDeleteSql(true, true);
		String sqlSetScript = tableInfo.getFieldList().stream().filter(field -> {
			if (tableInfo.isWithLogicDelete()) {
				return !(tableInfo.isWithLogicDelete() && field.isLogicDelete());
			}
			return true;
		}).map(field -> field.getSqlSet(true, ENTITY_DOT)).filter(Objects::nonNull).collect(Collectors.joining(StringPool.NEWLINE));
		
		String sql = String.format(SqlMethod.UPDATE_BY_ID.getSql(), tableInfo.getTableName(),
				SqlScriptUtils.convertSet(sqlSetScript),
				tableInfo.getKeyColumn(), ENTITY_DOT + tableInfo.getKeyProperty(), additional);
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
	}
	
	
}
