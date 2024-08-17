package com.Core_Service.repository.db_repository;

import com.Core_Service.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    @Query(value = "SELECT * FROM episodes WHERE belongs_to_movie = ?1", nativeQuery = true)
    Optional<Episode> findByBelongsToMovie(Long movieId);

    @Query(value = "SELECT * FROM episodes WHERE belongs_to_series = ?1", nativeQuery = true)
    List<Episode> findAllBySeriesId(Long seriesId);
}
