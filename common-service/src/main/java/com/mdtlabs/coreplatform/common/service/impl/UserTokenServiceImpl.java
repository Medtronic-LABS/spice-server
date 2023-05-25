package com.mdtlabs.coreplatform.common.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.UserToken;
import com.mdtlabs.coreplatform.common.repository.CommonRepository;
import com.mdtlabs.coreplatform.common.repository.UserTokenRepository;
import com.mdtlabs.coreplatform.common.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This a Service class for UserToken
 *
 * @author Jeyaharini T A
 */
@Service("commonUserTokenService")
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    /**
     * {@inheritDoc}
     */
    public void saveUserToken(String authToken, String username, String client, long userId) {
        String key = Constants.SPICE + Constants.COLON + Constants.LOGIN +
                Constants.COLON + username + Constants.COLON + authToken;
        redisTemplate.opsForList().leftPush(key, authToken);
        redisTemplate.expire(key, Constants.AUTH_TOKEN_EXPIRY_MINUTES, TimeUnit.MINUTES);
        UserToken usertoken = new UserToken();
        usertoken.setAuthToken(authToken);
        usertoken.setClient(client);
        usertoken.setUserId(userId);
        userTokenRepository.save(usertoken);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteUserTokenByToken(String redisKey, String token) {
        redisTemplate.delete(redisKey);
        UserToken userToken = userTokenRepository.findByAuthToken(token);
        userToken.setActive(Constants.BOOLEAN_FALSE);
        userTokenRepository.save(userToken);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteUserTokenByUserName(String username, Long userId) {
        List<UserToken> userTokens = userTokenRepository.findByUserIdAndIsActiveTrue(userId);
        List<String> tokens = userTokens.stream().map(authToken -> Constants.SPICE + Constants.COLON + Constants.LOGIN +
                Constants.COLON + username + Constants.COLON +
                authToken.getAuthToken()).toList();
        redisTemplate.delete(tokens);
        userTokens.forEach(token -> token.setActive(Constants.BOOLEAN_FALSE));
        userTokenRepository.saveAll(userTokens);
    }

}
