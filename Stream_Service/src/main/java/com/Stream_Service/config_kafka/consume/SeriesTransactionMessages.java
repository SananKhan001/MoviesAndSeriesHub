package com.Stream_Service.config_kafka.consume;

import com.Stream_Service.service.SeriesService;
import org.commonDTO.MovieBuyMessage;
import org.commonDTO.SeriesBuyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SeriesTransactionMessages {

    @Autowired
    private SeriesService seriesService;

    @Bean
    public Consumer<Long> seriesDeletionMessage(){
        return seriesId -> {
            seriesService.deleteUserSeriesMapping(seriesId);
        };
    }

    @Bean
    public Consumer<SeriesBuyMessage> seriesBuyMessage(){
        return seriesBuyMessage -> {
            seriesService.createUserMovieMapping(seriesBuyMessage);
        };
    }

}
