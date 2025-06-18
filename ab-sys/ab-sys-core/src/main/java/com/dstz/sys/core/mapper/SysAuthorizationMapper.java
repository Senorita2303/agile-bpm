package com.dstz.sys.core.mapper;

import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.sys.core.entity.SysAuthorization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * General resource authorization configuration Mapper interface
 * </p>
 *
 */
@Mapper
public interface SysAuthorizationMapper extends AbBaseMapper<SysAuthorization> {
    List<SysAuthorization> getByTarget(@Param("rightsObject") String rightsObject, @Param("rightsTarget") String rightsTarget);

    void deleteByTarget(@Param("rightsObject") String rightsObject, @Param("rightsTarget") String rightsTarget);
}
