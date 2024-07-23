package com.Core_Service.controller;

import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.model_request.SeriesCreateRequest;
import com.Core_Service.model_response.EpisodeResponse;
import com.Core_Service.model_response.SeriesResponse;
import com.Core_Service.service.SeriesService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @MutationMapping(name = "addEpisodeInSeries")
    public EpisodeResponse addEpisodeInSeries(    @Argument
                                                  @NotNull(message = "EpisodeName should not be null !!!")
                                                  @NotEmpty(message = "EpisodeName should not be empty !!!")
                                                  String episodeName,
                                                  @NotEmpty(message = "SeriesId sould not be null !!!")
                                                  Long seriesId) {
        return seriesService.addEpisodeInSeries(episodeName, seriesId);
    }
}
