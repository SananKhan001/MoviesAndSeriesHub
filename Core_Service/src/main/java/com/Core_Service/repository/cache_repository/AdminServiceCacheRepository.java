package com.Core_Service.repository.cache_repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
@CacheConfig(cacheNames = "admin_service_cache", cacheManager = "customCacheManager")
public class AdminServiceCacheRepository {

    @CacheEvict(cacheNames = "admin_list", allEntries = true)
    public void clearCacheAdminList(){}

    @CacheEvict(key = "'USER::' + #userId")
    public void clearCacheUserResponse(Long userId){}

}
