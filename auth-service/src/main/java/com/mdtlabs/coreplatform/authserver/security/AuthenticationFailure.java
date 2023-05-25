package com.mdtlabs.coreplatform.authserver.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdtlabs.coreplatform.common.Constants;

/**
 * <p>
 * The AuthenticationFailure class extends the SimpleUrlAuthenticationFailureHandler to send a failure response when
 * there is an authentication failure. It provides an implementation of the onAuthenticationFailure() method that is
 * called by the Spring Security framework. The method sends a response containing an error message and status code to
 * the client.
 * </p>
 *
 * @author Vigneshkumar
 * @since 30 Jun 2022
 */
public class AuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {

    /**
     * <p>
     * This method is called by the Spring Security framework when there is an authentication failure, and it sends
     * a response containing an error message and status code to the client.
     * </p>
     *
     * @param request   {@link HttpServletRequest} The HttpServletRequest object containing the request information
     *                  is given
     * @param response  {@link HttpServletResponse} The HttpServletResponse object for sending the response is given
     * @param exception {@link AuthenticationException} The AuthenticationException object containing the exception
     *                  information is given
     * @throws IOException If an input or output exception occurs while sending the response is given
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put(Constants.MESSAGE, exception.getMessage());
        response.setContentType(Constants.CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }
}
