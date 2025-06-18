package com.dstz.base.mapper.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * queryCursorByWrapper Implementing Encapsulation
 *
 */
public class AbQueryCursorByWrapper extends AbstractMethod {

	private static final long serialVersionUID = -4642949513973517235L;

	public AbQueryCursorByWrapper() {
		super("queryCursorByWrapper");
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		SqlMethod sqlMethod = SqlMethod.SELECT_LIST;
		String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlSelectColumns(tableInfo, true), tableInfo.getTableName(),
				sqlWhereEntityWrapper(true, tableInfo), sqlOrderBy(tableInfo), sqlComment());
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addSelectMappedStatementForTable(mapperClass, methodName, sqlSource, tableInfo);
	}
}
