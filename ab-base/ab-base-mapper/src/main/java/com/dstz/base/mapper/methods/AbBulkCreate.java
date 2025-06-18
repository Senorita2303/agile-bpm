package com.dstz.base.mapper.methods;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.dstz.base.enums.AbDbType;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * Batch Insert
 *
 */
public class AbBulkCreate extends AbstractMethod {

	public AbBulkCreate() {
		super("bulkCreate");
	}

	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		String columnJoinComma = CollUtil.join(tableInfo.getFieldList(), COMMA, TableFieldInfo::getColumn);
		String values = CollUtil.join(tableInfo.getFieldList(), COMMA, field -> String.format("#{entity.%s}", field.getProperty()));
		if (tableInfo.havePK()) {
			columnJoinComma = tableInfo.getKeyColumn() + COMMA + columnJoinComma;
			values = String.format("#{entity.%s}", tableInfo.getKeyProperty()) + COMMA + values;
		}

		String sql;
		if(StrUtil.equalsAny(configuration.getDatabaseId(), AbDbType.ORACLE.getDb())){
			sql = String.format(
					"<script>INSERT ALL <foreach collection=\"%s\" item=\"entity\">INTO %s (%s) VALUES (%s)</foreach> SELECT * FROM dual</script>",
					Constants.COLL,
					tableInfo.getTableName(),
					columnJoinComma,
					values
			);
		}else{
			sql = String.format(
					"<script>INSERT INTO %s (%s) VALUES <foreach collection=\"%s\" item=\"entity\" separator=\",\">(%s)</foreach></script>",
					tableInfo.getTableName(),
					columnJoinComma,
					Constants.COLL,
					values
			);
		}

		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return addInsertMappedStatement(mapperClass, modelClass, sqlSource, NoKeyGenerator.INSTANCE, null, null);
	}
}
