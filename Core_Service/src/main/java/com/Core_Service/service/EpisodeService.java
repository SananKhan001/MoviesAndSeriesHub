package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoEpisodeFoundException;
import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.custom_exceptions.NoSeriesFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Episode;
import com.Core_Service.model.Movie;
import com.Core_Service.model.Series;
import com.Core_Service.model_request.PrivateMessageRequest;
import com.Core_Service.model_response.EpisodeResponse;
import com.Core_Service.repository.db_repository.EpisodeRepository;
import com.Core_Service.repository.db_repository.MovieRepository;
import com.Core_Service.repository.cache_repository.EpisodeCacheRepository;
import com.Core_Service.repository.db_repository.SeriesRepository;
import org.commonDTO.EpisodeCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableCaching
@CacheConfig(cacheManager = "customCacheManager", cacheNames = "episode_cache")
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

    @Autowired
    private EpisodeCacheRepository cacheRepository;

    @Autowired
    private NotificationService notificationService;

    public EpisodeResponse createEpisodeForMovie(String episodeName, Long movieId) throws NoMovieFoundException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoMovieFoundException("No movie found with given id !!!"));

        Episode episode = Episode.builder().episodeName(episodeName)
                .uniquePosterId(Helper.generateUUID())
                .belongsToMovie(movie).build();
        episode = episodeRepository.save(episode);

        cacheRepository.deleteEpisodeCacheByEpisodeId(episode.getId());
        cacheRepository.deleteEpisodeCacheByMovieId(movieId);

        /**                                  ------------------------------
         *  EpisodeCreationMessage ======>>>| EpisodeCreationMessageTopic |
         *                                  ------------------------------
         */
        EpisodeCreationMessage episodeCreationMessage = EpisodeCreationMessage.builder()
                .id(episode.getId()).uniquePosterId(episode.getUniquePosterId())
                .movieId(episode.getBelongsToMovie().getId()).isNew(true).build();
        streamBridge.send("EpisodeCreationMessageTopic", episodeCreationMessage);

        List<Long> viewerIds = movie.getViewers()
                .stream()
                .map(viewer -> viewer.getUser().getId())
                .toList();
        notificationService.notifyUsers(PrivateMessageRequest.builder()
                .content("New Episode of Movie: " + movie.getName()
                        + " has been uploaded !!!")
                .userIdList(viewerIds).build());

        return episode.to();
    }

    @Cacheable(key = "'episodeId::' + #episodeId")
    public EpisodeResponse getEpisodeById(Long episodeId) throws NoEpisodeFoundException {
        return episodeRepository.findById(episodeId)
                .orElseThrow(() -> new NoEpisodeFoundException("There are no episode with provided id !!!"))
                .to();
    }

    @Cacheable(key = "'movieId::' + '#movieId'")
    public EpisodeResponse getEpisodeByMovieId(Long movieId) throws NoMovieFoundException {
        return episodeRepository.findByBelongsToMovie(movieId)
                .orElseThrow(() -> new NoMovieFoundException("No episode found by provided movieId !!!"))
                .to();
    }

    public EpisodeResponse updateEpisodeName(String episodeName, Long episodeId) throws NoEpisodeFoundException {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new NoEpisodeFoundException("No such episode found !!!"));
        episode.setEpisodeName(episodeName);
        episode = episodeRepository.save(episode);

        cacheRepository.deleteEpisodeCacheByEpisodeId(episodeId);
        Movie movie = episode.getBelongsToMovie();
        Series series = episode.getBelongsToSeries();
        if(movie != null) cacheRepository.deleteEpisodeCacheByMovieId(movie.getId());
        else cacheRepository.deleteEpisodeCacheBySeriesId(series.getId());

        return episode.to();
    }

    public Boolean deleteEpisode(Long episodeId) throws NoEpisodeFoundException {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new NoEpisodeFoundException("No Episode found with given id !!!"));

        /**                                  ------------------------------
         *  EpisodeCreationMessage ======>>>| EpisodeDeletionMessageTopic |
         *                                  ------------------------------
         */
        streamBridge.send("EpisodeDeletionMessageTopic", episode.getUniquePosterId());

        Movie movie = episode.getBelongsToMovie();
        Series series = episode.getBelongsToSeries();
        cacheRepository.deleteEpisodeCacheByEpisodeId(episodeId);
        if(movie != null) cacheRepository.deleteEpisodeCacheByMovieId(movie.getId());
        else cacheRepository.deleteEpisodeCacheBySeriesId(series.getId());

        episodeRepository.deleteById(episodeId);
        return true;
    }

    public EpisodeResponse addEpisodeInSeries(String episodeName, Long seriesId) throws NoSeriesFoundException {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new NoSeriesFoundException("No series found with given series id !!!"));
        Episode episode = Episode.builder()
                .episodeName(episodeName)
                .uniquePosterId(Helper.generateUUID())
                .belongsToSeries(series)
                .build();
        episode = episodeRepository.save(episode);

        cacheRepository.deleteEpisodeCacheByEpisodeId(episode.getId());
        cacheRepository.deleteEpisodeCacheBySeriesId(seriesId);

        /**                                  ------------------------------
         *  EpisodeCreationMessage ======>>>| EpisodeCreationMessageTopic |
         *                                  ------------------------------
         */
        streamBridge.send("EpisodeCreationMessageTopic",
                            EpisodeCreationMessage.builder()
                                    .id(episode.getId()).uniquePosterId(episode.getUniquePosterId())
                                    .seriesId(episode.getBelongsToSeries().getId()).isNew(true).build()
        );

        List<Long> viewerIds = series.getViewers()
                .stream()
                .map(viewer -> viewer.getUser().getId())
                .toList();
        notificationService.notifyUsers(PrivateMessageRequest.builder()
                .content("New Episode of Series: " + series.getName()
                        + " has been uploaded !!!")
                .userIdList(viewerIds).build());

        return episode.to();
    }

    @Cacheable(key = "'seriesId::' + #seriesId")
    public List<EpisodeResponse> getEpisodeBySeriesId(Long seriesId) {
        return episodeRepository.findAllBySeriesId(seriesId)
                .stream()
                .map(x -> x.to()).collect(Collectors.toList());
    }
}
