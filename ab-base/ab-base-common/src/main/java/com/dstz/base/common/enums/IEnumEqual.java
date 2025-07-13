package com.dstz.base.common.enums;


import cn.hutool.core.util.ReflectUtil;

public interface IEnumEqual {
	/**
	 * <pre>
	 * [Note that this is not a value] The definition of the field name of the enumeration KEY,
	 * Used for general interfaces to unify all enumeration key comparison logic
	 * The default is "key", it can also be "type", "name", etc.
	 * </pre>	
	 * @return
	 */
	default String getKeyName() {
		return "key";
	}
	
	default boolean equalsWithKey(String key) {
        return key.equals(ReflectUtil.getFieldValue(this, getKeyName()));
    }
}
