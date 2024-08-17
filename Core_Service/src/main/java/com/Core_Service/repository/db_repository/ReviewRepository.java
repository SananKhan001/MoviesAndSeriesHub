package com.Core_Service.repository.db_repository;

import com.Core_Service.model.Movie;
import com.Core_Service.model.Review;
import com.Core_Service.model.Series;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    public Optional<List<Review>> findByReviewForMovie(Movie movie, Pageable pageRequest);

    Optional<List<Review>> findByReviewForSeries(Series series, Pageable pageRequest);
}
