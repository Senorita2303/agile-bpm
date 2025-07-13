package com.dstz.base.mapper;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.dstz.base.mapper.methods.AbBulkCreate;
import com.dstz.base.mapper.methods.AbQuery;
import com.dstz.base.mapper.methods.AbQueryCursorByWrapper;
import com.dstz.base.mapper.methods.AbUpdateFullById;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 * Cover the timing of each mapper implantation method of mybatis-plus and implant the corresponding query method for each mapper
 * </pre>
 */
@Service
public class AbSqlInjector extends DefaultSqlInjector {
	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
		List<AbstractMethod> methods = super.getMethodList(mapperClass, tableInfo);
		methods.add(new AbQuery());// Add AB query method to mapper
		// Added Update based on ID integrity
		methods.add(new AbUpdateFullById());
		// Added Get cursor based on query conditions
		methods.add(new AbQueryCursorByWrapper());
		// Added batch insert method
		methods.add(new AbBulkCreate());
		return methods;
	}
}
