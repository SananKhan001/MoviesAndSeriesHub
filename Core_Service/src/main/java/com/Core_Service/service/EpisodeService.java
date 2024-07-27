package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoEpisodeFoundException;
import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Episode;
import com.Core_Service.model.Series;
import com.Core_Service.model_response.EpisodeResponse;
import com.Core_Service.repository.EpisodeRepository;
import com.Core_Service.repository.MovieRepository;
import com.Core_Service.repository.SeriesRepository;
import org.commonDTO.EpisodeCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpisodeService {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private StreamBridge streamBridge;

    public EpisodeResponse createEpisodeForMovie(String episodeName, Long movieId) {
        Episode episode = Episode.builder().episodeName(episodeName)
                .episodeId(Helper.generateUUID()).uniquePosterId(Helper.generateUUID())
                .belongsToMovie(movieRepository.findById(movieId).get()).build();
        episode = episodeRepository.save(episode);

        /**                                  ------------------------------
         *  EpisodeCreationMessage ======>>>| EpisodeCreationMessageTopic |
         *                                  ------------------------------
         */
        EpisodeCreationMessage episodeCreationMessage = EpisodeCreationMessage.builder()
                .id(episode.getId()).uniquePosterId(episode.getUniquePosterId())
                .movieId(episode.getBelongsToMovie().getId()).isNew(true).build();
        streamBridge.send("EpisodeCreationMessageTopic", episodeCreationMessage);

        return episode.to();
    }

    public EpisodeResponse getEpisodeById(Long episodeId) throws NoEpisodeFoundException {
        return episodeRepository.findById(episodeId)
                .orElseThrow(() -> new NoEpisodeFoundException("There are no episode with provided id !!!"))
                .to();
    }

    public EpisodeResponse getEpisodeByMovieId(Long movieId) throws NoMovieFoundException {
        return episodeRepository.findByBelongsToMovie(movieId)
                .orElseThrow(() -> new NoMovieFoundException("No episode found by provided movieId !!!"))
                .to();
    }

    public EpisodeResponse updateEpisodeName(String episodeName, Long episodeId) throws NoEpisodeFoundException {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new NoEpisodeFoundException("No such episode found !!!"));
        episode.setEpisodeName(episodeName);
        return episodeRepository.save(episode).to();
    }

    public Boolean deleteEpisode(Long episodeId) throws NoEpisodeFoundException {

        /**                                  ------------------------------
         *  EpisodeCreationMessage ======>>>| EpisodeDeletionMessageTopic |
         *                                  ------------------------------
         */
        streamBridge.send("EpisodeDeletionMessageTopic", episodeRepository.findById(episodeId)
                .orElseThrow(() -> new NoEpisodeFoundException("No episode found with given id !!!"))
                .getUniquePosterId());

        episodeRepository.deleteById(episodeId);
        return true;
    }

    public EpisodeResponse addEpisodeInSeries(String episodeName, Long seriesId) throws NoSeriesFoundException {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoSeriesFoundException("No series found with given series id !!!"));
        Episode episode = Episode.builder()
                .episodeName(episodeName)
                .episodeId(Helper.generateUUID())
                .uniquePosterId(Helper.generateUUID())
                .belongsToSeries(series)
                .build();
        return episodeRepository.save(episode).to();
    }

    public List<EpisodeResponse> getEpisodeBySeriesId(Long seriesId) {
        return episodeRepository.findAllBySeriesId(seriesId)
                .stream()
                .map(x -> x.to()).collect(Collectors.toList());
    }
}
