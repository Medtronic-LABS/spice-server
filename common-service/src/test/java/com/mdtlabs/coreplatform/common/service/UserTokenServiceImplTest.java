package com.mdtlabs.coreplatform.common.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.TestConstants;
import com.mdtlabs.coreplatform.common.TestDataProvider;
import com.mdtlabs.coreplatform.common.model.entity.UserToken;
import com.mdtlabs.coreplatform.common.repository.CommonRepository;
import com.mdtlabs.coreplatform.common.repository.UserTokenRepository;
import com.mdtlabs.coreplatform.common.service.impl.UserTokenServiceImpl;

/**
 * <p>
 * UserTokenServiceImplTest class has the test methods for the UserTokenServiceImpl class.
 * </p>
 *
 * @author JohnKennedy created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserTokenServiceImplTest {

    @InjectMocks
    private UserTokenServiceImpl userTokenService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private CommonRepository commonRepository;

    @Mock
    private UserTokenRepository userTokenRepository;

    @Test
    void saveUserToken() {
        //given
        String key = new StringBuilder().append(Constants.SPICE).append(Constants.COLON).append(Constants.LOGIN)
                .append(Constants.COLON).append(Constants.USER_DATA).append(Constants.COLON)
                .append(Constants.TOKEN).toString();
        UserToken usertoken = TestDataProvider.getUserToken();
        ListOperations listOperations = mock(ListOperations.class);

        //when
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(redisTemplate.expire(key, Constants.AUTH_TOKEN_EXPIRY_MINUTES, TimeUnit.MINUTES))
                .thenReturn(Constants.BOOLEAN_TRUE);
        when(userTokenRepository.save(usertoken)).thenReturn(usertoken);

        //then
        userTokenService.saveUserToken(Constants.TOKEN, Constants.USER_DATA, Constants.HEADER_CLIENT, 1L);
        verify(userTokenRepository, atLeastOnce()).save(usertoken);
        Assertions.assertNotNull(usertoken);
        Assertions.assertEquals(TestConstants.ONE, usertoken.getUserId());
    }

    @Test
    void deleteUserTokenByToken() {
        //given
        UserToken usertoken = TestDataProvider.getUserToken();

        //when
        when(redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY)).thenReturn(Constants.BOOLEAN_TRUE);
        when(userTokenRepository.findByAuthToken(Constants.TOKEN)).thenReturn(usertoken);
        when(userTokenRepository.save(usertoken)).thenReturn(usertoken);

        //then
        userTokenService.deleteUserTokenByToken(Constants.ORGANIZATION_REDIS_KEY, Constants.TOKEN);
        verify(userTokenRepository, atLeastOnce()).save(usertoken);
        Assertions.assertNotNull(usertoken);
        Assertions.assertEquals(Constants.TOKEN, usertoken.getAuthToken());
        Assertions.assertFalse(usertoken.isActive());
    }

    @Test
    void deleteUserTokenByUserName() {
        //given
        UserToken usertoken = TestDataProvider.getUserToken();
        List<UserToken> userTokens = List.of(usertoken);

        //when
        when(userTokenRepository.findByUserIdAndIsActiveTrue(TestConstants.ONE)).thenReturn(userTokens);
        when(redisTemplate.delete(Constants.TOKEN)).thenReturn(Constants.BOOLEAN_TRUE);
        when(userTokenRepository.saveAll(userTokens)).thenReturn(userTokens);

        //then
        userTokenService.deleteUserTokenByUserName(Constants.USER_DATA, TestConstants.ONE);
        verify(userTokenRepository, atLeastOnce()).saveAll(userTokens);
        Assertions.assertNotNull(userTokens);
        Assertions.assertEquals(TestConstants.ONE, userTokens.get(Constants.ZERO).getUserId());
    }
}