package com.dstz.org.rest.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.api.vo.PageListVO;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.events.AbUserEvent;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.org.core.constant.OrgStatusCode;
import com.dstz.org.core.entity.OrgUser;
import com.dstz.org.core.manager.OrgUserManager;
import com.dstz.org.dto.*;
import com.dstz.org.vo.OrgUserInfoVO;
import com.dstz.org.vo.OrgUserListJsonVO;
import com.dstz.org.vo.OrgUserVO;
import com.dstz.org.vo.ResourceUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dstz.base.api.vo.ApiResponse.fail;
import static com.dstz.base.api.vo.ApiResponse.success;

/**
 * <p>
 * User Table Front Controller
 * </p>
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.ORG_SERVICE_PREFIX + "/user")
public class OrgUserController extends AbCrudController<OrgUser> {

    /**
     * Allow fullname, account, status as input parameters to the list page
     */
    final Set<String> accessQueryFilters = CollUtil.newHashSet("fullname", "account", "sex", "status", "mobile");
    @Autowired
    OrgUserManager orgUserManager;

    @Override
    public Set<String> getAccessQueryFilters() {
        return accessQueryFilters;
    }

    /**
     * Save user information
     *
     * @param user Save User DTO
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "saveUserDto")
    public ApiResponse<Object> saveUserDto(@Valid @RequestBody SaveOrgUserDTO user) {
        //Save all user information
        orgUserManager.saveUserDto(user);
        // The current user has modified his or her information and needs to update the cached data
        if (StrUtil.equals(UserContextUtils.getUserId(), user.getId())) {
            SpringUtil.publishEvent(new AbUserEvent(CollectionUtil.newArrayList(UserContextUtils.getAccount()), AbUserEvent.EventType.UPDATE_USER));
        }
        return success().withMessage("Save user successfully");
    }

    /**
     * Personal center query personal information interface
     *
     * @return User Information
     */
    @RequestMapping(value = "getUserInfo")
    public ApiResponse<OrgUserInfoVO> getUserInfo() {
        return success(orgUserManager.getUserInfo());
    }

    @RequestMapping(value = "saveUserInfo")
    public ApiResponse<Object> saveUserInfo(@Valid @RequestBody SaveOrgUserInfoDTO user) {
        // Save all user information
        orgUserManager.saveUserInfo(user);
        // The current user has modified his or her information and needs to update the cached data
        SpringUtil.publishEvent(new AbUserEvent(CollectionUtil.newArrayList(UserContextUtils.getAccount()), AbUserEvent.EventType.UPDATE_USER));
        return success().withMessage("Save user successfully");
    }

    /**
     * Get user details
     *
     * @param id User ID
     * @return User Information
     */
    @RequestMapping(value = "getUserVO")
    public ApiResponse<OrgUserVO> getUserVO(@NotBlank(message = "Parameter cannot be empty") @RequestParam(name = "id") String id) {
        return success(orgUserManager.getUserVO(id));
    }

    /**
     * Reset password interface
     *
     * @param id User ID
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "resetUserPassword")
    public ApiResponse<Object> resetUserPassword(@NotBlank(message = "Parameter cannot be empty") @RequestParam(name = "id") String id) {
        if (!UserContextUtils.isSuperAdmin()) {
            return fail(OrgStatusCode.IS_SUPER_ADMIN.getCode(), OrgStatusCode.IS_SUPER_ADMIN.getMessage());
        }
        orgUserManager.resetUserPassword(id);
        // The current user has modified his or her information and needs to update the cached data
        if (StrUtil.equals(UserContextUtils.getUserId(), id)) {
            SpringUtil.publishEvent(new AbUserEvent(CollectionUtil.newArrayList(UserContextUtils.getAccount()), AbUserEvent.EventType.UPDATE_USER));
        }
        return success().withMessage("The password has been reset to 1");
    }

    /**
     * Modify user status
     *
     * @param id User ID
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "updateUserStatus")
    public ApiResponse<String> updateUserStatus(@NotBlank(message = "Parameter cannot be empty") @RequestParam(name = "id") String id) {
        checkIsDemoEnvironment();
    	orgUserManager.updateUserStatus(id);
        // The current user has modified his or her information and needs to update the cached data
        if (StrUtil.equals(UserContextUtils.getUserId(), id)) {
            SpringUtil.publishEvent(new AbUserEvent(CollectionUtil.newArrayList(UserContextUtils.getAccount()), AbUserEvent.EventType.UPDATE_USER));
        }
        return success();
    }

    /**
     * User update password
     *
     * @param updateUserPassWorldDTO Update Password DTO
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "updateUserPassWorld")
    public ApiResponse<String> updateUserPassWorld(@Valid @RequestBody UpdateUserPassWorldDTO updateUserPassWorldDTO) {
    	checkIsDemoEnvironment();
        orgUserManager.updateUserPassWorld(updateUserPassWorldDTO);
        // The current user has modified his or her information and needs to update the cached data
        SpringUtil.publishEvent(new AbUserEvent(CollectionUtil.newArrayList(UserContextUtils.getAccount()), AbUserEvent.EventType.UPDATE_USER));
        return success();
    }

    /**
     * Get password verification regular expression
     *
     * @return Password verification rules
     */
    @RequestMapping(value = "getPwdCheckRule")
    public ApiResponse<Map<String, String>> getPwdCheckRule() {
        return success(Collections.singletonMap(PropertyEnum.PWD_CHECK_RULE_KEY.getPropertyValue(String.class), PropertyEnum.PWD_CHECK_RULE_TXT.getPropertyValue(String.class)));
    }

    /**
     * Change password to log out
     *
     * @return Boolean value
     */
    @RequestMapping(value = "updatePwdIsLogOut")
    public ApiResponse<Boolean> updatePwdIsLogOut() {
        return success(PropertyEnum.CHANGE_PWD_iS_LOG_OUT.getPropertyValue(Boolean.class) || PropertyEnum.CHANGE_PWD_iS_Exit_SYSTEM.getPropertyValue(Boolean.class));
    }


    public void removeCheck(List<String> ids) {
        List<String> accounts = StrUtil.split(PropertyEnum.ADMIN_ACCOUNTS.getPropertyValue(String.class), CharUtil.COMMA);
        for (String userId : ids) {
            OrgUserVO userVO = orgUserManager.getUserVO(userId);
            if (accounts.contains(userVO.getAccount())) {
                throw new BusinessMessage(OrgStatusCode.NOT_ALLOW_DELETE_ADMIN);
            }
        }
    }


    /**
     * User rights query interface
     *
     * @param queryParamDto Query Parameters DTO
     * @return User rights query results
     */
    @RequestMapping(value = "getUserByResource")
    public ApiResponse<PageListVO<ResourceUserVO>> getUserByResource(@Valid @RequestBody QueryParamDTO queryParamDto) {
        AbQueryFilter queryFilter = new DefaultAbQueryFilter(queryParamDto);
        PageListDTO<ResourceUserVO> sysResourceList = orgUserManager.getUserByResource(queryFilter);
        return success(sysResourceList);
    }

    /**
     * How to retrieve password via email: Send verification code
     *
     * @param sendCaptchaEmailDTO Send verification code DTO
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "sendCaptchaEmail")
    public ApiResponse<String> sendCaptchaEmail(@Valid @RequestBody SendCaptchaEmailDTO sendCaptchaEmailDTO) {
        return success(() -> orgUserManager.sendCaptchaEmail(sendCaptchaEmailDTO));
    }

    /**
     * How to retrieve your password via email: Reset password
     *
     * @param setPwdByEmailDTO Setting Password DTO
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "setPwdByEmail")
    public ApiResponse<String> setPwdByEmail(@Valid @RequestBody SetPwdByEmailDTO setPwdByEmailDTO) {
        return success(() -> orgUserManager.setPwdByEmail(setPwdByEmailDTO));
    }

    @Override
    @RequestMapping("listJson")
    public ApiResponse<?> listJson(@Valid @RequestBody QueryParamDTO queryParamDto) {
        PageListDTO<OrgUserListJsonVO> pageList = orgUserManager.queryUserList(new DefaultAbQueryFilter(queryParamDto, getAccessQueryFilters()));
        return ApiResponse.success(pageList);
    }

    @Override
    protected String getEntityDesc() {
        return "user";
    }
}
