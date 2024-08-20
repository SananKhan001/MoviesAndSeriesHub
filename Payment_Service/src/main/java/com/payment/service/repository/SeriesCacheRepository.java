package com.payment.service.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "payment_series_cache", cacheManager = "customPaymentCacheManager")
public class SeriesCacheRepository {
    @CacheEvict(key = "'series_id::' + #id")
    public void clearSeriesCache(Long id) {
    }
}
