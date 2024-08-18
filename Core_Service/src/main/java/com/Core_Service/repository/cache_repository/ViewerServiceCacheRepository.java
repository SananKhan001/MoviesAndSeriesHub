package com.Core_Service.repository.cache_repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
@CacheConfig(cacheNames = "viewer_service_cache", cacheManager = "customCacheManager")
public class ViewerServiceCacheRepository {

    @CacheEvict(cacheNames = "viewer_list", allEntries = true)
    public void clearCacheViewerList() {}

    @CacheEvict(key = "'USER::' + #userId")
    public void clearCacheUserResponse(Long userId) {}

}
