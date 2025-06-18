package com.dstz.base.api.model;

import java.util.List;

/**
 * <pre>
 * Description: Tree structure object, used to convert list data into tree structure.
 * </pre>
 *
 * @param <C> Declare the type of the child data, so that the implementation class can be directly used as children in the implementation class. Eg: SysDataDict
 */
public interface Tree<C extends Tree<?>> {

    /**
     * Primary Key ID
     *
     * @return
     */
    String getId();

    /**
     * Parent ID
     *
     * @return
     */
    String getParentId();

    /**
     * Child object.
     *
     * @return
     */
    List<C> getChildren();

    /**
     * Set the child object.
     *
     * @param list
     */
    void setChildren(List<C> list);
}