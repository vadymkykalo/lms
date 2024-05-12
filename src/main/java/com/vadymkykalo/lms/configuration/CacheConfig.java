package com.vadymkykalo.lms.configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import static com.vadymkykalo.lms.constants.CacheConstants.*;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return mapper;
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return new GenericJackson2JsonRedisSerializer(redisObjectMapper());
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration(RedisSerializer<Object> redisSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(DEFAULT_CACHE_TTL)
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(RedisSerializer<Object> redisSerializer) {
        return (builder) -> builder
                .withCacheConfiguration(ROLES_CACHE,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(ROLES_CACHE_TTL)
                                .disableCachingNullValues()
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer)));
    }

}
