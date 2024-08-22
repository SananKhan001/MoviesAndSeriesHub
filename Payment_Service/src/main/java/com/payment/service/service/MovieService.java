package com.payment.service.service;

import com.payment.service.models.Movie;
import com.payment.service.repository.MovieCacheRepository;
import com.payment.service.repository.MovieRepository;
import org.commonDTO.MovieCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableCaching
@CacheConfig(cacheNames = "payment_movie_cache", cacheManager = "customPaymentCacheManager")
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieCacheRepository cacheRepository;

    public void save(MovieCreationMessage movieCreationMessage) {
        Movie movie = Movie.builder()
                .id(movieCreationMessage.getId()).name(movieCreationMessage.getName())
                .price(movieCreationMessage.getPrice()).build();

        movieRepository.save(movie);
        cacheRepository.clearMovieCache(movie.getId());
    }

    public void delete(Long id) {
        movieRepository.deleteById(id);
        cacheRepository.clearMovieCache(id);
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Cacheable(key = "'movie_id::' + #id")
    public Movie findById(Long id) {
        return movieRepository.findById(id).get();
    }

}
