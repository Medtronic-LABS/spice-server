package com.mdtlabs.coreplatform.authserver.security;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.FileCopyUtils;

import com.mdtlabs.coreplatform.authservice.service.OrganizationService;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.AuthOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.AuthUserDTO;
import com.mdtlabs.coreplatform.common.model.dto.RoleDTO;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.service.UserTokenService;

/**
 * <p>
 * <tt>AuthenticationSuccess</tt> Sent to successful authentication.
 * </p>
 *
 * @author Vigneshkumar
 * @since 16 Oct 2020
 */
public class AuthenticationSuccess extends SimpleUrlAuthenticationSuccessHandler {

    private RSAPublicKey publicRsaKey;

    @Value("${app.public-key}")
    private String publicKey;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private OrganizationService organizationService;


    /**
     * <p>
     * This method is used to initialize a public RSA key from a file resource.
     * </p>
     */
    @PostConstruct
    public void init() {
        try {
            Resource resource = new ClassPathResource(publicKey);
            byte[] data = FileCopyUtils.copyToByteArray(resource.getInputStream());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory kf = KeyFactory.getInstance(Constants.RSA);
            this.publicRsaKey = (RSAPublicKey) kf.generatePublic(spec);
        } catch (Exception e) {
            Logger.logError(ErrorConstants.EXCEPTION_TOKEN_UTILS, e);
        }
    }


    /**
     * <p>
     * This method is used to handle successful authentication and returns user information in JSON format.
     * </p>
     *
     * @param request        {@link HttpServletRequest} An object representing the HTTP request made by the client is given
     * @param response       {@link HttpServletResponse} The HttpServletResponse object that is used to send the response back to the
     *                       client after successful authentication is given
     * @param authentication {@link Authentication} The authentication object represents the user's authentication information is given
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(Constants.CONTENT_TEXT_TYPE);
            response.setHeader(Constants.CACHE_HEADER_NAME, Constants.CACHE_HEADER_VALUE);
            response.setHeader(Constants.ACCESS_CONTROL_EXPOSE_HEADERS, Constants.AUTHORIZATION);
            try {
                AuthUserDTO user = getLoggedInUser();
                if (user != null) {
                    String client = request.getHeader(Constants.HEADER_CLIENT);
                    if (StringUtils.isNotBlank(client)) {
                        if (Constants.CLIENT_SPICE_MOBILE.equalsIgnoreCase(client)) {
                            assignUserRole(Constants.SPICE_MOBILE_ROLES, user);
                        } else if (Constants.CLIENT_SPICE_WEB.equalsIgnoreCase(client)) {
                            assignUserRole(Constants.SPICE_WEB_ROLES, user);
                        } else if (Constants.CLIENT_CFR_WEB.equalsIgnoreCase(client)) {
                            assignUserRole(Constants.CFR_WEB_ROLES, user);
                        }
                    }
                    user.setCurrentDate(new Date().getTime());
                    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    String json = objectWriter.writeValueAsString(user);
                    response.getWriter().write(json);
                    responseHeaderUser(response, user, client);
                } else {
                    response.getWriter().write(ErrorConstants.INVALID_USER_ERROR);
                }
            } catch (IOException e) {
                Logger.logError(ErrorConstants.LOGIN_ERROR + e);
            }

        }
        clearAuthenticationAttributes(request);
    }


    /**
     * <p>
     * This method is used to assign a user role based on a role map and throws an exception if the user does
     * not have a valid role.
     * </p>
     *
     * @param roleMap {@link Map<String, String>} A map that contains the names of roles as keys and their corresponding values as
     *                values is given
     * @param user    {@link AuthUserDTO} An object of type AuthUserDTO which contains information about the user, including
     *                their roles is given
     */
    private void assignUserRole(Map<String, String> roleMap, AuthUserDTO user) {
        boolean isExist = false;
        for (RoleDTO roleDto : user.getRoles()) {
            if (null != roleMap.get(roleDto.getName())) {
                isExist = true;
                Role role = new Role(roleDto.getId(), roleDto.getName());
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                user.setRoles(roles);
                break;
            }
        }
        if (!isExist) {
            throw new BadCredentialsException(ErrorConstants.ERROR_USER_DOES_NOT_ROLE);
        }
    }


    /**
     * <p>
     * This method is used to set the response headers for a user's authentication token and tenant ID.
     * </p>
     *
     * @param response {@link HttpServletResponse} The HttpServletResponse object that represents the response to be sent back to
     *                 the client. It contains information such as the response status code, headers, and body is given
     * @param user     {@link AuthUserDTO} An object of type AuthUserDTO which contains information about the authenticated
     *                 user is given
     * @param client   {@link String} The "client" parameter is a String that represents the client application that is
     *                 making the request to the server. It is used to create a user token for the client is given
     */
    private void responseHeaderUser(HttpServletResponse response, AuthUserDTO user, String client) {
        Map<String, Object> userInfo = new ObjectMapper().convertValue(user, Map.class);
        String authToken = null;
        try {
            authToken = authTokenCreation(user, userInfo);
        } catch (JOSEException exception) {
            Logger.logError(ErrorConstants.ERROR_JWE_TOKEN, exception);
        }
        if (authToken != null) {
            createUserToken(authToken, client);
        }
        response.setHeader(Constants.AUTHORIZATION, authToken);
        response.setHeader(Constants.HEADER_TENANT_ID, String.valueOf(user.getTenantId()));
    }

    /**
     * <p>
     * This method is used to create an encrypted JWT authentication token with user and organization
     * information.
     * </p>
     *
     * @param user     {@link AuthUserDTO} The user parameter is an instance of the AuthUserDTO class, which contains
     *                 information about the authenticated user, such as their ID and the organizations they belong to
     *                 is given
     * @param userInfo {@link Map<String, Object>} The userInfo parameter is a Map object that contains additional information
     *                 about the user, such as their name, email, and other relevant details is given
     * @return {@link String} The method returns a string that represents an encrypted JWT (JSON Web Token) is given
     */
    private String authTokenCreation(AuthUserDTO user, Map<String, Object> userInfo) throws JOSEException {
        List<Long> tenantIds = new ArrayList<>();
        if (!user.getOrganizations().isEmpty()) {
            tenantIds = user.getOrganizations().stream().map(AuthOrganizationDTO::getId).toList();
        }

        JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();
        claimsSet.issuer(Constants.TOKEN_ISSUER);
        claimsSet.subject(Constants.AUTH_TOKEN_SUBJECT);
        claimsSet.claim(Constants.USER_ID_PARAM, user.getId());
        claimsSet.claim(Constants.USER_DATA, userInfo);
        claimsSet.claim(Constants.TENANT_IDS_CLAIM, tenantIds);
        claimsSet.claim(Constants.APPLICATION_TYPE, Constants.WEB);
        claimsSet.expirationTime(
                Date.from(ZonedDateTime.now().plusMinutes(Constants.AUTH_TOKEN_EXPIRY_MINUTES).toInstant()));
        claimsSet.notBeforeTime(new Date());
        claimsSet.jwtID(UUID.randomUUID().toString());
        JWEHeader header = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);
        EncryptedJWT jwt = new EncryptedJWT(header, claimsSet.build());
        RSAEncrypter rsaEncrypter = new RSAEncrypter(this.publicRsaKey);
        jwt.encrypt(rsaEncrypter);
        return Constants.BEARER.concat(jwt.serialize());
    }


    /**
     * <p>
     * This method is used to create a user token by saving the JWT token, username, client, and user ID in the
     * userTokenService if the user is logged in.
     * </p>
     *
     * @param jwtToken {@link String} The JWT token that needs to be saved for the user is given
     * @param client   {@link String} The "client" parameter in this method refers to the client application that is
     *                 requesting the creation of a user token is given
     */
    private void createUserToken(String jwtToken, String client) {
        AuthUserDTO user = getLoggedInUser();
        if (user != null) {
            userTokenService.saveUserToken(jwtToken.substring(Constants.BEARER.length()),
                    user.getUsername(), client, user.getId());
        }
    }


    /**
     * <p>
     * This method is used to retrieve the currently logged in user's information and maps it to an AuthUserDTO
     * object.
     * </p>
     *
     * @return {@link AuthUserDTO} The method is returning an instance of the class, which represents the
     * currently logged in user is given
     */
    private AuthUserDTO getLoggedInUser() {
        if (null == SecurityContextHolder.getContext() || null == SecurityContextHolder.getContext().getAuthentication()
                || null == SecurityContextHolder.getContext().getAuthentication().getPrincipal()) {
            return null;
        }
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(Constants.ANONYMOUS_USER)) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuthUserDTO authUserDTO;
        authUserDTO = new ModelMapper().map(SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                AuthUserDTO.class);
        User user = (User) principal;
        authUserDTO.setIsSuperUser(user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(Constants.ROLE_SUPER_USER)));
        return authUserDTO;
    }

}
