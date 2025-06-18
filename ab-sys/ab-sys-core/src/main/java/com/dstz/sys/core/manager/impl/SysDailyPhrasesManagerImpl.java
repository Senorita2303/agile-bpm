package com.dstz.sys.core.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.base.query.ConditionType;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.sys.api.constant.SysApiCodes;
import com.dstz.sys.core.entity.SysDailyPhrases;
import com.dstz.sys.core.manager.SysDailyPhrasesManager;
import com.dstz.sys.core.mapper.SysDailyPhrasesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.dstz.sys.api.constant.SysApiCodes.INIT_DATA_CANT_DELETE;

/**
 * Common user terms, general service implementation class
 *
 */
@Service("sysDailyPhrasesManager")
public class SysDailyPhrasesManagerImpl extends AbBaseManagerImpl<SysDailyPhrases> implements SysDailyPhrasesManager {

    @Autowired
    private SysDailyPhrasesMapper sysDailyPhrasesMapper;

    /**
     * Pagination query
     *
     * @param paramDTO Query conditions
     */
    @Override
    public PageListDTO<SysDailyPhrases> listJson(QueryParamDTO paramDTO) {
        DefaultAbQueryFilter filter = new DefaultAbQueryFilter(paramDTO);
        filter.addParam("userId", UserContextUtils.getUserId());
        return sysDailyPhrasesMapper.listJson(filter);
    }


    /**
     * Query all common phrases of the current user
     */
    @Override
    public List<SysDailyPhrases> enableList() {
        AbQueryFilter filter = new DefaultAbQueryFilter(new QueryParamDTO());
        filter.addParam("userId", UserContextUtils.getUserId());
        filter.addFilter("enable", 1, ConditionType.EQUAL);
        filter.noPage();
        return sysDailyPhrasesMapper.listJson(filter);
    }


    /**
     * Add or modify
     *
     * @param sysDailyPhrases Common phrase objects
     */
    @Override
    public void saveOrUpdate(SysDailyPhrases sysDailyPhrases) {
        validate(sysDailyPhrases);
        if (StrUtil.isEmpty(sysDailyPhrases.getId())) {
            sysDailyPhrases.setUpdateTime(new Date());
        }
        super.createOrUpdate(sysDailyPhrases);
    }

    /**
     * Check for duplicate operations
     *
     * @param sysDailyPhrases: Modified object
     **/
    private void validate(SysDailyPhrases sysDailyPhrases) {
        //别名查重
        if (StrUtil.isEmpty(sysDailyPhrases.getId()) || !StrUtil.equals(getById(sysDailyPhrases.getId()).getLocution(), sysDailyPhrases.getLocution())) {
            if (selectCount(Wrappers.lambdaQuery(SysDailyPhrases.class)
                    .eq(SysDailyPhrases::getLocution, sysDailyPhrases.getLocution())
                    .eq(SysDailyPhrases::getCreateBy, UserContextUtils.getUserId())) > 0) {
                throw new BusinessMessage(SysApiCodes.CODE_DUPLICATE);
            }
        }
    }

    /**
     * Delete common phrases in batches (deletion is prohibited for built-in data)
     **/
    @Override
    public int removeByIds(Collection<? extends Serializable> list) {
        List<SysDailyPhrases> sysDailyPhrases = selectByWrapper(Wrappers.<SysDailyPhrases>lambdaQuery().in(SysDailyPhrases::getId, list));
        for (SysDailyPhrases sysDailyPhrase : sysDailyPhrases) {
            if (sysDailyPhrase.getIsDefault() == 1) {
                throw new BusinessMessage(INIT_DATA_CANT_DELETE.formatDefaultMessage(sysDailyPhrase.getLocution()));
            }
        }
        return super.removeByIds(list);
    }
}
