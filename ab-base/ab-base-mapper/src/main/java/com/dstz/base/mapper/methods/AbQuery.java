package com.dstz.base.mapper.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.text.MessageFormat;
import java.util.Map;

public class AbQuery extends AbstractMethod {
	public AbQuery() {
		super("query");
	}

	public AbQuery(String name) {
		super(name);
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		StringBuilder sb = new StringBuilder("<script>SELECT ");
		// Populate Field Column
		sb.append("<choose>")
		  .append("<when test=\"@cn.hutool.core.util.StrUtil@isNotBlank(SELECT_COLUMN_NAMES)\">${SELECT_COLUMN_NAMES}</when>")
		  .append(MessageFormat.format("<otherwise>{0}</otherwise>", tableInfo.getAllSqlSelect()));
		sb.append("</choose>");
		
		// When assembling SQL, remember to surround it with <script> to run <if> and other tags
		sb.append(" FROM ").append(tableInfo.getTableName()).append(" ");
		// Query conditions
		sb.append("<where><if test=\"@cn.hutool.core.util.StrUtil@isNotBlank(whereSql)\">${whereSql}</if></where>");
		// Order
		sb.append(" ORDER BY  ");
		sb.append("<choose>")
		  .append("<when test=\"@cn.hutool.core.util.StrUtil@isNotBlank(orderBySql)\">${orderBySql}</when>")
		  .append(MessageFormat.format("<otherwise>{0} DESC</otherwise>", tableInfo.getKeyColumn()));
		sb.append("</choose>");
		  
		sb.append("</script>");

		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sb.toString(), Map.class);
		// Note that addSelectMappedStatementForTable is used for querying. Adding, updating, and deleting each correspond to different methods.
		return this.addSelectMappedStatementForTable(mapperClass, "query", sqlSource, tableInfo);
	}

}
