package com.mdtlabs.coreplatform.authserver.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * <p>
 * This class has the test methods for Security config class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    SecurityConfig securityConfig;

    @Test
    void passwordEncoder() {
        //then
        PasswordEncoder actualPasswordEncoder = securityConfig.passwordEncoder();
        assertNotNull(actualPasswordEncoder);
    }

    @Test
    void authenticationSuccess() {
        //then
        AuthenticationSuccess authenticationSuccess = securityConfig.authenticationSuccess();
        assertNotNull(authenticationSuccess);
    }

    @Test
    void authenticationFailure() {
        //then
        AuthenticationFailure authenticationFailure = securityConfig.authenticationFailure();
        assertNotNull(authenticationFailure);
    }

    @Test
    void authenticationProvider() {
        //then
        AuthenticationProvider authenticationProvider = securityConfig.authenticationProvider();
        assertNotNull(authenticationProvider);
    }

    @Test
    void logoutSuccess() {
        //then
        LogoutSuccess logoutSuccess = securityConfig.logoutSuccess();
        assertNotNull(logoutSuccess);
    }

    @Test
    void corsConfigurationSource() {
        //then
        CorsConfigurationSource configurationSource = securityConfig.corsConfigurationSource();
        assertNotNull(configurationSource);
    }
}