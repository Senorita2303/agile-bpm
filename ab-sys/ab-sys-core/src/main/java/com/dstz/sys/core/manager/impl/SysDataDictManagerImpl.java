package com.dstz.sys.core.manager.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanConversionUtils;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.sys.api.constant.SysApiCodes;
import com.dstz.sys.api.constant.SysCackeKeyConstant;
import com.dstz.sys.api.vo.SysDataDictVO;
import com.dstz.sys.core.entity.SysDataDict;
import com.dstz.sys.core.manager.SysDataDictManager;
import com.dstz.sys.core.mapper.SysDataDictMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Data dictionary general service implementation class
 *
 */
@Service("sysDataDictManager")
public class SysDataDictManagerImpl extends AbBaseManagerImpl<SysDataDict> implements SysDataDictManager {

    private final SysDataDictMapper sysDataDictMapper;
    private final ICache cache;

    public SysDataDictManagerImpl(SysDataDictMapper sysDataDictMapper, ICache cache) {
        this.sysDataDictMapper = sysDataDictMapper;
        this.cache = cache;
    }

    @Override
    public List<SysDataDict> getDictNodeList(String dictKey, Boolean hasRoot) {
        return sysDataDictMapper.getDictNodeList(dictKey, hasRoot);
    }

    @Override
    public List<SysDataDictVO> getDictTree(QueryParamDTO paramDTO) {
        AbQueryFilter filter = new DefaultAbQueryFilter(paramDTO);
        //filter.eqFilter("dictType", SysDataDict.TYPE_DICT);
        filter.noPage();
        //Search all dictionaries
        List<SysDataDictVO> sysDataDictVOList = BeanCopierUtils.transformList(this.query(filter).getRows(), SysDataDictVO.class);

        if (CollUtil.isEmpty(sysDataDictVOList)) {
            return sysDataDictVOList;
        }
        //Set root
        return setRoot(BeanConversionUtils.listToTree(sysDataDictVOList));
    }

    private List<SysDataDictVO> setRoot(List<SysDataDictVO> dictTree) {
        SysDataDict parent = new SysDataDict();
        parent.setName("All Data");

        SysDataDictVO vo = BeanCopierUtils.transformBean(parent, SysDataDictVO.class);
        vo.setChildren(dictTree);
        return Arrays.asList(vo);
    }

    @Override
    public List<SysDataDictVO> getDictTypeTree() {
        //First query the classification of the data dictionary
        List<SysDataDict> dictTypeList = getDictNodeList("dictType", Boolean.FALSE);
        Map<String, SysDataDict> dictTypeMap = dictTypeList.stream().collect(Collectors.toMap(SysDataDict::getCode, Function.identity(), (k1, k2) -> k1));
        //Query the dictionary under each category (excluding dictionary items)
        List<SysDataDict> dataDictList = sysDataDictMapper.selectList(Wrappers.lambdaQuery(SysDataDict.class).eq(SysDataDict::getDictType, SysDataDict.TYPE_DICT));
        if (CollUtil.isEmpty(dataDictList)) {
            return new ArrayList<>();
        }
        Map<String, List<SysDataDict>> dataDictMap = dataDictList.stream().collect(Collectors.groupingBy(SysDataDict::getTypeCode));
        List<SysDataDictVO> result = new ArrayList<>(dictTypeList.size());
        dataDictMap.forEach((typeCode, item) -> {
            SysDataDict type = dictTypeMap.get(typeCode);
            if (type != null) {
                SysDataDictVO vo = BeanCopierUtils.transformBean(type, SysDataDictVO.class);
                vo.setChildren(BeanCopierUtils.transformList(item, SysDataDictVO.class));
                result.add(vo);
            }
        });
        return result;
    }

    private List<SysDataDictVO> getDictByTypeCode(String typeCode) {
        List<SysDataDict> dictTypeList = sysDataDictMapper.selectList(Wrappers.<SysDataDict>lambdaQuery().eq(SysDataDict::getDictType, SysDataDict.TYPE_DICT)
                .eq(SysDataDict::getTypeCode, typeCode));
        return BeanCopierUtils.transformList(dictTypeList, SysDataDictVO.class);
    }


    @Override
    public int removeByIds(Collection<? extends Serializable> ids) {
        for (Serializable entityId : ids) {
            removeById(entityId);
        }
        return ids.size();
    }

    @Override
    public int removeById(Serializable id) {
        SysDataDict entity = sysDataDictMapper.selectById(id);
        if (entity == null) {
            return 0;
        }
        if (Integer.valueOf(StrPool.BOOLEAN_TRUE).equals(entity.getIsSystem())) {
            throw new BusinessMessage(SysApiCodes.DICT_DELETE_ERROR.formatDefaultMessage(entity.getName()));
        }
        return removeByEntity(entity);
    }

    /**
     * Cascade delete subset
     *
     * @param entity
     */
    private int removeByEntity(SysDataDict entity) {
        List<SysDataDict> dataDictChildren = sysDataDictMapper.getByParentId(entity.getId());
        for (SysDataDict dict : dataDictChildren) {
            removeByEntity(dict);
        }
        int result =  sysDataDictMapper.deleteById(entity.getId());
        cache.invalidate(AbCacheRegionConstant.DICT_CACHE_REGION, SysCackeKeyConstant.GET_DICT_NODE_LIST + entity.getDictKey());
        return result;
    }


    @Override
    public int create(SysDataDict dataDict) {
        saveCheck(dataDict);
        return super.create(dataDict);
    }


    @Override
    public int update(SysDataDict dataDict) {
        saveCheck(dataDict);
        return super.update(dataDict);
    }


    private void saveCheck(SysDataDict dataDict) {
        String dictType = dataDict.getDictType();
        String code = dataDict.getCode();
        String id = dataDict.getId();
        String dictKey = dataDict.getDictKey();

        if (SysDataDict.TYPE_DICT.equals(dictType) && sysDataDictMapper.isExistDict(code, id) > 0) {
            throw new BusinessMessage(SysApiCodes.KEY_WORD_DUPLICATE.formatDefaultMessage("Dictionary" + code));
        }

        if (SysDataDict.TYPE_NODE.equals(dictType) && sysDataDictMapper.isExistNode(dictKey, code, id) > 0) {
            throw new BusinessMessage(SysApiCodes.KEY_WORD_DUPLICATE.formatDefaultMessage("Dictionary items" + code));
        }
        // The type can only be dict node
        if (!SysDataDict.TYPE_DICT.equals(dictType) && !SysDataDict.TYPE_NODE.equals(dictType)) {
            throw new BusinessMessage(SysApiCodes.DICT_KEY_TYPE_ERROR);
        }
    }

    @Override
    public void updateCache(Set<String> dictKeys) {
        List<SysDataDict> sysDataDictList = this.selectByWrapper(Wrappers.lambdaQuery(SysDataDict.class).
                eq(SysDataDict::getDictType, SysDataDict.TYPE_NODE).
                in(SysDataDict::getDictKey, dictKeys));

        Map<String, List<SysDataDict>> dictKeyMap = sysDataDictList.stream().collect(Collectors.groupingBy(SysDataDict::getDictKey));

        dictKeyMap.forEach((dictKey, dictList) -> {
            if (CollUtil.isNotEmpty(dictList)) {
                List<SysDataDictVO> data = BeanCopierUtils.transformList(dictList, SysDataDictVO.class);
                cache.put(AbCacheRegionConstant.DICT_CACHE_REGION, SysCackeKeyConstant.GET_DICT_NODE_LIST + dictKey, data);
            }
        });

    }
}
