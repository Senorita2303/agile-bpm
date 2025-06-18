package com.dstz.sys.rest.model.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * @Name OnlineDocApiVO
 * @description: Online document call returns results
 */
public class OnlineDocApiVO implements Serializable {
    private String errorMessage;
    private String errorCode;
    private Object result;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "OnlineDocApiVO{" +
                "errorMessage='" + errorMessage + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", result=" + result +
                '}';
    }
}
