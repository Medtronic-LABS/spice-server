package com.mdtlabs.coreplatform.authserver.security;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.exception.Validation;
import com.mdtlabs.coreplatform.common.service.UserTokenService;
import com.mdtlabs.coreplatform.util.TestConstants;

/**
 * <p>
 * This class has the test methods for LogoutSuccess class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class LogoutSuccessTest {

    @InjectMocks
    LogoutSuccess logoutSuccess;

    @Mock
    UserTokenService userTokenService;

    @Test
    void toVerifyLogout() {
        //given
        ReflectionTestUtils.setField(logoutSuccess, "privateKey", "private_key.der");
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, Constants.BEARER + TestConstants.ERROR_TOKEN);
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        Authentication authentication = mock(Authentication.class);

        //then
        assertThrows(Validation.class, () -> logoutSuccess.logout(httpServletRequest, httpServletResponse,
                authentication));
    }

    //  @Test
    void logout() {
        //given
        ReflectionTestUtils.setField(logoutSuccess, "privateKey", "private_key.der");
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, Constants.BEARER + TestConstants.TOKEN);
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        Authentication authentication = mock(Authentication.class);

        //when
        userTokenService.deleteUserTokenByToken(TestConstants.REDIS_KEY, TestConstants.TOKEN);

        //then
        logoutSuccess.logout(httpServletRequest, httpServletResponse, authentication);
        verify(userTokenService, atLeastOnce()).deleteUserTokenByToken(TestConstants.REDIS_KEY, TestConstants.TOKEN);
    }

    @Test
    void throwDataNotFoundException() {
        //given
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, Constants.EMPTY);
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        Authentication authentication = mock(Authentication.class);

        //then
        assertThrows(DataNotFoundException.class, () -> logoutSuccess.logout(httpServletRequest, httpServletResponse,
                authentication));
    }

    @Test
    void throwValidationException() {
        //given
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJlbmMiOiJBMTI4R0NNIi");
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        Authentication authentication = mock(Authentication.class);

        //then
        assertThrows(Validation.class, () -> logoutSuccess.logout(httpServletRequest, httpServletResponse,
                authentication));
    }
}