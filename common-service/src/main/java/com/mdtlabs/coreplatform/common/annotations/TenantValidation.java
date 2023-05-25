package com.mdtlabs.coreplatform.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation class. It validate the api request.
 * </p>
 *
 * @author VigneshKumar created on Feb 10, 2023
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TenantValidation {

}