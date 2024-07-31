package com.Stream_Service.repository;

import com.Stream_Service.models.UserMovieMapping;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserMovieMappingRepository extends ReactiveCrudRepository<UserMovieMapping, Long> {
    @Query(value = "DELETE FROM user_movie_mapping WHERE movie_id = :movieId")
    Mono<Void> deleteByMovieId(Long movieId);

    @Query(value = "SELECT * FROM user_movie_mapping WHERE user_id = :userId")
    Flux<UserMovieMapping> userMovieMappingByUserId(Long userId);
}
