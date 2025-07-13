package com.dstz.base.common.requestlog;

import java.lang.annotation.*;

/**
 * Log masking fields
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogSensitiveField {

}
