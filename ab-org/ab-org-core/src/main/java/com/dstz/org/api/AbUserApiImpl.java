package com.dstz.org.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.org.api.model.IUser;
import com.dstz.org.core.entity.OrgUser;
import com.dstz.org.core.manager.OrgUserManager;
import com.dstz.org.enums.RelationTypeConstant;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User interface adaptation implementation
 *
 */
@Component("userApi")
public class AbUserApiImpl implements UserApi {

    @Autowired
    private OrgUserManager orgUserManager;

    @Override
    public Iterator<? extends IUser> getByUsernames(Collection<String> usernames) {
        if (CollUtil.isEmpty(usernames)) {
            return Collections.emptyListIterator();
        }
        LambdaQueryWrapper<OrgUser> queryWrapper = Wrappers.lambdaQuery(OrgUser.class).in(OrgUser::getAccount, usernames);
        return orgUserManager.selectByWrapper(queryWrapper).stream().map(AbUser::new).iterator();
    }

    @Override
    public Iterator<? extends IUser> getByUserIds(Collection<String> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyListIterator();
        }
        return orgUserManager.selectByIds(userIds).stream().map(AbUser::new).iterator();
    }

    @Override
    public Iterator<? extends IUser> getByGroupTypeAndGroupIds(String groupType, Collection<String> groupIds) {
        RelationTypeConstant relationTypeConstant = RelationTypeConstant.getUserRelationTypeByGroupType(groupType);
        Assert.notNull(relationTypeConstant, "%s GroupType not matched", StrUtil.nullToEmpty(groupType));
        return groupIds.stream()
                .map(o -> orgUserManager.getUserListByRelation(o, relationTypeConstant.getKey()))
                .flatMap(Collection::stream)
                .map(AbUser::new)
                .iterator();
    }

    @Override
    public PageListDTO<? extends IUser> queryFilter(QueryParamDTO queryParamDTO) {
        QueryParamDTO newQueryParamDTO = queryParamDTO;
        if (CharSequenceUtil.isNotEmpty(queryParamDTO.getSortColumn()) || MapUtil.isNotEmpty(queryParamDTO.getQueryParam())) {
            ImmutableMap<String, String> fieldMapping = ImmutableMap.of("username", "account", "fullName", "fullname", "userId", "id");
            newQueryParamDTO = queryParamDTO.clone();

            Map<String, Object> newQueryParam = MapUtil.newHashMap(CollUtil.size(queryParamDTO.getQueryParam()));
            CollUtil.forEach(queryParamDTO.getQueryParam(), (k, v, index) -> {
                int dollarIndex = k.lastIndexOf('$');
                String fieldName = k;
                if(dollarIndex != -1){
                    String mappingName = fieldMapping.get(k.substring(0, dollarIndex));
                    if(mappingName != null){
                        fieldName = mappingName + k.substring(dollarIndex);
                    }
                }
                newQueryParam.put(fieldName, v);
            });

            newQueryParamDTO.setQueryParam(newQueryParam);


            // Replace the sort field
            newQueryParamDTO.setSortColumn(fieldMapping.get(queryParamDTO.getSortColumn()));
        }
        PageListDTO<OrgUser> pageListDTO = orgUserManager.queryUser(newQueryParamDTO);
        List<IUser> userRows = ObjectUtil.defaultIfNull(pageListDTO.getRows(), Collections.<OrgUser>emptyList()).stream().map(AbUser::new).collect(Collectors.toList());
        return new PageListDTO<>(pageListDTO.getPageSize(), pageListDTO.getPage(), pageListDTO.getTotal(), userRows);
    }



}