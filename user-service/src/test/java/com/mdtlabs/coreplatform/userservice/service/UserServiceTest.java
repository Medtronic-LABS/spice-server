package com.mdtlabs.coreplatform.userservice.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.mdtlabs.coreplatform.AuthenticationFilter;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.exception.SpiceValidation;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.fhir.FhirSiteRequestDTO;
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
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.repository.TimezoneRepository;
import com.mdtlabs.coreplatform.common.service.UserTokenService;
import com.mdtlabs.coreplatform.common.util.DateUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.common.util.UniqueCodeGenerator;
import com.mdtlabs.coreplatform.userservice.NotificationApiInterface;
import com.mdtlabs.coreplatform.userservice.mapper.UserMapper;
import com.mdtlabs.coreplatform.userservice.repository.UserRepository;
import com.mdtlabs.coreplatform.userservice.service.impl.OrganizationServiceImpl;
import com.mdtlabs.coreplatform.userservice.service.impl.UserServiceImpl;
import com.mdtlabs.coreplatform.userservice.util.TestConstants;
import com.mdtlabs.coreplatform.userservice.util.TestDataProvider;

/**
 * <p>
 * User Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 09, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TimezoneRepository timezoneRepository;

    private ModelMapper modelMapper;

    @Mock
    private RoleService roleService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private OrganizationServiceImpl organizationService;

    @Mock
    private UserTokenService userTokenService;

    @Mock
    private AuthenticationFilter authenticationFilter;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private NotificationApiInterface notificationApiInterface;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestDataProvider.setUp(UserServiceImpl.class, "modelMapper", userService);
    }

    @Mock
    private DateUtil dateUtil;

    @Test
    void testAddUser() {
        //given
        User user = TestDataProvider.getUser();

        //when
        when(userRepository.save(user)).thenReturn(user);

        //then
        User userResponse = userService.addUser(user);
        Assertions.assertEquals(TestConstants.FIRST_NAME, userResponse.getFirstName());
    }

    @Test
    void toVerifyAddUser() {
        //given
        User user = new User();
        user.setId(TestConstants.ZERO);
        user.setRoles(TestDataProvider.getRolesSet());
        user.setUsername(TestConstants.USER_NAME);
        ReflectionTestUtils.setField(userService, "enableFhir", true);
        ReflectionTestUtils.setField(userService, "exchange", "exchange");
        ReflectionTestUtils.setField(userService, "routingKey", "key");
        String jsonMessage = user.toString();
        //when

        when(userRepository.findByUsernameAndIsDeletedFalse(user.getUsername())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.getUserByUsername(TestConstants.USER_NAME, Boolean.TRUE)).thenReturn(null);
        MockedConstruction<ObjectMapper> objectMapperMockedConstruction =
                Mockito.mockConstruction(ObjectMapper.class, (objectMapper, context) -> {
                    when(objectMapper.writeValueAsString(any(FhirSiteRequestDTO.class))).thenReturn(jsonMessage);
                });
        MockedStatic<UniqueCodeGenerator> uniqueCodeGeneratorMockedStatic = Mockito.mockStatic(UniqueCodeGenerator.class);
        uniqueCodeGeneratorMockedStatic.when(() -> UniqueCodeGenerator.generateUniqueCode(jsonMessage)).thenReturn(Constants.DEDUPLICATION_ID);
        doNothing().when(rabbitTemplate).convertAndSend("exchange", "keyName", jsonMessage);


        //then
        User userResponse = userService.addUser(user);
        objectMapperMockedConstruction.close();
        uniqueCodeGeneratorMockedStatic.close();
        Assertions.assertEquals(user.getUsername(), userResponse.getUsername());
    }

    @Test
    void checkNull() {
        //given
        User user = new User();

        //when

        when(userRepository.findByUsernameAndIsDeletedFalse(user.getUsername())).thenReturn(null);

        //then
        Assertions.assertThrows(SpiceValidation.class, () -> userService.addUser(user));
    }

    @Test
    void testGetUsers() {
        //given
        ReflectionTestUtils.setField(userService, TestConstants.GRID_DISPLAY_VALUE, TestConstants.TEN);
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN);
        List<User> usersList = TestDataProvider.getUsers();
        Page<User> users = new PageImpl<>(usersList);

        //when
        when(userRepository.getUsers(Boolean.TRUE, pageable)).thenReturn(users);

        //then
        List<User> actualUsers = userService.getUsers(Constants.ONE);
        assertNotNull(actualUsers);
        assertFalse(actualUsers.isEmpty());
        assertEquals(usersList.get(Constants.ZERO), actualUsers.get(Constants.ZERO));
    }

    @Test
    void testGetUsersWithNull() {
        //given
        ReflectionTestUtils.setField(userService, TestConstants.GRID_DISPLAY_VALUE, TestConstants.TEN);
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN);

        //when
        when(userRepository.getUsers(Boolean.TRUE, pageable)).thenReturn(null);

        //then
        List<User> actualUsers = userService.getUsers(Constants.ONE);
        assertNotNull(actualUsers);
        assertTrue(actualUsers.isEmpty());
    }

    @Test
    void SpiceValidation() {
        //given
        Role role = TestDataProvider.getRole();
        User user = new User();
        user.setId(0L);
        user.setPassword(TestConstants.PASSWORD);
        user.setFirstName(TestConstants.FIRST_NAME);
        user.setRoles(Set.of(role));

        //when
        when(userRepository.findByUsernameAndIsDeletedFalse(user.getUsername())).thenReturn(user);

        //then
        assertThrows(SpiceValidation.class, () -> userService.addUser(user));
    }

    @Test
    void testUpdateUser() {
        //given
        User user = TestDataProvider.getUser();
        user.setFirstName(user.getFirstName());
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(userRepository.getUserById(TestConstants.ONE, Boolean.TRUE)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        //then
        User userResponse = userService.updateUser(user);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(TestConstants.ONE, userResponse.getId());
        assertNotNull(user);
    }

    @Test
    void toVerifyUpdateUser() {
        //given
        User user = new User();
        user.setId(TestConstants.TWO);
        user.setPassword(TestConstants.PASSWORD);
        user.setFirstName(TestConstants.FIRST_NAME);
        user.setLastName(TestConstants.LAST_NAME);
        user.setPhoneNumber(TestConstants.PHONE_NUMBER);
        user.setUsername(TestConstants.USER_NAME);
        TestDataProvider.init();

        //when
        when(userRepository.getUserById(TestConstants.TWO, Boolean.TRUE)).thenReturn(user);
        TestDataProvider.getStaticMock();

        //then
        assertThrows(SpiceValidation.class, () -> userService.updateUser(user));
        assertNotNull(user);
        TestDataProvider.cleanUp();
    }

    @Test
    void testDataNotAcceptableException() {
        //given
        User user = new User();
        User existingUser = TestDataProvider.getUser();
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setEmail(" ");
        CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
        Map<String, String> userInfo = new HashMap<>();

        //when
        when(userRepository.findByIdAndIsDeletedFalse(TestConstants.TWO)).thenReturn(null);
        when(userRepository.findByIdAndIsDeletedFalse(TestConstants.FIVE)).thenReturn(existingUser);

        //then
        assertThrows(DataNotAcceptableException.class, () -> userService.updateUser(user));
        assertThrows(DataNotAcceptableException.class, () -> userService.getSuperAdminById(null));
        assertThrows(DataNotAcceptableException.class, () -> userService.validateUser(requestDTO));
        assertThrows(DataNotAcceptableException.class, () -> userService.deleteOrganizationUser(commonRequestDTO));
        assertThrows(DataNotAcceptableException.class, () -> userService.updateOrganizationUser(user, Boolean.FALSE,
                Boolean.FALSE));
        assertThrows(DataNotAcceptableException.class, () -> userService.removeSuperAdmin(null));
        assertThrows(DataNotAcceptableException.class, () -> userService.updateSuperAdmin(null));
        assertThrows(DataNotAcceptableException.class, () -> userService.changeSiteUserPassword(null, TestConstants.PASSWORD));
        assertThrows(DataNotAcceptableException.class, () -> userService.changeSiteUserPassword(TestConstants.ONE, null));
        assertThrows(DataNotAcceptableException.class, () -> userService.changeSiteUserPassword(TestConstants.TWO, TestConstants.PASSWORD));
        assertThrows(DataNotAcceptableException.class, () -> userService.changeSiteUserPassword(TestConstants.FIVE, TestConstants.PASSWORD));
        assertThrows(DataNotAcceptableException.class, () -> userService.resetUserPassword(TestConstants.TOKEN, userInfo));
        assertThrows(DataNotAcceptableException.class, () -> userService.setUserPassword(TestConstants.TOKEN, userInfo));
    }

    @Test
    void testDeleteUserById() {
        //given
        User user = TestDataProvider.getUser();
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(userRepository.getUserById(user.getId(), Boolean.TRUE)).thenReturn(user);
        doNothing().when(userTokenService).deleteUserTokenByUserName(user.getUsername(), user.getId());
        when(userRepository.save(user)).thenReturn(null);

        //then
        boolean deletedUsers = userService.deleteUserById(TestConstants.ONE);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(Boolean.TRUE, deletedUsers);
        Assertions.assertNull(null);
    }

    @Test
    void testDeleteUserException() {

        UserDTO userDto = new UserDTO();
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);

        //when
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDto);

        assertThrows(SpiceValidation.class, () -> userService.deleteUserById(TestConstants.ONE));
        userContextHolder.close();
    }

    @Test
    void testDeleteUserWithNull() {
        User user = null;
        TestDataProvider.init();

        TestDataProvider.getStaticMock();

        when(userService.getUserById(TestConstants.ONE)).thenReturn(null);
        Assertions.assertEquals(Boolean.FALSE, userService.deleteUserById(TestConstants.ONE));
        TestDataProvider.cleanUp();

    }

    @Test
    void testGetUserById() {
        //given
        User user = TestDataProvider.getUser();

        //when
        when(userRepository.getUserById(user.getId(), Boolean.TRUE)).thenReturn(user);

        //then
        User userResponse = userService.getUserById(user.getId());
        Assertions.assertEquals(TestConstants.FIRST_NAME, userResponse.getFirstName());
    }

    @Test
    void testGetUserByUserName() {
        //given
        User user = TestDataProvider.getUser();

        //when
        when(userRepository.getUserByUsername(user.getUsername(), Boolean.TRUE)).thenReturn(user);

        //then
        User userResponse = userService.getUserByUsername(user.getUsername());
        Assertions.assertEquals(TestConstants.FIRST_NAME, userResponse.getFirstName());
    }

    @Test
    void testGetUserByTenantIds() {
        //given
        List<Long> tenantIds = TestDataProvider.getTenantIds();
        List<User> users = TestDataProvider.getUsers();
        List<UserDTO> userDTOS = TestDataProvider.getUserDTOS();

        //when
        when(userRepository.findUsersByTenantIds(tenantIds, Boolean.TRUE)).thenReturn(users);
        when(modelMapper.map(users, new TypeToken<List<UserOrganizationDTO>>() {
        }.getType())).thenReturn(userDTOS);

        //then
        List<UserOrganizationDTO> responses = userService.getUsersByTenantIds(tenantIds);
        Assertions.assertEquals(users.size(), responses.size());
        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    void testGetUserByTenantIdsWithNull() {
        //given
        List<Long> tenantIds = TestDataProvider.getTenantIds();

        //when
        when(userRepository.findUsersByTenantIds(tenantIds, Boolean.TRUE)).thenReturn(null);

        //then
        List<UserOrganizationDTO> responses = userService.getUsersByTenantIds(tenantIds);
        assertNull(responses);
    }

    @Test
    void testValidateUsers() {
        //given
        List<String> newUserEmails = new ArrayList<>();
        List<User> users = TestDataProvider.getUsers();

        //when
        when(userRepository.findByUsernameInAndIsDeletedFalse(newUserEmails)).thenReturn(users);

        //then
        assertThrows(DataConflictException.class, () -> userService.validateUsers(newUserEmails));
    }

    @Test
    void testForgotPassword() {

        //when
        when(userRepository.getUserByUsername(TestConstants.USER_NAME, Boolean.TRUE)).thenReturn(null);

        //then
        Boolean booleanResponse = userService.forgotPassword(TestConstants.USER_NAME, null, Boolean.TRUE,
                Constants.SPICE);
        Assertions.assertEquals(Boolean.TRUE, booleanResponse);
    }

    @Test
    void testForgotPasswordWithBadRequestException() {
        User user = new User();
        String appType = null;
        String email = TestConstants.EMAIL;

        assertThrows(BadRequestException.class, () -> userService.forgotPassword(email, user, false, null));

    }

    @Test
    void BadCredentialsException() {
        //given
        User user = new User();
        user.setId(TestConstants.ONE);
        user.setPassword(TestConstants.PASSWORD);
        user.setFirstName(TestConstants.FIRST_NAME);
        user.setLastName(TestConstants.LAST_NAME);
        user.setPhoneNumber(TestConstants.PHONE_NUMBER);
        user.setUsername(TestConstants.USER_NAME);
        user.setForgetPasswordCount(Constants.ONE);
        user.setForgetPasswordTime(new Date());
        user.setInvalidLoginAttempts(Constants.ONE);
        user.setTenantId(TestConstants.FIVE);
        user.setCountryCode(TestConstants.COUNTRY_CODE);
        user.setIsBlocked(Boolean.TRUE);
        ReflectionTestUtils.setField(userService, "forgotPasswordTimeLimitInMinutes", 99999999);
        //when
        when(userRepository.save(user)).thenReturn(user);

        //then
        assertThrows(BadCredentialsException.class, () -> userService.forgotPassword(TestConstants.USER_NAME, user, Boolean.TRUE, Constants.SPICE));
    }

    @Test
    void testGetTotalSize() {
        //given
        List<User> users = TestDataProvider.getUsers();

        //when
        when(userRepository.getUsers(Boolean.TRUE)).thenReturn(users);

        //then
        int totalSize = userService.getTotalSize();
        Assertions.assertEquals(users.size(), totalSize);
    }

    @Test
    void testLoadUserByUserName() {
        //given
        User user = TestDataProvider.getUser();

        //when
        when(userRepository.getUserByUsername(TestConstants.USER_NAME, Boolean.TRUE)).thenReturn(user);

        //then
        UserDetails userDetails = userService.loadUserByUsername(TestConstants.USER_NAME);
        Assertions.assertEquals(user.getUsername(), userDetails.getUsername());
    }

    @Test
    void testGetSuperAdminUsers() {
        //given
        Role role = new Role();
        role.setId(TestConstants.ONE);
        role.setName(Constants.ROLE_SUPER_ADMIN);
        role.setLevel(TestConstants.LEVEL);
        List<User> usersList = TestDataProvider.getUsers();
        PaginateDTO paginateDTO = TestDataProvider.getPaginateDTO();
        Pageable pageable = Pagination.setPagination(paginateDTO.getSkip(), paginateDTO.getLimit(),
                Constants.UPDATED_AT, Constants.BOOLEAN_FALSE);
        Page<User> users = new PageImpl<>(usersList);

        //when
        when(roleService.getRoleByName(Constants.ROLE_SUPER_ADMIN)).thenReturn(role);
        when(userRepository.searchSuperAdminUsersWithPagination(Constants.ROLE_SUPER_ADMIN, paginateDTO.getSearchTerm(),
                pageable)).thenReturn(users);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());

        //then
        ResponseListDTO superAdminResponse = userService.getSuperAdminUsers(paginateDTO);
        Assertions.assertEquals(Constants.ONE, superAdminResponse.getTotalCount());
        assertNotNull(role);
    }

    @Test
    void testGetSuperAdminWithEmptyValues() {
        TestDataProvider.init();
        TestDataProvider.getStaticMockValidationFalse(TestConstants.INVALID_SEARCH_TERM);
        PaginateDTO paginateDTO = TestDataProvider.getPaginateDTO();
        paginateDTO.setSearchTerm(TestConstants.INVALID_SEARCH_TERM);
        ResponseListDTO response = new ResponseListDTO(new ArrayList<>(), 0L);

        ResponseListDTO superAdminResponse = userService.getSuperAdminUsers(paginateDTO);
        Assertions.assertEquals(superAdminResponse, response);
        TestDataProvider.cleanUp();
    }

    @Test
    void testRemoveSuperAdmin() {
        //given
        User user = TestDataProvider.getUser();
        user.setDeleted(Boolean.FALSE);

        //when
        when(userRepository.findById(TestConstants.ONE)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        //then
        userService.removeSuperAdmin(TestConstants.ONE);
        verify(userRepository, atLeastOnce()).findById(TestConstants.ONE);
        verify(userRepository, atLeastOnce()).save(user);
    }

    @Test
    void toVerifyRemoveSuperAdmin() {
        //given
        User user = TestDataProvider.getUser();
        user.setDeleted(Boolean.TRUE);

        //when
        when(userRepository.findById(TestConstants.ONE)).thenReturn(Optional.of(user));

        //then
        assertThrows(DataConflictException.class, () -> userService.removeSuperAdmin(TestConstants.ONE));
    }

    @Test
    void testUpdateSuperAdmin() {
        //given
        User user = TestDataProvider.getUser();
        UserSuperAdminDto userSuperAdminDto = TestDataProvider.getUserSuperAdminDto();
        Timezone timezone = TestDataProvider.getTimezone();

        //when
        when(userRepository.findById(userSuperAdminDto.getId())).thenReturn(Optional.of(user));
        when(userMapper.setSuperAdminUser(userSuperAdminDto, user)).thenReturn(user);
        when(timezoneRepository.findById(userSuperAdminDto.getId())).thenReturn(Optional.of(timezone));
        when(userRepository.save(user)).thenReturn(user);

        //then
        userService.updateSuperAdmin(userSuperAdminDto);
        verify(userRepository, atLeastOnce()).findById(userSuperAdminDto.getId());
        verify(userMapper, atLeastOnce()).setSuperAdminUser(userSuperAdminDto, user);
        verify(userRepository, atLeastOnce()).save(user);
    }

    @Test
    void toVerifyUpdateSuperAdmin() {
        //given
        User user = TestDataProvider.getUser();
        UserSuperAdminDto userSuperAdminDto = TestDataProvider.getUserSuperAdminDto();

        //when
        when(userRepository.findById(userSuperAdminDto.getId())).thenReturn(Optional.of(user));
        when(userMapper.setSuperAdminUser(userSuperAdminDto, user)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        //then
        userService.updateSuperAdmin(userSuperAdminDto);
        verify(userRepository, atLeastOnce()).findById(userSuperAdminDto.getId());
        verify(userMapper, atLeastOnce()).setSuperAdminUser(userSuperAdminDto, user);
        verify(userRepository, atLeastOnce()).save(user);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testUpdateOrganizationUser(boolean isSiteUser) {
        //given
        Timezone timezone = new Timezone();
        timezone.setId(TestConstants.ONE);
        User user = TestDataProvider.getUser();
        User existingUser = new User();
        Role role = TestDataProvider.getRole();
        role.setName(Constants.ROLE_RED_RISK_USER);
        user.setTimezone(timezone);

        //when
        when(roleService.getRoleByName(Constants.ROLE_RED_RISK_USER)).thenReturn(role);
        when(userRepository.findByIdAndTenantIdAndIsActiveTrue(user.getId(),
                user.getTenantId())).thenReturn(existingUser);
        doNothing().when(userMapper).setExistingUser(user, existingUser);
        when(userRepository.save(existingUser)).thenReturn(user);

        //then
        User userResponse = userService.updateOrganizationUser(user, isSiteUser, Boolean.TRUE);
        Assertions.assertEquals(user.getFirstName(), userResponse.getFirstName());
        assertNotNull(user);
    }

    @Test
    void testUpdateOrganizationUserWithNull() {
        //given
        User user = TestDataProvider.getUser();

        //when
        when(userRepository.findByIdAndTenantIdAndIsActiveTrue(user.getId(),
                user.getTenantId())).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> userService
                .updateOrganizationUser(user, Boolean.FALSE, Boolean.FALSE));
    }

    @Test
    void testDeleteOrganizationUser() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();
        User user = TestDataProvider.getUser();

        Organization organizationOne = TestDataProvider.getOrganization();
        organizationOne.setId(5L);
        organizationOne.setFormDataId(20L);
        organizationOne.setTenantId(2L);
        user.getOrganizations().add(organizationOne);

        Organization organizationTwo = TestDataProvider.getOrganization();
        organizationTwo.setId(7L);
        organizationTwo.setFormDataId(100L);
        organizationTwo.setTenantId(200L);
        user.getOrganizations().add(organizationTwo);

        //when
        when(userRepository.findByIdAndIsActiveTrue(anyLong())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        //then
        Boolean booleanResponse = userService.deleteOrganizationUser(commonRequestDTO);
        Assertions.assertEquals(Boolean.TRUE, booleanResponse);
    }

    @Test
    void checkDataNotFoundException() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();
        User user = new User();
        user.setId(TestConstants.ONE);
        user.setPassword(TestConstants.PASSWORD);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put(FieldConstants.PASSWORD, TestConstants.PASSWORD);

        //when
        when(userRepository.findByIdAndIsActiveTrue(commonRequestDTO.getId())).thenReturn(null);
        when(userRepository.findByForgetPasswordToken(TestConstants.TOKEN)).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> userService
                .deleteOrganizationUser(commonRequestDTO));
        assertThrows(DataNotFoundException.class, () -> userService.resetUserPassword(TestConstants.TOKEN, userInfo));
    }

    private Method validateToken() throws NoSuchMethodException {
        Method method = UserServiceImpl.class.getDeclaredMethod("validateToken", String.class);
//        Method method2 = ReflectionTestUtils.class.getDeclaredMethod(null, null);
        method.setAccessible(true);
        return method;
    }

    @Test
    void testSetUserPasswordWithException() {
        //given
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put(FieldConstants.PASSWORD, TestConstants.PASSWORD);

        //when
        when(userRepository.findByForgetPasswordToken(TestConstants.TOKEN)).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> userService.setUserPassword(TestConstants.TOKEN, userInfo));
    }

    @Test
    void testValidateUser() {
        //given
        RequestDTO requestData = TestDataProvider.getRequestDTO();
        User user = TestDataProvider.getUser();
        UserListDTO userDTO = TestDataProvider.getUserListDTO();

        //when
        when(userRepository.findByUsernameAndIsDeletedFalse(requestData.getEmail().toLowerCase()))
                .thenReturn(user);
        when(modelMapper.map(user, UserListDTO.class)).thenReturn(userDTO);

        //then
        UserListDTO userDTOResponse = userService.validateUser(requestData);
        Assertions.assertEquals(userDTO.getFirstName(), userDTOResponse.getFirstName());
        Assertions.assertEquals(user.getUsername(), userDTO.getUsername());
        assertNotNull(user);
    }

    @Test
    void testValidateUserWithNull() {
        //given
        RequestDTO requestData = TestDataProvider.getRequestDTO();

        //when
        when(userRepository.findByUsernameAndIsDeletedFalse(requestData.getEmail()))
                .thenReturn(null);

        //then
        UserListDTO userDTO = userService.validateUser(requestData);
        Assertions.assertNull(userDTO);
    }

    @Test
    void toVerifyValidateUser() {
        //given
        RequestDTO requestData = new RequestDTO();
        requestData.setUserId(TestConstants.ONE);
        requestData.setEmail(TestConstants.USER_NAME);
        requestData.setNewPassword(TestConstants.PASSWORD);
        requestData.setParentOrganizationId(TestConstants.FIVE);
        requestData.setIgnoreTenantId(TestConstants.ZERO);
        User user = TestDataProvider.getUser();
        UserListDTO userDTO = TestDataProvider.getUserListDTO();

        //when
        when(userRepository.findByUsernameAndIsDeletedFalse(requestData.getEmail().toLowerCase()))
                .thenReturn(user);
        doNothing().when(organizationService).validateParentOrganization(TestConstants.ONE, user);
        when(modelMapper.map(user, UserListDTO.class)).thenReturn(userDTO);

        //then
        UserListDTO userDTOResponse = userService.validateUser(requestData);
        Assertions.assertEquals(userDTO.getFirstName(), userDTOResponse.getFirstName());
        Assertions.assertEquals(user.getUsername(), userDTO.getUsername());
        assertNotNull(requestData);
    }

    @Test
    void testValidateUserWithException() {
        //given
        RequestDTO requestData = new RequestDTO();
        requestData.setUserId(TestConstants.ONE);
        requestData.setEmail(TestConstants.USER_NAME);
        requestData.setNewPassword(TestConstants.PASSWORD);
        requestData.setParentOrganizationId(TestConstants.FIVE);
        requestData.setIgnoreTenantId(TestConstants.ZERO);
        User user = TestDataProvider.getUser();
        UserListDTO userDTO = new UserListDTO();
        userDTO.setRoles(Set.of(new Role(1L, Constants.EMR_ACCOUNT_ADMIN)));

        //when
        when(userRepository.findByUsernameAndIsDeletedFalse(requestData.getEmail().toLowerCase()))
                .thenReturn(user);
        doNothing().when(organizationService).validateParentOrganization(TestConstants.ONE, user);
        when(modelMapper.map(user, UserListDTO.class)).thenReturn(userDTO);

        //then
        assertThrows(BadRequestException.class, () -> userService.validateUser(requestData));
    }

    @Test
    void checkDataConflictException() {
        //given
        RequestDTO requestData = new RequestDTO();
        requestData.setUserId(TestConstants.ONE);
        requestData.setEmail(TestConstants.USER_NAME);
        requestData.setNewPassword(TestConstants.PASSWORD);
        requestData.setParentOrganizationId(5L);
        requestData.setIgnoreTenantId(TestConstants.ONE);
        User user = TestDataProvider.getUser();
        UserDTO userDTO = TestDataProvider.getUserDTO();

        //when
        when(userRepository.findByUsernameAndIsDeletedFalse(requestData.getEmail().toLowerCase()))
                .thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        //then
        assertNotNull(requestData);
        assertThrows(DataConflictException.class, () -> userService.validateUser(requestData));
    }

    @Test
    void testGetUserBySearchTermAndTenantId() {
        //given
        List<User> users = TestDataProvider.getUsers();

        //when
        when(userRepository.findUserBySearchTermAndTenantId(TestConstants.SEARCH_TERM,
                TestConstants.ONE)).thenReturn(users);

        //then
        List<User> responses = userService.getUserBySearchTermAndTenantId(TestConstants.ONE,
                TestConstants.SEARCH_TERM);
        Assertions.assertEquals(users.size(), responses.size());
    }

    @Test
    void testGetSuperAdminById() {
        //given
        UserSuperAdminDto userSuperAdminDto = TestDataProvider.getUserSuperAdminDto();
        User user = TestDataProvider.getUser();

        //when
        when(userRepository.findById(TestConstants.ONE)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserSuperAdminDto.class)).thenReturn(userSuperAdminDto);

        //then
        UserSuperAdminDto superAdminDto = userService.getSuperAdminById(TestConstants.ONE);
        Assertions.assertEquals(user.getFirstName(), superAdminDto.getFirstName());
        assertNotNull(user);
    }

    @Test
    void testActivateDeactivateUser() {
        //given
        List<Long> tenantIds = TestDataProvider.getTenantIds();
        List<User> users = TestDataProvider.getUsers();

        //when
        when(userRepository.findUsersByTenantIds(tenantIds, Boolean.TRUE)).thenReturn(users);
        when(userRepository.saveAll(users)).thenReturn(users);

        //then
        Boolean booleanResponse = userService.activateDeactivateUser(tenantIds, Boolean.TRUE);
        Assertions.assertEquals(Boolean.TRUE, booleanResponse);
        Assertions.assertFalse(tenantIds.isEmpty());
    }

    @Test
    void testDeactivateUser() {
        //given
        List<Long> tenantIds = TestDataProvider.getTenantIds();
        List<User> users = TestDataProvider.getUsers();

        //when
        when(userRepository.findUsersByTenantIds(tenantIds, Boolean.FALSE)).thenReturn(users);
        when(userRepository.saveAll(users)).thenReturn(users);

        //then
        Boolean booleanResponse = userService.activateDeactivateUser(tenantIds, Boolean.FALSE);
        Assertions.assertEquals(Boolean.TRUE, booleanResponse);
        Assertions.assertFalse(tenantIds.isEmpty());
    }

    @Test
    void toVerifyActivateDeactivateUser() {
        //given
        List<User> users = new ArrayList<>();
        List<Long> tenantIds = TestDataProvider.getTenantIds();

        //when
        when(userRepository.findUsersByTenantIds(tenantIds, Boolean.TRUE)).thenReturn(users);

        //then
        Boolean booleanResponse = userService.activateDeactivateUser(tenantIds, Boolean.FALSE);
        Assertions.assertEquals(Boolean.TRUE, booleanResponse);
        assertTrue(users.isEmpty());
        Assertions.assertFalse(tenantIds.isEmpty());
    }

    @Test
    void testActivateDeactivateUsersWithNull() {
        //given
        List<User> users = TestDataProvider.getUsers();
        List<Long> tenantIds = TestDataProvider.getTenantIds();

        //when
        when(userRepository.findUsersByTenantIds(tenantIds, Boolean.TRUE)).thenReturn(users);
        when(userRepository.saveAll(users)).thenReturn(null);

        //then
        Boolean booleanResponse = userService.activateDeactivateUser(tenantIds, Boolean.FALSE);
        Assertions.assertEquals(Boolean.FALSE, booleanResponse);
    }

    @Test
    void testGetUserByIds() {
        //given
        Set<Long> ids = TestDataProvider.getUserIds();
        List<User> users = TestDataProvider.getUsers();

        //when
        when(userRepository.findByIdIn(ids)).thenReturn(users);

        //then
        Map<Long, User> userIds = userService.getUserByIds(ids);
        Assertions.assertEquals(users.size(), userIds.size());
    }

    @Test
    void testGetUserByRoleName() {
        //given
        List<User> users = TestDataProvider.getUsers();

        //when
        when(userRepository.getUsersByRoleName(TestConstants.ROLE_NAME)).thenReturn(users);

        //then
        List<User> userList = userService.getUserByRoleName(TestConstants.ROLE_NAME);
        Assertions.assertEquals(users.size(), userList.size());
    }

    @Test
    void testGetUserListByRole() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();
        commonRequestDTO.setSearchTerm(Constants.EMPTY);
        CommonRequestDTO secondCommonRequestDTO = TestDataProvider.getCommonRequestDTO();
        secondCommonRequestDTO.setRoleNames(List.of(Constants.ROLE_PROVIDER));

        List<String> roleNames = Arrays.asList(Constants.ROLE_PROVIDER, Constants.ROLE_PHYSICIAN_PRESCRIBER);
        Set<Role> roles = TestDataProvider.getRolesSet();
        List<User> users = TestDataProvider.getUsers();

        //when

        when(userRepository.findUsersByRoleNames(roleNames, 1L, commonRequestDTO.getSearchTerm())).thenReturn(users);
        when(userRepository.findUsersByRoleNames(List.of(Constants.ROLE_PROVIDER), 1L, commonRequestDTO.getSearchTerm())).thenReturn(users);

        //then
        List<User> actualUsers = userService.getUserListByRole(commonRequestDTO);
        assertNotNull(actualUsers);
        assertEquals(users.size(), actualUsers.size());
        assertFalse(actualUsers.isEmpty());
        assertEquals(users.get(Constants.ZERO), actualUsers.get(Constants.ZERO));
    }

    @Test
    void testUnlockUser() {
        //given
        User userToUnlock = TestDataProvider.getUser();
        userToUnlock.setIsBlocked(Boolean.TRUE);
        User user = TestDataProvider.getUser();
        user.setIsBlocked(Constants.BOOLEAN_FALSE);
        user.setInvalidLoginAttempts(Constants.ZERO);
        user.setActive(Constants.BOOLEAN_TRUE);
        user.setBlockedDate(null);
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();

        //when
        when(userRepository.findByIdAndIsDeletedFalse(commonRequestDTO.getId())).thenReturn(userToUnlock);
        when(userRepository.save(user)).thenReturn(user);

        //then
        userService.unlockUser(commonRequestDTO);
        assertNotNull(user);
    }

    @Test
    void unlockUserExceptions() {
        //given
        User userToUnlock = TestDataProvider.getUser();
        userToUnlock.setIsBlocked(Boolean.FALSE);
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();
        commonRequestDTO.setId(null);
        CommonRequestDTO secondCommonRequestDTO = TestDataProvider.getCommonRequestDTO();
        secondCommonRequestDTO.setId(TestConstants.TWO);
        CommonRequestDTO thirdCommonRequestDTO = TestDataProvider.getCommonRequestDTO();

        //when
        when(userRepository.findByIdAndIsDeletedFalse(TestConstants.TWO)).thenReturn(null);
        when(userRepository.findByIdAndIsDeletedFalse(thirdCommonRequestDTO.getId())).thenReturn(userToUnlock);

        //then
        assertThrows(DataNotAcceptableException.class, () -> userService.unlockUser(commonRequestDTO));
        assertThrows(DataNotFoundException.class, () -> userService.unlockUser(secondCommonRequestDTO));
        assertThrows(BadRequestException.class, () -> userService.unlockUser(thirdCommonRequestDTO));
    }

    @Test
    void testChangeSiteUserPassword() {
        //given
        Organization organization = new Organization();
        organization.setId(TestConstants.ONE);
        organization.setFormName(Constants.SITE);
        User user = new User();
        user.setId(TestConstants.ONE);
        user.setPassword(TestConstants.PASSWORD);
        user.setOrganizations(Set.of(organization));

        //when
        when(userRepository.findByIdAndIsDeletedFalse(TestConstants.ONE)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        //then
        userService.changeSiteUserPassword(TestConstants.ONE, TestConstants.PASSWORD);
        verify(userRepository, atLeastOnce()).save(user);
    }

    @Test
    void testCreateSuperAdmin() {
        //given
        User user = new User();
        user.setUsername(TestConstants.USER_NAME);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        Role role = TestDataProvider.getRole();

        //when
        when(roleService.getRolesByName(List.of(Constants.ROLE_SUPER_ADMIN))).thenReturn(Set.of(role));
        when(userRepository.saveAll(userList)).thenReturn(userList);
        when(userRepository.getUserByUsername(TestConstants.USER_NAME, Boolean.TRUE)).thenReturn(null);

        //then
        userService.createSuperAdmin(userList);
        verify(userRepository, atLeastOnce()).saveAll(userList);
    }

    @Test
    void testSearchUser() {
        //given
        TestDataProvider.init();
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDTO();
        searchRequestDTO.setUserType("user");
        Organization organization = TestDataProvider.getOrganization();
        Map<String, List<Long>> childIds = TestDataProvider.getChildIds();
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN,
                Sort.by(Constants.UPDATED_AT).descending());

        //when
        TestDataProvider.getStaticMockValidation(TestConstants.SEARCH_TERM);
        when(organizationService.getOrganizationById(TestConstants.ONE)).thenReturn(organization);
        when(organizationService.getChildOrganizations(TestConstants.ONE, organization.getFormName()))
                .thenReturn(childIds);
        when(userRepository.searchUsersByTenantIds(List.of(TestConstants.ONE), TestConstants.SEARCH_TERM,
                searchRequestDTO.getUserType(), pageable)).thenReturn(null);

        //then
        ResponseListDTO responseListDTO = userService.getAdminUsers(searchRequestDTO);
        TestDataProvider.cleanUp();
        assertNotNull(responseListDTO);
    }

    @Test
    void toVerifySearchUser() {
        //given
        TestDataProvider.init();
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDTO();

        //when
        TestDataProvider.getStaticMockValidationFalse(TestConstants.SEARCH_TERM);

        //then
        ResponseListDTO responseListDTO = userService.getAdminUsers(searchRequestDTO);
        TestDataProvider.cleanUp();
        assertNotNull(responseListDTO);
    }

    @Test
    void searchUser() {
        //given
        TestDataProvider.init();
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDTO();
        searchRequestDTO.setUserType("user");
        Organization organization = TestDataProvider.getOrganization();
        Map<String, List<Long>> childIds = TestDataProvider.getChildIds();
        List<User> usersList = TestDataProvider.getUsers();
        Page<User> users = new PageImpl<>(usersList);
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN,
                Sort.by(Constants.UPDATED_AT).descending());
        UserListDTO userListDTO = TestDataProvider.getUserListDTO();

        //when
        TestDataProvider.getStaticMockValidation(TestConstants.SEARCH_TERM);
        when(organizationService.getOrganizationById(TestConstants.ONE)).thenReturn(organization);

        when(organizationService.getChildOrganizations(TestConstants.ONE, organization.getFormName()))
                .thenReturn(childIds);
        when(userRepository.searchUsersByTenantIds(List.of(TestConstants.ONE, TestConstants.TWO, TestConstants.ONE),
                TestConstants.SEARCH_TERM,
                searchRequestDTO.getUserType(), pageable)).thenReturn(users);
        when(modelMapper.map(users.stream().toList(),
                new com.google.common.reflect.TypeToken<List<UserListDTO>>() {
                }.getType())).thenReturn(userListDTO);

        //then
        ResponseListDTO responseListDTO = userService.getAdminUsers(searchRequestDTO);
        TestDataProvider.cleanUp();
        assertNotNull(responseListDTO);
    }

    @Test
    void testGetLockedUsers() {
        //given
        TestDataProvider.init();
        RequestDTO requestObject = TestDataProvider.getRequestDTO();
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN,
                Sort.by(Constants.UPDATED_AT).descending());
        Organization organization = TestDataProvider.getOrganization();
        Map<String, List<Long>> childIds = TestDataProvider.getChildIds();
        List<User> users = TestDataProvider.getUsers();
        Page<User> usersPage = new PageImpl<>(users);

        UserOrganizationDTO userOrganizationDTO = TestDataProvider.getUserOrganizationDTO();

        //when
        TestDataProvider.getStaticMockValidation(TestConstants.SEARCH_TERM);
        when(organizationService.getOrganizationById(TestConstants.ONE)).thenReturn(organization);
        when(organizationService.getChildOrganizations(TestConstants.ONE, organization.getFormName()))
                .thenReturn(childIds);
        when(userRepository.getLockedUsers(TestConstants.SEARCH_TERM, pageable)).thenReturn(usersPage);
        when(modelMapper.map(users,
                new com.google.common.reflect.TypeToken<List<UserOrganizationDTO>>() {
                }.getType()))
                .thenReturn(List.of(userOrganizationDTO));
        when(userRepository.getLockedUsersCount(TestConstants.SEARCH_TERM,
                List.of(TestConstants.ONE, TestConstants.TWO, TestConstants.ONE))).thenReturn(Constants.ONE);

        //then
        ResponseListDTO response = userService.getLockedUsers(requestObject);
        TestDataProvider.cleanUp();
        assertNotNull(response);
    }

    @Test
    void toVerifyLockedUsers() {
        //given
        TestDataProvider.init();
        RequestDTO requestObject = TestDataProvider.getRequestDTO();

        //when
        TestDataProvider.getStaticMockValidationFalse(TestConstants.SEARCH_TERM);

        //then
        ResponseListDTO response = userService.getLockedUsers(requestObject);
        TestDataProvider.cleanUp();
        Assertions.assertNotNull(response);
    }

    @Test
    void getLockedUsers() {
        //given
        TestDataProvider.init();
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setSearchTerm(TestConstants.SEARCH_TERM);
        requestDTO.setUserId(TestConstants.ONE);
        requestDTO.setEmail(TestConstants.USER_NAME);
        requestDTO.setNewPassword(TestConstants.PASSWORD);
        Pageable pageable = PageRequest.of(Constants.ZERO,
                TestConstants.TEN, Sort.by(Constants.UPDATED_AT).descending());
        List<User> usersList = TestDataProvider.getUsers();
        Page<User> users = new PageImpl<>(usersList);
        UserOrganizationDTO userOrganizationDTO = TestDataProvider.getUserOrganizationDTO();

        //when
        TestDataProvider.getStaticMockValidation(TestConstants.SEARCH_TERM);
        when(userRepository.getLockedUsers(TestConstants.SEARCH_TERM, pageable)).thenReturn(users);
        when(userRepository.getBlockedUsersCount(TestConstants.SEARCH_TERM)).thenReturn(Constants.ONE);
        when(modelMapper.map(users,
                new com.google.common.reflect.TypeToken<List<UserOrganizationDTO>>() {
                }.getType()))
                .thenReturn(List.of(userOrganizationDTO));

        //then
        ResponseListDTO responseMap = userService.getLockedUsers(requestDTO);
        TestDataProvider.cleanUp();
        Assertions.assertNotNull(responseMap);
    }

    @Test
    void testClearApiPermission() {
        //given
        authenticationFilter.apiPermissionMap = new HashMap<>();
        authenticationFilter.apiPermissionMap.put("Test", new HashMap<>());

        //then
        userService.clearApiPermissions();
        assertEquals(0, authenticationFilter.apiPermissionMap.size());
    }


    @Test
    void testUpdateCulture() {
        //given
        CultureRequestDTO cultureRequestDTO = TestDataProvider.getCultureRequestDTO();
        User user = TestDataProvider.getUser();
        user.setCultureId(TestConstants.ONE);
        Optional<User> optionalUser = Optional.of(user);

        //when
        when(userRepository.findById(TestConstants.ONE)).thenReturn(optionalUser);
        when(userRepository.save(user)).thenReturn(user);

        //then
        userService.updateCulture(cultureRequestDTO);
        verify(userRepository, atLeastOnce()).findById(TestConstants.ONE);
        verify(userRepository, atLeastOnce()).save(user);
    }

    @Test
    void testUpdateCultureException() {
        //given
        CultureRequestDTO cultureRequestDTO = TestDataProvider.getCultureRequestDTO();
        User user = TestDataProvider.getUser();
        user.setCultureId(TestConstants.ONE);
        Optional<User> optionalUser = Optional.empty();

        //when
        when(userRepository.findById(TestConstants.ONE)).thenReturn(optionalUser);

        //then
        assertThrows(DataNotFoundException.class, () -> userService.updateCulture(cultureRequestDTO));
    }

    @Test
    void addUsers() {
        //given
        List<User> users = TestDataProvider.getUsers();
        List<String> newUserNames = List.of(TestConstants.USER_NAME);

        //when
        when(userRepository.saveAll(users)).thenReturn(users);

        //then
        userService.addUsers(users, newUserNames);
        verify(userRepository, atLeastOnce()).saveAll(users);
    }

    @Test
    void testRedRiskUserUpdate() {
        //given
        boolean isRedRisk = false;
        User existingUser = new User(1L);
        Set<Role> roles = new HashSet<>();
        Role redRiskRole = new Role(2L, Constants.ROLE_RED_RISK_USER);
        roles.add(redRiskRole);
        existingUser.setRoles(roles);

        //then
        userService.redRiskUserUpdate(existingUser, isRedRisk);
        assertFalse(existingUser.getRoles().contains(redRiskRole));
    }

    @Test
    void sendEmailTestWithException() {
        //given
        User user = TestDataProvider.getUser();
        String jwt = "Token";
        boolean isFromCreation = false;
        String appType = "";
        ResponseEntity<EmailTemplate> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        //when
        when(notificationApiInterface.getEmailTemplate(
                (Constants.FORGOT_PASSWORD_USER), appType)).thenReturn(responseEntity);
        //then
        userService.sendEmail(user, jwt, isFromCreation, appType);
        verify(notificationApiInterface, never()).createOutBoundEmail(
                any(OutBoundEmailDTO.class));
    }
}
