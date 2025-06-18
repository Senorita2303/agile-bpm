package com.dstz.sys.core.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.reflect.MethodHandleUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.datasource.DataSourceLoading;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.base.utils.AbDataSourceUtils;
import com.dstz.sys.api.constant.SysApiCodes;
import com.dstz.sys.api.dto.SysDataSourceDefAttribute;
import com.dstz.sys.core.entity.SysDataSource;
import com.dstz.sys.core.manager.SysDataSourceManager;
import com.dstz.sys.core.mapper.SysDataSourceMapper;
import com.dstz.sys.rest.model.dto.SysDataSourceSaveDTO;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data source General service implementation class
 *
 */
@Service("sysDataSourceManager")
public class SysDataSourceManagerImpl extends AbBaseManagerImpl<SysDataSource> implements SysDataSourceManager, DataSourceLoading {


    private final SysDataSourceMapper sysDataSourceMapper;

    public SysDataSourceManagerImpl(SysDataSourceMapper sysDataSourceMapper) {
        this.sysDataSourceMapper = sysDataSourceMapper;
    }

    @Override
    public DataSource loadDataSource(String alias) {
        SysDataSource sysDataSource = sysDataSourceMapper.selectOne(Wrappers.lambdaQuery(SysDataSource.class).select(SysDataSource::getClassPath, SysDataSource::getAttributesJson).eq(SysDataSource::getAlias, alias));
        if (sysDataSource == null) {
            throw new BusinessMessage(SysApiCodes.DATA_SOURCE_NOT_FOUND_ERROR.formatDefaultMessage(alias));
        }
        Class<DataSource> dataSourceClass = ClassUtil.loadClass(sysDataSource.getClassPath());
        DataSource dataSource = ReflectUtil.newInstance(dataSourceClass);
        List<SysDataSourceDefAttribute> sysDataSourceDefAttributeList = JsonUtils.parseArray(sysDataSource.getAttributesJson(), SysDataSourceDefAttribute.class);
        if (CollUtil.isEmpty(sysDataSourceDefAttributeList)) {
            throw new BusinessMessage(SysApiCodes.DATA_SOURCE_ATTRIBUTE_IS_EMPTY.formatDefaultMessage(SysApiCodes.DATA_SOURCE_ATTRIBUTE_IS_EMPTY, alias));
        }
        for (SysDataSourceDefAttribute sysDataSourceDefAttribute : sysDataSourceDefAttributeList) {
            Method writeMethod;
            if (StrUtil.isNotEmpty(sysDataSourceDefAttribute.getValue()) && Objects.nonNull(writeMethod = Optional.ofNullable(BeanUtil.getPropertyDescriptor(dataSourceClass, sysDataSourceDefAttribute.getName())).map(PropertyDescriptor::getWriteMethod).orElse(null))) {
                Object convertValue = Convert.convert(writeMethod.getParameterTypes()[0], sysDataSourceDefAttribute.getValue());
                MethodHandleUtil.invoke(dataSource, writeMethod, convertValue);
            }
        }
        return dataSource;
    }

    @Override
    public int removeById(Serializable id) {
        return removeByIds(Collections.singleton(id));
    }

    @Override
    public int removeByIds(Collection<? extends Serializable> ids) {
        List<SysDataSource> sysDataSourceList = sysDataSourceMapper.selectList(Wrappers.lambdaQuery(SysDataSource.class).select(SysDataSource::getId, SysDataSource::getAlias).in(SysDataSource::getId, ids));
        if (CollUtil.isNotEmpty(sysDataSourceList)) {
            for (SysDataSource sysDataSource : sysDataSourceList) {
                AbDataSourceUtils.getDataSourceContext().removeDataSource(sysDataSource.getAlias());
            }
            return sysDataSourceMapper.deleteBatchIds(sysDataSourceList.stream().map(SysDataSource::getId).collect(Collectors.toSet()));
        }
        return 0;
    }

    @Override
    public boolean isExist(String alias, String id) {
        LambdaQueryWrapper<SysDataSource> queryWrapper = Wrappers.lambdaQuery(SysDataSource.class);
        queryWrapper.eq(SysDataSource::getAlias, alias);
        if (StrUtil.isNotEmpty(id)) {
            queryWrapper.ne(SysDataSource::getId, id);
        }
        return sysDataSourceMapper.exists(queryWrapper);
    }

    @Override
    public int save(SysDataSourceSaveDTO saveDTO) {
        //Determine whether an alias exists
        if (isExist(saveDTO.getAlias(), saveDTO.getId())) {
            throw new BusinessMessage(SysApiCodes.KEY_WORD_DUPLICATE.formatDefaultMessage("Alias" + saveDTO.getAlias()));
        }

        SysDataSource sysDataSource = BeanCopierUtils.transformBean(saveDTO, SysDataSource.class);
        List<SysDataSourceDefAttribute> attributeList = saveDTO.getAttributeList();
        if (CollUtil.isNotEmpty(attributeList)) {
            sysDataSource.setAttributesJson(JsonUtils.toJSONString(saveDTO.getAttributeList()));
        }

        if (StrUtil.isEmpty(saveDTO.getId())) {
            return sysDataSourceMapper.insert(sysDataSource);
        } else {
            AbDataSourceUtils.getDataSourceContext().removeDataSource(sysDataSource.getAlias());
            return sysDataSourceMapper.updateById(sysDataSource);
        }
    }

    @Override
    public PageListDTO<SysDataSource> query(QueryParamDTO dto) {
        return super.query(new DefaultAbQueryFilter(dto));
    }


}
