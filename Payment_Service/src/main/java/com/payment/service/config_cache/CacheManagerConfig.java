package com.payment.service.config_cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
public class CacheManagerConfig {

    @Value("${redis.ttl.default}")
    private String defaultTTL;

    @Value("${redis.ttl.payment_cache_repository}")
    private String paymentCacheTTL;

    @Bean(name = "customPaymentCacheManager")
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .withCacheConfiguration("payment_cache_repository", RedisCacheConfiguration
                        .defaultCacheConfig().entryTtl(Duration.ofMinutes(Long.parseLong(paymentCacheTTL))))
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(Long.parseLong(defaultTTL))))
                .build();
    }
}
