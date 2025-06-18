package com.dstz.org.core.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.auth.utils.VerifyCodeUtil;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.encrypt.EncryptUtil;
import com.dstz.base.common.enums.EnvironmentConstants;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.enums.IdentityType;
import com.dstz.base.common.events.AbUserEvent;
import com.dstz.base.common.exceptions.ApiException;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.identityconvert.SysIdentity;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.component.msg.api.MsgApi;
import com.dstz.component.msg.api.dto.MsgDTO;
import com.dstz.org.core.constant.OrgStatusCode;
import com.dstz.org.core.entity.OrgRelation;
import com.dstz.org.core.entity.OrgUser;
import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.core.manager.OrgUserManager;
import com.dstz.org.core.mapper.OrgUserMapper;
import com.dstz.org.dto.*;
import com.dstz.org.enums.RelationTypeConstant;
import com.dstz.org.vo.OrgUserInfoVO;
import com.dstz.org.vo.OrgUserListJsonVO;
import com.dstz.org.vo.OrgUserVO;
import com.dstz.org.vo.ResourceUserVO;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * User table General service implementation class
 *
 */
@Service("userManager")
public class OrgUserManagerImpl extends AbBaseManagerImpl<OrgUser> implements OrgUserManager {

    @Autowired
    OrgUserMapper orgUserMapper;
    @Autowired
    OrgRelationManager orgRelationManager;
    @Autowired
    ICache cache;
    @Autowired
    private MsgApi msgApi;


    /**
     * Determine whether the user exists in the system
     * @param user  user
     * @return  Boolean
     */
    @Override
    public boolean isUserExist(OrgUser user) {
        return orgUserMapper.isUserExist(user) > NumberPool.INTEGER_ZERO;
    }

    /**
     * Save User Method
     *
     * @param saveOrgUserDTO Save User DTO
     */
    @Override
    public void saveUserDto(SaveOrgUserDTO saveOrgUserDTO) {
        OrgUser orgUser = BeanCopierUtils.transformBean(saveOrgUserDTO, OrgUser.class);
        Assert.notNull(orgUser, () -> new BusinessMessage(OrgStatusCode.INPUT_INFORMATION_IS_EMPTY));
        // Check whether the account already exists
        Assert.isFalse(
                isUserExist(orgUser),
                () -> new BusinessMessage(OrgStatusCode.ACCOUNT_IS_EXIST));

        // Add a new user
        if (StrUtil.isEmpty(orgUser.getId())) {
            if (StrUtil.isEmpty(orgUser.getPassword())){
                orgUser.setPassword(StrPool.STRING_PWD_ONZ);
            }
            // Password encryption
            orgUser.setPassword(EncryptUtil.encryptSha256(orgUser.getPassword()));
            orgUserMapper.insert(orgUser);
        }else {
            orgUserMapper.updateById(orgUser);
            // Delete the old relationship
            orgRelationManager.removeByUserId(orgUser.getId());
        }

        // Get new relationships
        List<OrgRelationDTO> orgRelationList = saveOrgUserDTO.getOrgRelationList();
        if (CollectionUtil.isEmpty(orgRelationList)) return;

        // Set the bound user id
        orgRelationList.forEach(rel -> rel.setUserId(orgUser.getId()));
        // Add new associations in batches
        orgRelationManager.bulkCreate(BeanCopierUtils.transformList(orgRelationList, OrgRelation.class));
    }

    /**
     * Get personal center information
     *
     * @return User information VO
     */
    @Override
    public OrgUserInfoVO getUserInfo() {
        OrgUserInfoVO orgUserInfoVO = null;
        OrgUser user=getById(UserContextUtils.getUserId());
        if(user!= null) {
            orgUserInfoVO=BeanCopierUtils.transformBean(getById(UserContextUtils.getUserId()), OrgUserInfoVO.class);
            orgUserInfoVO.setOrgRelationList(orgRelationManager.getUserRelation(user.getId(), null));
        }
        return orgUserInfoVO;
    }

    /**
     * Save personal information
     * @param saveOrgUserInfoDTO-Save personal information DTO
     */
    @Override
    public void saveUserInfo(SaveOrgUserInfoDTO saveOrgUserInfoDTO) {
        OrgUser orgUser = BeanCopierUtils.transformBean(saveOrgUserInfoDTO, OrgUser.class);
        Assert.notNull(orgUser, () -> new BusinessMessage(OrgStatusCode.INPUT_INFORMATION_IS_EMPTY));
        orgUserMapper.updateById(orgUser);
    }

    /**
     * Get user information
     *
     * @param id User id
     * @return User information VO
     */
    public OrgUserVO getUserVO(String id) {
        OrgUserVO orgUserVO = BeanCopierUtils.transformBean(getById(id), OrgUserVO.class);
        Assert.notNull(orgUserVO, () -> new BusinessMessage(OrgStatusCode.OPERATION_FAILURE));

        // Set user relationship information
        orgUserVO.setOrgRelationList(orgRelationManager.getUserRelation(id, null));
        return orgUserVO;
    }

    /**
     * Reset password to 1
     *
     * @param id User id
     */
    @Override
    public void resetUserPassword(String id) {
        OrgUser user = getById(id);
        // Set an encrypted password
        user.setPassword("a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=");
        // Clear account expiration time
        user.setExpireDate(null);

        orgUserMapper.updateById(user);
    }

    /**
     * Modify user status
     *
     * @param id User id
     */
    @Override
    public void updateUserStatus(String id) {
        OrgUser user = getById(id);
        // Set status
        user.setStatus(NumberPool.INTEGER_ONE.equals(user.getStatus()) ? NumberPool.INTEGER_ZERO : NumberPool.INTEGER_ONE);
        orgUserMapper.updateById(user);
    }

    /**
     * * Deleting a user requires deleting the associated relationship
     *
     * @param ids Entity ID Set
     * @return Number of deleted users
     */
    @Override
    public int removeByIds(Collection<? extends Serializable> ids) {
        orgRelationManager.removeByIds(ids);
        return super.removeByIds(ids);
    }

    /**
     * Change personal password
     *
     * @param updateUserPassWorldDTO Update Password DTO
     */
    @Override
    public void updateUserPassWorld(UpdateUserPassWorldDTO updateUserPassWorldDTO) {
        // Get User
        OrgUser user;
        String currentUserId = UserContextUtils.getUserId();
        if (StrUtil.isEmpty(currentUserId)) {
            user = selectOne(Wrappers.lambdaQuery(OrgUser.class).eq(OrgUser::getAccount, updateUserPassWorldDTO.getAccount()));
        } else {
            user = getById(currentUserId);
        }

        // Regularity test
        checkPassWorld(updateUserPassWorldDTO.getNewPassword());
        // Check original password
        Assert.isTrue(
                EncryptUtil.encryptSha256(updateUserPassWorldDTO.getOldPassword()).equals(user.getPassword()),
                () -> new BusinessMessage(OrgStatusCode.OLD_PWD_INPUT_ERROR));

        user.setPassword(EncryptUtil.encryptSha256(updateUserPassWorldDTO.getNewPassword()));

        // Set expiration time
        user.setExpireDate(DateUtils.addDays(new Date(), PropertyEnum.PWD_LOSE_COUNT.getPropertyValue(int.class)));
        orgUserMapper.updateById(user);
    }

    /**
     * Update password verification
     *
     * @param passWord Password
     */
    private void checkPassWorld(String passWord) {
        // The demo environment is not allowed to be modified
        Assert.isFalse(
                SpringUtil.getActiveProfile().equalsIgnoreCase(EnvironmentConstants.DEMO.getKey()),
                () -> new ApiException(OrgStatusCode.FROM_MODIFICATION_DEMO_PWD));

        // Regular verification password
        Assert.isTrue(
                Pattern.matches(PropertyEnum.PWD_CHECK_RULE_KEY.getPropertyValue(String.class), passWord),
                () -> new BusinessMessage(
                        GlobalApiCodes.PARAMETER_INVALID.formatDefaultMessage(
                                PropertyEnum.PWD_CHECK_RULE_TXT.getPropertyValue(String.class))));
    }

    /**
     * Send verification code for password retrieval
     *
     * @param sendCaptchaEmailDTO Send verification code DTO
     */
    @Override
    public void sendCaptchaEmail(SendCaptchaEmailDTO sendCaptchaEmailDTO) {
        // Query users by account
        OrgUser user = selectOne(Wrappers.lambdaQuery(OrgUser.class).eq(OrgUser::getAccount, sendCaptchaEmailDTO.getAccount()));
        // Verify user and mailbox
        Assert.notNull(user, () -> new BusinessMessage(OrgStatusCode.USER_DOES_NOT_EXIST));
        Assert.notNull(user.getEmail(), () -> new BusinessMessage(OrgStatusCode.EMAIL_DOES_NOT_EXIST));
        Assert.isTrue(sendCaptchaEmailDTO.getEmail().equals(user.getEmail()), () -> new BusinessMessage(OrgStatusCode.EMAIL_INPUT_ERROR));

        // Generate verification code
        final String verifyCode = VerifyCodeUtil.generateVerifyCode();
        List<SysIdentity> receivers = Collections.singletonList(new SysIdentity(user.getId(), user.getAccount(), IdentityType.USER.getKey()));
        List<String> msgTypes = Collections.singletonList("email");
        // Set the validity period of the verification code in the cache
        cache.put(AbCacheRegionConstant.LOGIN_PWD_REGION, user.getAccount(), verifyCode);
        MsgDTO msgDTO = new MsgDTO("AgileBPM 找回密码验证码", "forgotPasswordValidation", receivers, msgTypes, ImmutableMap.of("verifyCode", verifyCode));
        msgApi.sendMsg(msgDTO);
    }

    /**
     * Set password via verification code
     *
     * @param setPwdByEmailDTO Set password DTO
     */
    @Override
    public void setPwdByEmail(SetPwdByEmailDTO setPwdByEmailDTO) {
        OrgUser user = selectOne(Wrappers.lambdaQuery(OrgUser.class).eq(OrgUser::getAccount, setPwdByEmailDTO.getAccount()));
        Assert.notNull(user, () -> new BusinessMessage(OrgStatusCode.USER_DOES_NOT_EXIST));
        // Verify whether the email password retrieval function is enabled
        Assert.isFalse(
                PropertyEnum.IS_OPEN_RESET_PWD_BY_EMAIL.getPropertyValue(boolean.class),
                () -> new ApiException(OrgStatusCode.PWD_RESET_FUNCTION_IS_NOT_ENABLED));

        Assert.isTrue(
                setPwdByEmailDTO.getNewPassword().equals(setPwdByEmailDTO.getConfirmPassword()),
                () -> new BusinessMessage(OrgStatusCode.NEW_PWD_IS_DIFFERENT_CONFIRM_PWD));
        // Get verification code from cache 
        Object captcha = cache.getIfPresent(AbCacheRegionConstant.LOGIN_PWD_REGION, setPwdByEmailDTO.getAccount());
        // Verification code verification
        Assert.isFalse(
                StrUtil.isEmptyIfStr(captcha) || setPwdByEmailDTO.getCaptcha().equals(captcha),
                () -> new BusinessMessage(OrgStatusCode.VERIFICATION_CODE_IS_EXPIRED));

        // Expiration time
        user.setExpireDate(DateUtils.addDays(new Date(), PropertyEnum.PWD_LOSE_COUNT.getPropertyValue(int.class)));
        // Password encryption
        user.setPassword(EncryptUtil.encryptSha256(setPwdByEmailDTO.getNewPassword()));
        orgUserMapper.updateById(user);
    }

    /**
     * Find users by relId and type
     *
     * @param relId Position id
     * @param type  Type
     * @return OrgUser collection
     */
    @Override
    public List<OrgUser> getUserListByRelation(String relId, String type) {
        if (type.equals(RelationTypeConstant.POST_USER.getKey())) {
            String[] postId = relId.split(StrPool.UNDERLINE);
            if (postId.length != 2) {
                return Collections.emptyList();
            }
            return orgUserMapper.getUserListByPost(postId[1], postId[0]);
        }
        return orgUserMapper.getUserListByRelation(relId, type);
    }

    /**
     * Authorized user query
     *
     * @param queryFilter Parameter filtering
     */
    @Override
    public PageListDTO<ResourceUserVO> getUserByResource(AbQueryFilter queryFilter) {
        return orgUserMapper.getUserByResource(queryFilter);
    }

    /**
     * Only for use with the built-in custom dialog box - query user list
     *
     * @param paramDTO Request parameters
     * @return PageListDTO data object
     */
    @Override
    public PageListDTO<OrgUser> queryUser(QueryParamDTO paramDTO) {
        return orgUserMapper.queryUser(new DefaultAbQueryFilter(paramDTO));
    }

    @Override
    public int updateByPrimaryKeySelective(OrgUser record) {
        return orgUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public PageListDTO<OrgUserListJsonVO> queryUserList(AbQueryFilter abQueryFilter) {
        return orgUserMapper.queryUserList(abQueryFilter);
    }
}
