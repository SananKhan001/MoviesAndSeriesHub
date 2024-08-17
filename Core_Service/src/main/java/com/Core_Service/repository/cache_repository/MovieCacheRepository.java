package com.Core_Service.repository.cache_repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
@CacheConfig(cacheNames = "movie_cache", cacheManager = "customCacheManager")
public class MovieCacheRepository {

    @CacheEvict(key = "'movie::' + #movieId")
    public void clearCacheByMovieId(Long movieId) {}

    @CacheEvict(cacheNames = "'latest_movies::' + #genre", allEntries = true)
    public void clearCacheByMovieGenre(String genre) {}

    @CacheEvict(cacheNames = "'movie_review::' + #movieId", allEntries = true)
    public void clearCacheByMovieReview(Long movieId) {}

}
