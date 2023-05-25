package com.mdtlabs.coreplatform.userservice.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
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
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.spice.UserInfo;
import com.mdtlabs.coreplatform.userservice.AdminApiInterface;
import com.mdtlabs.coreplatform.userservice.SpiceApiInterface;
import com.mdtlabs.coreplatform.userservice.message.SuccessCode;
import com.mdtlabs.coreplatform.userservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.userservice.service.UserService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * User Controller class that defines various REST API endpoints for managing users,
 * including updating, retrieving, creating, and deleting using user service.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private AdminApiInterface adminApiInterface;

    @Autowired
    private SpiceApiInterface spiceApiInterface;

    @Autowired
    private UserService userService;

    @Value("${app.page-count}")
    private int gridDisplayValue;

    /**
     * <p>
     * This method is used to create a user using the details provided in the UserDTO.
     * </p>
     *
     * @param requestUserDto {@link UserDTO} It contains the user details that need to be added
     * @return {@link SuccessResponse<UserDTO>} The User for given UserDTO is created and returned with status
     */
    @PostMapping
    public SuccessResponse<UserDTO> addUser(@Valid @RequestBody UserDTO requestUserDto) {
        User addUser = userService.addUser(modelMapper.map(requestUserDto, User.class));
        if (null != addUser) {
            UserDTO userDto = modelMapper.map(addUser, UserDTO.class);
            return new SuccessResponse<>(SuccessCode.USER_SAVE, userDto, HttpStatus.OK);
        }
        return new SuccessResponse<>(SuccessCode.USER_NOT_SAVED, requestUserDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * <p>
     * This method is used to get all users information with pagination.
     * </p>
     *
     * @param pageNumber The page number of the list of users to be retrieved
     * @return {@link SuccessResponse<List>} The List of users for the given pagination is returned with status
     */
    @GetMapping("/all/{pageNumber}")
    public SuccessResponse<List<UserDTO>> getUsers(@PathVariable(Constants.PAGE_NUMBER) int pageNumber) {
        List<User> allUsers = userService.getUsers(pageNumber);
        if (allUsers.isEmpty()) {
            return new SuccessResponse(SuccessCode.GET_USERS, Constants.NO_DATA_LIST, HttpStatus.OK);
        }
        List<UserDTO> users = modelMapper.map(allUsers, new TypeToken<List<UserDTO>>() {
        }.getType());
        return new SuccessResponse(SuccessCode.GET_USERS, users, userService.getTotalSize(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update the user using the provided user details.
     * </p>
     *
     * @param userDto {@link UserDTO} The user dto to be updated is given
     * @return {@link SuccessResponse<UserDTO>} The user for the given user details is
     * updated and returned with status
     */
    @PostMapping("/update")
    public SuccessResponse<UserDTO> updateUser(@RequestBody UserDTO userDto) {
        UserDTO updatedUserDto = modelMapper.map(userService.updateUser(modelMapper.map(userDto, User.class)),
                UserDTO.class);
        return new SuccessResponse<>(SuccessCode.USER_UPDATE, updatedUserDto, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to delete the user using the details provided in request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The data of the user to be deleted is given
     * @return {@link SuccessResponse<Boolean>} The admin user is deleted and returned as boolean value with status
     */
    @PutMapping("/delete")
    public SuccessResponse<Boolean> deleteUserById(@RequestBody CommonRequestDTO requestDto) {
        return new SuccessResponse<>(SuccessCode.USER_DELETE, userService.deleteUserById(requestDto.getId()), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve the user for the given id.
     * </p>
     *
     * @param userId The id of the user for which the user to be retrieved is given
     * @return {@link UserListDTO} The user for the given id is returned
     */
    @GetMapping("/details/{id}")
    public UserListDTO getUserById(@PathVariable(Constants.ID) long userId) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        User user = userService.getUserById(userId);
        return modelMapper.map(user, UserListDTO.class);
    }

    /**
     * <p>
     * This method is used to get user profile by id.
     * </p>
     *
     * @return {@link SuccessResponse<UserProfileDTO>} The user profile for the given id is
     * retrieved and returned with status
     */
    @GetMapping("/profile")
    public SuccessResponse<UserProfileDTO> getUserProfileById() {
        UserDTO userDto = UserContextHolder.getUserDto();
        User user = userService.getUserById(userDto.getId());
        if (null != user) {
            UserProfileDTO userProfileDto = modelMapper.map(user, UserProfileDTO.class);
            return new SuccessResponse<>(SuccessCode.GET_USER, userProfileDto, HttpStatus.OK);
        }
        return new SuccessResponse<>(SuccessCode.USER_NOT_FOUND, user, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get user by given user name.
     * </p>
     *
     * @param username {@link String} The username for which the user need to be retrieved is given
     * @return {@link SuccessResponse<User>} The user for the given username is retrieved and returned with status
     */
    @GetMapping("/username/{username}")
    public SuccessResponse<User> getUserByUsername(@PathVariable(FieldConstants.USERNAME) String username) {
        return new SuccessResponse<>(SuccessCode.GET_USER, userService.getUserByUsername(username), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to handle a forgot password validation request .
     * </p>
     *
     * @param username {@link String} The username of the user who need to reset their password is given
     * @param appType  {@link String} The appType parameter is a path variable that is used to specify the
     *                 type of application for which the email template is being requested is given
     * @return {@link SuccessResponse<String>} Returns a message indicating that the password has been validated.
     */
    @GetMapping(value = "/forgot-password/{username}/{appType}", produces = "application/json")
    public SuccessResponse<String> forgotPasswordValidation(
            @PathVariable(FieldConstants.USERNAME) String username, @PathVariable(Constants.APP_TYPE) String appType) {
        Boolean response = userService.forgotPassword(username, null, Constants.BOOLEAN_FALSE, appType);
        return Boolean.TRUE.equals(response)
                ? new SuccessResponse<>(SuccessCode.SEND_EMAIL_USING_SMTP, response, HttpStatus.OK)
                : new SuccessResponse<>(SuccessCode.FORGOT_LIMIT_EXCEEDED, response, HttpStatus.OK);

    }

    /**
     * <p>
     * This method is used to clear the api permission role map.
     * </p>
     *
     * @return {@link SuccessResponse<Boolean>} Returns a message indicating that the
     * permission role map is cleared with status
     */
    @GetMapping("/clear")
    public SuccessResponse<Boolean> clearApiPermissions() {
        userService.clearApiPermissions();
        String token = CommonUtil.getAuthToken();
        adminApiInterface.clearApiPermissions(token);
        spiceApiInterface.clearApiPermissions(token);
        return new SuccessResponse<>(SuccessCode.API_PERMISSION_CLEARED, Constants.API_ROLES_MAP_CLEARED,
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get a list of users based on a list of tenant IDs passed as a request.
     * </p>
     *
     * @param tenantIds {@link List<Long>} The list of tenant ids for which the users to be retrieved is given
     * @return {@link List<UserOrganizationDTO>} The List of users organization for the given tenant id is returned
     */
    @PostMapping("/get-by-tenants")
    public List<UserOrganizationDTO> getUsersByTenantIds(@RequestBody List<Long> tenantIds) {
        return userService.getUsersByTenantIds(tenantIds);
    }

    /**
     * <p>
     * This method is used to validate a session whether session is alive.
     * </p>
     *
     * @return {@link ResponseEntity<String>} The session alive message with status is returned
     */
    @GetMapping("/validate")
    public ResponseEntity<String> validateSession() {
        return ResponseEntity.ok().body(Constants.SESSION_ALIVE);
    }

    /**
     * <p>
     * This method is used to validate a user by its username using the provided details.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request that contains necessary information required
     *                   to get the list of users is given
     * @return {@link SuccessResponse<UserListDTO>} The validated user for the given request is returned with status
     */
    @PostMapping("/validate-user")
    public SuccessResponse<UserListDTO> validateUser(@RequestBody RequestDTO requestDto) {
        return new SuccessResponse<>(SuccessCode.GET_USER, userService.validateUser(requestDto), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is to activate or deactivate users based on tenantIds.
     * </p>
     *
     * @param tenantIds {@link List<Long>} The list of tenantIds that belongs to the users which to
     *                  be activated or deactivated is given
     * @param isActive  Active status of the user is given
     * @return {@link ResponseEntity<Boolean>} The response containing the boolean value whether the
     * activation/deactivation of the user is successful or not is returned.
     */
    @PostMapping("/update-active-status")
    public ResponseEntity<Boolean> activateDeactivateUser(@RequestBody List<Long> tenantIds,
                                                          @RequestParam(Constants.IS_ACTIVE) boolean isActive) {
        return ResponseEntity.ok().body(userService.activateDeactivateUser(tenantIds, isActive));
    }

    /**
     * <p>
     * This method is used to search users based on a given request.
     * </p>
     *
     * @param requestDTO {@link SearchRequestDTO} The request that contains necessary information required
     *                   to get the list of users is given
     * @return {@link SuccessResponse<List>} The List of users for the given search request is returned with status
     */
    @UserTenantValidation
    @PostMapping("/admin-users")
    public SuccessResponse<List<UserListDTO>> getAdminUsers(@RequestBody SearchRequestDTO requestDTO) {
        ResponseListDTO response = userService.getAdminUsers(requestDTO);
        if (Objects.isNull(response.getTotalCount()) || 0L == response.getTotalCount()) {
            return new SuccessResponse(SuccessCode.GET_USERS, response.getData(), HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_USERS, response.getData(), response.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve user details based on provided SearchTerm.
     * </p>
     *
     * @param requestObject {@link RequestDTO} The request that contains necessary information required
     *                      to get the list of users is given
     * @return {@link SuccessResponse<List>} The List of users for the given request is returned with status
     */
    @UserTenantValidation
    @PostMapping("/list")
    public SuccessResponse<List<User>> getUserBySearchTermAndTenantId(@RequestBody RequestDTO requestObject) {
        return new SuccessResponse(SuccessCode.GET_USERS,
                userService.getUserBySearchTermAndTenantId(requestObject.getTenantId(), requestObject.getSearchTerm()),
                Constants.ZERO, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get all super-admin users details with pagination.
     * </p>
     *
     * @param searchRequest {@link PaginateDTO} The request that contains pagination information required
     *                      to get the list of users is given
     * @return {@link SuccessResponse<List>} The List of user super admin for the given pagination is returned with status
     */
    @PostMapping("/super-admin/list")
    public SuccessResponse<List<UserSuperAdminDto>> getSuperAdminUsers(@RequestBody PaginateDTO searchRequest) {
        ResponseListDTO response = userService.getSuperAdminUsers(searchRequest);

        if (Objects.isNull(response.getTotalCount()) || 0L == response.getTotalCount()) {
            return new SuccessResponse(SuccessCode.GET_SUPER_ADMIN_USER, response.getData(), HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_SUPER_ADMIN_USER, response.getData(), response.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to remove super-admin role for the provided user.
     * </p>
     *
     * @param id The id of the user who need to be removed
     * @return {@link SuccessResponse<String>} Returns a message indicating that the user has been removed
     */
    @PutMapping("/super-admin/remove/{id}")
    public SuccessResponse<String> removeSuperAdminRoleForUser(@PathVariable("id") long id) {
        userService.removeSuperAdmin(id);
        return new SuccessResponse<>(SuccessCode.REMOVE_SUPER_ADMIN_USER, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update super admin user details by id.
     * </p>
     *
     * @param userSuperAdminDto {@link UserSuperAdminDto} The user super admin to update is given
     * @return {@link SuccessResponse<String>} Returns a message indicating that the user has been updated.
     */
    @PostMapping("/super-admin/update")
    public SuccessResponse<String> updateSuperAdminUser(@RequestBody UserSuperAdminDto userSuperAdminDto) {
        userService.updateSuperAdmin(userSuperAdminDto);
        return new SuccessResponse<>(SuccessCode.UPDATE_SUPER_ADMIN_USER, HttpStatus.OK);

    }

    /**
     * <p>
     * This method is used to get a super admin users details.
     * </p>
     *
     * @param id The id of the user who need to be retrieved is given
     * @return {@link SuccessResponse<UserSuperAdminDto>} The user super admin for given id is retrieved and returned with status
     */
    @GetMapping("/super-admin/details/{id}")
    public SuccessResponse<UserSuperAdminDto> getSuperAdminUserById(@PathVariable("id") long id) {
        return new SuccessResponse<>(SuccessCode.GET_SUPER_ADMIN_USER,
                userService.getSuperAdminById(id), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to verify a set password token using the given token.
     * </p>
     *
     * @param token {@link String} The token to verify user identity before allowing them to set a new password
     * @return {@link SuccessResponse<Map>} A map containing the response whether the password resets or not and
     * the status is returned
     */
    @GetMapping("/verify-set-password/{token}")
    public SuccessResponse<Map<String, Object>> verifySetPassword(@PathVariable(Constants.TOKEN) String token) {
        return new SuccessResponse<>(SuccessCode.GET_SUPER_ADMIN_USER, userService.verifySetPassword(token),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to unlock a user based on the request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The request that contains necessary information required
     *                   to get the list of users is given
     * @return {@link SuccessResponse<String>} Returns a message indicating that the user has been unlocked
     */
    @PostMapping("/unlock")
    public SuccessResponse<String> unlockUser(@RequestBody CommonRequestDTO requestDto) {
        userService.unlockUser(requestDto);
        return new SuccessResponse<>(SuccessCode.USER_UNLOCK, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get user information of the given set of ids.
     * </p>
     *
     * @param ids {@link Set<Long>} The ids of the users to be retrieved is given
     * @return {@link SuccessResponse<Map>} A map of id and user information is returned with status
     */
    @PostMapping("/get-by-ids")
    public ResponseEntity<Map<Long, UserDTO>> getUserByIds(@RequestBody Set<Long> ids) {
        Map<Long, UserDTO> userDTOMap = new HashMap<>();
        Map<Long, User> users = userService.getUserByIds(ids);
        if (!Objects.isNull(users) && !users.isEmpty()) {
            users.values().forEach(
                    user -> userDTOMap.put(user.getId(), modelMapper.map(user, UserDTO.class))
            );
        }
        return ResponseEntity.ok().body(userDTOMap);
    }

    /**
     * <p>
     * This method is used to get list of users having the given role name.
     * </p>
     *
     * @param roleName {@link String} The role name of the user to be searched is given
     * @return {@link ResponseEntity<List>} The list of users for the given name is retrieved with status
     */
    @PostMapping("/get-by-rolename")
    public ResponseEntity<List<UserResponseDTO>> getUserByRoleName(@RequestBody String roleName) {
        List<UserResponseDTO> userDTOS = new ArrayList<>();
        List<User> users = userService.getUserByRoleName(roleName);
        if (!Objects.isNull(users) && !users.isEmpty()) {
            userDTOS = modelMapper.map(users, new TypeToken<List<UserResponseDTO>>() {
            }.getType());
        }
        return ResponseEntity.ok().body(userDTOS);
    }

    /**
     * <p>
     * This method is used to retrieve a list of locked users with respect
     * to the provided details.
     * </p>
     *
     * @param requestObject {@link RequestDTO} The conditions to get the locked users is given
     * @return {@link SuccessResponse<List>} The list of locked users is retrieved and
     * returned with the status
     */
    @UserTenantValidation
    @PostMapping("/locked-users")
    public SuccessResponse<List<UserOrganizationDTO>> getLockedUsers(@RequestBody RequestDTO requestObject) {
        ResponseListDTO response = userService.getLockedUsers(requestObject);
        if (Objects.isNull(response.getTotalCount()) || 0L == response.getTotalCount()) {
            return new SuccessResponse(SuccessCode.GET_USERS, response.getData(), HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_USERS, response.getData(), response.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to changes the password of a site user using given request.
     * </p>
     *
     * @param request {@link RequestDTO} The request with user id and new password is given
     * @return {@link SuccessResponse<Boolean>} A success message with status is returned
     * after changing the site user password
     */
    @PostMapping("/change-password")
    public SuccessResponse<Boolean> changeSiteUserPassword(@RequestBody RequestDTO request) {
        userService.changeSiteUserPassword(request.getUserId(), request.getNewPassword());
        return new SuccessResponse<>(SuccessCode.CHANGE_SITE_USER_PASSWORD, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to reset a user's password using a token and user information.
     * </p>
     *
     * @param token    {@link String} The token to identify the user for whom the password is being reset is given.
     * @param userInfo {@link Map} A map of user's information is given
     * @return {@link SuccessResponse<Map>} A map containing the response whether the password resets or not and
     * the status is returned
     */
    @PostMapping("/reset-password/{token}")
    public SuccessResponse<Map<String, Boolean>> resetUserPassword(@PathVariable(Constants.TOKEN) String token,
                                                                   @RequestBody Map<String, String> userInfo) {
        return new SuccessResponse<>(SuccessCode.SET_PASSWORD,
                userService.resetUserPassword(token, userInfo), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to set a user's password using a token and user information.
     * </p>
     *
     * @param token    {@link String} The token to identify the user for whom the password is being set is given.
     * @param userInfo {@link Map} A map of user's information is given
     * @return {@link SuccessResponse<Map>} A map containing the response whether the password sets or not and
     * the status is returned
     */
    @PostMapping("/set-password/{token}")
    public SuccessResponse<Map<String, Boolean>> setUserPassword(@PathVariable(Constants.TOKEN) String token,
                                                                 @RequestBody Map<String, String> userInfo) {
        return new SuccessResponse<>(SuccessCode.SET_PASSWORD,
                userService.setUserPassword(token, userInfo), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to create a super admin user by taking a list of validated users.
     * </p>
     *
     * @param usersList {@link List<User>} The List of users need to create super admin is given
     * @return {@link SuccessResponse<String>} A success message with status is returned after creating the super admin
     */
    @PostMapping("/super-admin/create")
    public SuccessResponse<String> createSuperAdminUser(@Validated(UserInfo.class) @RequestBody List<User> usersList) {
        userService.createSuperAdmin(usersList);
        return new SuccessResponse<>(SuccessCode.CREATE_SUPER_ADMIN_USER, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to get the list of users information by role provided in the request.
     * </p>
     *
     * @param requestDTO {@link CfrRequestDTO} The request that contains necessary information required
     *                   to get the list of users is given
     * @return {@link SuccessResponse<List>} The list of users of given role is retrieved is returned with status
     */
    @PostMapping("/role-user-list")
    public SuccessResponse<List<User>> getUserListByRole(@RequestBody CommonRequestDTO requestDTO) {
        List<User> response = userService.getUserListByRole(requestDTO);
        return new SuccessResponse(SuccessCode.GET_USERS, response, response.size(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update the culture of a user using the provided details.
     * </p>
     *
     * @param requestDto {@link CultureRequestDTO} The culture needs to be updated is given
     * @return {@link SuccessResponse<String>} A success message with status is returned after updating the culture
     */
    @PostMapping("/update-culture")
    public SuccessResponse<String> updateCulture(@RequestBody CultureRequestDTO requestDto) {
        userService.updateCulture(requestDto);
        return new SuccessResponse<>(SuccessCode.USER_UPDATE, HttpStatus.OK);
    }
}