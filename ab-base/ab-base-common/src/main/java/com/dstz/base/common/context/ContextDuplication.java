package com.dstz.base.common.context;


/**
 * Context copy, used for data transfer under multi-threading
 *
 */
public interface ContextDuplication {

    /**
     * Make a copy from the Context
     *
     * @return Instances
     */
    Object duplicate();

    /**
     * The copied copy fills the current Context
     *
     * @param duplicate Instances
     */
    void fill(Object duplicate);

}
