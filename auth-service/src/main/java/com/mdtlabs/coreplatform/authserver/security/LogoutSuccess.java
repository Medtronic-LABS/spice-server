package com.mdtlabs.coreplatform.authserver.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.exception.Validation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.service.UserTokenService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;

/**
 * <p>
 * <tt>LogoutSuccess</tt> to handle successful logout.
 * </p>
 *
 * @author Vigneshkumar
 * @since 16 Oct 2020
 */
public class LogoutSuccess implements LogoutHandler {

    private RSAPrivateKey privateRsaKey = null;

    @Value("${app.private-key}")
    private String privateKey;

    @Autowired
    private UserTokenService userTokenService;


    /**
     * <p>
     * This method is used to log out a user by deleting their token from the userTokenService.
     * </p>
     *
     * @param request        {@link HttpServletRequest} An object of the HttpServletRequest class, which represents the HTTP request that
     *                       was sent to the server is given
     * @param response       {@link HttpServletResponse} The HttpServletResponse is used to set response headers,
     *                       status codes, and write response data is given
     * @param authentication {@link Authentication} The authentication parameter is used to check if the user is currently
     *                       authenticated and authorized to perform certain actions is given
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(token)) {
            throw new DataNotFoundException(20002);
        }

        UserDTO userDetail = null;
        try {
            userDetail = getUserDetails(token.substring(Constants.BEARER.length()));
        } catch (JsonProcessingException | ParseException e) {
            e.printStackTrace();
        }
        String redisKey = null;
        if (userDetail != null) {
            redisKey = Constants.SPICE + Constants.COLON + Constants.LOGIN +
                    Constants.COLON + userDetail.getUsername() + Constants.COLON +
                    token.substring(Constants.BEARER.length());
        }
        userTokenService.deleteUserTokenByToken(redisKey, token.substring(Constants.BEARER.length()));
    }

    /**
     * <p>
     * This method is used to retrieve user details from an encrypted JWT token.
     * </p>
     *
     * @param jwtToken {@link String} The JWT token that contains the user's information is given
     * @return {@link UserDTO} object, which contains the details of a user obtained
     * from a JWT token is given
     */
    private UserDTO getUserDetails(String jwtToken) throws ParseException, JsonProcessingException {
        if (StringUtils.isBlank(jwtToken)) {
            jwtToken = Constants.SPACE;
        }
        if (null == privateRsaKey) {
            tokenDecrypt();
        }

        EncryptedJWT jwt;
        try {
            jwt = EncryptedJWT.parse(jwtToken);
        } catch (ParseException e) {
            Logger.logError(e);
            throw new Validation(20002);
        }
        RSADecrypter rsaDecrypter = new RSADecrypter(privateRsaKey);
        try {
            jwt.decrypt(rsaDecrypter);
        } catch (JOSEException e) {
            Logger.logError(e);
            throw new Validation(20002);
        }
        UserDTO userDetail;
        String rawJson = String.valueOf(jwt.getJWTClaimsSet().getClaim(Constants.USER_DATA));
        ObjectMapper objectMapper = new ObjectMapper();
        userDetail = objectMapper.readValue(rawJson, UserDTO.class);
        return userDetail;
    }


    /**
     * <p>
     * This method is used to decrypts a token using a private RSA key.
     * </p>
     */
    private void tokenDecrypt() {
        try {
            Resource resource = new ClassPathResource(privateKey);
            byte[] data = FileCopyUtils.copyToByteArray(resource.getInputStream());
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(data);
            KeyFactory kf = KeyFactory.getInstance(Constants.RSA);
            this.privateRsaKey = (RSAPrivateKey) kf.generatePrivate(privateKeySpec);
        } catch (Exception exception) {
            Logger.logError(ErrorConstants.EXCEPTION_DURING_TOKEN_UTIL, exception);
        }

    }

}
