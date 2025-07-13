package com.dstz.auth.rest.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.auth.authentication.api.constant.AuthStatusCode;
import com.dstz.auth.authentication.api.constant.ResouceTypeConstant;
import com.dstz.auth.authentication.api.model.ISysApplication;
import com.dstz.auth.core.entity.SysApplication;
import com.dstz.auth.core.entity.SysResource;
import com.dstz.auth.core.manager.SysApplicationManager;
import com.dstz.auth.core.manager.SysResourceManager;
import com.dstz.auth.rest.model.vo.SysResourceTreeVO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanConversionUtils;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.org.api.GroupApi;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * User Resources
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/userResource")
public class UserResourceController {

    @Autowired
    private SysResourceManager sysResourceManager;

    @Autowired
    private GroupApi groupApi;

    @Autowired
    private SysApplicationManager sysApplicationManager;

    @RequestMapping(value = "/userMsg", method = {RequestMethod.POST, RequestMethod.GET})
    public ApiResponse<?> userMsg() {
        return userMsg(false);
    }

    @RequestMapping(value = "/appUserMsg", method = {RequestMethod.POST, RequestMethod.GET})
    public ApiResponse<?> appUserMsg() {
        return userMsg(true);
    }

    private ApiResponse<?> userMsg(boolean isMobile) {
        final IUser currentUser = UserContextUtils.getUser().orElseThrow(() -> new BusinessException(GlobalApiCodes.LOGIN_INVALID));

        // Get the list of applications available to the current user
        List<SysApplication> sysApplications;

        // Filter out the application list of different terminals
        if (isMobile) {
            sysApplications = CollUtil.filter(sysApplicationManager.getCurrentUserSystem(), item -> NumberPool.INTEGER_ONE.equals(item.getAppType()));
        } else {
            sysApplications = CollUtil.filter(sysApplicationManager.getCurrentUserSystem(), item -> !NumberPool.INTEGER_ONE.equals(item.getAppType()));
        }

        if (CollectionUtil.isEmpty(sysApplications)) {
            throw new BusinessMessage(AuthStatusCode.USER_HAS_NOT_ASSIGNED_ANY_RESOURCES);
        }

        ISysApplication currentApplication = getCurrentUserApp(isMobile, currentUser , sysApplications);


        Map<String, Object> map = MapUtil.newHashMap();
        map.put("currentEnviroment", SpringUtil.getActiveProfile());
        map.put("subsystemList", sysApplications);
        map.put("currentSystem", currentApplication.getCode());
        map.put("username", currentUser.getFullName());
        map.put("currentOrg", UserContextUtils.getGroup().orElse(null));
        map.put("orgList", groupApi.getByGroupTypeAndUserId(GroupType.ORG.getType(), currentUser.getUserId()));
        map.put("user", userModel(currentUser));
        map.put("pwdIsexpire", getUserPwdIsExpired());

        getSysResource(map, currentApplication.getId(), currentUser.getUserId());

        return ApiResponse.success(map);
    }


    private ISysApplication getCurrentUserApp(boolean isMobile, IUser currentUser, List<SysApplication> sysApplications) {
        // TODO Get the name of the application after switching
        return sysApplications.stream()
                .filter(sysApplication -> NumberPool.BOOLEAN_TRUE.equals(sysApplication.getIsDefault()))
                .findFirst()
                .orElseGet(() -> CollUtil.getFirst(sysApplications));
    }

    private Map<String, Object> userModel(IUser user) {
        Map<String, Object> userModel = BeanCopierUtils.transformMap(user, IUser.class);
        // Signature
        userModel.put("signature", user.getAttrValue("signature", String.class));
        // Photo
        userModel.put("photo", user.getAttrValue("photo", String.class));
        return userModel;
    }


    /**
     * <12 is just 12 milliseconds. If the user expiration time is less than the current time, it is considered that the password needs to be reset.
     *
     * @return Boolean
     */
    private Boolean getUserPwdIsExpired() {
        Optional<IUser> user = UserContextUtils.getUserContext().getUser();
        Date expireDate = user.map(o -> o.getAttrValue(IUser.ATTR_EXPIRE_DATE, Date.class)).orElse(null);
        return expireDate != null && expireDate.getTime() - System.currentTimeMillis() > 0;
    }

    private void getSysResource(Map<String, Object> map, String appId, String userId) {
        List<SysResource> sysResourceList;
        if (UserContextUtils.isSuperAdmin()) {
            sysResourceList = sysResourceManager.getBySystemId(appId);
        } else {
            sysResourceList = sysResourceManager.getByAppIdAndUserId(appId, userId);
        }
        // Separation of menu and button
        List<SysResourceTreeVO> menuList = new ArrayList<>();
        Map<String, Boolean> buttonPermission = MapUtil.newHashMap();
        List<SysResourceTreeVO> sysResources = BeanCopierUtils.transformList(sysResourceList, SysResourceTreeVO.class);
        for (SysResourceTreeVO sysResource : sysResources) {
            if (ResouceTypeConstant.MENU.getKey().equals(sysResource.getType())) {
                menuList.add(sysResource);
            } else {
                buttonPermission.put(sysResource.getCode(), NumberPool.INTEGER_ONE.equals(sysResource.getEnable()));
            }
        }
        map.put("userMenuList", BeanConversionUtils.listToTree(menuList));
        map.put("buttonPermission", buttonPermission);
    }

    /**
     * Switch apps
     *
     * @param appCode Application Code
     * @return Interface response
     */
    @RequestMapping(value = "/switchApp/{appCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> switchApp(@PathVariable("appCode") String appCode, @RequestParam(value = "isMobile", defaultValue = "false", required = false) boolean isMobile) {
        SysApplication sysApplication = CollUtil.findOne(sysApplicationManager.getCurrentUserSystem(), item -> StrUtil.equalsIgnoreCase(appCode, item.getCode()));
        Assert.notNull(sysApplication, () -> new BusinessMessage(AuthStatusCode.APPLICATION_NO_PERMISSIONS.formatDefaultMessage(appCode)));
        List<SysResource> sysResourceList;
        if (UserContextUtils.isSuperAdmin()) {
            sysResourceList = sysResourceManager.getBySystemId(sysApplication.getId());
        } else {
            sysResourceList = sysResourceManager.getByAppIdAndUserId(sysApplication.getId(), UserContextUtils.getUserId());
        }
        List<SysResourceTreeVO> menuList = new ArrayList<>();
        List<SysResourceTreeVO> sysResources = BeanCopierUtils.transformList(sysResourceList, SysResourceTreeVO.class);
        for (SysResourceTreeVO sysResource : sysResources) {
            if (ResouceTypeConstant.MENU.getKey().equals(sysResource.getType()) && sysResource.getEnable().equals(NumberPool.INTEGER_ONE)) {
                menuList.add(sysResource);
            }
        }
        if (menuList.size() == NumberPool.INTEGER_ZERO) {
            return AuthStatusCode.APP_NO_RESOURCE.formatDefaultMessage(sysApplication.getName()).buildApiResponse();
        }
        // The opening method is 0 (jump), and it is necessary to check whether there is a menu resource.
        if (StrPool.NUMBER_ZERO.equals(sysApplication.getOpenType())) {
            if (sysResourceManager.selectCount(Wrappers.lambdaQuery(SysResource.class).eq(SysResource::getAppId, sysApplication.getId())) <= NumberPool.INTEGER_ZERO) {
                return AuthStatusCode.APP_NO_RESOURCE.formatDefaultMessage(sysApplication.getName()).buildApiResponse();
            }
        }
        // TODO Switch application implementation
        return ApiResponse.success();
    }

    /**
     * Switch Organization
     *
     * @param orgCode Organization Code
     * @return Interface response
     */
    @RequestMapping(value = "/switchOrg/{orgCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Void> switchOrg(@PathVariable("orgCode") String orgCode) {
        final IUser currentUser = UserContextUtils.getValidUser();
        List<? extends IGroup> orgList = groupApi.getByGroupTypeAndUserId(GroupType.ORG.getType(), currentUser.getUserId());
        if (CollUtil.isEmpty(orgList)) {
            return AuthStatusCode.USER_UNABSORBED_ORG.buildApiResponse();
        }
        IGroup org = CollUtil.findOne(orgList, filterItem -> StrUtil.equals(filterItem.getGroupCode(), orgCode));
        if (org == null) {
            return AuthStatusCode.ILLEGAL_CURRENT_ORG.formatDefaultMessage(orgCode).buildApiResponse();
        }
        // TODO switch organization implementation
        return ApiResponse.success();
    }
}
