package com.dstz.sys.core.mapper;

import com.dstz.sys.core.entity.SysDataDict;
import com.dstz.base.mapper.AbBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Data dictionary Mapper interface
 * </p>
 *
 */
@Mapper
public interface SysDataDictMapper extends AbBaseMapper<SysDataDict> {

    /**
     * Get dictionary item by dicKey. If hasRoot, it contains the dictionary itself
     * @param dictKey
     * @param hasRoot
     * @return
     */
    List<SysDataDict> getDictNodeList(@Param("dictKey")String dictKey, @Param("hasRoot") Boolean hasRoot);

    /**
     * Determine whether the dictionary exists,
     * @param code
     * @param id If it is not null, exclude id to determine whether it exists. Used for update
     * @return
     */
    Integer isExistDict(@Param("code")String code,@Param("id") String id);

    /**
     * Determine whether a dictionary item exists
     * @param dictKey
     * @param id  id If it is not null, exclude id to determine whether it exists. Used for update
     * @return
     */
    Integer isExistNode(@Param("dictKey")String dictKey,@Param("code")String code,@Param("id") String id);

    /**
     * Get dictionary by ParentId
     * @param id
     * @return
     */
    List<SysDataDict> getByParentId(String id);

}
