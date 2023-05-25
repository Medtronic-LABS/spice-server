package com.mdtlabs.coreplatform.userservice.controller;

import static com.mdtlabs.coreplatform.userservice.util.TestDataProvider.getUserDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.UserProfileDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CultureRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PaginateDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserSuperAdminDto;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.userservice.AdminApiInterface;
import com.mdtlabs.coreplatform.userservice.SpiceApiInterface;
import com.mdtlabs.coreplatform.userservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.userservice.service.impl.UserServiceImpl;
import com.mdtlabs.coreplatform.userservice.util.TestConstants;
import com.mdtlabs.coreplatform.userservice.util.TestDataProvider;

/**
 * <p>
 * User Controller Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Jan 24, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    private ModelMapper modelMapper;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private AdminApiInterface adminApiInterface;

    @Mock
    private SpiceApiInterface spiceApiInterface;

    private User user = TestDataProvider.getUser();

    private List<User> users = TestDataProvider.getUsers();

    private UserDTO userDTO = getUserDTO();

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestDataProvider.setUp(UserController.class, "modelMapper", userController);
    }
    
    @Test
    void testAddUser() {
        //given
        User user = TestDataProvider.getUser();
        UserDTO userDto = TestDataProvider.getUserDTO();
        
        //when
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userService.addUser(user)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDto);

        //then
        SuccessResponse<UserDTO> successResponse = userController.addUser(userDto);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifyAddUser() {
        //given
        User newUser = null;

        //when
        when(modelMapper.map(userDTO, User.class)).thenReturn(newUser);
        when(userService.addUser(newUser)).thenReturn(newUser);

        //then
        SuccessResponse<UserDTO> successResponse = userController.addUser(userDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, successResponse.getStatusCode());
    }

    @Test
    void testGetUsers() {
        //given
        List<UserDTO> userDTOS = TestDataProvider.getUserDTOS();
        List<User> users = TestDataProvider.getUsers();

        //when
        when(userService.getUsers(Constants.ZERO)).thenReturn(users);
        when(modelMapper.map(users, new TypeToken<List<UserDTO>>() {
        }.getType())).thenReturn(userDTOS);

        //then
        SuccessResponse<List<UserDTO>> successResponse = userController.getUsers(Constants.ZERO);
        Assertions.assertNotNull(users);
        Assertions.assertNotNull(userDTOS);
        Assertions.assertEquals(users.size(), userDTOS.size());
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifyGetUsers() {
        //given
        List<User> newUsers = Collections.emptyList();

        //when
        when(userService.getUsers(Constants.ZERO)).thenReturn(newUsers);

        //then
        SuccessResponse<List<UserDTO>> successResponse = userController.getUsers(Constants.ZERO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testUpdateUser() {
        //given
        User user = TestDataProvider.getUser();

        //when
        when(userService.updateUser(user)).thenReturn(user);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);

        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        //then
        SuccessResponse<UserDTO> successResponse = userController.updateUser(userDTO);
        Assertions.assertNotNull(userDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testDeleteUserById() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();

        //when
        when(userService.deleteUserById(TestConstants.ONE)).thenReturn(Boolean.TRUE);

        //then
        SuccessResponse<Boolean> successResponse = userController.deleteUserById(commonRequestDTO);
        Assertions.assertEquals(Boolean.TRUE, userService.deleteUserById(TestConstants.ONE));
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetUserById() {
        //given
        User user = TestDataProvider.getUser();
        UserListDTO userListDto = TestDataProvider.getUserListDTO();

        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(userService.getUserById(TestConstants.ONE)).thenReturn((user));
        when(modelMapper.map(user, UserListDTO.class)).thenReturn(userListDto);

        //then
        UserListDTO userDTO = userController.getUserById(TestConstants.ONE);
        Assertions.assertEquals(userDTO.getFirstName(), user.getFirstName());
    }

    @Test
    void testGetUserProfileById() {
        //given
        User user = TestDataProvider.getUser();
        UserProfileDTO userProfileDto = TestDataProvider.getUserProfileDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(TestConstants.ONE);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);

        //when
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        when(userService.getUserById(userDTO.getId())).thenReturn(user);
        when(modelMapper.map(user, UserProfileDTO.class)).thenReturn(userProfileDto);

        //then
        SuccessResponse<UserProfileDTO> successResponse = userController.getUserProfileById();
        userContextHolder.close();
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifyGetUserProfileById() {
        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(TestConstants.ZERO);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);

        //when
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        when(userService.getUserById(userDTO.getId())).thenReturn(null);

        //then
        SuccessResponse<UserProfileDTO> successResponse = userController.getUserProfileById();
        userContextHolder.close();
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetUserByUsername() {
        //given
        User user = TestDataProvider.getUser();


        //when
        when(userService.getUserByUsername(TestConstants.USER_NAME)).thenReturn(user);

        //then
        SuccessResponse<User> successResponse = userController.getUserByUsername(TestConstants.USER_NAME);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testForgotPasswordValidation() {
        //given
        boolean response = Boolean.FALSE;

        //when
        when(userService.forgotPassword(TestConstants.USER_NAME, null, Boolean.FALSE, Constants.SPICE)).thenReturn(response);

        //then
        SuccessResponse<String> successResponse = userController
                .forgotPasswordValidation(TestConstants.USER_NAME, Constants.SPICE);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifyForgotPasswordValidation() {
        //given
        boolean response = Boolean.TRUE;

        //when
        when(userService.forgotPassword(TestConstants.USER_NAME, null, Boolean.FALSE, Constants.SPICE)).thenReturn(response);

        //then
        SuccessResponse<String> successResponse = userController
                .forgotPasswordValidation(TestConstants.USER_NAME, Constants.SPICE);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testClearApiPermissions() {
        //given
        TestDataProvider.init();
        ResponseEntity<Boolean> clearApiPermissions = adminApiInterface.clearApiPermissions(TestConstants.TOKEN);
        ResponseEntity<Boolean> clearApiPermission = spiceApiInterface.clearApiPermissions(TestConstants.TOKEN);

        //when
        doNothing().when(userService).clearApiPermissions();
        TestDataProvider.getStaticMock();
        when(adminApiInterface.clearApiPermissions(TestConstants.TOKEN)).thenReturn(clearApiPermissions);
        when(spiceApiInterface.clearApiPermissions(TestConstants.TOKEN)).thenReturn(clearApiPermission);

        //then
        SuccessResponse<Boolean> successResponse = userController.clearApiPermissions();
        TestDataProvider.cleanUp();
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetUserByTenantIds() {
        //given
        List<Long> tenantIds = TestDataProvider.getUserTenantIds();
        List<UserOrganizationDTO> userOrganizationDTOS = TestDataProvider.getListUserOrganizationDTO();
        List<User> users = TestDataProvider.getUsers();


        //when
        when(userService.getUsersByTenantIds(tenantIds)).thenReturn(userOrganizationDTOS);

        //then
        List<UserOrganizationDTO> userOrganizationDTOList = userController.getUsersByTenantIds(tenantIds);
        Assertions.assertEquals(users.size(), userOrganizationDTOList.size());
        Assertions.assertEquals(users.size(), userService.getUsersByTenantIds(tenantIds).size());
        Assertions.assertNotNull(users);
    }

    @Test
    void testValidateSession() {
        //then
        ResponseEntity<String> responseEntity = userController.validateSession();
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testValidateUsers() {
        //given
        RequestDTO requestDto = TestDataProvider.getRequestDTO();
        UserListDTO userListDTO = TestDataProvider.getUserListDTO();

        //when
        when(userService.validateUser(requestDto)).thenReturn(userListDTO);

        //then
        SuccessResponse<UserListDTO> successResponse = userController.validateUser(requestDto);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testActivateDeactivateUser() {
        //given
        List<Long> tenantIds = TestDataProvider.getUserTenantIds();

        //when
        when(userService.activateDeactivateUser(tenantIds, Boolean.TRUE)).thenReturn(Boolean.TRUE);

        //then
        ResponseEntity<Boolean> responseEntity = userController.activateDeactivateUser(tenantIds,
                Boolean.TRUE);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testSearchUser() {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDTO();
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();

        //when
        when(userService.getAdminUsers(searchRequestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<UserListDTO>> successResponse = userController.getAdminUsers(searchRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifySearchUserWithNull() {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDTO();
        ResponseListDTO responseListDTO = new ResponseListDTO();
        responseListDTO.setTotalCount(TestConstants.ZERO);

        //when

        when(userService.getAdminUsers(searchRequestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<UserListDTO>> successResponse = userController.getAdminUsers(searchRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifySearchUser() {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDTO();
        ResponseListDTO responseListDTO = new ResponseListDTO();

        //when
        when(userService.getAdminUsers(searchRequestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<UserListDTO>> successResponse = userController.getAdminUsers(searchRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetUserBySearchTermAndTenantId() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDTO();
        List<User> users = TestDataProvider.getUsers();


        //when
        when(userService.getUserBySearchTermAndTenantId(requestDTO.getTenantId(), requestDTO
                .getSearchTerm())).thenReturn(users);

        //then
        SuccessResponse<List<User>> successResponse = userController
                .getUserBySearchTermAndTenantId(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetSuperAdminUsers() {
        //given

        PaginateDTO paginateDTO = TestDataProvider.getPaginateDTO();
        ResponseListDTO response = TestDataProvider.getResponseListDTO();

        //when
        when(userService.getSuperAdminUsers(paginateDTO)).thenReturn(response);

        //then
        SuccessResponse<List<UserSuperAdminDto>> successResponse = userController.getSuperAdminUsers(paginateDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifyGetSuperAdminUsers() {
        //given
        PaginateDTO paginateDTO = TestDataProvider.getPaginateDTO();

        ResponseListDTO response = new ResponseListDTO();

        //when
        when(userService.getSuperAdminUsers(paginateDTO)).thenReturn(response);

        //then
        SuccessResponse<List<UserSuperAdminDto>> successResponse = userController.getSuperAdminUsers(paginateDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testRemoveSuperAdminRoleForUser() {
        //when
        doNothing().when(userService).removeSuperAdmin(TestConstants.ONE);

        //then
        SuccessResponse successResponse = userController.removeSuperAdminRoleForUser(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testUpdateSuperAdminUser() {
        //given
        UserSuperAdminDto userSuperAdminDto = TestDataProvider.getUserSuperAdminDto();

        //when
        doNothing().when(userService).updateSuperAdmin(userSuperAdminDto);

        //then
        SuccessResponse successResponse = userController.updateSuperAdminUser(userSuperAdminDto);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetSuperAdminUserById() {
        //given
        UserSuperAdminDto userSuperAdminDto = TestDataProvider.getUserSuperAdminDto();

        //when
        when(userService.getSuperAdminById(TestConstants.ONE)).thenReturn(userSuperAdminDto);

        //then
        SuccessResponse<UserSuperAdminDto> successResponse = userController
                .getSuperAdminUserById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        Assertions.assertNotNull(userSuperAdminDto);
    }

    @Test
    void testVerifySetPassword() {
        //given
        Map<String, Object> password = TestDataProvider.verifySetPassword();

        //when
        when(userService.verifySetPassword(TestConstants.TOKEN)).thenReturn(password);

        //then
        SuccessResponse<Map<String, Object>> successResponse = userController
                .verifySetPassword(TestConstants.TOKEN);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testUnlockUser() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();
        
        //when
        doNothing().when(userService).unlockUser(commonRequestDTO);

        //then
        SuccessResponse successResponse = userController.unlockUser(commonRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetUserByIds() {
        //given
        Set<Long> userIds = TestDataProvider.getUserIds();
        Map<Long, User> users = TestDataProvider.getMappedUsers();

        //when
        when(userService.getUserByIds(userIds)).thenReturn(users);
        when(modelMapper.map(users, UserDTO.class)).thenReturn(userDTO);

        //then
        ResponseEntity<Map<Long, UserDTO>> responseEntity = userController.getUserByIds(userIds);
        Assertions.assertNotNull(users);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void toVerifyGetUserByIds() {
        //given
        Set<Long> userIds = TestDataProvider.getUserIds();

        //when
        when(userService.getUserByIds(userIds)).thenReturn(null);

        //then
        ResponseEntity<Map<Long, UserDTO>> responseEntity = userController.getUserByIds(userIds);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetUserByIdsIsEmpty() {
        //given
        Set<Long> userIds = TestDataProvider.getUserIds();
        Map<Long, User> users = new HashMap<>();

        //when
        when(userService.getUserByIds(userIds)).thenReturn(users);

        //then
        ResponseEntity<Map<Long, UserDTO>> responseEntity = userController.getUserByIds(userIds);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetUserByRoleName() {
        //given
        List<UserResponseDTO> userResponseDTOs = TestDataProvider.getUserResponseDTOS();
        List<User> users = TestDataProvider.getUsers();


        //when
        when(userService.getUserByRoleName(TestConstants.ROLE_NAME)).thenReturn(users);
        when(modelMapper.map(users, new TypeToken<List<UserDTO>>() {
        }.getType())).thenReturn(userResponseDTOs);

        //then
        ResponseEntity<List<UserResponseDTO>> responseEntity = userController.getUserByRoleName(TestConstants.ROLE_NAME);
        Assertions.assertNotNull(users);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void toVerifyGetUserByRoleName() {
        //when
        when(userService.getUserByRoleName(TestConstants.ROLE_NAME)).thenReturn(null);

        //then
        ResponseEntity<List<UserResponseDTO>> responseEntity = userController.getUserByRoleName(TestConstants.ROLE_NAME);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetUserByRoleNameIsEmpty() {
        //given
        List<User> userList = new ArrayList<>();

        //when
        when(userService.getUserByRoleName(TestConstants.ROLE_NAME)).thenReturn(userList);

        //then
        ResponseEntity<List<UserResponseDTO>> responseEntity = userController.getUserByRoleName(TestConstants.ROLE_NAME);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetLockedUsers() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDTO();
        ResponseListDTO response = new ResponseListDTO(users, 1L);
        
        //when
        when(userService.getLockedUsers(requestDTO)).thenReturn(response);

        //then
        SuccessResponse<List<UserOrganizationDTO>> responseEntity = userController
                .getLockedUsers(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetLockedUsersWithNull() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDTO();
        ResponseListDTO response = new ResponseListDTO(users, 1L);

        //when
        when(userService.getLockedUsers(requestDTO)).thenReturn(response);

        //then
        SuccessResponse<List<UserOrganizationDTO>> successResponse = userController
                .getLockedUsers(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testVerifyGetLockedUsers() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDTO();
        ResponseListDTO response = new ResponseListDTO(users, 1L);

        //when
        when(userService.getLockedUsers(requestDTO)).thenReturn(response);
        //then
        SuccessResponse<List<UserOrganizationDTO>> successResponse = userController
                .getLockedUsers(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }


    @Test
    void testChangeSiteUserPassword() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDTO();

        //when
        doNothing().when(userService).changeSiteUserPassword(requestDTO.getUserId(), requestDTO.getNewPassword());

        //then
        SuccessResponse<Boolean> successResponse = userController.changeSiteUserPassword(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testResetUserPassword() {
        //given
        Map<String, String> userInfo = TestDataProvider.getUserInfo();
        Map<String, Object> password = TestDataProvider.verifySetPassword();

        //when
        when(userService.resetUserPassword(TestConstants.TOKEN, userInfo)).thenReturn(password);

        //then
        SuccessResponse<Map<String, Boolean>> successResponse = userController
                .resetUserPassword(TestConstants.TOKEN, userInfo);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testSetUserPassword() {
        //given
        Map<String, String> userInfo = TestDataProvider.getUserInfo();
        Map<String, Object> password = TestDataProvider.verifySetPassword();

        //when
        when(userService.setUserPassword(TestConstants.TOKEN, userInfo)).thenReturn(password);

        //then
        SuccessResponse<Map<String, Boolean>> successResponse = userController
                .setUserPassword(TestConstants.TOKEN, userInfo);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testCreateSuperAdminUser() {
        //given
        List<User> users = TestDataProvider.getUsers();

        //when
        doNothing().when(userService).createSuperAdmin(users);

        //then
        SuccessResponse successResponse = userController.createSuperAdminUser(users);
        Assertions.assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
    }

    @Test
    void testGetUserListByRole() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();
        List<User> users = TestDataProvider.getUsers();

        //when
        when(userService.getUserListByRole(commonRequestDTO)).thenReturn(users);

        //then
        SuccessResponse<List<User>> successResponse = userController.getUserListByRole(commonRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }
    
    @Test
    void testUpdateCulture() {
        //given
        CultureRequestDTO cultureRequestDTO = TestDataProvider.getCultureRequestDTO();

        //when
        doNothing().when(userService).updateCulture(cultureRequestDTO);

        //then
        SuccessResponse<String> actualResponse = userController.updateCulture(cultureRequestDTO);
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }
}

