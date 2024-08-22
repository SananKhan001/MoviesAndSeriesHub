package com.Core_Service.controller;

import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.enums.Genre;
import com.Core_Service.model_request.MovieCreateRequest;
import com.Core_Service.model_request.ReviewCreateRequest;
import com.Core_Service.model_response.MovieResponse;
import com.Core_Service.model_response.ReviewResponse;
import com.Core_Service.service.MovieService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "addMovie")
    public MovieResponse addMovie(@Argument MovieCreateRequest movieCreateRequest) {
        return movieService.addMovie(movieCreateRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "updateMovie")
    public MovieResponse updateMovie(@Argument MovieCreateRequest movieCreateRequest,
                                     @Argument
                                     @NotNull(message = "Movie Id should not be null !!")
                                     Long movieId) throws NoMovieFoundException {
        return movieService.updateMovie(movieCreateRequest, movieId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "deleteMovie")
    public Boolean deleteMovie(@Argument @NotNull(message = "Movie Id should not be null !!") Long movieId) throws NoMovieFoundException {
        return movieService.deleteMovie(movieId);
    }

    @PermitAll
    @QueryMapping(name = "getMovieById")
    public MovieResponse getMovieById(@Argument
                                      @NotNull(message = "Movie Id should not be null !!")
                                      Long movieId) throws NoMovieFoundException {
        return movieService.getMovieById(movieId);
    }

    @PermitAll
    @QueryMapping(name = "getNewReleaseMoviesByGenre")
    public List<MovieResponse> getNewReleaseMoviesByGenre(@Argument
                                                          @NotNull(message = "Genre should not be null !!")
                                                          Genre genre,
                                                          @Argument
                                                          @NotNull(message = "Page should not be null !!!")
                                                          Integer page,
                                                          @Argument
                                                          @NotNull(message = "Page Size should not be null !!!")
                                                          Integer size){
        Pageable pageRequest = PageRequest.of(page, size);
        return movieService.getNewReleaseMoviesByGenre(genre.toString(), pageRequest);
    }

    @PreAuthorize("hasAuthority('VIEWER')")
    @MutationMapping(name = "reviewMovie")
    public ReviewResponse reviewMovie(@Argument Long movieId, @Argument ReviewCreateRequest reviewCreateRequest) throws NoMovieFoundException {
        return movieService.reviewMovie(movieId, reviewCreateRequest);
    }

    @PreAuthorize("hasAnyAuthority('VIEWER', 'ADMIN')")
    @QueryMapping(name = "getReviewsOfMovie")
    public List<ReviewResponse> getReviewsOfMovie(@Argument Long movieId,
                                                  @Argument Integer page,
                                                  @Argument Integer size) throws NoMovieFoundException {
        Pageable pageRequest = PageRequest.of(page, size);
        return movieService.getReviewsOfMovie(movieId, pageRequest);
    }

    @PreAuthorize("hasAuthority('VIEWER')")
    @QueryMapping(name = "getAllBoughtMovie")
    public List<MovieResponse> getAllBoughtMovie() {
        return movieService.getAllBoughtMovie();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @QueryMapping(name = "getAllBoughtMovieByUserId")
    public List<MovieResponse> getAllBoughtMovieByUserId(@Argument Long userId) throws NoUserFoundException {
        return movieService.getAllBoughtMovieByUserId(userId);
    }
}
