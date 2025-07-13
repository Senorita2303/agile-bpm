package com.dstz.sys.api;

import com.dstz.sys.api.vo.SysDataDictVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Name SysDataDictApi
 * @description: Dictionary
 */
public interface SysDataDictApi {
    /**
     * Get dictionary list
     *
     * @param dictKey
     * @param hasRoot
     * @return
     */
    List<SysDataDictVO> getDictNodeList(String dictKey, Boolean hasRoot);

    /**
     * Add or save in batches
     * @param dtoList
     */
    void batchSaveOrUpdate(List<SysDataDictVO> dtoList) ;

    /**
     * Export categories
     * @param dictKey Dictionary key of the category
     * @param typeCodes The codes collection of the classification dictionary items to be exported
     * @return
     */
    List<SysDataDictVO> exportTypeData(String dictKey, Set<String> typeCodes);

    /**
     * Delete by code
     * @param codes
     */
    void deleteByCodes(Collection<? extends Serializable> codes);
}
