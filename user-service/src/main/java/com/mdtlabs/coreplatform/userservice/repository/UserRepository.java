package com.mdtlabs.coreplatform.userservice.repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the user module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    String SEARCH_TERM_CHECK = "AND (:searchTerm is null or lower(user.username) "
            + "LIKE CONCAT('%',lower(:searchTerm),'%')) ";

    String GET_ALL_USERS = "select user from User as user where user.isActive =:status";

    String GET_USER_BY_ID = "select user from User as user where"
            + " user.id =:userId and user.isActive =:status and user.isDeleted=false";

    String GET_USER_BY_USERNAME = "select user from User as user "
            + "where user.username =:username and user.isActive =:status and user.isDeleted=false";

    String GET_USERS_BY_TENANT_IDS = "select user from User user "
            + "join user.organizations as org where org.id in (:tenantIds) AND user.isDeleted=false"
            + " and user.isActive=:isActive";

    String GET_USER_BY_SEARCH_TERM_AND_TENANT_ID = "select user from User as user "
            + "join user.organizations as org where org.id=:tenantId AND user.isDeleted=false "
            + "AND ((:searchTerm is null or lower(user.firstName) LIKE CONCAT('%',lower(:searchTerm),'%')) "
            + "OR (:searchTerm is null or lower(user.lastName) LIKE CONCAT('%',lower(:searchTerm),'%')) "
            + "OR (:searchTerm is null or lower(user.username) LIKE CONCAT('%',lower(:searchTerm),'%')))";

    String SEARCH_USERS = "select distinct user from User user "
            + "join user.organizations as org where org.id in (:tenantIds) AND user.isDeleted=false and "
            + "user.isActive=true AND ((:searchTerm is null or lower(user.firstName) LIKE"
            + " CONCAT('%',lower(:searchTerm),'%')) OR (:searchTerm is null or lower(user.lastName) LIKE "
            + "CONCAT('%',lower(:searchTerm),'%')) OR (:searchTerm is null or lower(user.username) LIKE "
            + "CONCAT('%',lower(:searchTerm),'%'))) AND org.formName=:type";

    String GET_USERS_BY_ROLE_NAME = "select user from User as user join user.roles "
            + "as role where role.name=:roleName and user.isActive=true";

    String GET_LOCKED_USERS = "select distinct user from User user join user.organizations "
            + "as org where org.id in (:tenantIds)  AND user.isBlocked = true AND user.isDeleted = false "
            + SEARCH_TERM_CHECK;

    String GET_BLOCKED_USERS = "select user from User user where user.isBlocked = true"
            + " AND user.isDeleted = false "
            + SEARCH_TERM_CHECK;

    String GET_LOCKED_USERS_COUNT = "select distinct count(user.id) from User user join "
            + "user.organizations as org where org.id in (:tenantIds)  AND user.isBlocked = true AND "
            + "user.isDeleted = false "
            + SEARCH_TERM_CHECK;

    String GET_BLOCKED_USERS_COUNT = "select count(user.id) from User user where "
            + "user.isBlocked = true AND user.isDeleted = false "
            + SEARCH_TERM_CHECK;

    String SEARCH_SUPER_ADMIN_USERS = "select user from User as user join user.roles as role"
            + " WHERE role.name=:roleName AND user.isActive=true AND user.isDeleted=false "
            + " AND ((:searchTerm is null) or (lower(user.firstName) LIKE CONCAT('%',lower(:searchTerm),'%') "
            + " OR (lower(user.lastName) LIKE CONCAT('%',lower(:searchTerm),'%')) "
            + " OR (lower(user.username) LIKE CONCAT('%',lower(:searchTerm),'%')))) ";

    String GET_USERS_BY_ROLE_NAMES = "Select user from User user join user.roles as role "
            + "join user.organizations as org where org.id=:tenantId AND (:searchTerm is null or lower(user.username) "
            + "LIKE CONCAT('%',lower(:searchTerm),'%')) AND role.name in :roleNames AND user.isDeleted=false";

    String GET_ROLES_BY_USER_ID = "select u.roles from User u where u.id = :userId";

    String GET_USERS = "select u as user, "
            + "o.formName as applicationName, o.name as orgName, o.parentOrganizationId as parentOrganization from "
            + "User u left outer join Organization o on u.tenantId=o.id where "
            + "u.country.id = :countryId and u.isDeleted = false and u.isActive = true and u.tenantId is not null and "
            + "(:searchTerm is null or u.username LIKE CONCAT('%',:searchTerm,'%')) ";

    String GET_SITE_LIST_BY_USER_ID = "select s.\"name\", s.tenant_id as tenantId, s.id, "
            + "(select r.\"name\" from user_organization uo, \"user\" u1, user_role ur, \"role\" r "
            + "where u1.id = uo.user_id and u1.id = ur.user_id and ur.role_id = r.id and u1.id = u.id "
            + "and uo.organization_id = s.tenant_id and r.\"name\" = 'EMR_SITE_ADMIN') is not null as isAssigned, "
            + "s.tenant_id = u.tenant_id as isDefault from  \"user\" u, site s where  u.country_id = s.country_id "
            + "and u.id = :userId and u.is_deleted = false and s.is_deleted = false and s.is_active = true "
            + "order by s.\"name\"";

    String GET_USER_AND_ORGANIZATION_BY_USER_ID = "select u as user, o.formName as applicationName from User u left "
            + "outer join Organization o on u.tenantId=o.id where u.id = :userId and u.isDeleted = false ";

    /**
     * <p>
     * This method is used to get the user of given user id from the database
     * who is either active or inactive and not deleted.
     * </p>
     *
     * @param userId The id of the user for which the user to be retrieved from a database is given
     * @param status The status that is used to filter the list of users that have the specified active status is given
     * @return {@link User} The user for given id and active status is retrieved and returned from the database
     */
    @Query(value = GET_USER_BY_ID)
    User getUserById(@Param(Constants.USER_ID_PARAM) long userId, @Param(FieldConstants.STATUS) Boolean status);

    /**
     * <p>
     * This method is used to get the user of given user name from the database
     * who is either active or inactive and not deleted.
     * </p>
     *
     * @param username {@link String} The name of the user who need to retrieve from the database is given
     * @param status   The status that is used to filter the list of users that have the specified active status is given
     * @return {@link User} The user for given username and active status is retrieved and returned from the database
     */
    @Query(value = GET_USER_BY_USERNAME)
    User getUserByUsername(@Param(FieldConstants.USERNAME) String username,
                           @Param(FieldConstants.STATUS) Boolean status);

    /**
     * <p>
     * This method is used to get the page of users from the database
     * who are all either active or inactive.
     * </p>
     *
     * @param status   {@link Boolean} The status that is used to filter the list of users
     *                 that have the specified active status is given
     * @param pageable {@link Pageable} The pagination information that contains information such as the page number,
     *                 page size, sorting criteria, and more is given
     * @return {@link Page<User>}  A Page of Users that match the search criteria for super admin users, with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_ALL_USERS)
    Page<User> getUsers(@Param(FieldConstants.STATUS) Boolean status, Pageable pageable);

    /**
     * <p>
     * This method is used to get the list of users from the database
     * who are all either active or inactive.
     * </p>
     *
     * @param status {@link Boolean} The status that is used to filter the list of users that have the
     *               specified active status is given
     * @return {@link List<User>} The list of users for given active status is retrieved and
     * returned from database
     */
    @Query(value = GET_ALL_USERS)
    List<User> getUsers(@Param(FieldConstants.STATUS) Boolean status);

    /**
     * <p>
     * This method is used to get the list of users of given user name from the database
     * who are all either active or inactive.
     * </p>
     *
     * @param newUserEmails {@link List<String>}
     * @return {@link List<User>} The list of non-deleted users for given usernames
     * is retrieved and returned from database
     */
    List<User> findByUsernameInAndIsDeletedFalse(List<String> newUserEmails);

    /**
     * <p>
     * This method is used to get the list of users of given list of tenant ids from the database
     * who are all either active or inactive and not deleted.
     * </p>
     *
     * @param tenantIds {@link List<Long>} The list of tenantIds that belongs to the users who are all
     *                  need to be retrieved is given
     * @param isActive  The status that is used to filter the list of users that have the specified
     *                  active status is given
     * @return {@link List<User>} The list of users for given list of tenant IDs and active status is retrieved and
     * returned from database
     */
    @Query(value = GET_USERS_BY_TENANT_IDS)
    List<User> findUsersByTenantIds(@Param(Constants.TENANT_IDS) List<Long> tenantIds,
                                    @Param(Constants.IS_ACTIVE) boolean isActive);

    /**
     * <p>
     * This method is used to get the user of given tenant id and id from the database
     * who is active.
     * </p>
     *
     * @param id       {@link Long} The id of the user for which the user to be retrieved from a database is given
     * @param tenantId {@link Long} The tenant id of the user to be retrieved is given
     * @return {@link User} The active user and having given id and tenant id is retrieved
     * and returned from the database
     */
    User findByIdAndTenantIdAndIsActiveTrue(Long id, Long tenantId);

    /**
     * <p>
     * This method is used to get the user of given id from the database
     * who is active.
     * </p>
     *
     * @param userId The id of the user for which the user to be retrieved from a database is given
     * @return {@link User} The active user and having given id is retrieved
     * and returned from the database
     */
    User findByIdAndIsActiveTrue(Long userId);

    /**
     * <p>
     * This method is used to get the user of given user name from the database
     * who is not deleted.
     * </p>
     *
     * @param email {@link String} The username of the user to be searched is given
     * @return {@link User} The non-deleted user for given username(ignore case) is retrieved
     * and returned from the database
     */
    User findByUsernameAndIsDeletedFalse(String email);

    /**
     * <p>
     * This method is used to get the list of users of given search term and tenant id from the database
     * who are all not deleted.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to filter the results is given
     * @param tenantId   {@link Long} The tenant id of the users to be retrieved is given
     * @return {@link List<User>} The list of non-deleted users for given search term and tenant id
     * is retrieved and returned from database
     */
    @Query(value = GET_USER_BY_SEARCH_TERM_AND_TENANT_ID)
    List<User> findUserBySearchTermAndTenantId(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                               @Param(Constants.TENANT_PARAMETER_NAME) Long tenantId);

    /**
     * <p>
     * This method is used to get the page of users of given list of tenant ids, search term
     * and user type with given pageable from the data base who are all not deleted.
     * </p>
     *
     * @param tenantIds  {@link List<Long>} The list of tenantIds that belongs to the users who are all need to
     *                   be retrieved is given
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to filter the
     *                   results is given
     * @param userType   {@link String} The type of the user to be searched is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<User>}  A Page of Users that match the search criteria for super admin users, with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = SEARCH_USERS)
    Page<User> searchUsersByTenantIds(@Param(Constants.TENANT_IDS) List<Long> tenantIds,
                                      @Param(Constants.SEARCH_TERM_FIELD) String searchTerm, @Param(Constants.TYPE) String userType,
                                      Pageable pageable);

    /**
     * <p>
     * This method is used to get the list of users of given set of ids from the database.
     * </p>
     *
     * @param ids {@link Set<Long>}
     * @return {@link List<User>} The list of users for given set of IDs is retrieved and returned from database
     */
    List<User> findByIdIn(Set<Long> ids);

    /**
     * <p>
     * This method is used to get the list of users of given role name from the database
     * who are all active.
     * </p>
     *
     * @param roleName {@link String} The role name of the user to be retrieved is given
     * @return {@link List<User>} The list of active users for given role is retrieved and returned from database
     */
    @Query(value = GET_USERS_BY_ROLE_NAME)
    List<User> getUsersByRoleName(@Param(Constants.ROLE_NAME_CLAIM) String roleName);

    /**
     * <p>
     * This method is used to get the user of given forgot password token from the database.
     * </p>
     *
     * @param token {@link String} The token to identify the user for whom the password is being forgotten is given
     * @return {@link User} The user for given password token is retrieved and returned from database
     */
    User findByForgetPasswordToken(String token);

    /**
     * <p>
     * This method is used to get the user of given id from the database
     * who is not deleted.
     * </p>
     *
     * @param id {@link Long} The id of the user for which the user to be retrieved from a database is given
     * @return {@link User} The non-deleted user for given id is retrieved and returned from the database
     */
    User findByIdAndIsDeletedFalse(Long id);

    /**
     * <p>
     * This method is used to get the list of distinct users of given search term
     * and list of tenant ids with pageable from the database who are all
     * blocked and not deleted.
     * </p>
     *
     * @param tenantIds  {@link List<Long>} The list of tenantIds that belongs to the users who are locked is given
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to filter the
     *                   results is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link List<User>} The list of locked users for given search term, list of tenant ids and pagination
     * is retrieved and returned from database
     */
    @Query(value = GET_LOCKED_USERS)
    Page<User> getLockedUsers(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,  Pageable pageable,
                              @Param(Constants.TENANT_IDS) List<Long> tenantIds);

    /**
     * <p>
     * This method is used to get the list of users of given search term with pageable
     * from the database who are all blocked and not deleted.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to filter the results is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link List<User>} The list of blocked users for given search term and pagination is retrieved and
     * returned from database
     */
    @Query(value = GET_BLOCKED_USERS)
    Page<User> getLockedUsers(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm, Pageable pageable);

    /**
     * <p>
     * This method is used to get the count of users of given search term
     * and list of tenant ids with pageable from the database who are all
     * blocked and not deleted.
     * </p>
     *
     * @param tenantIds  {@link List<Long>} The list of tenantIds that belongs to the users who are all locked is given
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to filter the results is given
     * @return {@link Integer} The count of the locked users for given search term and tenant IDs is retrieved and
     * returned from database
     */
    @Query(value = GET_LOCKED_USERS_COUNT)
    Integer getLockedUsersCount(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                @Param(Constants.TENANT_IDS) List<Long> tenantIds);

    /**
     * <p>
     * This method is used to get the count users of given search term from the database who are all
     * blocked and not deleted.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @return {@link Integer} The count of the blocked users for given search term is retrieved and
     * returned from database
     */
    @Query(value = GET_BLOCKED_USERS_COUNT)
    Integer getBlockedUsersCount(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm);

    /**
     * <p>
     * This method is used to get list of super admin users based on given role ID and a search term.
     * </p>
     *
     * @param roleId     {@link String} The role name of the user for which the user to be retrieved from a database
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query
     *                   to filter the results is given
     * @return {@link List<User>} The list of super admin users based on given role ID and a search
     * term is retrieved and returned from database
     */
    @Query(value = SEARCH_SUPER_ADMIN_USERS)
    List<User> searchSuperAdminUsers(@Param(Constants.ROLE_NAME_CLAIM) String roleName,
                                     @Param("searchTerm") String searchTerm);


    /**
     * <p>
     * This method is used to get page of super admin users with pagination based on a role ID and a search term.
     * </p>
     *
     * @param roleName     {@link String} The role name of the user for which the user to be retrieved from a database is given
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to filter the
     *                   results is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<User>} A Page of Users that match the search criteria for super admin users, with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = SEARCH_SUPER_ADMIN_USERS)
    Page<User> searchSuperAdminUsersWithPagination(@Param(Constants.ROLE_NAME_CLAIM) String roleName,
                                                   @Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * <p>
     * This method is used to get a list of users based on given role IDs, tenant ID, and search term.
     * </p>
     *
     * @param roleNames    {@link List<String>} The names representing the roles of the users is given
     * @param tenantId   {@link Long} The tenant id of the users to be retrieved is given
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query
     *                   to filter the results is given
     * @return {@link List<User>} The list of users for the given list of role IDs, search term and
     * tenant id is retrieved and returned from the database
     */
    @Query(value = GET_USERS_BY_ROLE_NAMES)
    List<User> findUsersByRoleNames(@Param("roleNames") List<String> roleNames, @Param("tenantId") Long tenantId,
                                  @Param("searchTerm") String searchTerm);

    /**
     * <p>
     * This method is used to retrieve a set of roles associated with the given user ID.
     * </p>
     *
     * @param userId {@link Long} The id of the user for which the user to be retrieved from a database is given
     * @return Set of {@link Set<Role>} The set of roles for the given user id is retrieved and returned
     * from the database
     */
    @Query(value = GET_ROLES_BY_USER_ID)
    Set<Role> findRolesById(@Param("userId") Long userId);

    /**
     * <p>
     * This method is used to retrieve a page of users based on a given country ID and search term.
     * </p>
     *
     * @param countryId  {@link Long} The country id of the user for which the user to be retrieved from a
     *                   database is given
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to filter
     *                   the results is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<Map>} A paginated result using the provided pageable details where the keys are
     * the column names and the values are the corresponding values for each user
     */
    @Query(value = GET_USERS)
    Page<Map<String, Object>> findUsers(@Param("countryId") Long countryId, @Param("searchTerm") String searchTerm,
                                        Pageable pageable);

    /**
     * <p>
     * This method is used to retrieve a list of sites associated with a given user ID.
     * </p>
     *
     * @param userId {@link Long} The id of the user for which the user to be retrieved from a database is given
     * @return {@link List<Map>} A list of maps, where each map contains key-value pairs representing the site information for a user with
     * the given user ID and the keys in the map correspond to the column names in the database table for the site
     * information is returned
     */
    @Query(value = GET_SITE_LIST_BY_USER_ID, nativeQuery = true)
    List<Map<String, Object>> findSiteListByUserId(@Param("userId") Long userId);

    /**
     * <p>
     * This method is used to get a map of user and organization data based on a given user ID.
     * </p>
     *
     * @param id {@link Long} The id of the user for which the user to be retrieved from a database is given
     * @return {@link Map} A map which contains information about a user and their associated organization,
     * based on the provided id is returned
     */
    @Query(value = GET_USER_AND_ORGANIZATION_BY_USER_ID)
    Map<String, Object> findUserAndOrganizationByUserId(@Param("userId") Long id);
}
