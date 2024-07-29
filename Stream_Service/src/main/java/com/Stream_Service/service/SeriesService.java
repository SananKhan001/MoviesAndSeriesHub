package com.Stream_Service.service;

import com.Stream_Service.models.UserSeriesMapping;
import com.Stream_Service.repository.UserSeriesMappingRepository;
import org.commonDTO.SeriesBuyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SeriesService {

    @Autowired
    private UserSeriesMappingRepository userSeriesMappingRepository;

    public Mono<Void> createUserMovieMapping(SeriesBuyMessage seriesBuyMessage) {
        UserSeriesMapping userSeriesMapping = UserSeriesMapping.builder()
                .userId(seriesBuyMessage.getUserId())
                .seriesId(seriesBuyMessage.getSeriesId())
                .isNew(seriesBuyMessage.isNew()).build();
        userSeriesMappingRepository.save(userSeriesMapping).subscribe();
        return Mono.empty();
    }

    public Mono<Void> deleteUserSeriesMapping(Long seriesId) {
        userSeriesMappingRepository.deleteBySeriesId(seriesId).subscribe();
        return Mono.empty();
    }
}
