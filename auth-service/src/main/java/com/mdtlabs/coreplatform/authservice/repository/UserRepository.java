package com.mdtlabs.coreplatform.authservice.repository;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the user module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar created on Aug 26, 2022
 * @since Aug 26, 2022
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    String GET_USER_BY_USERNAME = "select user from User as user "
            + "where user.username =:username and user.isActive =:status and user.isDeleted=false";

    /**
     * <p>
     * This method is used to get user data by passing username.
     * </p>
     *
     * @param username username of the user is given
     * @param status   active status of the user is given
     * @return {@link User}   user information which is stored
     */
    @Query(value = GET_USER_BY_USERNAME)
    User getUserByUsername(@Param(FieldConstants.USERNAME) String username,
                           @Param(FieldConstants.STATUS) Boolean status);

}
