package com.dstz.base.common.exceptions;

import com.dstz.base.common.codes.IBaseCode;

/**
 * Business messages are fed back to the requesting end and are not recorded by the backend.
 *
 */
public class BusinessMessage extends ApiException {

    private static final long serialVersionUID = -3468412951817107639L;

    public BusinessMessage(IBaseCode baseCode) {
        super(baseCode, null, false, false);
    }
}
