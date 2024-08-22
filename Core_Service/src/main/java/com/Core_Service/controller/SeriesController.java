package com.Core_Service.controller;

import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.custom_exceptions.NoUserFoundException;
import com.Core_Service.enums.Genre;
import com.Core_Service.model_request.ReviewCreateRequest;
import com.Core_Service.model_request.SeriesCreateRequest;
import com.Core_Service.model_response.ReviewResponse;
import com.Core_Service.model_response.SeriesResponse;
import com.Core_Service.service.SeriesService;
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
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "addSeries")
    public SeriesResponse addSeries(@Argument SeriesCreateRequest seriesCreateRequest) {
        return seriesService.addSeries(seriesCreateRequest);
    }

    @PermitAll
    @QueryMapping(name = "getSeriesById")
    public SeriesResponse getSeriesById(@Argument
                                        @NotNull(message = "SeriesId should not be null !!!")
                                        Long seriesId) throws NoSeriesFoundException {
        return seriesService.getSeriesById(seriesId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "updateSeries")
    public SeriesResponse updateSeries(@Argument SeriesCreateRequest seriesCreateRequest,
                                       @Argument
                                       @NotNull(message = "SeriesId should not be null !!!")
                                       Long seriesId) throws NoSeriesFoundException {
        return seriesService.updateSeries(seriesCreateRequest, seriesId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "deleteSeries")
    public Boolean deleteSeries(@Argument @NotNull(message = "SeriesId should not be null !!!") Long seriesId) throws NoSeriesFoundException {
        return seriesService.deleteSeries(seriesId);
    }

    @PermitAll
    @QueryMapping(name = "getNewReleaseSeriesByGenre")
    public List<SeriesResponse> getNewReleaseSeriesByGenre(@Argument
                                                           @NotNull(message = "Genre should not be null !!!")
                                                           Genre genre,
                                                           @Argument int page, @Argument int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return seriesService.getNewReleaseSeriesByGenre(genre, pageRequest);
    }

    @PreAuthorize("hasAuthority('VIEWER')")
    @MutationMapping(name = "reviewSeries")
    public ReviewResponse reviewSeries(@Argument Long seriesId, @Argument ReviewCreateRequest reviewCreateRequest) throws NoSeriesFoundException {
        return seriesService.reviewSeries(seriesId, reviewCreateRequest);
    }

    @PreAuthorize("hasAnyAuthority('VIEWER', 'ADMIN')")
    @QueryMapping(name = "getReviewsOfSeries")
    public List<ReviewResponse> getReviewsOfSeries(@Argument Long seriesId,
                                                  @Argument Integer page,
                                                  @Argument Integer size) throws NoMovieFoundException {
        Pageable pageRequest = PageRequest.of(page, size);
        return seriesService.getReviewsOfMovie(seriesId, pageRequest);
    }

    @PreAuthorize("hasAuthority('VIEWER')")
    @QueryMapping(name = "getAllBoughtSeries")
    public List<SeriesResponse> getAllBoughtSeries() {
        return seriesService.getAllBoughtSeries();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @QueryMapping(name = "getAllBoughtSeriesByUserId")
    public List<SeriesResponse> getAllBoughtSeriesByUserId(@Argument Long userId) throws NoUserFoundException {
        return seriesService.getAllboughtSeriesByUserId(userId);
    }
}