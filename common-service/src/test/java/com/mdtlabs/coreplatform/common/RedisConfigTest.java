package com.mdtlabs.coreplatform.common;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.mdtlabs.coreplatform.common.model.entity.Organization;

/**
 * <p>
 * RedisConfig class used to test all possible positive
 * and negative cases for all methods and conditions used in RedisConfig
 * class.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RedisConfigTest {

    @InjectMocks
    private RedisConfig redisConfig;

    @Test
    void connectionFactory() {
        //given 
        ReflectionTestUtils.setField(redisConfig, "redisHost", "localhost");
        ReflectionTestUtils.setField(redisConfig, "redisPort", 6782);

        //then
        JedisConnectionFactory response = redisConfig.connectionFactory();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(6782, response.getPort());
    }

    @Test
    void redisCacheManagerBuilderCustomizer() {
        //then
        RedisCacheManagerBuilderCustomizer response = redisConfig.redisCacheManagerBuilderCustomizer();
        Assertions.assertNotNull(response);
    }

    @Test
    void cacheConfiguration() {
        //then
        RedisCacheConfiguration response = redisConfig.cacheConfiguration();
        Assertions.assertNotNull(response);
    }

    @Test
    void redisTemplate() {
        //then
        RedisTemplate<String, List<Organization>> response = redisConfig.redisTemplate();
        Assertions.assertNotNull(response);
    }
}
