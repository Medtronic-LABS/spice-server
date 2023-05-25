package com.mdtlabs.coreplatform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.CustomDateSerializer;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserTenantsContextHolder;
import com.mdtlabs.coreplatform.common.exception.Validation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.RoleDTO;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.repository.CommonRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * <p>
 * Used to do internal filter on token validation.
 * </p>
 *
 * @author Prabu created on Oct 06, 2022
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(MediaType.valueOf(Constants.TEXT),
            MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
            MediaType.valueOf(Constants.APPLICATION_JSON), MediaType.valueOf(Constants.APPLICATION_XML),
            MediaType.MULTIPART_FORM_DATA);
    public Map<String, Map<String, List<String>>> apiPermissionMap = new HashMap<>();
    @Autowired
    public CommonRepository commonRepository;
    org.apache.logging.log4j.Logger log = LogManager.getLogger(AuthenticationFilter.class);
    private RSAPrivateKey privateRsaKey = null;
    @Value("${app.private-key}")
    private String privateKey;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private CustomDateSerializer customDateSerializer;

    /**
     * <p>
     * This method is used to collect the request details of api.
     * </p>
     *
     * @param request {@link HttpServletRequest} api request
     * @return ContentCachingRequestWrapper  request wrapper
     */
    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            return requestWrapper;
        }
        return new ContentCachingRequestWrapper(request);
    }

    /**
     * <p>
     * This method is used to collect the response details of api.
     * </p>
     *
     * @param response {@link HttpServletResponse} api response
     * @return {@link ContentCachingResponseWrapper}  response wrapper
     */
    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper responseWrapper) {
            return responseWrapper;
        }
        return new ContentCachingResponseWrapper(response);
    }

    /**
     * <p>
     * This is a Java function that filters incoming HTTP requests and checks for valid authentication
     * tokens before allowing access to protected resources.
     * </p>
     *
     * @param request     {@link HttpServletRequest} An object representing the HTTP request made by the client.
     * @param response    {@link HttpServletRequest} The HttpServletResponse object represents the response that will be sent back to
     *                    the client after the request has been processed
     * @param filterChain {@link FilterChain} The filterChain parameter is an object that represents the chain of filters
     *                    that will be applied to the request and response
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isOpenUri(request)) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } else {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isNotBlank(token) && token.startsWith(Constants.BEARER)) {
                String tenantId = request.getHeader(Constants.HEADER_TENANT_ID);
                UserDTO userDto;
                try {
                    userDto = validateAspect(token.substring(Constants.BEARER.length()));
                    setAuthenticationInSecurityContext(userDto, tenantId);
                    setApiRolesAgainstMethod();
                    boolean isExist = isValidRoleExist(userDto, request);
                    if (isExist) {
                        doLogApi(wrapRequest(request), wrapResponse(response), filterChain);
                    } else {
                        throw new Validation(20001);
                    }
                } catch (JsonProcessingException | ParseException e) {
                    Logger.logError(e);
                    throw new Validation(20001);
                }
            } else {
                throw new Validation(20001);
            }
        }
    }

    /**
     * <p>
     * Used to validate the authentication token.
     * </p>
     *
     * @param jwtToken jwt token of the logged-in user
     * @return UserDTO {@link UserDTO}  user information
     * @throws ParseException          Parse exception
     * @throws JsonProcessingException Json processing exception
     */
    public UserDTO validateAspect(String jwtToken)
            throws ParseException, JsonProcessingException {

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
        Object tenants = jwt.getJWTClaimsSet().getClaim(Constants.TENANT_IDS_CLAIM);
        UserDTO userDetail;
        String rawJson = String.valueOf(jwt.getJWTClaimsSet().getClaim(Constants.USER_DATA));

        ObjectMapper objectMapper = new ObjectMapper();
        userDetail = objectMapper.readValue(rawJson, UserDTO.class);
        userDetail.setAuthorization(jwtToken);
        if (tenants instanceof List) {
            List<Long> tenantIds = (List<Long>) tenants;
            UserTenantsContextHolder.set(tenantIds);

        }
        if (null != userDetail.getTimezone()) {
            customDateSerializer.setUserZoneId(userDetail.getTimezone().getOffset());
        }
        String key = Constants.SPICE + Constants.COLON + Constants.LOGIN + Constants.COLON + userDetail.getUsername()
                + Constants.COLON + userDetail.getAuthorization();
        List<String> redisKeyList = redisTemplate.opsForList().range(key, Constants.ZERO, Constants.ONE);
        if (null == redisKeyList || redisKeyList.isEmpty()) {
            throw new ExpiredJwtException(null, null, ErrorConstants.TOKEN_EXPIRED);
        }
        redisTemplate.expire(key, Constants.AUTH_TOKEN_EXPIRY_MINUTES, TimeUnit.MINUTES);
        UserContextHolder.setUserDto(userDetail);
        return userDetail;
    }

    /**
     * <p>
     * Decrypt given jwe token using private key.
     * </p>
     */
    @PostConstruct
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

    /**
     * <p>
     * This method is used to log request and response of api.
     * </p>
     *
     * @param request     {@link ContentCachingRequestWrapper} api request
     * @param response    {@link ContentCachingResponseWrapper} api response
     * @param filterChain {@link FilterChain} the chain proceeding of flow
     * @throws ServletException - servlet exception
     * @throws IOException      - IO exception
     */
    protected void doLogApi(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response,
                            FilterChain filterChain) throws ServletException, IOException {
        try {
            beforeRequest(request, response);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response);
            response.copyBodyToResponse();
        }
    }

    /**
     * <p>
     * This method is used to log the request details of api.
     * </p>
     *
     * @param request  {@link ContentCachingRequestWrapper} api request
     * @param response {@link ContentCachingResponseWrapper} api response
     * @throws IOException IO exception
     */
    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response)
            throws IOException {
        logRequestHeader(request, request.getRemoteAddr() + Constants.LOG_PREFIX_REQUEST);
    }

    /**
     * <p>
     * This method is used to log the response details of api.
     * </p>
     * <p>
     *
     * @param request  {@link ContentCachingRequestWrapper} api request
     * @param response {@link ContentCachingResponseWrapper} api response
     */
    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        logRequestBody(request, request.getRemoteAddr() + Constants.LOG_PREFIX_REQUEST);
        logResponse(response, request.getRemoteAddr() + Constants.LOG_PREFIX_RESPONSE);
    }

    /**
     * <p>
     * This method is used to log request header details of api.
     * </p>
     *
     * @param request {@link ContentCachingRequestWrapper} api request
     * @param prefix  {@link String} prefix text in log
     * @throws IOException IO exception
     */
    private void logRequestHeader(ContentCachingRequestWrapper request, String prefix) throws IOException {
        log.info("{} {} {}", prefix, request.getMethod(), request.getRequestURI());
        byte[] content = request.getContentAsByteArray();
        String contentString = new String(content, request.getCharacterEncoding());
        Stream.of(contentString.split(Constants.SPLIT_CONTENT)).forEach(line -> log.info("{}", line));
    }

    /**
     * <p>
     * This method is used to log request body details of api.
     * </p>
     *
     * @param request {@link ContentCachingRequestWrapper} api request
     * @param prefix  {@link String} prefix text in log
     */
    private void logRequestBody(ContentCachingRequestWrapper request, String prefix) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > Constants.ZERO) {
            logContent(content, request.getContentType(), request.getCharacterEncoding(), prefix);
        }
    }

    /**
     * <p>
     * This method is used to log response details of api.
     * </p>
     *
     * @param response {@link ContentCachingResponseWrapper} api request
     * @param prefix   {@link String} prefix text in log
     */
    private void logResponse(ContentCachingResponseWrapper response, String prefix) {
        int status = response.getStatus();
        log.info("{} {} {}", prefix, status, HttpStatus.valueOf(status).getReasonPhrase());
        byte[] content = response.getContentAsByteArray();
        if (content.length > Constants.ZERO) {
            logContent(content, response.getContentType(), response.getCharacterEncoding(), prefix);
        }
    }

    /**
     * <p>
     * This method is used to print log on console.
     * </p>
     *
     * @param content         {@link byte[]} log content
     * @param contentType     content type
     * @param contentEncoding content encoding type
     * @param prefix          prefix text in log
     */
    private void logContent(byte[] content, String contentType, String contentEncoding, String prefix) {
        MediaType mediaType = MediaType.valueOf(contentType);
        boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible) {
            try {
                String contentString = new String(content, contentEncoding);
                boolean logInfo = setLogInfo(contentString, true);
                if (logInfo) {
                    Stream.of(contentString.split(Constants.SPLIT_CONTENT)).forEach(line -> log.info("{}", line));
                }
            } catch (UnsupportedEncodingException e) {
                log.info("{} [{} bytes content]", prefix, content.length);
            }
        } else {
            log.info("{} [{} bytes content]", prefix, content.length);
        }
    }

    /**
     * <p>
     * Sets log information value based on content string.
     * </p>
     *
     * @param contentString content string
     * @param logInfo       true or false
     * @return boolean  true or false
     * @throws JSONException - Json exception
     */
    private boolean setLogInfo(String contentString, boolean logInfo) throws JSONException {
        if (contentString.startsWith("{") && contentString.endsWith("}")) {
            JSONObject json = new JSONObject(contentString);
            if (json.has(Constants.ENTITY_LIST) && json.get(Constants.ENTITY_LIST) instanceof JSONArray entityList
                    && (entityList.length() > 1)) {
                logInfo = false;
            }
        }
        return logInfo;
    }

    /**
     * <p>
     * This method is used to set api roles.
     * </p>
     */
    private void setApiRolesAgainstMethod() {
        if (apiPermissionMap.isEmpty()) {
            commonRepository.getApiRolePermission().forEach(api -> {
                if (StringUtils.isNotBlank(api.getRoles())) {
                    Map<String, List<String>> apiRoleMap;
                    if (apiPermissionMap.get(api.getMethod()) == null) {
                        apiRoleMap = new HashMap<>();
                    } else {
                        apiRoleMap = apiPermissionMap.get(api.getMethod());
                    }
                    apiRoleMap.put(api.getApi(), Arrays.asList(api.getRoles().split("\\s*,\\s*")));
                    apiPermissionMap.put(api.getMethod(), apiRoleMap);
                }
            });
        }
    }

    /**
     * <p>
     * This is method is used to check whether valid role exist or not.
     * </p>
     *
     * @param userDto {@link UserDTO} user DTO
     * @param request - {@link HttpServletRequest} http request data
     * @return Boolean
     */
    private boolean isValidRoleExist(UserDTO userDto, HttpServletRequest request) {
        String apiRequest = Constants.EMPTY;
        if (Boolean.TRUE.equals(userDto.getIsSuperUser())) {
            return true;
        } else {
            Set<String> requestTypeSet = apiPermissionMap.get(request.getMethod()).keySet();
            for (String requestType : requestTypeSet) {
                if (request.getRequestURI().contains(requestType)) {
                    apiRequest = requestType;
                    break;
                }
            }

            for (RoleDTO role : userDto.getRoles()) {
                if (apiPermissionMap.get(request.getMethod()).get(apiRequest).contains(role.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * Used to find open uri to access directly without authorization.
     * </p>
     *
     * @param request {@link HttpServletRequest}
     * @return boolean
     */
    private boolean isOpenUri(HttpServletRequest request) {
        return request.getRequestURI().contains("/user/forgot-password")
                || request.getRequestURI().contains("/user/update-password")
                || request.getRequestURI().contains("/user/login-limit-exceed")
                || request.getRequestURI().contains("/user/is-forget-password-limit-exceed")
                || request.getRequestURI().contains("/user/is-reset-password-limit-exceed")
                || request.getRequestURI().contains("/webjars/swagger-ui")
                || request.getRequestURI().contains("/v3/api-docs")
                || request.getRequestURI().contains("/auth/generate-token")
                || request.getRequestURI().contains("/email/create")
                || request.getRequestURI().contains("/email/email-type")
                || request.getRequestURI().contains("/sms/save-outboundsms")
                || request.getRequestURI().contains("/sms/get-sms-template-values")
                || request.getRequestURI().contains("/user/verify-set-password")
                || request.getRequestURI().contains("/user/set-password")
                || request.getRequestURI().contains("/user/reset-password")
                || request.getRequestURI().contains("/static-data/health-check")
                || request.getRequestURI().contains("/data/health-check")
                || request.getRequestURI().contains("/email/health-check")
                || request.getRequestURI().contains("/sms/get-sms-template")
                || request.getRequestURI().contains("/sms/save-outboundsms-values")
                || request.getRequestURI().contains("/site/get-sites");
    }

    /**
     * <p>
     * Used to set authentication in the security context.
     * </p>
     *
     * @param userDto  {@link UserDTO}
     * @param tenantId {@link String}
     */
    private void setAuthenticationInSecurityContext(UserDTO userDto, String tenantId) {
        if (null != userDto.getAuthorization()) {
            String username = userDto.getUsername();
            if (username != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username,
                        null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        if (StringUtils.isNotBlank(tenantId) && tenantId.matches("[\\d]+")) {
            UserSelectedTenantContextHolder.set(Long.parseLong(tenantId));
        }
    }
}
