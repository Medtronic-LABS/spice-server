package com.mdtlabs.coreplatform.authserver.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

/**
 * <p>
 * This class has the test methods for AuthenticationFailure class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationFailureTest {

    @InjectMocks
    AuthenticationFailure authenticationFailure;

    @Test
    void onAuthenticationFailure() throws ServletException, IOException {
        //given
        AuthenticationException authenticationException = new AuthenticationException("exception") {
        };
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        HttpServletRequest httpServletRequest = new MockHttpServletRequest();

        //then
        authenticationFailure.onAuthenticationFailure(httpServletRequest, httpServletResponse, authenticationException);
        assertEquals("exception", authenticationException.getMessage());
    }
}