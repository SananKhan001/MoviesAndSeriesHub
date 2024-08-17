package com.Core_Service.config_cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
public class CacheManagerConfig {

    @Value("${redis.ttl.episode_cache}")
    private String episodeCacheTTL;

    @Value("${redis.ttl.movie_cache}")
    private String movieCacheTTL;

    @Value("${redis.ttl.series_cache}")
    private String seriesCacheTTL;

    @Value("${redis.ttl.default}")
    private String defaultTTL;

    @Bean(name = "customCacheManager")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .withCacheConfiguration("episode_cache",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(Long.parseLong(episodeCacheTTL))))
                .withCacheConfiguration("movie_cache",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(Long.parseLong(movieCacheTTL))))
                .withCacheConfiguration("series_cache",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(Long.parseLong(seriesCacheTTL))))
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(Long.parseLong(defaultTTL))))
                .build();
    }
}
