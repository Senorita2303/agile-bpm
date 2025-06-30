package com.dstz.base.common.context;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;

/**
 * <pre>
 * User context, which obtains the user, the user's organization, and permission information from the current thread
 * </pre>
 */
public interface UserContext extends ContextDuplication {

    /**
     * Get User
     *
     * @return user
     */
    Optional<IUser> getUser();

    /**
     * Set current user
     *
     * @param user user
     * @return user
     */
    void setUser(IUser user);

    /**
     * Get the current organization
     *
     * @return Current Organization
     */
    Optional<IGroup> getOrg();

    /**
     * Set Current Organization
     *
     * @param org organize
     * @return Current Organization
     */
    void setOrg(IGroup org);

    /**
     * Get permissions
     *
     * @return Permissions
     */
    Collection<String> getAuthorities();

    /**
     * Set permissions
     *
     * @param authorities Permissions
     */
    void setAuthorities(Collection<String> authorities);

    /**
     * Is it a super administrator?
     *
     * @return Is it a super administrator?
     */
    boolean isSuperAdmin();

    /**
     * Setting up a super administrator
     *
     * @param superAdmin Super Administrator
     */
    void setSuperAdmin(boolean superAdmin);
    
    /**
     * Cleaning
     */
    void clear();

    /**
     * Filter users Work handover related
     * @param userId
     * @return
     */
    Set<String> getFilterUserIds(String userId);
}
