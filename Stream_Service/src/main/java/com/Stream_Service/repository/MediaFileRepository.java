package com.Stream_Service.repository;

import com.Stream_Service.models.MediaFile;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MediaFileRepository extends ReactiveCrudRepository<MediaFile, Long> {
    @Query("SELECT * FROM media_files WHERE unique_id = :uniquePosterId")
    Mono<MediaFile> findByUniqueId(String uniquePosterId);
}
