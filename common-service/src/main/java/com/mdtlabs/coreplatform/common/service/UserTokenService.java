package com.mdtlabs.coreplatform.common.service;

/**
 * <p>
 * This an interface class for UserToken entity
 * </p>
 *
 * @author Jeyaharini T A
 */
public interface UserTokenService {

    /**
     * To save the UserToken
     *
     * @param authToken auth token
     */
    void saveUserToken(String authToken, String username, String client, long userId);

    /**
     * <p>
     * This method used to delete all UserTokens by given authToken.
     * </p>
     *
     * @param redisKey redis key
     */
    void deleteUserTokenByToken(String redisKey, String token);

    /**
     * <p>
     * Deletes an user token.
     * </p>
     *
     * @param username  user email ID
     * @param userId    user ID
     */
    void deleteUserTokenByUserName(String username, Long userId);

}