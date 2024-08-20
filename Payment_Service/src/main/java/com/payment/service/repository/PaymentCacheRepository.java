package com.payment.service.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "payment_cache_repository", cacheManager = "customPaymentCacheManager")
public class PaymentCacheRepository {

}
