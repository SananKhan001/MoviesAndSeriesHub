package com.Core_Service.repository.db_repository;

import com.Core_Service.model.Movie;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query(value = "SELECT * FROM movies WHERE genre LIKE %?1% ORDER BY created_at DESC", nativeQuery = true)
    List<Movie> findNewReleaseMoviesByGenre(String genre, Pageable pageRequest);

    @Query(value = "SELECT AVG(rev.rating) FROM reviews rev JOIN movies mv ON rev.review_for_movie = mv.id WHERE mv.id = ?1", nativeQuery = true)
    double findAverageRating(Long movieId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO movie_viewer_mapping (movie_id, viewer_id)
            VALUES(?1, ?2)
            """)
    void updateMovieViewerMapping(Long movieId, Long viewerId);
}
