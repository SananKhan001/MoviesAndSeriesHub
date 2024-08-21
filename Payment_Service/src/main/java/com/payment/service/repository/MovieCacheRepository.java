package com.payment.service.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
@CacheConfig(cacheNames = "payment_movie_cache", cacheManager = "customPaymentCacheManager")
public class MovieCacheRepository {
    @CacheEvict(key = "'movie_id::' + #id")
    public void clearMovieCache(Long id) {}
}
