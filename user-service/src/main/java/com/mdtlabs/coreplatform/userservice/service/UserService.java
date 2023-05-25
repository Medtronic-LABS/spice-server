package com.mdtlabs.coreplatform.userservice.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CultureRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PaginateDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserSuperAdminDto;
import com.mdtlabs.coreplatform.common.model.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * This an interface class for user module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface UserService {

    /**
     * <p>
     * This method is used to create a user using the details provided in the UserDTO.
     * </p>
     *
     * @param user {@link User} It contains the user details that need to be added
     * @return {@link User} The User for given user details is created and returned
     */
    User addUser(User user);

    /**
     * <p>
     * This method is used to update the user using the provided user details.
     * </p>
     *
     * @param user {@link User} The user to be updated is given
     * @return {@link User} The user for the given user details is
     * updated and returned with status
     */
    User updateUser(User user);

    /**
     * <p>
     * This method is used to delete the user using the details provided in request.
     * </p>
     *
     * @param userId The id of the user for which the user to be deleted is given
     * @return The admin user is deleted and returned as boolean value
     */
    boolean deleteUserById(long userId);

    /**
     * <p>
     * This method is used to retrieve the user for the given id.
     * </p>
     *
     * @param userId The id of the user for which the user to be retrieved is given
     * @return {@link User} The user for the given id is returned
     */
    User getUserById(long userId);

    /**
     * <p>
     * This method is used to get user by given user name.
     * </p>
     *
     * @param username {@link String} The username for which the user need to be retrieved is given
     * @return {@link User} The user for the given username is retrieved and returned
     */
    User getUserByUsername(String username);

    /**
     * <p>
     * This method is used to handle a forgot password validation request .
     * </p>
     *
     * @param email          {@link String} The username of the user who need to reset their password is given
     * @param user           The user contains information about the user, such as their name, password,
     *                       and other details is given.
     * @param isFromCreation The indication whether the "forgot password" request is being made during the
     *                       user creation process or not is given
     * @param appType        {@link String} The appType parameter is a path variable that is used to specify the
     *                       type of application for which the email template is being requested is given
     * @return {@link SuccessResponse<String>} Returns a message indicating that the password has been validated
     */
    Boolean forgotPassword(String email, User user, boolean isFromCreation, String appType);

    /**
     * <p>
     * This method is used to get the count of all users.
     * </p>
     *
     * @return The count of users is returned
     */
    int getTotalSize();

    /**
     * <p>
     * This method is used to get all users information with pagination.
     * </p>
     *
     * @param pageNumber The page number of the list of users to be retrieved
     * @return {@link List<User>} The List of users for the given pagination is returned
     */
    List<User> getUsers(int pageNumber);

    /**
     * <p>
     * This method is used to clear the api permission role map.
     * </p>
     */
    void clearApiPermissions();

    /**
     * <p>
     * This method is used to get a list of users based on a list of tenant IDs passed as a request.
     * </p>
     *
     * @param tenantIds {@link List<Long>} The list of tenant ids for which the users to be retrieved is given
     * @return {@link List<UserOrganizationDTO>} The List of users organization for the given tenant id is returned
     */
    List<UserOrganizationDTO> getUsersByTenantIds(List<Long> tenantIds);

    /**
     * <p>
     * This method is used to updates organization user.
     * </p>
     *
     * @param user       {@link User} The user contains information about the user, such as their name, password, and other details is given
     * @param isSiteUser The boolean value indicating whether the user is site user or not
     * @param isRedRisk  The status of red risk of user is given
     * @return {@link User} The user for the given user detail is returned
     */
    User updateOrganizationUser(User user, boolean isSiteUser, boolean isRedRisk);

    /**
     * <p>
     * This method is used to delete an organization User of the given id and tenant id.
     * </p>
     *
     * @param requestDto The request data with user id and tenantId is given
     * @return {@link Boolean} The boolean value whether the organization user of
     * given id and tenant id is deleted or not is returned
     */
    Boolean deleteOrganizationUser(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to validate a user by its username using the provided details.
     * </p>
     *
     * @param requestUsers {@link List<String>} The list of usernames of the users to be validated is given
     */
    void validateUsers(List<String> requestUsers);

    /**
     * <p>
     * This method is used to get the list of users of given search term and tenant id
     * who are all not deleted.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being used to filter the results is given
     * @param tenantId   {@link Long} The tenant id of the users to be retrieved is given
     * @return {@link List<User>} The list of users for given search term and tenant id
     * is retrieved and returned
     */
    List<User> getUserBySearchTermAndTenantId(Long tenantId, String searchTerm);

    /**
     * <p>
     * This method is used to validate a user by its username using the provided details.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request that contains necessary information required
     *                   to get the list of users is given
     * @return {@link UserListDTO} The validated user for the given request is returned
     */
    UserListDTO validateUser(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to get all super-admin users details with pagination.
     * </p>
     *
     * @param searchRequest {@link PaginateDTO} The request that contains pagination information required
     *                      to get the list of users is given
     * @return {@link ResponseListDTO} A Object of total count and the List of user super admin for the given pagination
     * is returned
     */
    ResponseListDTO getSuperAdminUsers(PaginateDTO searchRequest);

    /**
     * <p>
     * This method is used to remove super-admin role for the provided user.
     * </p>
     *
     * @param id {@link Long} The id of the user who need to be removed
     */
    void removeSuperAdmin(Long id);

    /**
     * <p>
     * This method is used to update super admin user details by id.
     * </p>
     *
     * @param userDto {@link UserSuperAdminDto} The user super admin to update is given
     */
    void updateSuperAdmin(UserSuperAdminDto userDto);

    /**
     * <p>
     * This method is used to get a super admin users details.
     * </p>
     *
     * @param id {@link Long} The id of the user who need to be retrieved is given
     * @return {@link UserSuperAdminDto} The user super admin for given id is retrieved and returned
     */
    UserSuperAdminDto getSuperAdminById(Long id);

    /**
     * <p>
     * This method is to activate or deactivate users based on tenantIds.
     * </p>
     *
     * @param tenantIds {@link List<Long>} The list of tenantIds that belongs to the users which to
     *                  be activated or deactivated is given
     * @param isActive  The active status of the user is given
     * @return {@link Boolean} The boolean value whether the activation/deactivation of the user
     * is successful or not is returned.
     */
    Boolean activateDeactivateUser(List<Long> tenantIds, boolean isActive);

    /**
     * <p>
     * This method is used to search users based on a given request.
     * </p>
     *
     * @param requestDTO {@link SearchRequestDTO} The request that contains necessary information required
     *                   to get the list of users is given
     * @return {@link ResponseListDTO} The List of users and total count of users for the given search
     * request is returned
     */
    ResponseListDTO getAdminUsers(SearchRequestDTO requestDTO);

    /**
     * <p>
     * This method is used to get user information of the given set of ids.
     * </p>
     *
     * @param ids {@link Set<Long>} The ids of the users to be retrieved is given
     * @return {@link Map} A map of id and user information for given set of IDs is returned
     */
    Map<Long, User> getUserByIds(Set<Long> ids);

    /**
     * <p>
     * This method is used to get list of users having the given role name.
     * </p>
     *
     * @param roleName {@link String} The role name of the user to be searched is given
     * @return {@link List<User>} The list of users for the given name is retrieved and returned
     */
    List<User> getUserByRoleName(String roleName);

    /**
     * <p>
     * This method is used to verify a set password token using the given token.
     * </p>
     *
     * @param token {@link String} The token to verify user identity before allowing them to set a new password
     * @return {@link Map} A map containing the response whether the password resets or not is returned
     */
    Map<String, Object> verifySetPassword(String token);

    /**
     * <p>
     * This method is used to unlock a user based on the request.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The request that contains necessary information required
     *                   to get the list of users is given
     */
    void unlockUser(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to retrieve a list of locked users with respect
     * to the provided details.
     * </p>
     *
     * @param requestObject {@link RequestDTO} The conditions to get the locked users is given
     * @return {@link Map} A map of total count and list of locked users is retrieved and returned
     */
    ResponseListDTO getLockedUsers(RequestDTO requestObject);

    /**
     * <p>
     * This method is used to changes the password of a site user using given request.
     * </p>
     *
     * @param userId      {@link RequestDTO} The id of the user whose password need to be changed is given
     * @param newPassword {@link String} The new password to be changed is given
     */
    void changeSiteUserPassword(Long userId, String newPassword);

    /**
     * <p>
     * This method is used to reset a user's password using a token and user information.
     * </p>
     *
     * @param token    {@link String} The token to identify the user for whom the password is being reset is given.
     * @param userInfo {@link Map} A map of user's information is given
     * @return {@link Map} A map containing the response whether the password resets or not is returned
     */
    Map<String, Object> resetUserPassword(String token, Map<String, String> userInfo);

    /**
     * <p>
     * This method is used to set a user's password using a token and user information.
     * </p>
     *
     * @param token    {@link String} The token to identify the user for whom the password is being set is given.
     * @param userInfo {@link Map} A map of user's information is given
     * @return {@link Map} A map containing the response whether the password sets or not is returned
     */
    Map<String, Object> setUserPassword(String token, Map<String, String> userInfo);

    /**
     * <p>
     * This method is used to create a super admin user by taking a list of users.
     * </p>
     *
     * @param usersList {@link List<User>} The List of users need to create super admin is given
     */
    void createSuperAdmin(List<User> usersList);

    /**
     * <p>
     * This method is used to add new users.
     * </p>
     *
     * @param users        {@link List<User>} The List of new users to add is given
     * @param newUsernames {@link List<User>} The List of new usernames to validate is given
     */
    void addUsers(List<User> users, List<String> newUsernames);


    /**
     * <p>
     * This method is used to get the list of users information by role provided in the request.
     * </p>
     *
     * @param requestDTO {@link CfrRequestDTO} The request that contains necessary information required
     *                   to get the list of users is given
     * @return {@link List<User>} The list of users of given role is retrieved and returned
     */
    List<User> getUserListByRole(CommonRequestDTO requestDTO);

    /**
     * <p>
     * This method is used to update the culture of a user using the provided details.
     * </p>
     *
     * @param requestDTO {@link CultureRequestDTO} The culture needs to be updated is given
     */
    void updateCulture(CultureRequestDTO requestDTO);

    /**
     * <p>
     * This method is used to update red risk value for an existing user.
     * </p>
     *
     * @param existingUser The user whose red risk value to be updated is given
     * @param isRedRisk    The status of red risk to be updated is given
     */
    void redRiskUserUpdate(User existingUser, boolean isRedRisk);
}
