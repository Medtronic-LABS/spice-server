package com.mdtlabs.coreplatform;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.CustomDateSerializer;
import com.mdtlabs.coreplatform.common.TestConstants;
import com.mdtlabs.coreplatform.common.exception.Validation;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.entity.ApiRolePermission;
import com.mdtlabs.coreplatform.common.repository.CommonRepository;

/**
 * <p>
 * AuthenticationFilterTest class used to test all possible positive
 * and negative cases for all methods and conditions used in AuthenticationFilter
 * class.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationFilterTest {

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @Mock
    private CommonRepository commonRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private CustomDateSerializer customDateSerializer;

    @Mock
    private ListOperations listOperations;

    @ParameterizedTest
    @ValueSource(strings = {"/user/forgot-password", "/user/update-password", "/user/login-limit-exceed",
            "/user/is-forget-password-limit-exceed", "/user/is-reset-password-limit-exceed", "/webjars/swagger-ui",
            "/v3/api-docs", "/auth/generate-token", "/email/create", "/email/email-type", "/sms/save-outboundsms",
            "/sms/get-sms-template-values", "/user/verify-set-password", "/user/set-password", "/user/reset-password",
            "/static-data/health-check", "/data/health-check", "/email/health-check", "/sms/get-sms-template",
            "/sms/save-outboundsms-values", "/site/get-sites"})
    void doFilterInternal(String uri) throws ServletException, IOException {
        //given
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI(uri);
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();
        ApiRolePermission apiRolePermission = new ApiRolePermission();
        apiRolePermission.setMethod(Constants.USER_API);

        //then
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        assertEquals(uri, httpServletRequest.getRequestURI());
    }

    @Test
    void doFilterInternal() throws ServletException, IOException {
        //given
        ReflectionTestUtils.setField(authenticationFilter, "privateKey", "private_key.der");
        ReflectionTestUtils.setField(authenticationFilter, "privateRsaKey", null);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setRequestURI("/user/forgot-user");
        httpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, Constants.BEARER + TestConstants.JWT_TOKEN);
        httpServletRequest.addHeader(Constants.HEADER_TENANT_ID, TestConstants.ONE);
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();
        ApiRolePermission apiRolePermission = new ApiRolePermission();
        apiRolePermission.setMethod(Constants.USER_API);
        apiRolePermission.setRoles(Constants.ROLE_ACCOUNT_ADMIN);
        List<ApiRolePermission> apiRolePermissions = List.of(apiRolePermission);
        MockedStatic<StringUtils> stringUtils = mockStatic(StringUtils.class);
        String key = Constants.SPICE + Constants.COLON + Constants.LOGIN + Constants.COLON + "superuser@test.com" +
                Constants.COLON + TestConstants.JWT_TOKEN;

        //when
        stringUtils.when(() -> StringUtils.isNotBlank(Constants.BEARER + TestConstants.JWT_TOKEN)).thenReturn(Boolean.TRUE);
        stringUtils.when(() -> StringUtils.isNotBlank(Constants.ROLE_ACCOUNT_ADMIN)).thenReturn(Boolean.TRUE);
        when(commonRepository.getApiRolePermission()).thenReturn(apiRolePermissions);
        doNothing().when(customDateSerializer).setUserZoneId("+05:30");
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range(key, Constants.ZERO, Constants.ONE)).thenReturn(List.of(Constants.ROLE_SUPER_USER));
        when(redisTemplate.expire(TestConstants.REDIS_KEY, Constants.AUTH_TOKEN_EXPIRY_MINUTES, TimeUnit.MINUTES)).thenReturn(Boolean.TRUE);

        //then
        authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        verify(commonRepository, atLeastOnce()).getApiRolePermission();
        verify(customDateSerializer, atLeastOnce()).setUserZoneId("+05:30");
        verify(redisTemplate, atLeastOnce()).opsForList();
        verify(redisTemplate, atLeastOnce()).expire(TestConstants.REDIS_KEY, Constants.AUTH_TOKEN_EXPIRY_MINUTES, TimeUnit.MINUTES);
        stringUtils.close();
    }

    @Test
    void validateAspect() throws ParseException, JsonProcessingException {
        //given
        ReflectionTestUtils.setField(authenticationFilter, "privateKey", "private_key.der");
        ReflectionTestUtils.setField(authenticationFilter, "privateRsaKey", null);
        String key = Constants.SPICE + Constants.COLON + Constants.LOGIN + Constants.COLON + "superuser@test.com" +
                Constants.COLON + TestConstants.JWT_TOKEN;

        //when
        doNothing().when(customDateSerializer).setUserZoneId("+05:30");
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range(key, Constants.ZERO, Constants.ONE)).thenReturn(List.of(Constants.ROLE_SUPER_USER));
        when(redisTemplate.expire(TestConstants.REDIS_KEY, Constants.AUTH_TOKEN_EXPIRY_MINUTES, TimeUnit.MINUTES))
                .thenReturn(Boolean.TRUE);

        //then
        UserDTO userDTO = authenticationFilter.validateAspect(TestConstants.JWT_TOKEN);
        assertNotNull(userDTO);
        assertEquals("+05:30", userDTO.getTimezone().getOffset());
        assertEquals(TestConstants.JWT_TOKEN, userDTO.getAuthorization());
    }

    @Test
    void validateAspectValidationException() throws ParseException, JsonProcessingException {
        //given
        ReflectionTestUtils.setField(authenticationFilter, "privateKey", "private.der");
        ReflectionTestUtils.setField(authenticationFilter, "privateRsaKey", null);

        //then
        assertThrows(Validation.class, () -> authenticationFilter.validateAspect("Bearer eyJlbmMiOiJBMTI4R0NNIi"));
    }

    @Test
    void validateAspectJwtException() {
        //given
        ReflectionTestUtils.setField(authenticationFilter, "privateKey", "private_key.der");
        ReflectionTestUtils.setField(authenticationFilter, "privateRsaKey", null);
        String key = Constants.SPICE + Constants.COLON + Constants.LOGIN + Constants.COLON + "superuser@test.com" +
                Constants.COLON + TestConstants.JWT_TOKEN;
        //when
        doNothing().when(customDateSerializer).setUserZoneId("+05:30");
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range(key, Constants.ZERO, Constants.ONE)).thenReturn(null);

        //then
        assertThrows(ExpiredJwtException.class, () -> authenticationFilter.validateAspect(TestConstants.JWT_TOKEN));
    }

    @Test
    void doLogApi() throws ServletException, IOException {
        //given
        ContentCachingRequestWrapper requestWrapper = getContentCachingRequestWrapper();
        ContentCachingResponseWrapper responseWrapper = getContentCachingResponseWrapper();
        FilterChain filterChain = new MockFilterChain();

        //then
        authenticationFilter.doLogApi(requestWrapper, responseWrapper, filterChain);
        assertNotNull(getContentCachingRequestWrapper().getRequest());
        assertNotNull(getContentCachingResponseWrapper().getResponse());
        assertNotNull(filterChain);
    }

    @Test
    void beforeRequest() throws IOException {
        //then
        authenticationFilter.beforeRequest(getContentCachingRequestWrapper(), getContentCachingResponseWrapper());
        assertNotNull(getContentCachingRequestWrapper().getRequest());
    }

    @Test
    void afterRequest() {
        //then
        authenticationFilter.afterRequest(getContentCachingRequestWrapper(), getContentCachingResponseWrapper());
        assertNotNull(getContentCachingRequestWrapper().getRequest());
    }

    @Test
    void afterRequestTest() {
        //given
        byte[] content = {70, 121, 101};

        ContentCachingRequestWrapper contentCachingRequestWrapper = mock(ContentCachingRequestWrapper.class);
        ContentCachingResponseWrapper contentCachingResponseWrapper = mock(ContentCachingResponseWrapper.class);

        //when
        when(contentCachingRequestWrapper.getContentAsByteArray()).thenReturn(content);
        when(contentCachingRequestWrapper.getContentType()).thenReturn(Constants.APPLICATION_JSON);
        when(contentCachingRequestWrapper.getCharacterEncoding()).thenReturn(Constants.UTF_8);
        when(contentCachingResponseWrapper.getStatus()).thenReturn(200);
        when(contentCachingResponseWrapper.getContentAsByteArray()).thenReturn(content);
        when(contentCachingResponseWrapper.getContentType()).thenReturn(Constants.APPLICATION_JSON);
        when(contentCachingResponseWrapper.getCharacterEncoding()).thenReturn(Constants.UTF_8);

        //then
        authenticationFilter.afterRequest(contentCachingRequestWrapper, contentCachingResponseWrapper);
        assertNotNull(getContentCachingRequestWrapper().getRequest());
    }

    private ContentCachingRequestWrapper getContentCachingRequestWrapper() {
        return new ContentCachingRequestWrapper(new MockHttpServletRequest());
    }

    private ContentCachingResponseWrapper getContentCachingResponseWrapper() {
        return new ContentCachingResponseWrapper(new MockHttpServletResponse());
    }
}