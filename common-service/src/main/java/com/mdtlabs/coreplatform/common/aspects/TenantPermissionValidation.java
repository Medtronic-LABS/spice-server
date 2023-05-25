package com.mdtlabs.coreplatform.common.aspects;

import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserTenantsContextHolder;
import com.mdtlabs.coreplatform.common.exception.Validation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * This class is the business logic implementation for validation annotation
 * used under @TenantValidation annotation.
 * </p>
 *
 * @author VigneshKumar created on Feb 09, 2023
 */
@Aspect
@Component
public class TenantPermissionValidation {

    /**
     * <p>
     * This is a Java aspect that validates the selected tenant ID and throws a validation exception if
     * it is null or not contained in the list of user tenants.
     * </p>
     *
     * @param joinPoint {@link JoinPoint} object represents the execution of a method in Spring AOP is given.
     * @param body      The `body` represents the request body of a REST API call is given
     */
    @Before("@annotation(com.mdtlabs.coreplatform.common.annotations.TenantValidation)  && args(.., @RequestBody body)")
    public void validateAspect(JoinPoint joinPoint, final Object body) {
        Long selectedTenantId = UserSelectedTenantContextHolder.get();
        if (Objects.isNull(selectedTenantId)) {
            throw new Validation(20006);
        }
        List<Long> tenantList = UserTenantsContextHolder.get();
        if (!tenantList.contains(selectedTenantId)) {
            throw new Validation(20005);
        }
    }
}