package com.Core_Service.repository.cache_repository;

import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
@CacheConfig(cacheNames = "series_cache", cacheManager = "customCacheManager")
public class SeriesCacheRepository {

    @CacheEvict(key = "'series::' + #seriesId")
    public void clearCacheBySeriesId(Long seriesId) {}

    @CacheEvict(cacheNames = "'latest_series::' + #genre", allEntries = true)
    public void clearCacheBySeriesGenre(String genre) {}

    @CacheEvict(cacheNames = "'series_review::' + #seriesId", allEntries = true)
    public void clearCacheBySeriesReview(Long seriesId) {}
}
