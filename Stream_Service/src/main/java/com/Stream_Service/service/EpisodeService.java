package com.Stream_Service.service;

import com.Stream_Service.models.Episodes;
import com.Stream_Service.repository.EpisodeRepository;
import com.Stream_Service.repository.MediaFileRepository;
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

    @Autowired
    private MediaFileService mediaFileService;

    public Mono<Void> createEpisode(EpisodeCreationMessage episodeCreationMessage) {
        Episodes episodes = Episodes.builder()
                .id(episodeCreationMessage.getId())
                .uniquePosterId(episodeCreationMessage.getUniquePosterId())
                .movieId(episodeCreationMessage.getMovieId())
                .seriesId(episodeCreationMessage.getSeriesId())
                .isNew(episodeCreationMessage.isNew()).build();
        episodeRepository.save(episodes).subscribe();
        return Mono.empty();
    }

    public Mono<Void> deleteEpisode(String uniquePosterId) {
        mediaFileService.deleteMediaFileByUniqueId(uniquePosterId).subscribe();
        episodeRepository.deleteByUniquePosterId(uniquePosterId).subscribe();
        return Mono.empty();
    }
}
