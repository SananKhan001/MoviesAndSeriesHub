package com.Core_Service.service;

import com.Core_Service.custom_exceptions.NoEpisodeFoundException;
import com.Core_Service.custom_exceptions.NoMovieFoundException;
import com.Core_Service.helpers.Helper;
import com.Core_Service.model.Episode;
import com.Core_Service.model_response.EpisodeResponse;
import com.Core_Service.repository.EpisodeRepository;
import com.Core_Service.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EpisodeService {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    public EpisodeResponse createEpisodeForMovie(String episodeName, Long movieId) {
        Episode episode = Episode.builder().episodeName(episodeName)
                .episodeId(Helper.generateUUID()).uniquePosterId(Helper.generateUUID())
                .belongsToMovie(movieRepository.findById(movieId).get()).build();
        return episodeRepository.save(episode).to();
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

    public Boolean deleteEpisode(Long episodeId) {
        episodeRepository.deleteById(episodeId);
        return true;
    }
}
