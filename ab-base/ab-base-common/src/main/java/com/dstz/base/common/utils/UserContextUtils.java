package com.dstz.base.common.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.context.UserContext;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.org.api.UserApi;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import org.springframework.util.function.SingletonSupplier;

import java.util.Optional;
import java.util.Set;

/**
 * User context tool class
 *
 */
public class UserContextUtils {

    private static final SingletonSupplier<UserContext> USER_CONTEXT_SUPPLER = SingletonSupplier.of(() -> SpringUtil.getBean(UserContext.class));

    private UserContextUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * Get user context
     *
     * @return User context
     */
    public static UserContext getUserContext() {
        return USER_CONTEXT_SUPPLER.obtain();
    }

    /**
     * Get user, please judge as empty
     *
     * @return User
     */
    public static Optional<IUser> getUser() {
        return ObjectUtil.defaultIfNull(getUserContext().getUser(), Optional.empty());
    }
    
    /**
     * Interface scenarios that strongly depend on the current user<br/>
     * If your method may appear in the scenario of non-logged-in users, do not use it
     * @return
     */
    public static IUser getValidUser() {
        return getUserContext().getUser().orElseThrow(() -> new BusinessException(GlobalApiCodes.NO_LOGIN_USER));
    }

    /**
     * Get User ID
     *
     * @return User ID
     */
    public static String getUserId() {
        return getUser().map(IUser::getUserId).orElse(null);
    }

    /**
     * Get user name
     *
     * @return Get user name
     */
    public static String getUserName() {
        return getUser().map(IUser::getFullName).orElse(null);
    }

    /**
     * Get user account
     *
     * @return User account
     */
    public static String getAccount(){
        return getUser().map(IUser::getUsername).orElse(null);
    }

    /**
     * Get organization
     *
     * @return Organization
     */
    public static Optional<IGroup> getGroup() {
        return ObjectUtil.defaultIfNull(getUserContext().getOrg(), Optional.empty());
    }

    /**
     * Get the current organization ID
     *
     * @return Organization ID
     */
    public static String getGroupId() {
        return getUserContext().getOrg().map(IGroup::getGroupId).orElse(null);
    }


    /**
     * Is it a super administrator?
     *
     * @return Is it a super administrator?
     */
    public static boolean isSuperAdmin() {
        return getUserContext().isSuperAdmin();
    }

    /**
     * Clear the current executor.
     * void
     */
    public static void clear() {
         getUserContext().clear();
    }
    
    /**
     * 
     * @param userId
     * @return
     */
	public static Set<String> getFilterUserIds(String userId) {
        return getUserContext().getFilterUserIds(userId);
	}

    /**
     * Set the current user through the user account
     * @param account User account
     */
	public static void setUserByAccount(String account){
	    Assert.notEmpty(account,()->new BusinessMessage(GlobalApiCodes.PARAMETER_INVALID.formatDefaultMessage("User account cannot be empty")));

        final UserApi userApi = SpringUtil.getBean(UserApi.class);
        IUser user = userApi.getByUsername(account);
        Assert.notNull(user,()->new BusinessException(GlobalApiCodes.DATA_NOT_FOUND.formatDefaultMessage(String.format("User account 【%s】",account))));

        UserContext userContext = getUserContext();
        userContext.clear();
        userContext.setUser(user);
    }
}
