package com.mdtlabs.coreplatform.common.aspects;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.TestDataProvider;

/**
 * <p>
 * TenantAspectTest class has the test methods for the TenantAspect class.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class TenantAspectTest {

    @InjectMocks
    private TenantAspect tenantAspect;

    @Mock
    private EntityManager entityManager;

    @Test
    void beforeFindOfTenantTableRepositoryTest() {
        //given
        MethodSignature methodSignature = mock(MethodSignature.class);
        JoinPoint joinPoint = mock(JoinPoint.class);
        Session session = mock(Session.class);
        Filter filter = mock(Filter.class);

        //when
        TestDataProvider.init();
        TestDataProvider.getUserDtoMock(Boolean.FALSE);
        TestDataProvider.getAnnotationUtilsMock(methodSignature);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(session.enableFilter(Constants.TENANT_FILTER_NAME)).thenReturn(filter);

        //then
        tenantAspect.beforeFindOfTenantableRepository(joinPoint);
        verify(entityManager, times(2)).unwrap(Session.class);
        TestDataProvider.cleanUp();
    }

    @Test
    void beforeFindOfTenantTableRepository() {
        //given
        MethodSignature methodSignature = mock(MethodSignature.class);
        JoinPoint joinPoint = mock(JoinPoint.class);
        Session session = mock(Session.class);

        //when
        TestDataProvider.init();
        TestDataProvider.getUserDtoMock(Boolean.TRUE);
        TestDataProvider.getAnnotationUtilsMock(methodSignature);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(joinPoint.getSignature()).thenReturn(methodSignature);

        //then
        tenantAspect.beforeFindOfTenantableRepository(joinPoint);
        verify(entityManager, times(1)).unwrap(Session.class);
        TestDataProvider.cleanUp();
    }
}