package com.Core_Service.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
@CacheConfig(cacheManager = "customCacheManager", cacheNames = "episode_cache")
public class RedisCacheRepository {

    @CacheEvict(key = "'movieId::' + '#movieId'")
    public void deleteEpisodeCacheByMovieId(Long movieId) {}

    @CacheEvict(key = "'episodeId::' + #episodeId")
    public void deleteEpisodeCacheByEpisodeId(Long episodeId) {}

    @CacheEvict(key = "'seriesId::' + #seriesId")
    public void deleteEpisodeCacheBySeriesId(Long seriesId) {}

}
