package com.dstz.org.api;

import com.dstz.org.api.model.IUser;

/**
 * OpenId interface for user operations
 */
public interface UserOpenIdApi {

    /**
     * Get the user based on the user openId
     *
     * @param openId User openId
     * @return user
     */
    IUser getByOpenId(String openId);

    /**
     * 设置用户openId
     * @param account
     * @param openId
     */
    void saveOpenIdByAccount(String account,String openId);
}
