package com.Stream_Service.service;

import com.Stream_Service.models.Episodes;
import com.Stream_Service.repository.EpisodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.commonDTO.EpisodeCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class EpisodeService {

    @Autowired
    private EpisodeRepository episodeRepository;

    public Mono<Void> createEpisode(EpisodeCreationMessage episodeCreationMessage) {
        System.out.println("trying to create episode here !!!");
        Episodes episodes = Episodes.builder()
                .id(episodeCreationMessage.getId())
                .uniquePosterId(episodeCreationMessage.getUniquePosterId())
                .movieId(episodeCreationMessage.getMovieId())
                .seriesId(episodeCreationMessage.getSeriesId())
                .isNew(episodeCreationMessage.isNew()).build();
        episodeRepository.save(episodes)
                .subscribe(eps -> {
                    System.out.println(eps.getUniquePosterId());
                });
        return Mono.empty();
    }

    public Mono<Void> deleteEpisode(String uniquePosterId) {
        log.info("deletion request for episode id - {}", uniquePosterId);
        episodeRepository.deleteByUniquePosterId(uniquePosterId).subscribe();
        log.info("deletion executed");
        return Mono.empty();
    }
}
