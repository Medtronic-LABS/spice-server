package com.mdtlabs.coreplatform.common.aspects;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mdtlabs.coreplatform.common.TestDataProvider;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserTenantsContextHolder;
import com.mdtlabs.coreplatform.common.exception.Validation;

/**
 * <p>
 * TenantPermissionValidationTest class has the test methods for the TenantPermissionValidation class.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class TenantPermissionValidationTest {

    @InjectMocks
    TenantPermissionValidation tenantPermissionValidation;

    @Test
    void validateAspect() {
        //given
        JoinPoint joinPoint = mock(JoinPoint.class);
        String value = "value";

        //when
        TestDataProvider.init();
        TestDataProvider.getStaticMock();

        //then
        tenantPermissionValidation.validateAspect(joinPoint, value);
        TestDataProvider.cleanUp();
    }

    @Test
    void validateAspectException() {
        //given
        JoinPoint joinPoint = mock(JoinPoint.class);
        String value = "value";
        MockedStatic<UserSelectedTenantContextHolder> contextHolder = mockStatic(UserSelectedTenantContextHolder.class);

        //when
        contextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(null);

        //then
        assertThrows(Validation.class, () -> tenantPermissionValidation.validateAspect(joinPoint, value));
        contextHolder.close();
    }

    @Test
    void validateAspectExceptionNull() {
        //given
        JoinPoint joinPoint = mock(JoinPoint.class);
        String value = "value";
        MockedStatic<UserSelectedTenantContextHolder> contextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        MockedStatic<UserTenantsContextHolder> userTenantsContextHolder = mockStatic(UserTenantsContextHolder.class);

        //when
        contextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(1L);
        userTenantsContextHolder.when(UserTenantsContextHolder::get).thenReturn(List.of(2L));

        //then
        assertThrows(Validation.class, () -> tenantPermissionValidation.validateAspect(joinPoint, value));
        contextHolder.close();
        userTenantsContextHolder.close();
    }
}