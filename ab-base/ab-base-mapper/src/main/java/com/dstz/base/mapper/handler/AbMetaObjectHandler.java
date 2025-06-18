package com.dstz.base.mapper.handler;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Business Entity Generic Field Processor
 *
 */
@Component
public class AbMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		Optional<IUser> currentUser = UserContextUtils.getUser();
		Optional<IGroup> currentGroup = UserContextUtils.getGroup();

		// Creator ID
		setFieldValueIfNull(metaObject, "createBy", () -> currentUser.map(IUser::getUserId).orElse(StrUtil.EMPTY));
		// Creator Name
		setFieldValueIfNull(metaObject, "creator", () -> currentUser.map(IUser::getFullName).orElse(StrUtil.EMPTY));
		// Creation time
		setFieldValueIfNull(metaObject, "createTime", Date::new);
		// Create the organization ID
		setFieldValueIfNull(metaObject, "createOrgId", () -> currentGroup.map(IGroup::getGroupId).orElse(StrUtil.EMPTY));
		// Updater ID
		setFieldValueIfNull(metaObject, "updateBy", () -> currentUser.map(IUser::getUserId).orElse(StrUtil.EMPTY));
		// Updater
		setFieldValueIfNull(metaObject, "updater", () -> currentUser.map(IUser::getFullName).orElse(StrUtil.EMPTY));
		// Update time
		setFieldValueIfNull(metaObject, "updateTime", Date::new);
		// Leguan lock default fields
		TableFieldInfo versionFieldInfo = findTableInfo(metaObject).getVersionFieldInfo();
		if (versionFieldInfo != null) {
			setFieldValueIfNull(metaObject, versionFieldInfo.getField().getName(), () -> getTypeDefaultValue(versionFieldInfo.getPropertyType()));
		}
	}

	private void setFieldValueIfNull(MetaObject metaObject, String fieldName, Supplier<Object> supplier){
		if (metaObject.hasSetter(fieldName) && metaObject.getValue(fieldName) == null) {
			metaObject.setValue(fieldName, supplier.get());
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		UserContextUtils.getUser().ifPresent(currentUser -> {
			// Updater ID
			setFieldValByName("updateBy", currentUser.getUserId(), metaObject);
			// Updater
			setFieldValByName("updater", currentUser.getFullName(), metaObject);
		});
		setFieldValByName("updateTime", new Date(), metaObject);
	}

	private Object getTypeDefaultValue(Class<?> typeClass) {
		if (ClassUtil.isBasicType(typeClass) || ClassUtil.isAssignable(BigDecimal.class, typeClass) || ClassUtil.isAssignable(BigInteger.class, typeClass)) {
			return Convert.convert(typeClass, "0");
		} else if (ClassUtil.isAssignable(java.sql.Date.class, typeClass)) {
			return new java.sql.Date(System.currentTimeMillis());
		} else if (ClassUtil.isAssignable(java.sql.Timestamp.class, typeClass)) {
			return new java.sql.Timestamp(System.currentTimeMillis());
		} else if (ClassUtil.isAssignable(Date.class, typeClass)) {
			return new Date();
		}
		throw new UnsupportedOperationException(String.format("Type (%s) does not support default value", typeClass.getName()));
	}
}
