package com.Stream_Service.repository;

import com.Stream_Service.models.UserSeriesMapping;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserSeriesMappingRepository extends ReactiveCrudRepository<UserSeriesMapping, Long> {
    @Query("DELETE FROM user_series_mapping WHERE series_id = :seriesId")
    Mono<Void> deleteBySeriesId(Long seriesId);

    @Query("SELECT * FROM user_series_mapping WHERE user_id = :userId")
    Flux<UserSeriesMapping> userSeriesMappingByUserId(Long userId);
}
