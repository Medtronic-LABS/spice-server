package com.mdtlabs.coreplatform.common;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mockStatic;

import org.aspectj.lang.reflect.MethodSignature;
import org.mockito.MockedStatic;
import org.modelmapper.ModelMapper;
import org.springframework.core.annotation.AnnotationUtils;

import com.mdtlabs.coreplatform.common.annotations.DisableTenantFilter;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserTenantsContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.entity.ApiRolePermission;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.model.entity.UserToken;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.util.CommonUtil;

/**
 * <p>
 * The TestDataProvider class provides methods for initializing and mocking various objects and entities used in unit
 * tests.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
public class TestDataProvider {

    public static ModelMapper modelMapper = new ModelMapper();
    private static MockedStatic<CommonUtil> commonUtil;
    private static MockedStatic<UserSelectedTenantContextHolder> userSelectedTenantContextHolder;
    private static MockedStatic<UserTenantsContextHolder> userTenantsContextHolder;
    private static MockedStatic<UserContextHolder> userContextHolder;
    private static MockedStatic<MessageFormat> messageFormat;
    private static MockedStatic<AnnotationUtils> annotationUtils;

    public static void init() {
        commonUtil = mockStatic(CommonUtil.class);
        userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        userTenantsContextHolder = mockStatic(UserTenantsContextHolder.class);
        userContextHolder = mockStatic(UserContextHolder.class);
        messageFormat = mockStatic(MessageFormat.class);
        annotationUtils = mockStatic(AnnotationUtils.class);
    }

    public static void getStaticMock() {
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.setId(TestConstants.ONE);
        userDTO.setIsSuperUser(Boolean.FALSE);
        userDTO.setTenantId(TestConstants.ONE);
        commonUtil.when(CommonUtil::getAuthToken).thenReturn("BearerTest");
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(1L);
        userTenantsContextHolder.when(UserTenantsContextHolder::get).thenReturn(List.of(1L));
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
    }

    public static void getUserDtoMock(boolean isSuperUser) {
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.setId(TestConstants.ONE);
        userDTO.setIsSuperUser(isSuperUser);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
    }

    public static void getMessageValidatorMock() {
        messageFormat.when(() -> MessageFormat.format("Invalid token.", TestConstants.ARGUMENT, TestConstants.MESSAGE)).thenReturn("message");
        messageFormat.when(() -> MessageFormat.format("Invalid token.", TestConstants.ARGUMENT)).thenReturn("message");
    }

    public static void getAnnotationUtilsMock(MethodSignature methodSignature) {
        annotationUtils.when(() -> AnnotationUtils.getAnnotation(methodSignature.getMethod(), DisableTenantFilter.class)).thenReturn(null);
    }

    public static void cleanUp() {
        commonUtil.close();
        userSelectedTenantContextHolder.close();
        userTenantsContextHolder.close();
        userContextHolder.close();
        messageFormat.close();
        annotationUtils.close();
    }

    public static Collection<Object> setUp(Class<?> injectClass, String field, Object mockedObject,
                                           Collection<Object> collection) throws NoSuchFieldException, IllegalAccessException {
        Field mapper = injectClass.getDeclaredField(field);
        mapper.setAccessible(true);
        mapper.set(mockedObject, collection);
        return collection;
    }

    public static BaseEntity getBaseEntity() {
        BaseEntity baseEntity = new BaseEntity();
        return baseEntity;
    }

    public static TenantBaseEntity getTenantBaseEntity() {
        TenantBaseEntity tenantBaseEntity = new TenantBaseEntity();
        return tenantBaseEntity;
    }

    public static UserToken getUserToken() {
        UserToken usertoken = new UserToken();
        usertoken.setAuthToken(Constants.TOKEN);
        usertoken.setClient(Constants.HEADER_CLIENT);
        usertoken.setUserId(TestConstants.ONE);
        return usertoken;
    }

    public static ApiRolePermission getApiRolePermission() {
        ApiRolePermission apiRolePermission = new ApiRolePermission();
        apiRolePermission.setId(TestConstants.ONE);
        apiRolePermission.setRoles(Constants.ROLE_ACCOUNT_ADMIN);
        return apiRolePermission;
    }

    public static UserDTO getUserDTO() {
        UserDTO userDTO = modelMapper.map(getUser(), UserDTO.class);
        return userDTO;
    }

    public static User getUser() {
        User user = new User();
        user.setId(TestConstants.ONE);
        user.setPhoneNumber(TestConstants.PHONE_NUMBER);
        user.setUsername(TestConstants.USER_NAME);
        user.setForgetPasswordCount(Constants.ONE);
        user.setForgetPasswordTime(new Date());
        user.setInvalidLoginAttempts(Constants.ONE);
        user.setTenantId(TestConstants.FIVE);
        return user;
    }

    public static BpLog getBpLog() {
        BpLog bpLog = new BpLog();
        bpLog.setId(1L);
        bpLog.setAssessmentTenantId(1L);
        bpLog.setPatientTrackId(1L);
        bpLog.setBpLogId(1L);
        bpLog.setTenantId(1L);
        bpLog.setWeight(33.2d);
        bpLog.setHeight(173.1d);
        bpLog.setTemperature(30.1d);
        return bpLog;
    }

    public static Organization getOrganization() {
        Organization organization = new Organization();
        organization.setId(TestConstants.ONE);
        organization.setFormName(Constants.FORM_NAME);
        organization.setFormDataId(TestConstants.ONE);
        organization.setName(Constants.ORGANIZATION_REDIS_KEY);
        organization.setSequence(TestConstants.ZERO);
        organization.setTenantId(TestConstants.ONE);
        organization.setParentOrganizationId(TestConstants.ONE);
        organization.setFormDataId(TestConstants.ONE);
        organization.setActive(Boolean.TRUE);
        organization.setTenantId(TestConstants.ONE);
        return organization;
    }

    public static List<Organization> getAllOrganization() {
        List<Organization> organizations = new ArrayList<>();
        organizations.add(getOrganization());
        return organizations;
    }

    public static GlucoseLog getGlucoseLog() {
        return new GlucoseLog();
    }
}
