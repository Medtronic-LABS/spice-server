package com.mdtlabs.coreplatform.common;

import com.mdtlabs.coreplatform.common.model.entity.Organization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;


/**
 * <p>
 * Redis configuration on user token.
 * </p>
 *
 * @author Prabu Created on 04 Jan 2023
 */
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    /**
     * <p>
     * Connection factory bean to register jedis connection factory.
     * </p>
     *
     * @return {@link JedisConnectionFactory} - bean
     */
    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redisStandaloneconfiguration = new RedisStandaloneConfiguration();
        redisStandaloneconfiguration.setHostName(redisHost);
        redisStandaloneconfiguration.setPort(redisPort);
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(Constants.ONE);
        poolConfig.setBlockWhenExhausted(Constants.BOOLEAN_TRUE);
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisConfig =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        jedisConfig.poolConfig(poolConfig);
        JedisClientConfiguration jedisClientConfig = jedisConfig.build();
        return new JedisConnectionFactory(redisStandaloneconfiguration, jedisClientConfig);
    }

    /**
     * <p>
     * Redis cache manager builder customizer bean is to register builder.
     * </p>
     *
     * @return {@link RedisCacheManagerBuilderCustomizer} - bean
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder.withCacheConfiguration(Constants.TOKENS,
                RedisCacheConfiguration.defaultCacheConfig());
    }

    /**
     * <p>
     * Cache configuration bean is to register redis cache configuration.
     * </p>
     *
     * @return {@link RedisCacheConfiguration} - bean
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    /**
     * <p>
     * Redis template bean is to register redis template.
     * </p>
     *
     * @return {@link RedisTemplate} - bean
     */
    @Bean
    RedisTemplate<String, List<Organization>> redisTemplate() {
        final RedisTemplate<String, List<Organization>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.afterPropertiesSet();
        return template;
    }

}