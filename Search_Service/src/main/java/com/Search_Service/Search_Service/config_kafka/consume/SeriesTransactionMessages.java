package com.Search_Service.Search_Service.config_kafka.consume;

import com.Search_Service.Search_Service.service.SeriesService;
import org.commonDTO.MovieCreationMessage;
import org.commonDTO.SeriesCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SeriesTransactionMessages {

    @Autowired
    private SeriesService seriesService;

    @Bean
    public Consumer<SeriesCreationMessage> seriesCreationMessage(){
        return seriesCreationMessage -> seriesService.save(seriesCreationMessage);
    }

    @Bean
    public Consumer<Long> seriesDeletionMessage(){
        return id -> seriesService.delete(id);
    }

    @Bean
    public Consumer<SeriesCreationMessage> updateSeriesMessage() {
        return seriesCreationMessage -> seriesService.save(seriesCreationMessage);
    }

}
