package com.vadymkykalo.lms.configuration;

import lombok.RequiredArgsConstructor;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import java.util.HashMap;
import java.util.Map;

import static com.vadymkykalo.lms.constants.CacheConstants.ROLES_CACHE;
import static com.vadymkykalo.lms.constants.CacheConstants.ROLES_CACHE_TTL;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            final RedissonConnectionFactory redissonConnectionFactory
    ) {
        Map<String, RedisCacheConfiguration> caches = new HashMap<>();
        caches.put(ROLES_CACHE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(ROLES_CACHE_TTL));

        return RedisCacheManager.builder(redissonConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                .withInitialCacheConfigurations(caches)
                .build();
    }

}
