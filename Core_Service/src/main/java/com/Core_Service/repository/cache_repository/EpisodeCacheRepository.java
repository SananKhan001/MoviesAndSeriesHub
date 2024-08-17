package com.Core_Service.repository.cache_repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
@CacheConfig(cacheManager = "customCacheManager", cacheNames = "episode_cache")
public class EpisodeCacheRepository {

    @CacheEvict(key = "'movieId::' + '#movieId'")
    public void deleteEpisodeCacheByMovieId(Long movieId) {}

    @CacheEvict(key = "'episodeId::' + #episodeId")
    public void deleteEpisodeCacheByEpisodeId(Long episodeId) {}

    @CacheEvict(key = "'seriesId::' + #seriesId")
    public void deleteEpisodeCacheBySeriesId(Long seriesId) {}

}
