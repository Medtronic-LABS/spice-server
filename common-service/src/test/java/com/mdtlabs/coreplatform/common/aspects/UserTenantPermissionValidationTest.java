package com.mdtlabs.coreplatform.common.aspects;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.TestConstants;
import com.mdtlabs.coreplatform.common.TestDataProvider;
import com.mdtlabs.coreplatform.common.exception.Validation;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.repository.OrganizationRepository;

/**
 * <p>
 * UserTenantPermissionValidationTest class has the test methods for the UserTenantPermissionValidation class.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserTenantPermissionValidationTest {

    @InjectMocks
    UserTenantPermissionValidation userTenantPermissionValidation;

    @Mock
    private RedisTemplate<String, List<Organization>> redisTemplate;

    @Mock
    OrganizationRepository organizationRepository;

    @Mock
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @ValueSource(strings = {Constants.METHOD_POST, Constants.METHOD_PUT, Constants.METHOD_DELETE, Constants.METHOD_PATCH})
    void validateAspect(String method) throws NoSuchFieldException, IllegalAccessException {
        //given
        setUp();
        MockedStatic<RequestContextHolder> requestContextHolder = mockStatic(RequestContextHolder.class);
        JoinPoint joinPoint = mock(JoinPoint.class);
        List<Object> body = new ArrayList<>();
        body.add(Constants.ACTIVATE);
        ArrayList<Object> bodyList = new ArrayList<>();
        bodyList.add(Constants.ACTIVATE);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setMethod(method);
        ServletRequestAttributes requestAttributes = mock(ServletRequestAttributes.class);
        ListOperations operations = mock(ListOperations.class);
        List<List<Organization>> organizationListRedis = new ArrayList<>();
        List<Organization> redisList = new ArrayList<>();
        redisList.add(TestDataProvider.getOrganization());
        organizationListRedis.add(redisList);
        List<Organization> organizations = TestDataProvider.getAllOrganization();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(Constants.TENANT_PARAMETER_NAME, TestConstants.ONE);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        requestContextHolder.when(RequestContextHolder::currentRequestAttributes).thenReturn(requestAttributes);
        when(requestAttributes.getRequest()).thenReturn(httpServletRequest);
        when(objectMapper.convertValue(body, ArrayList.class)).thenReturn(bodyList);
        when(objectMapper.convertValue(Constants.ACTIVATE, Map.class)).thenReturn(requestBody);
        when(redisTemplate.opsForList()).thenReturn(operations);
        when(operations.range(Constants.ORGANIZATION_REDIS_KEY, Constants.ZERO, 0)).thenReturn(new ArrayList<>());
        when(organizationRepository.findAll()).thenReturn(organizations);
        when(operations.leftPush(Constants.ORGANIZATION_REDIS_KEY, organizations)).thenReturn(TestConstants.ONE);
        when(operations.range(Constants.ORGANIZATION_REDIS_KEY, Constants.ZERO, Constants.ONE)).thenReturn(organizationListRedis);


        //then
        userTenantPermissionValidation.validateAspect(joinPoint, body);
        verify(objectMapper, atLeastOnce()).convertValue(body, ArrayList.class);
        verify(objectMapper, atLeastOnce()).convertValue(Constants.ACTIVATE, Map.class);
        verify(redisTemplate, atLeastOnce()).opsForList();
        verify(organizationRepository, atLeastOnce()).findAll();
        TestDataProvider.cleanUp();
        requestContextHolder.close();
    }

    @Test
    void validateAspectTest() throws NoSuchFieldException, IllegalAccessException {
        //given
        setUp();
        MockedStatic<RequestContextHolder> requestContextHolder = mockStatic(RequestContextHolder.class);
        JoinPoint joinPoint = mock(JoinPoint.class);
        List<Object> body = new ArrayList<>();
        body.add(Constants.ACTIVATE);
        ArrayList<Object> bodyList = new ArrayList<>();
        bodyList.add(Constants.ACTIVATE);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setMethod(Constants.METHOD_POST);
        ServletRequestAttributes requestAttributes = mock(ServletRequestAttributes.class);
        ListOperations operations = mock(ListOperations.class);
        List<List<Organization>> organizationListRedis = new ArrayList<>();
        List<Organization> redisList = new ArrayList<>();
        redisList.add(TestDataProvider.getOrganization());
        organizationListRedis.add(redisList);
        List<Organization> organizations = TestDataProvider.getAllOrganization();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(Constants.TENANT_PARAMETER_NAME, TestConstants.ONE);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        requestContextHolder.when(RequestContextHolder::currentRequestAttributes).thenReturn(requestAttributes);
        when(requestAttributes.getRequest()).thenReturn(httpServletRequest);
        when(objectMapper.convertValue(body, ArrayList.class)).thenReturn(bodyList);
        when(objectMapper.convertValue(Constants.ACTIVATE, Map.class)).thenReturn(requestBody);
        when(redisTemplate.opsForList()).thenReturn(operations);
        when(operations.range(Constants.ORGANIZATION_REDIS_KEY, Constants.ZERO, 0)).thenReturn(organizationListRedis);
        when(operations.leftPush(Constants.ORGANIZATION_REDIS_KEY, organizations)).thenReturn(TestConstants.ONE);
        when(operations.range(Constants.ORGANIZATION_REDIS_KEY, Constants.ZERO, Constants.ONE)).thenReturn(organizationListRedis);

        //then
        userTenantPermissionValidation.validateAspect(joinPoint, body);
        verify(objectMapper, atLeastOnce()).convertValue(body, ArrayList.class);
        verify(objectMapper, atLeastOnce()).convertValue(Constants.ACTIVATE, Map.class);
        verify(redisTemplate, atLeastOnce()).opsForList();
        TestDataProvider.cleanUp();
        requestContextHolder.close();
    }

    @Test
    void verifyValidateAspect() throws NoSuchFieldException, IllegalAccessException {
        //given
        setUp();
        MockedStatic<RequestContextHolder> requestContextHolder = mockStatic(RequestContextHolder.class);
        JoinPoint joinPoint = mock(JoinPoint.class);
        List<Object> body = new LinkedList<>();
        body.add(Constants.ACTIVATE);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setMethod(Constants.METHOD_POST);
        ServletRequestAttributes requestAttributes = mock(ServletRequestAttributes.class);
        ListOperations operations = mock(ListOperations.class);
        List<List<Organization>> organizationListRedis = new ArrayList<>();
        List<Organization> redisList = new ArrayList<>();
        redisList.add(TestDataProvider.getOrganization());
        organizationListRedis.add(redisList);
        List<Organization> organizations = TestDataProvider.getAllOrganization();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(Constants.TENANT_PARAMETER_NAME, TestConstants.ONE);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        requestContextHolder.when(RequestContextHolder::currentRequestAttributes).thenReturn(requestAttributes);
        when(requestAttributes.getRequest()).thenReturn(httpServletRequest);
        when(objectMapper.convertValue(body, Map.class)).thenReturn(requestBody);
        when(redisTemplate.opsForList()).thenReturn(operations);
        when(operations.range(Constants.ORGANIZATION_REDIS_KEY, Constants.ZERO, 0)).thenReturn(new ArrayList<>());
        when(organizationRepository.findAll()).thenReturn(organizations);
        when(operations.leftPush(Constants.ORGANIZATION_REDIS_KEY, organizations)).thenReturn(TestConstants.ONE);
        when(operations.range(Constants.ORGANIZATION_REDIS_KEY, Constants.ZERO, Constants.ONE)).thenReturn(organizationListRedis);

        //then
        userTenantPermissionValidation.validateAspect(joinPoint, body);
        verify(objectMapper, atLeastOnce()).convertValue(body, Map.class);
        verify(redisTemplate, atLeastOnce()).opsForList();
        verify(organizationRepository, atLeastOnce()).findAll();
        TestDataProvider.cleanUp();
        requestContextHolder.close();
    }

    @Test
    void validateAspectTestException() throws NoSuchFieldException, IllegalAccessException {
        //given
        setUp();
        MockedStatic<RequestContextHolder> requestContextHolder = mockStatic(RequestContextHolder.class);
        JoinPoint joinPoint = mock(JoinPoint.class);
        List<Object> body = new ArrayList<>();
        body.add(Constants.ACTIVATE);
        ArrayList<Object> bodyList = new ArrayList<>();
        bodyList.add(Constants.ACTIVATE);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setMethod(Constants.METHOD_POST);
        ServletRequestAttributes requestAttributes = mock(ServletRequestAttributes.class);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(Constants.ACTIVATE, TestConstants.ONE);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        requestContextHolder.when(RequestContextHolder::currentRequestAttributes).thenReturn(requestAttributes);
        when(requestAttributes.getRequest()).thenReturn(httpServletRequest);
        when(objectMapper.convertValue(body, ArrayList.class)).thenReturn(bodyList);
        when(objectMapper.convertValue(Constants.ACTIVATE, Map.class)).thenReturn(requestBody);

        //then
        assertThrows(Validation.class, () -> userTenantPermissionValidation.validateAspect(joinPoint, body));
        TestDataProvider.cleanUp();
        requestContextHolder.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {Constants.COUNTRY, Constants.OPERATING_UNIT, Constants.ACCOUNT, Constants.SITE})
    void getChildOrganizations(String formName) {
        //given
        List<Organization> organizations = TestDataProvider.getAllOrganization();
        organizations.get(0).setFormName(formName);
        Set<Long> childIDs = new HashSet<>();
        List<List<Organization>> organizationListRedis = List.of(organizations);

        //then
        userTenantPermissionValidation
                .getChildOrganizations(TestConstants.ONE, childIDs, organizationListRedis);
        assertNotNull(formName);
    }

    @ParameterizedTest
    @CsvSource({",1,0", "2,1,0", "1,1,1"})
    void getTenantIds(Long parentOrgID, Long parentId, int expectedSize) {
        //given
        List<Organization> organizations = TestDataProvider.getAllOrganization();
        organizations.get(Constants.ZERO).setParentOrganizationId(parentOrgID);

        //then
        Set<Long> actualIDs = userTenantPermissionValidation.getTenantIds(organizations, parentId);
        assertNotNull(actualIDs);
        assertEquals(expectedSize, actualIDs.size());
    }

    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field mapper = UserTenantPermissionValidation.class.getDeclaredField("mapper");
        mapper.setAccessible(true);
        mapper.set(userTenantPermissionValidation, objectMapper);
    }
}