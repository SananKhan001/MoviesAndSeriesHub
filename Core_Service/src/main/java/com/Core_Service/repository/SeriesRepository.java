package com.Core_Service.repository;

import com.Core_Service.model.Series;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    @Query(value = "SELECT * FROM series WHERE genre LIKE %?1% ORDER BY created_at DESC", nativeQuery = true)
    List<Series> findByNewReleaseSeriesByGenre(String toString, Pageable pageRequest);
}
