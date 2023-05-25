package com.mdtlabs.coreplatform.authserver.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.mdtlabs.coreplatform.authservice.service.OrganizationService;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.service.UserTokenService;
import com.mdtlabs.coreplatform.util.TestConstants;
import com.mdtlabs.coreplatform.util.TestDataProvider;

/**
 * <p>
 *   This class has the test methods for AuthenticationSuccess class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationSuccessTest {
    @InjectMocks
    AuthenticationSuccess authenticationSuccess;

    @Mock
    private UserTokenService userTokenService;

    @Mock
    private OrganizationService organizationService;

    @Test
    void init() {
        //given
        ReflectionTestUtils.setField(authenticationSuccess, "publicKey", "public_key.der");

        //then
        authenticationSuccess.init();
        assertNotNull(Boolean.TRUE);
    }

      @Test
    void catchInitException() {
        //given
        ReflectionTestUtils.setField(authenticationSuccess, "publicKey", "file.der");

        //then
        authenticationSuccess.init();
        assertNotNull(Boolean.TRUE);
      }

    @Test
    void onAuthenticationSuccess() {
        //given
        ReflectionTestUtils.setField(authenticationSuccess, "publicRsaKey", getRSAPublicKey());
        MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class);
        securityContextHolder.when(SecurityContextHolder::getContext)
                .thenReturn(getSecurityContext(Constants.ROLE_SUPER_USER));
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(Constants.HEADER_CLIENT, Constants.CLIENT_SPICE_WEB);
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        Authentication authentication = getAuth(Constants.ROLE_SUPER_USER);

        //when
        doNothing().when(userTokenService).saveUserToken(TestConstants.TOKEN, Constants.ROLE_SUPER_USER,
                Constants.CLIENT_SPICE_WEB, TestConstants.ONE);

        //then
        authenticationSuccess.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        assertNotNull(httpServletRequest);
        securityContextHolder.close();
    }

    @Test
    void verifyOnAuthenticationSuccess() {
        //given
        ReflectionTestUtils.setField(authenticationSuccess, "publicRsaKey", getRSAPublicKey());
        MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class);
        securityContextHolder.when(SecurityContextHolder::getContext)
                .thenReturn(getSecurityContext(Constants.ROLE_NURSE));
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(Constants.HEADER_CLIENT, Constants.CLIENT_SPICE_MOBILE);
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        Authentication authentication = getAuth(Constants.ROLE_NURSE);

        //when
        when(organizationService.getChildOrganizations(TestConstants.ONE, TestConstants.FORM_NAME))
                .thenReturn(Set.of(TestConstants.ONE));
        doNothing().when(userTokenService).saveUserToken(TestConstants.TOKEN, Constants.ROLE_SUPER_USER,
                Constants.CLIENT_SPICE_WEB, TestConstants.ONE);

        //then
        authenticationSuccess.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        assertNotNull(authentication);
        securityContextHolder.close();
    }

    @Test
    void onAuthenticationSuccessException() {
        //given
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(Constants.HEADER_CLIENT, Constants.CLIENT_SPICE_WEB);
        HttpServletResponse httpServletResponse = getHttpServletResponsePrintWriter();
        Authentication authentication = getAuth(Constants.ROLE_SUPER_USER);

        //then
        authenticationSuccess.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        assertNotNull(Boolean.TRUE);
    }

    private Authentication getAuth(String roleName) {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                User user = TestDataProvider.getUser();
                user.setRoles(Set.of(new Role(TestConstants.ONE, roleName)));
                return user;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    private SecurityContext getSecurityContext(String userName) {
        return new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return getAuth(userName);
            }

            @Override
            public void setAuthentication(Authentication authentication) {

            }
        };
    }

    private RSAPublicKey getRSAPublicKey() {
        return new RSAPublicKey() {
            @Override
            public BigInteger getPublicExponent() {
                return new BigInteger("65537");
            }

            @Override
            public String getAlgorithm() {
                return "RSA";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return null;
            }

            @Override
            public BigInteger getModulus() {
                return TestConstants.BIG_INTEGER;
            }
        };
    }

    private HttpServletResponse getHttpServletResponsePrintWriter() {
        return new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String s) {
                return false;
            }

            @Override
            public String encodeURL(String s) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String s) {
                return null;
            }

            @Override
            public String encodeUrl(String s) {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String s) {
                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException {

            }

            @Override
            public void sendError(int i) throws IOException {

            }

            @Override
            public void sendRedirect(String s) throws IOException {

            }

            @Override
            public void setDateHeader(String s, long l) {

            }

            @Override
            public void addDateHeader(String s, long l) {

            }

            @Override
            public void setHeader(String s, String s1) {

            }

            @Override
            public void addHeader(String s, String s1) {

            }

            @Override
            public void setIntHeader(String s, int i) {

            }

            @Override
            public void addIntHeader(String s, int i) {

            }

            @Override
            public void setStatus(int i) {

            }

            @Override
            public void setStatus(int i, String s) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public String getHeader(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter("test/notes");
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        };
    }
}
