package com.dstz.org.api.model;

/**
 * User Interface
 *
 */
public interface IUser extends java.io.Serializable {

    /**
     * Properties-Mailbox
     */
    String ATTR_EMAIL = "email";

    /**
     * Attribute - Mobile Number
     */
    String ATTR_MOBILE = "mobile";

    /**
     * Expired
     */
    String ATTR_EXPIRE_DATE = "expireDate";

    /**
     * Get User ID
     *
     * @return User ID
     */
    String getUserId();

    /**
     * Get Username
     *
     * @return username
     */
    String getUsername();

    /**
     * Get User Name
     *
     * @return User Name
     */
    String getFullName();

    /**
     * Get attribute value
     *
     * @param attrName Attribute Name
     * @param tClass   Attribute Type
     * @param <T>      T
     * @return Attribute Value
     */
    <T> T getAttrValue(String attrName, Class<T> tClass);
}
