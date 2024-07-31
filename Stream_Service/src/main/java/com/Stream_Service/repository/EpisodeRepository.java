package com.Stream_Service.repository;

import com.Stream_Service.models.Episodes;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EpisodeRepository extends ReactiveCrudRepository<Episodes, Long> {
    @Query("DELETE FROM episodes WHERE unique_poster_id = :uniquePosterId")
    Mono<Void> deleteByUniquePosterId(String uniquePosterId);

    @Query("SELECT * FROM episodes WHERE unique_poster_id = :uniquePosterId")
    Mono<Episodes> findByUniquePosterId(String uniquePosterId);
}
