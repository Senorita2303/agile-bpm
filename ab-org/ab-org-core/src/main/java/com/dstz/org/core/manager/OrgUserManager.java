package com.dstz.org.core.manager;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.org.core.entity.OrgUser;
import com.dstz.org.dto.*;
import com.dstz.org.vo.OrgUserInfoVO;
import com.dstz.org.vo.OrgUserListJsonVO;
import com.dstz.org.vo.OrgUserVO;
import com.dstz.org.vo.ResourceUserVO;

import java.util.List;

/**
 * <p>
 * User table General business class
 * </p>
 *
 */
public interface OrgUserManager extends AbBaseManager<OrgUser> {

    /**
     * User permission query
     *
     * @param queryFilter Query Filters
     * @return Paginate user query results
     */
    PageListDTO<ResourceUserVO> getUserByResource(AbQueryFilter queryFilter);

    /**
     * Only for use with the built-in custom dialog box - Query User List
     *
     * @param queryParamDTO Query Condition DTO
     * @return Paginated Role Collection
     */
    PageListDTO<OrgUser> queryUser(QueryParamDTO queryParamDTO);

    /**
     * Get user information
     *
     * @param id User ID
     * @return User Information
     */
    OrgUserVO getUserVO(String id);

    /**
     * Send verification code
     *
     * @param sendCaptchaEmailDTO Send verification code DTO
     */
    void sendCaptchaEmail(SendCaptchaEmailDTO sendCaptchaEmailDTO);

    /**
     * Set password
     *
     * @param setPwdByEmailDTO Set up face DTO
     */
    void setPwdByEmail(SetPwdByEmailDTO setPwdByEmailDTO);

    /**
     * Get a list of users by role ID
     *
     * @param relId Position ID
     * @param type  type
     * @return User List
     */
    List<OrgUser> getUserListByRelation(String relId, String type);

    /**
     * Reset Password
     *
     * @param id User ID
     */
    void resetUserPassword(String id);

    /**
     * Update Password
     *
     * @param updateUserPassWorldDTO Update Password DTO
     */
    void updateUserPassWorld(UpdateUserPassWorldDTO updateUserPassWorldDTO);

    /**
     * Modify user status
     *
     * @param id User ID
     */
    void updateUserStatus(String id);

    /**
     * Save User
     *
     * @param saveOrgUserDTO Save User DTO
     */
    void saveUserDto(SaveOrgUserDTO saveOrgUserDTO);

    /**
     * Storage of personal information
     * @param saveOrgUserDTO-Save personal information DTO
     */
    void saveUserInfo(SaveOrgUserInfoDTO saveOrgUserDTO);

    /**
     * Selectively update records based on primary key
     *
     * @param record Update Record
     * @return Number of rows affected
     */
    int updateByPrimaryKeySelective(OrgUser record);

    /**
     * Personal center information
     *
     * @return Personal information VO
     */
    OrgUserInfoVO getUserInfo();


    /**
     * Determine whether the user exists in the system.
     */
    boolean isUserExist(OrgUser user);

    /**
     * Paginated list query
     *
     * @param abQueryFilter  Query Filters
     * @return  OrgUserListJsonVO Set
     */
    PageListDTO<OrgUserListJsonVO> queryUserList(AbQueryFilter abQueryFilter);

}
