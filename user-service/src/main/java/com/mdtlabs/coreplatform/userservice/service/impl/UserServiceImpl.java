package com.mdtlabs.coreplatform.userservice.service.impl;

import com.mdtlabs.coreplatform.AuthenticationFilter;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.exception.SpiceValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.fhir.FhirSiteRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.fhir.FhirUserRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CultureRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OutBoundEmailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PaginateDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserSuperAdminDto;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplate;
import com.mdtlabs.coreplatform.common.model.entity.EmailTemplateValue;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.service.UserTokenService;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.DateUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.common.util.StringUtil;
import com.mdtlabs.coreplatform.common.util.UniqueCodeGenerator;
import com.mdtlabs.coreplatform.userservice.NotificationApiInterface;
import com.mdtlabs.coreplatform.userservice.mapper.UserMapper;
import com.mdtlabs.coreplatform.userservice.repository.UserRepository;
import com.mdtlabs.coreplatform.userservice.service.OrganizationService;
import com.mdtlabs.coreplatform.userservice.service.RoleService;
import com.mdtlabs.coreplatform.userservice.service.UserService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * UserServiceImpl class contains all the business logic for user module and
 * performs the CRUD operations.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Autowired
    private NotificationApiInterface notificationApiInterface;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.email-app-url}")
    private String appUrl;

    @Value("${app.forget-password-count-limit}")
    private int forgotPasswordCountLimit;

    @Value("${app.forgot-password-time-limit-in-minutes}")
    private int forgotPasswordTimeLimitInMinutes;

    @Value("${app.page-count}")
    private int gridDisplayValue;

    @Value("${app.login-count-limit}")
    private int loginCountLimit;

    @Value("${app.login-time-limit-in-hour}")
    private int loginTimeLimitInHour;

    @Value("${app.mail-user}")
    private String mailUser;

    @Value("${app.reset-password-count-limit}")
    private int resetPasswordCountLimit;

    @Value("${app.reset-password-time-limit-in-minutes}")
    private int resetPasswordTimeLimitInMinutes;

    @Value("${app.enableFhir}")
    private boolean enableFhir;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.name}")
    private String routingKey;

    /**
     * {@inheritDoc}
     */
    public User addUser(User user) {
        User updatedUser;
        if (!user.getRoles().isEmpty()) {
            if (Objects.isNull(user.getId()) || Constants.ZERO == user.getId()) {
                if (null != userRepository.findByUsernameAndIsDeletedFalse(user.getUsername())) {
                    throw new SpiceValidation(1009);
                }
                user.setForgetPasswordCount(Constants.ZERO);
                updatedUser = userRepository.save(user);
                forgotPassword(updatedUser.getUsername(), updatedUser, Boolean.TRUE, Constants.SPICE);
                if (enableFhir) {
                    setAndSendUserRequest(List.of(updatedUser));
                }
            } else {
                updatedUser = userRepository.save(user);
            }
            return updatedUser;
        }
        throw new SpiceValidation(1007);
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsers(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - Constants.ONE, gridDisplayValue);
        Page<User> users = userRepository.getUsers(Boolean.TRUE, pageable);
        return Objects.nonNull(users) ? users.stream().toList() : Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    public User updateUser(User user) {
        if (Objects.isNull(user.getId())) {
            throw new DataNotAcceptableException(1016);
        }
        User existingUser = getUserById(user.getId());
        if (!Objects.equals(UserContextHolder.getUserDto().getId(), user.getId())) {
            throw new SpiceValidation(1104);
        }
        userMapper.setExistingUser(user, existingUser);
        return userRepository.save(existingUser);
    }

    /**
     * {@inheritDoc}
     */
    public boolean deleteUserById(long userId) {
        UserDTO userDto = UserContextHolder.getUserDto();
        if (!userDto.getRoles().isEmpty()) {
            User user = getUserById(userId);
            if (null != user) {
                user.setActive(Boolean.FALSE);
                user.setDeleted(Boolean.TRUE);
                user.setOrganizations(null);
                user.setUpdatedBy(userDto.getId());
                userRepository.save(user);
                userTokenService.deleteUserTokenByUserName(user.getUsername(), user.getId());
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        throw new SpiceValidation(1011);
    }

    /**
     * {@inheritDoc}
     */
    public User getUserById(long userId) {
        return userRepository.getUserById(userId, Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username, Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    public List<UserOrganizationDTO> getUsersByTenantIds(List<Long> tenantIds) {
        List<User> users = userRepository.findUsersByTenantIds(tenantIds, Boolean.TRUE);
        List<UserOrganizationDTO> userDTOList = null;
        if (!Objects.isNull(users)) {
            userDTOList = users.stream().map(user ->
                    modelMapper.map(user, UserOrganizationDTO.class)).toList();
        }
        return userDTOList;
    }

    /**
     * {@inheritDoc}
     */
    public void validateUsers(List<String> requestUsers) {
        if (requestUsers.isEmpty()) {
            List<User> validatedUsers = userRepository.findByUsernameInAndIsDeletedFalse(requestUsers);
            if (!Objects.isNull(validatedUsers) && !validatedUsers.isEmpty()) {
                throw new DataConflictException(1103);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public Boolean forgotPassword(String emailId, User user, boolean isFromCreation, String appType) {
        user = (null == user) ? userRepository.getUserByUsername(emailId, Boolean.TRUE) : user;
        if (null != user) {
            if (StringUtils.isBlank(appType)) {
                throw new BadRequestException(3013);
            }
            boolean forgotPasswordLimitExceed = checkForgotPasswordLimitExceed(user, isFromCreation);
            if (Boolean.TRUE.equals(user.getIsBlocked())) {
                Logger.logError(StringUtil.constructString(ErrorConstants.ERROR_ACCOUNT_DISABLED));
                throw new BadCredentialsException(ErrorConstants.ERROR_ACCOUNT_DISABLED);
            }
            if (!forgotPasswordLimitExceed) {
                String jwtToken;
                try {
                    jwtToken = forgotPasswordTokenCreation(user);
                    sendEmail(user, jwtToken, isFromCreation, appType);
                    user.setForgetPasswordToken(jwtToken);
                    userRepository.save(user);
                    return Boolean.TRUE;
                } catch (Exception exception) {
                    Logger.logError(String.valueOf(exception));
                }
            }
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    public int getTotalSize() {
        return userRepository.getUsers(Boolean.TRUE).size();
    }

    /**
     * {@inheritDoc}
     */
    public void sendEmail(User user, String jweToken, boolean isFromCreation, String appType) {
        try {
            ResponseEntity<EmailTemplate> emailTemplateResponse = notificationApiInterface.getEmailTemplate(
                    (isFromCreation ? Constants.NEW_USER_CREATION : Constants.FORGOT_PASSWORD_USER), appType);
            if (null == emailTemplateResponse.getBody()) {
                throw new SpiceValidation(3010);
            } else {
                EmailDTO emailDto = new EmailDTO();
                Map<String, String> data = new HashMap<>();
                if (isFromCreation) {
                    emailDto = constructUserCreationEmail(user, jweToken, data, emailDto,
                            emailTemplateResponse.getBody(), appType);
                } else {
                    emailDto = constructForgotEmail(user, jweToken, data, emailDto, emailTemplateResponse.getBody(),
                            appType);
                }
                createOutBoundEmail(emailDto);
            }
        } catch (Exception exception) {
            Logger.logError(String.valueOf(exception));

        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username, Boolean.TRUE);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getRoles().forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                grantedAuthorities);
    }

    /**
     * {@inheritDoc}
     */
    public void clearApiPermissions() {
        authenticationFilter.apiPermissionMap.clear();
    }

    /**
     * {@inheritDoc}
     */
    public User updateOrganizationUser(User user, boolean isSiteUser, boolean isRedRisk) {
        if (Objects.isNull(user.getId())) {
            throw new DataNotAcceptableException(1016);
        }
        User existingUser = userRepository.findByIdAndTenantIdAndIsActiveTrue(user.getId(), user.getTenantId());
        if (Objects.isNull(existingUser)) {
            throw new DataNotFoundException(1010);
        }
        if (isSiteUser) {
            redRiskUserUpdate(existingUser, isRedRisk);
        }
        userMapper.setExistingUser(user, existingUser);
        return userRepository.save(existingUser);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean deleteOrganizationUser(CommonRequestDTO requestData) {
        Long userId = requestData.getId();
        Long tenantId = requestData.getTenantId();
        if (Objects.isNull(userId)) {
            throw new DataNotAcceptableException(1016);
        }
        User existingUser = userRepository.findByIdAndIsActiveTrue(userId);
        if (Objects.isNull(existingUser)) {
            throw new DataNotFoundException(1010);
        }
        existingUser.getOrganizations().removeIf(organization -> Objects.equals(organization.getId(), tenantId));
        if (existingUser.getOrganizations().isEmpty()) {
            userTokenService.deleteUserTokenByUserName(existingUser.getUsername(),
                    existingUser.getId());
            existingUser.setDeleted(Constants.BOOLEAN_TRUE);
        } else {
            if (Objects.equals(tenantId, existingUser.getTenantId())) {
                Optional<Organization> optionalExistingUserValue = existingUser.getOrganizations().stream().findFirst();
                optionalExistingUserValue.ifPresent(organization -> existingUser.setTenantId(organization.getId()));
            }
        }
        userRepository.save(existingUser);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public UserListDTO validateUser(RequestDTO requestData) {
        if (requestData.getEmail().isBlank()) {
            throw new DataNotAcceptableException(5003);
        }
        User user = userRepository.findByUsernameAndIsDeletedFalse(requestData.getEmail().toLowerCase());
        UserListDTO userDTO = null;
        if (!Objects.isNull(user)) {
            if (!Objects.isNull(requestData.getParentOrganizationId()) && (user.getOrganizations().stream()
                    .map(Organization::getId).toList().contains(requestData.getIgnoreTenantId()))) {
                throw new DataConflictException(1103);
            }
            if (!Objects.isNull(requestData.getParentOrganizationId())) {
                organizationService.validateParentOrganization(requestData.getParentOrganizationId(), user);
            }
            userDTO = modelMapper.map(user, UserListDTO.class);
            String defaultRoleName = userDTO.getDefaultRoleName();
            if (null != defaultRoleName && Constants.SPICE_EMR_ROLES.containsValue(defaultRoleName)) {
                throw new BadRequestException(1105);
            }
        }
        return userDTO;
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUserBySearchTermAndTenantId(Long tenantId, String searchTerm) {
        return userRepository.findUserBySearchTermAndTenantId(searchTerm, tenantId);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getSuperAdminUsers(PaginateDTO searchRequest) {
        Pageable pageable = null;
        String searchTerm = null;
        if (!Objects.isNull(searchRequest)) {
            searchTerm = searchRequest.getSearchTerm();
            if (!CommonUtil.isValidSearchData(searchTerm, Constants.USER_SEARCH_TERM)) {
                return new ResponseListDTO(new ArrayList<>(), 0L);
            }
            pageable = Pagination.setPagination(searchRequest.getSkip(), searchRequest.getLimit(), Constants.UPDATED_AT,
                    Constants.BOOLEAN_FALSE);
        }
        List<UserSuperAdminDto> usersResponse = new ArrayList<>();
        Page<User> usersList = userRepository.searchSuperAdminUsersWithPagination(Constants.ROLE_SUPER_ADMIN,
                searchTerm, pageable);
        if (!Objects.isNull(usersList)) {
            usersResponse = usersList.stream().map(user -> modelMapper.map(user, UserSuperAdminDto.class))
                    .toList();
        }
        return new ResponseListDTO(usersResponse, Objects.isNull(usersList) ? 0 : usersList.getTotalElements());
    }

    /**
     * {@inheritDoc}
     */
    public void removeSuperAdmin(Long id) {
        if (Objects.isNull(id)) {
            throw new DataNotAcceptableException(1102);
        }
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException(1102));
        if (user.isDeleted()) {
            throw new DataConflictException(5004);
        }
        user.setDeleted(Constants.BOOLEAN_TRUE);
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    public void updateSuperAdmin(UserSuperAdminDto userDto) {
        if (Objects.isNull(userDto)) {
            throw new DataNotAcceptableException(20004);
        }
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new DataNotFoundException(1102));
        user = userMapper.setSuperAdminUser(userDto, user);
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    public UserSuperAdminDto getSuperAdminById(Long id) {
        if (Objects.isNull(id)) {
            throw new DataNotAcceptableException(1102);
        }
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException(1102));
        return modelMapper.map(user, UserSuperAdminDto.class);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean activateDeactivateUser(List<Long> tenantIds, boolean isActive) {
        List<User> users = userRepository.findUsersByTenantIds(tenantIds, !isActive);
        if (!users.isEmpty()) {
            users.forEach(user -> user.setActive(isActive));
            users = userRepository.saveAll(users);
        }
        return null != users;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getAdminUsers(SearchRequestDTO requestDto) {
        ResponseListDTO response = new ResponseListDTO();
        String searchTerm = requestDto.getSearchTerm().strip();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.USER_SEARCH_TERM)) {
            response.setTotalCount(0L);
            return response;
        }
        Organization organization = organizationService.getOrganizationById(requestDto.getTenantId());
        Page<User> users = null;
        Map<String, List<Long>> childIds;
        if (!Objects.isNull(organization)) {
            childIds = organizationService.getChildOrganizations(requestDto.getTenantId(), organization.getFormName());
            List<Long> tenantIds = childIds.values().stream().flatMap(List::stream).collect(Collectors.toList());
            tenantIds.add(requestDto.getTenantId());
            Pageable pageable = Pagination.setPagination(requestDto.getSkip(), requestDto.getLimit(),
                    Constants.UPDATED_AT, Constants.BOOLEAN_FALSE);
            users = userRepository.searchUsersByTenantIds(tenantIds, searchTerm,
                    requestDto.getUserType(), pageable);
        }
        if (!Objects.isNull(users) && !users.isEmpty()) {
            response.setData(users.stream().map(user -> modelMapper.map(user, UserListDTO.class))
                    .toList());
            response.setTotalCount(users.getTotalElements());
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    public Map<Long, User> getUserByIds(Set<Long> ids) {
        List<User> users = userRepository.findByIdIn(ids);
        Map<Long, User> userMap = new HashMap<>();
        users.forEach(user -> userMap.put(user.getId(), user));
        return userMap;
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUserByRoleName(String roleName) {
        return userRepository.getUsersByRoleName(roleName);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> verifySetPassword(String token) {
        validateToken(token);
        Boolean isPasswordSet = Constants.BOOLEAN_FALSE;
        return Map.of(Constants.IS_PASSWORD_SET, isPasswordSet);
    }

    /**
     * {@inheritDoc}
     */
    public void unlockUser(CommonRequestDTO requestDto) {
        if (Objects.isNull(requestDto.getId())) {
            throw new DataNotAcceptableException(1016);
        }
        User user = userRepository.findByIdAndIsDeletedFalse(requestDto.getId());
        if (Objects.isNull(user)) {
            throw new DataNotFoundException(1010);
        }
        if (Boolean.FALSE.equals(user.getIsBlocked())) {
            throw new BadRequestException(1018);
        }
        user.setIsBlocked(Constants.BOOLEAN_FALSE);
        user.setInvalidLoginAttempts(Constants.ZERO);
        user.setActive(Constants.BOOLEAN_TRUE);
        user.setBlockedDate(null);
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getLockedUsers(RequestDTO requestObject) {
        String searchTerm = requestObject.getSearchTerm();
        long totalCount = 0L;
        List<UserOrganizationDTO> lockedUsersDTOS = new ArrayList<>();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.USER_SEARCH_TERM)) {
            return new ResponseListDTO(lockedUsersDTOS, totalCount);
        }
        Pageable pageable = Pagination.setPagination(requestObject.getSkip(), requestObject.getLimit(),
                Constants.UPDATED_AT, Constants.BOOLEAN_FALSE);
        Page<User> lockedUsers = null;
        List<Long> tenantIdList;
        if (Objects.isNull(requestObject.getTenantId())) {
            lockedUsers = userRepository.getLockedUsers(searchTerm, pageable);
        } else {
            Organization organization = organizationService.getOrganizationById(requestObject.getTenantId());
            if (!Objects.isNull(organization)) {
                Map<String, List<Long>> childIds = organizationService
                        .getChildOrganizations(requestObject.getTenantId(), organization.getFormName());
                tenantIdList = childIds.values().stream().flatMap(List::stream).collect(Collectors.toList());
                tenantIdList.add(requestObject.getTenantId());
                lockedUsers = userRepository.getLockedUsers(searchTerm, pageable, tenantIdList);
            }
        }
        if (!Objects.isNull(lockedUsers)) {
            totalCount = lockedUsers.getTotalElements();
            lockedUsersDTOS = lockedUsers.stream().map(user -> modelMapper.map(user, UserOrganizationDTO.class))
                    .toList();
        }
        return new ResponseListDTO(lockedUsersDTOS, totalCount);
    }

    /**
     * {@inheritDoc}
     */
    public void changeSiteUserPassword(Long userId, String newPassword) {
        if (Objects.isNull(userId) || Objects.isNull(newPassword)) {
            throw new DataNotAcceptableException(1021);
        }
        User user = userRepository.findByIdAndIsDeletedFalse(userId);
        if (Objects.isNull(user)) {
            throw new DataNotAcceptableException(1010);
        }
        Organization organization = user.getOrganizations().iterator().next();
        if (!organization.getFormName().equals(Constants.SITE)) {
            throw new DataNotAcceptableException(1021);
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> setUserPassword(String token, Map<String, String> userInfo) {
        if (Objects.isNull(userInfo.get(FieldConstants.PASSWORD))) {
            throw new DataNotAcceptableException(1019);
        }
        User user = validateToken(token);
        if (Objects.isNull(user.getPassword()) || user.getPassword().isBlank()) {
            user.setPassword(userInfo.get(FieldConstants.PASSWORD));
            user.setForgetPasswordToken(null);
            userRepository.save(user);
            return Map.of(Constants.IS_PASSWORD_SET, Constants.BOOLEAN_FALSE);
        }
        return Map.of(Constants.IS_PASSWORD_SET, Constants.BOOLEAN_TRUE);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> resetUserPassword(String token, Map<String, String> userInfo) {
        if (Objects.isNull(userInfo.get(FieldConstants.PASSWORD))) {
            throw new DataNotAcceptableException(1019);
        }
        User user = validateToken(token);
        if (!Objects.isNull(user.getPassword()) && user.getPassword().equals(userInfo.get(FieldConstants.PASSWORD))) {
            Logger.logError(StringUtil.constructString(ErrorConstants.SAME_PASSWORD));
            throw new SpiceValidation(1012);
        }
        user.setPassword(userInfo.get(FieldConstants.PASSWORD));
        userRepository.save(user);
        return Map.of(Constants.IS_PASSWORD_SET, Constants.BOOLEAN_TRUE);
    }

    /**
     * {@inheritDoc}
     */
    public void createSuperAdmin(List<User> usersList) {
        validateUsers(usersList.stream().map(User::getUsername).toList());
        Role role = roleService.getRoleByName(Constants.ROLE_SUPER_ADMIN);
        usersList.forEach(user -> {
            user.getRoles().add(role);
            user.setTenantId(null);
        });
        List<User> userResponse = userRepository.saveAll(usersList);
        for (User user : userResponse) {
            forgotPassword(user.getUsername(), user, Boolean.TRUE, Constants.SPICE);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addUsers(List<User> users, List<String> newUsernames) {
        List<User> userResponse = userRepository.saveAll(users);
        if (enableFhir) {
            setAndSendUserRequest(userResponse);
        }
        for (User user : userResponse) {
            if (!Objects.isNull(newUsernames) && newUsernames.contains(user.getUsername())) {
                forgotPassword(user.getUsername(), user, Boolean.TRUE, Constants.SPICE);
            }
        }
    }

    /**
     * set values to FhirSiteRequest and send to rabbitmq server.
     *
     * @param userResponse list of users.
     *
     */
    private void setAndSendUserRequest(List<User> userResponse) {
        try {
            FhirUserRequestDTO userRequestDTO = new FhirUserRequestDTO();
            userRequestDTO.setUsers(userResponse);
            userRequestDTO.setType(Constants.FHIR_USER_DATA);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(userRequestDTO);
            String deduplicationId = UniqueCodeGenerator.generateUniqueCode(jsonString);
            Map<String, Object> message = new HashMap<>();
            message.put(Constants.DEDUPLICATION_ID, deduplicationId);
            message.put(Constants.BODY,jsonString);
            String jsonMessage = objectMapper.writeValueAsString(message);
            Logger.logDebug(Constants.USER_LOGGER + jsonMessage);
            rabbitTemplate.convertAndSend(exchange,routingKey,jsonMessage);
        } catch (JsonProcessingException jsonException) {
            Logger.logError(Constants.OBJECT_TO_STRING_LOGGER ,jsonException);
        } catch (AmqpException amqpException) {
            Logger.logError(Constants.RABBIT_MQ_LOGGER, amqpException);
        } catch (Exception e){
            System.out.println(e);
            Logger.logError(Constants.ERROR_LOGGER + e);
        }

    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUserListByRole(CommonRequestDTO requestDto) {
        List<String> roleNames = Objects.isNull(requestDto.getRoleNames())
                ? new ArrayList<>(Arrays.asList(Constants.ROLE_PROVIDER, Constants.ROLE_PHYSICIAN_PRESCRIBER))
                : requestDto.getRoleNames();
        return userRepository.findUsersByRoleNames(roleNames, requestDto.getTenantId(), requestDto.getSearchTerm());
    }

    /**
     * {@inheritDoc}
     */
    public void updateCulture(CultureRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new DataNotFoundException(1010));
        user.setCultureId(requestDTO.getCultureId());
        userRepository.save(user);
    }

    /**
     * <p>
     * The method is used to check if the user has exceeded the limit for forgot password attempts and
     * blocks the user if necessary.
     * </p>
     *
     * @param user           {@link User} The user object for which the forgot password limit needs to be checked.
     * @param isFromCreation The indication, whether the user is being created for the first time or not is given
     * @return {@link Boolean} A boolean value is returned whether the forgot password limit exceeds or not
     */
    private Boolean checkForgotPasswordLimitExceed(User user, boolean isFromCreation) {
        int forgotPasswordCount = user.getForgetPasswordCount();
        Date forgotPasswordTime = DateUtil.formatDate(user.getForgetPasswordTime());
        Date currentDate = DateUtil.formatDate(new Date());
        long getDateDiffInMinutes = DateUtil.getDateDiffInMinutes(forgotPasswordTime, currentDate);
        if (getDateDiffInMinutes >= forgotPasswordTimeLimitInMinutes) {
            user.setForgetPasswordTime(currentDate);
            user.setForgetPasswordCount(Constants.ONE);
            user.setIsBlocked(Boolean.FALSE);
        } else {
            if (Constants.ZERO == forgotPasswordCount) {
                user.setForgetPasswordTime(currentDate);
            }
            if (forgotPasswordCount < forgotPasswordCountLimit && forgotPasswordCount >= 0 && !isFromCreation) {
                user.setForgetPasswordCount(++forgotPasswordCount);
            }
            if (forgotPasswordCount == forgotPasswordCountLimit) {
                user.setIsBlocked(Boolean.TRUE);
                user.setForgetPasswordCount(Constants.ZERO);
                user.setBlockedDate(currentDate);
                userRepository.save(user);
                return Boolean.TRUE;
            }
        }
        userRepository.save(user);
        return Boolean.FALSE;
    }

    /**
     * <p>
     * The method is used to create a secret key specification using a given signature algorithm and
     * a base64-encoded API key
     * secret.
     * </p>
     *
     * @return The created SecretKeySpec is returned.
     */
    private Key secretKeySpecCreation() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Constants.AES_KEY_TOKEN);
        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }

    /**
     * <p>
     * This method is used to create a JWT token with user information and expiration time.
     * </p>
     *
     * @param user {@link User} The user for whom the forgot password token is being constructed.
     * @return {@link String} A JSON Web Token (JWT) created using the user's information and a
     * secret key is returned
     */
    private String forgotPasswordTokenCreation(User user) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Constants.AES_KEY_TOKEN);
        Key secretKeySpec = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(FieldConstants.USERNAME, user.getUsername());
        JwtBuilder jwtBuilder = Jwts.builder().setClaims(userInfo).signWith(signatureAlgorithm, secretKeySpec);
        return jwtBuilder.setId(String.valueOf(user.getId()))
                .setExpiration(Date.from(ZonedDateTime.now().plusHours(Constants.TWENTY_FOUR).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant())).setIssuer(Constants.ISSUER).compact();
    }

    /**
     * <p>
     * The method is used to construct an email for user creation with a specific email template and data.
     * </p>
     *
     * @param user          {@link User} The user for whom the user creation email is being constructed.
     * @param jwtToken      {@link String} The JWT token is a JSON Web Token is given for authentication and authorization purposes,
     *                      and it contains encoded information about the user and their permissions
     * @param data          {@link Map} A map containing additional data to be used in constructing the email is given
     * @param emailDto      {@link EmailDTO} The email to construct and to be sent to a
     *                      user after their account has been created is given
     * @param emailTemplate {@link EmailTemplate} The email template that will be used to construct the email message is given
     * @param appType       {@link String} The specification of the type of application is given
     * @return {@link EmailDTO} The constructed user creation email is returned
     */
    private EmailDTO constructUserCreationEmail(User user, String jwtToken, Map<String, String> data, EmailDTO emailDto,
                                                EmailTemplate emailTemplate, String appType) {
        String url = "";
        if (appType.equals(Constants.SPICE)) {
            url = appUrl;
        }
        for (EmailTemplateValue emailTemplateValue : emailTemplate.getEmailTemplateValues()) {
            if (Constants.APP_URL_EMAIL.equalsIgnoreCase(emailTemplateValue.getName())) {
                data.put(Constants.APP_URL_EMAIL, url + jwtToken);
                data.put(Constants.EMAIL, user.getUsername());
                emailTemplateValue.setValue(appUrl + jwtToken);
            }
        }
        return userMapper.setUserCreationEmailTemplate(user, emailTemplate, emailDto, data);
    }

    /**
     * <p>
     * The method is used to construct a forgot password email with a URL containing a JWT token and expiration time.
     * </p>
     *
     * @param user          {@link User} The user for whom the forgot password email is being constructed.
     * @param jwtToken      {@link String} A JSON Web Token (JWT) is given for authentication and authorization purposes,
     *                      and it is likely generated when a user requests a password reset.
     * @param data          {@link Map} A map containing additional data to be used in the email template is given
     * @param emailDto      {@link EmailDTO} The email to construct and sent to the user is given
     * @param emailTemplate {@link EmailTemplate} The email template to be used for constructing the forgot
     *                      password email is given
     * @param appType       {@link String} The specification of the type of application (either "SPICE" or "CFR") is given
     * @return {@link EmailDTO} The constructed forgot email is returned
     */
    private EmailDTO constructForgotEmail(User user, String jwtToken, Map<String, String> data, EmailDTO emailDto,
                                          EmailTemplate emailTemplate, String appType) {
        String url = "";
        if (appType.equals(Constants.SPICE)) {
            url = appUrl;
        }
        StringBuilder appUrlWithTime = new StringBuilder().append(url).append(jwtToken).append(Constants.EXPIRES)
                .append(ZonedDateTime.now(ZoneId.of(FieldConstants.UTC)).plusMinutes(forgotPasswordTimeLimitInMinutes)
                        .toInstant().toEpochMilli())
                .append(Constants.RESET_PASSWORD);

        for (EmailTemplateValue emailTemplateValue : emailTemplate.getEmailTemplateValues()) {
            if (Constants.APP_URL_EMAIL.equals(emailTemplateValue.getName())) {
                emailTemplateValue.setValue(appUrlWithTime.toString());
                data.put(Constants.APP_URL_EMAIL, appUrlWithTime.toString());
            } else if (FieldConstants.FORGET_PASSWORD_TOKEN.equals(emailTemplateValue.getName())) {
                emailTemplateValue.setValue(jwtToken);
                data.put(FieldConstants.FORGET_PASSWORD_TOKEN, jwtToken);
            }
        }
        return userMapper.setForgotPasswordEmailTemplate(emailTemplate, mailUser, user, emailDto, data);
    }

    /**
     * <p>
     * The method is used to create an outbound email using the data from a given EmailDTO object.
     * </p>
     *
     * @param emailDto {@link EmailDTO} The details to create outbound email is given
     */
    private void createOutBoundEmail(EmailDTO emailDto) {
        OutBoundEmailDTO outBoundEmailDto = modelMapper.map(emailDto, OutBoundEmailDTO.class);
        ResponseEntity<Boolean> emailResponse = notificationApiInterface.createOutBoundEmail(outBoundEmailDto);
        if (Boolean.FALSE.equals(emailResponse.getBody())) {
            throw new SpiceValidation(3011);
        }
    }

    /**
     * <p>
     * The method is used to update a user's role to include or exclude the "red risk user"
     * role based on a boolean value.
     * </p>
     *
     * @param existingUser {@link User} The user who needs to be updated is given
     * @param isRedRisk    A boolean value indicating whether the user is now considered a red risk user or not.
     */
    public void redRiskUserUpdate(User existingUser, boolean isRedRisk) {
        boolean isRedRiskUser = existingUser.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Constants.ROLE_RED_RISK_USER));

        if (isRedRisk) {
            if (!isRedRiskUser) {
                Role role = roleService.getRoleByName(Constants.ROLE_RED_RISK_USER);
                existingUser.getRoles().add(role);
            }
        } else {
            if (isRedRiskUser) {
                existingUser.getRoles().removeIf(role -> role.getName().equalsIgnoreCase(Constants.ROLE_RED_RISK_USER));
            }
        }
    }

    /**
     * <p>
     * The method is used to validate a token, checks if it has expired, and returns the corresponding user.
     * </p>
     *
     * @param token {@link String} The token to identify the user and to validate is given
     * @return {@link User} The user for the given token is validated and returned
     */
    private User validateToken(String token) {
        User user = userRepository.findByForgetPasswordToken(token);
        if (Objects.isNull(user)) {
            throw new DataNotFoundException(3012);
        }
        user.setForgetPasswordToken(null);
        Key secretKeySpec = secretKeySpecCreation();
        Claims body = Jwts.parser().setSigningKey(secretKeySpec).parseClaimsJws(token).getBody();

        if (ZonedDateTime.ofInstant(body.getExpiration().toInstant(), ZoneId.of(FieldConstants.UTC))
                .isBefore(ZonedDateTime.now(ZoneId.of(FieldConstants.UTC)))) {
            Logger.logError(StringUtil.constructString(ErrorConstants.LINK_EXPIRED));
            throw new SpiceValidation(3009);
        }
        return user;
    }
}
