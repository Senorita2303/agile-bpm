package com.dstz.sys.core.manager;

import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.sys.api.vo.SysDataDictVO;
import com.dstz.sys.core.entity.SysDataDict;
import com.dstz.base.manager.AbBaseManager;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Data dictionary General business class
 * </p>
 *
 */
public interface SysDataDictManager extends AbBaseManager<SysDataDict> {
    /**
     * Get dictionary item by dicKey. If hasRoot, it contains the dictionary itself.
     * @param dictKey
     * @param hasRoot
     * @return
     */
    List<SysDataDict> getDictNodeList(String dictKey, Boolean hasRoot);

    /**
     * Get dictionary tree
     * @param paramDTO
     * @return
     */
    List<SysDataDictVO> getDictTree(QueryParamDTO paramDTO);

    /**
     * Get the dictionary category and the dictionary below to construct a tree stump data structure
     * @return
     */
    List<SysDataDictVO> getDictTypeTree();

    /**
     * Add
     * @param entry
     * @return id
     */
    @Override
    int create(SysDataDict entry);

    /**
     * Modify
     * @param entry
     * @return id
     */
    @Override
    int update(SysDataDict entry);


    /**
     * Update dictionary cache
     * @param dictKeys
     */
    void updateCache(Set<String> dictKeys);
}
