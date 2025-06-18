package com.dstz.base.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dstz.base.common.utils.ToStringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.hutool.core.util.StrUtil;

/**
 * <pre>
 * The Model class of baomidou after optimization according to ab features
 * Comes with id primary key attribute and toString method
 * </pre>
 *
 * @param <T>
 */
public abstract class AbModel<T extends AbModel<?>> extends Model<T> implements IPersistentEntity {

    @Override
    public String toString() {
        return ToStringUtils.toString(this);
    }
    
    /**
     * <pre>
     * Determine whether id is empty
     * </pre>	
     * @return
     */
    @JsonIgnore
    public boolean isIdEmpty() {
    	return StrUtil.isEmpty(this.getId());
    }
    
    /**
     * <pre>
     * Determine whether id is non-empty
     * </pre>	
     * @return
     */
    @JsonIgnore
    public boolean isIdNotEmpty() {
    	return StrUtil.isNotEmpty(this.getId());
    }
}
