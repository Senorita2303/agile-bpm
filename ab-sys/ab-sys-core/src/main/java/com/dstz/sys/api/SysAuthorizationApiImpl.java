package com.dstz.sys.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.sys.api.constant.RightsObjectConstants;
import com.dstz.sys.api.dto.SysAuthorizationDTO;
import com.dstz.sys.core.entity.SysAuthorization;
import com.dstz.sys.core.manager.SysAuthorizationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysAuthorizationApiImpl implements SysAuthorizationApi {

    private final SysAuthorizationManager sysAuthorizationManager;

    public SysAuthorizationApiImpl(SysAuthorizationManager sysAuthorizationManager) {
        this.sysAuthorizationManager = sysAuthorizationManager;
    }

    @Override
    public Set<String> getUserRights(String userId) {
        return sysAuthorizationManager.getUserRights(userId);
    }

    @Override
    public Map<String, Object> getUserRightsSql(RightsObjectConstants rightsObject, String userId, String targetKey) {
        return sysAuthorizationManager.getUserRightsSql(rightsObject, userId, targetKey);
    }

    @Override
    public List<SysAuthorizationDTO> getAuthorizationByRights(RightsObjectConstants rightsObject, String rightsCode) {
        return BeanUtil.copyToList(sysAuthorizationManager.getByTarget(rightsObject, rightsCode),SysAuthorizationDTO.class);
    }

    @Override
    public void deleteAuthorizationByRights(RightsObjectConstants rightsObject, String rightsTarget) {
        sysAuthorizationManager.deleteAuthorizationByRights(rightsObject, rightsTarget);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importAuthorization(List<SysAuthorizationDTO> authorizationDTOS) {
        if (CollUtil.isEmpty(authorizationDTOS)){
            return;
        }
        //Delete first, then insert
        Map<String,List<SysAuthorizationDTO>> authMap = authorizationDTOS.stream().collect(Collectors.groupingBy((tt)-> String.format("%s-%s",tt.getRightsObject(),tt.getRightsTarget())));

        authMap.forEach((key,dtoList)->{
           RightsObjectConstants objectConstant = RightsObjectConstants.getByKey( key.split("-")[0]);
           String targetKey = key.split("-")[1];
           //Delete first
           sysAuthorizationManager.deleteAuthorizationByRights(objectConstant,targetKey);

           List<SysAuthorization> newDataList = new ArrayList<>(dtoList.size());
            dtoList.forEach((dto)->{
                SysAuthorization newData = BeanCopierUtils.transformBean(dto,SysAuthorization.class);
                newData.setRightsCreateTime(new Date());
                newData.setRightsCreateBy(UserContextUtils.getUserId());
                newDataList.add(newData);
            });
            //Batch add
            sysAuthorizationManager.bulkCreate(newDataList);
        });

    }

}
