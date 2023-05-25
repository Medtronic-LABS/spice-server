package com.mdtlabs.coreplatform.common.aspects;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.DisableTenantFilter;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Session;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <p>
 * This class uses Spring AOP and an EntityManager to enable and disable a filter for a specific
 * tenant in a multi-tenant application.
 * </p>
 *
 * @author Vigneshkumar Created on 30 Jun 2022
 */

/**
 *
 */
@Aspect
@Component
public class TenantAspect {

    /**
     * <p>
     * EntityManager method is used to enable and disable a filter for a specific tenant in a multi-tenant
     * application.
     * </p>
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * <p>
     * This method is used to disable a filter and enable it with a parameter based on user context
     * and method annotations for a repository find operation.
     * </p>
     *
     * @param joinPoint {@link JoinPoint}  object  represents the execution of a method in Spring AOP is given.
     */
    @Before("execution(* com.mdtlabs.coreplatform.common.repository.TenantableRepository+.find*(..))")
    public void beforeFindOfTenantableRepository(JoinPoint joinPoint) {
        entityManager.unwrap(Session.class).disableFilter(Constants.TENANT_FILTER_NAME);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        UserDTO userDto = UserContextHolder.getUserDto();
        if (Boolean.FALSE.equals(userDto.getIsSuperUser()) &&
                (AnnotationUtils.getAnnotation(methodSignature.getMethod(), DisableTenantFilter.class) == null)) {
            entityManager
                    .unwrap(Session.class)
                    .enableFilter(Constants.TENANT_FILTER_NAME)
                    .setParameter(Constants.TENANT_PARAMETER_NAME, UserSelectedTenantContextHolder.get());
        }
    }
}