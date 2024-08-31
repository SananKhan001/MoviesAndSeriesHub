package com.Core_Service.controller;

import com.Core_Service.custom_exceptions.NoEpisodeFoundException;
import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.model_response.EpisodeResponse;
import com.Core_Service.service.EpisodeService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EpisodeController {

    @Autowired
    private EpisodeService episodeService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "createEpisodeForMovie")
    public EpisodeResponse createEpisodeForMovie(@Argument
                                                 @NotNull(message = "Episode Name should not be null !!!")
                                                 @NotEmpty(message = "Episode Value should not be empty !!!")
                                                 String episodeName,
                                                 @Argument
                                                 @NotNull(message = "MovieId should not be null !!!")
                                                 Long movieId) throws NoMovieFoundException {
        return episodeService.createEpisodeForMovie(episodeName, movieId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    @QueryMapping(name = "getEpisodeById")
    public EpisodeResponse getEpisodeById(@Argument
                                          @NotNull(message = "EpisodeId should not be null !!!")
                                          Long episodeId) throws NoEpisodeFoundException {
        return episodeService.getEpisodeById(episodeId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    @QueryMapping(name = "getEpisodeByMovieId")
    public EpisodeResponse getEpisodeByMovieId(@Argument
                                               @NotNull(message = "MovieId should not be null !!!")
                                               Long movieId) throws NoMovieFoundException {
        return episodeService.getEpisodeByMovieId(movieId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "updateEpisodeName")
    EpisodeResponse updateEpisodeName(@Argument
                                      @NotNull(message = "EpisodeName should not be null !!!")
                                      @NotEmpty(message = "EpisodeName should not be empty !!!")
                                      String episodeName,
                                      @Argument
                                      @NotNull(message = "EpisodeId should not be null !!!")
                                      Long episodeId) throws NoEpisodeFoundException {
        return episodeService.updateEpisodeName(episodeName, episodeId);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "deleteEpisode")
    public Boolean deleteEpisode(   @Argument
                                    @NotNull(message = "Episode Id should not be null !!!")
                                    Long episodeId) throws NoEpisodeFoundException {
        return episodeService.deleteEpisode(episodeId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @MutationMapping(name = "addEpisodeInSeries")
    public EpisodeResponse addEpisodeInSeries(    @Argument
                                                  @NotNull(message = "EpisodeName should not be null !!!")
                                                  @NotEmpty(message = "EpisodeName should not be empty !!!")
                                                  String episodeName,
                                                  @Argument
                                                  @NotNull(message = "SeriesId should not be null !!!")
                                                  Long seriesId) throws NoSeriesFoundException {
        return episodeService.addEpisodeInSeries(episodeName, seriesId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VIEWER')")
    @QueryMapping(name = "getEpisodesBySeriesId")
    List<EpisodeResponse> getEpisodesBySeriesId(@Argument @NotNull(message = "SeriesId should not be null !!!)") Long seriesId) {
        return episodeService.getEpisodeBySeriesId(seriesId);
    }
}
