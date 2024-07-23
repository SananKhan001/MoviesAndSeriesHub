package com.Core_Service.controller;

import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.enums.Genre;
import com.Core_Service.model_request.SeriesCreateRequest;
import com.Core_Service.model_response.SeriesResponse;
import com.Core_Service.service.SeriesService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @MutationMapping(name = "addSeries")
    public SeriesResponse addSeries(@Argument SeriesCreateRequest seriesCreateRequest) {
        return seriesService.addSeries(seriesCreateRequest);
    }

    @QueryMapping(name = "getSeriesById")
    public SeriesResponse getSeriesById(@Argument
                                        @NotNull(message = "SeriesId should not be null !!!")
                                        Long seriesId) throws NoSeriesFoundException {
        return seriesService.getSeriesById(seriesId);
    }

    @MutationMapping(name = "updateSeries")
    public SeriesResponse updateSeries(@Argument SeriesCreateRequest seriesCreateRequest,
                                       @Argument
                                       @NotNull(message = "SeriesId should not be null !!!")
                                       Long seriesId) throws NoSeriesFoundException {
        return seriesService.updateSeries(seriesCreateRequest, seriesId);
    }

    @MutationMapping(name = "deleteSeries")
    public Boolean deleteSeries(@Argument @NotNull(message = "SeriesId should not be null !!!") Long seriesId){
        return seriesService.deleteSeries(seriesId);
    }

    @QueryMapping(name = "getNewReleaseSeriesByGenre")
    public List<SeriesResponse> getNewReleaseSeriesByGenre(@Argument
                                                           @NotNull(message = "Genre should not be null !!!")
                                                           Genre genre,
                                                           @Argument int page, @Argument int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return seriesService.getNewReleaseSeriesByGenre(genre, pageRequest);
    }
}
