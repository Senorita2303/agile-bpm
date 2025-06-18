package com.dstz.sys.rest.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.utils.BeanConversionUtils;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.sys.api.constant.SysCackeKeyConstant;
import com.dstz.sys.api.vo.SysDataDictVO;
import com.dstz.sys.core.entity.SysDataDict;
import com.dstz.sys.core.manager.SysDataDictManager;
import com.dstz.sys.rest.model.dto.GetDictDTO;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * Data dictionary front-end controller
 * </p>
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/dataDict/")
public class
SysDataDictController extends AbCrudController<SysDataDict> {

    private final SysDataDictManager sysDataDictManager;
    private final ICache iCache;

    public SysDataDictController(SysDataDictManager sysDataDictManager, ICache iCache) {
        this.sysDataDictManager = sysDataDictManager;
        this.iCache = iCache;
    }

    /**
     * Get dictionary items through dicKey, the data structure is list
     */
    @RequestMapping("getDictNodeList")
    public ApiResponse<List<SysDataDict>> getByDictKey(@RequestParam("dictKey") String dictKey) {
        return ApiResponse.success(sysDataDictManager.getDictNodeList(dictKey, false));
    }


    /**
     * Get dictionary item by dicKey. If hasRoot, it contains the dictionary itself
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("getDictData")
    public ApiResponse<List<SysDataDictVO>> getByDictKey(@RequestBody @Valid GetDictDTO getDictDTO) {
        List<SysDataDictVO> nodeVoList = BeanCopierUtils.transformList(sysDataDictManager.getDictNodeList(getDictDTO.getDictKey(), getDictDTO.getHasRoot()), SysDataDictVO.class);
        // If the dictionary itself is included, the dictionary name is used as the root node of the dictionary item tree
        if (CollUtil.isEmpty(nodeVoList) || getDictDTO.getHasRoot()) {
            return ApiResponse.success(BeanConversionUtils.listToTree(nodeVoList));
        }
        SysDataDictVO parent = null;
        // Custom root node
        if (StrUtil.isNotEmpty(getDictDTO.getRootName())) {
            parent = new SysDataDictVO();
            parent.setParentId("0");
            parent.setName(getDictDTO.getRootName());
        } else {
            // Get the dictionary itself as the Root
            parent = CollUtil.findOne(nodeVoList, e -> SysDataDict.TYPE_DICT.equals(e.getDictType()));
        }

        if (parent == null) {
            return ApiResponse.success(BeanConversionUtils.listToTree(nodeVoList));
        }

        parent.setChildren(BeanConversionUtils.listToTree(nodeVoList));
        return ApiResponse.success(Arrays.asList(parent));
    }

    /**
     * Get all dictionaries (excluding dictionary items)
     *
     * @param queryParamDTO
     * @return
     */
    @RequestMapping("getDictList")
    public ApiResponse getDictList(@RequestBody QueryParamDTO queryParamDTO) {
        AbQueryFilter filter = new DefaultAbQueryFilter(queryParamDTO);
        filter.eqFilter("dictType", SysDataDict.TYPE_DICT);
        return ApiResponse.success(sysDataDictManager.query(filter));
    }


    @RequestMapping("getDictTypeTree")
    public ApiResponse<List<SysDataDictVO>> getDictTree() {
        return ApiResponse.success(sysDataDictManager.getDictTypeTree());
    }

    @RequestMapping("getDictTree")
    public ApiResponse<List<SysDataDictVO>> getDictTree(@RequestBody QueryParamDTO queryParamDTO) throws Exception {
        return ApiResponse.success(sysDataDictManager.getDictTree(queryParamDTO));
    }

    /**
     * Save entity data
     *
     * @param entity Entity
     * @return Interface response-entity ID
     */
    @RequestMapping(value = "save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> save(@Valid @RequestBody SysDataDict entity) {
        String desc;
        if (StringUtils.hasLength(entity.getId())) {
            desc = "Update %s successfully";
            sysDataDictManager.update(entity);
        } else {
            desc = "Add %s successfully";
            sysDataDictManager.create(entity);
        }
        // Put dictionary items in cache
        sysDataDictManager.updateCache(CollUtil.newHashSet(entity.getDictKey()));
        return ApiResponse.success(entity.getId()).withMessage(String.format(desc, getEntityDesc()));
    }


    /**
     * Batch save entity data
     *
     * @param entityList Entity collection
     * @return Interface response-entity ID
     */
    @RequestMapping(value = "saveBatch", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse saveBatch(@Valid @RequestBody List<SysDataDict> entityList) {
        entityList.forEach(entity -> {
            if (StringUtils.hasLength(entity.getId())) {
                sysDataDictManager.update(entity);
            } else {
                sysDataDictManager.create(entity);
            }
        });
        // Put dictionary items in cache
        if (CollUtil.isNotEmpty(entityList)) {
            Set<String> dictKeys = entityList.stream().map(SysDataDict::getDictKey).collect(Collectors.toSet());
            sysDataDictManager.updateCache(dictKeys);
        }
        return ApiResponse.success().withMessage("Batch operation successful");
    }


    /**
     * Batch delete of entities
     *
     * @param id Entity ID, multiple, separated
     * @return Interface response
     */
    @RequestMapping(value = "remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> remove(@RequestParam(name = "id") String id) {
        List<SysDataDict> sysDataDicts = sysDataDictManager.selectByIds(StrUtil.split(id, CharUtil.COMMA));
        sysDataDicts.forEach(e -> {
            iCache.invalidate(AbCacheRegionConstant.DICT_CACHE_REGION, SysCackeKeyConstant.GET_DICT_NODE_LIST + e.getDictKey());
        });
        sysDataDictManager.removeByIds(StrUtil.split(id, CharUtil.COMMA));
        final String message = String.format("Delete %s successfully", getEntityDesc());
        return ApiResponse.<String>success().withMessage(message);
    }

    @Override
    protected String getEntityDesc() {
        return "Data dictionary";
    }
}
