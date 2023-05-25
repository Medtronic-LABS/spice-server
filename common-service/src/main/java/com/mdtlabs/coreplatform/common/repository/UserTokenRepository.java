package com.mdtlabs.coreplatform.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the user module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar created on Aug 26, 2022
 */
@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    /**
     * <p>
     * Finds user token by user authentication token.
     * </p>
     *
     * @param token  authentication token
     * @return {@link UserToken}
     */
    UserToken findByAuthToken(String token);

    /**
     * <p>
     * Finds user token by user ID.
     * </p>
     *
     * @param userId  user ID
     * @return {@link List<UserToken>}  List  of UserToken
     */
    List<UserToken> findByUserIdAndIsActiveTrue(Long userId);

}
