package com.dstz.base.web.controller.advice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.ApiException;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.script.ScriptLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

/**
 * rest rest advice
 *
 */
@RestControllerAdvice
public class AbRestControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(AbRestControllerAdvice.class);

    /**
     * Parameter validation exception handler
     *
     * @param validationException Check abnormality
     * @return Interface response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> validationExceptionHandler(MethodArgumentNotValidException validationException) {
        ApiResponse<Void> apiResponse = new ApiResponse<Void>().withCode(GlobalApiCodes.PARAMETER_INVALID.getCode());
        FieldError fieldError = validationException.getBindingResult().getFieldError();
        if (Objects.nonNull(fieldError)) {
            apiResponse.withMessage(fieldError.getDefaultMessage());
        }
        return apiResponse;
    }

    /**
     * Internal error handler
     *
     * @param throwable throwable
     * @return Interface response
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> internalErrorHandler(Throwable throwable) {
        Throwable rootCause = ExceptionUtil.getCausedBy(throwable, ApiException.class);
        if(rootCause == null){
            rootCause = throwable;
        }
        // Promptly do not do stack logging
        if (rootCause instanceof BusinessMessage) {
            return ApiResponse.fail(((BusinessMessage) rootCause).getBaseCode().getCode(), rootCause.getMessage());
        }

        logger.error(throwable.getMessage(), throwable);

        String code;
        String message;
        if(rootCause instanceof ApiException){
            ApiException apiException = (ApiException) rootCause;
            code = apiException.getBaseCode().getCode();
            message = apiException.getBaseCode().getMessage();
        }else{
            code = GlobalApiCodes.INTERNAL_ERROR.getCode();
            message ="PROD".equalsIgnoreCase(SpringUtil.getActiveProfile()) ? GlobalApiCodes.INTERNAL_ERROR.getMessage() : rootCause.toString();
        }
        ApiResponse<Void> res = ApiResponse.fail(code, message);
        
        // Script log output
        if(ScriptLog.isOpenScriptLog()) {
        	List<String> msgs = ScriptLog.getMsgsAndClear();
			// Set the return value
			if(CollUtil.isNotEmpty(msgs)) {
				res.setScriptLog(StrUtil.join("\n", msgs));
			}
        }
        
        return res;
    }
}
