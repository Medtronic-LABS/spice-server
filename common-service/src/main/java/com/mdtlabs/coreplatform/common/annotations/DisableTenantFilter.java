package com.mdtlabs.coreplatform.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * DisableTenantFilter annotation is used to disable tenant filter.
 * </p>
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableTenantFilter {
}