package com.dstz.sys.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.constats.ThreadMapKeyConstant;
import com.dstz.base.common.utils.BeanConversionUtils;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.ThreadMapUtil;
import com.dstz.base.common.utils.ThreadNowUtil;
import com.dstz.sys.api.constant.SysCackeKeyConstant;
import com.dstz.sys.api.vo.SysDataDictVO;
import com.dstz.sys.core.entity.SysDataDict;
import com.dstz.sys.core.manager.SysDataDictManager;
import com.dstz.sys.core.mapper.SysDataDictMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Name SysDataDictServiceImpl
 */

@Service
public class SysDataDictApiImpl implements SysDataDictApi {

    private final SysDataDictManager sysDataDictManager;
    private final SysDataDictMapper sysDataDictMapper;

    public SysDataDictApiImpl(SysDataDictManager sysDataDictManager, SysDataDictMapper sysDataDictMapper) {
        this.sysDataDictManager = sysDataDictManager;
        this.sysDataDictMapper = sysDataDictMapper;
    }

    @Override
    //@Cacheable(cacheNames = AbCacheRegionConstant.DICT_CACHE_REGION, key = SysCackeKeyConstant.GET_DICT_NODE_LIST_RECEIVE_EL)
    public List<SysDataDictVO> getDictNodeList(String dictKey, Boolean hasRoot) {
        if (dictKey == null) {
            return null;
        }

        List<SysDataDict> dictNodeList = sysDataDictManager.getDictNodeList(dictKey, hasRoot);
        if (CollUtil.isEmpty(dictNodeList)) {
            return null;
        }
        return BeanCopierUtils.transformList(dictNodeList, SysDataDictVO.class);
    }

    @Override
    public void batchSaveOrUpdate(List<SysDataDictVO> voList) {
        if (CollUtil.isEmpty(voList)) {
            return;
        }
        //To ensure that the data is in a tree structure, use BeanConversionUtils.listToTree to convert it
        BeanConversionUtils.listToTree(voList).forEach(e -> {
            handleData(e);
        });

        //Put in cache
        sysDataDictManager.updateCache(voList.stream().map(SysDataDictVO::getDictKey).collect(Collectors.toSet()));

        //Put it in the thread variable
        HashSet<String> dictKeySet = (HashSet<String>) ThreadMapUtil.getOrDefault(ThreadMapKeyConstant.AppInstallInfo.DICT_KEY,new HashSet<>());
        voList.forEach(vo -> {
            dictKeySet.add(String.format("%s.%s", vo.getDictKey(), vo.getCode()));
        });
        ThreadMapUtil.put(ThreadMapKeyConstant.AppInstallInfo.DICT_KEY, dictKeySet);
    }

    @Override
    public List<SysDataDictVO> exportTypeData(String dictKey, Set<String> typeCodes) {
        //Find all dictionary entries under the dictionary
        List<SysDataDictVO> sysDataDictVOList = getDictNodeList(dictKey, Boolean.TRUE);
        if (CollUtil.isEmpty(sysDataDictVOList)) {
            return null;
        }
        List<SysDataDictVO> result = new ArrayList<>();
        //Import the parent level when importing to prevent gaps
        typeCodes.forEach(typeCode -> {
            if (StrUtil.isNotEmpty(typeCode)) {
                SysDataDictVO dictVO = sysDataDictVOList.stream().filter(e -> e.getCode().equals(typeCode)).findFirst().orElse(null);
                if (dictVO != null) {
                    //Deduplication, multiple children have the same parent class
                    if (!(result.stream().map(SysDataDictVO::getId).collect(Collectors.toSet()).contains(dictVO.getId()))) {
                        result.add(dictVO);
                    }
                    getDataDictParent(dictVO, sysDataDictVOList, result);
                }
            }
        });
        return result;
    }

    @Override
    public void deleteByCodes(Collection<? extends Serializable> codes) {
        //The system's built-in dictionary cannot be deleted
        if (CollUtil.isEmpty(codes)){
            return;
        }
        List<SysDataDict> dictList = sysDataDictManager.selectByWrapper(Wrappers.<SysDataDict>lambdaQuery().
                eq(SysDataDict::getIsSystem, NumberPool.BOOLEAN_FALSE).
                in(SysDataDict::getCode, codes));
        if (CollUtil.isNotEmpty(dictList)){
            sysDataDictManager.removeByIds(dictList.stream().map(SysDataDict::getId).collect(Collectors.toSet()));
        }
    }

    private void getDataDictParent(SysDataDictVO sysDataDictVO, List<SysDataDictVO> allDataDict, List<SysDataDictVO> result) {
        SysDataDictVO parent = allDataDict.stream().filter(e -> e.getId().equals(sysDataDictVO.getParentId())).findFirst().orElse(null);
        if (parent != null) {
            result.add(parent);
            getDataDictParent(parent, allDataDict, result);
        }
    }

    private void handleData(SysDataDictVO sysDataDictVO) {
        //Save or update the parent first, get the parent's id and give it to the children's parentId
        String parentId = saveData(sysDataDictVO);

        List<SysDataDictVO> children =  sysDataDictVO.getChildren();
        if (CollUtil.isNotEmpty(children)) {
            for (SysDataDictVO child : children) {
                child.setParentId(parentId);
                handleData(child);
            }
        }
    }


    private String saveData(SysDataDictVO sysDataDictVO) {
        SysDataDict dbDict = getByCode(sysDataDictVO);
        SysDataDict newDict = BeanCopierUtils.transformBean(sysDataDictVO, SysDataDict.class);
        if (dbDict == null) {
            newDict.setId(null);
            sysDataDictMapper.insert(newDict);
        } else {
            newDict.setId(dbDict.getId());
            sysDataDictMapper.updateById(newDict);
        }
        return newDict.getId();
    }


    private SysDataDict getByCode(SysDataDictVO sysDataDictVO) {
        String dictType = sysDataDictVO.getDictType();

        if (SysDataDict.TYPE_DICT.equals(dictType)) {
            List<SysDataDict> existList = sysDataDictManager.selectByWrapper(Wrappers.lambdaQuery(SysDataDict.class).
                    eq(SysDataDict::getDictType, SysDataDict.TYPE_DICT).
                    eq(SysDataDict::getCode, sysDataDictVO.getCode()));
            if (CollUtil.isNotEmpty(existList)) {
                return existList.get(0);
            }
            return null;
        }

        List<SysDataDict> existNodeList = sysDataDictManager.selectByWrapper(Wrappers.lambdaQuery(SysDataDict.class).
                eq(SysDataDict::getDictType, SysDataDict.TYPE_NODE).
                eq(SysDataDict::getDictKey, sysDataDictVO.getDictKey()).
                eq(SysDataDict::getCode, sysDataDictVO.getCode()));
        if (CollUtil.isNotEmpty(existNodeList)) {
            return existNodeList.get(0);
        }
        return null;
    }

}
