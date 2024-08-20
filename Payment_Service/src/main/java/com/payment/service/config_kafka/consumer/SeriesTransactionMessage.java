package com.payment.service.config_kafka.consumer;

import com.payment.service.service.SeriesService;
import org.commonDTO.SeriesCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SeriesTransactionMessage {

    @Autowired
    private SeriesService seriesService;

    @Bean
    public Consumer<SeriesCreationMessage> seriesCreationMessage() {
        return seriesCreationMessage -> seriesService.save(seriesCreationMessage);
    }

    @Bean
    public Consumer<SeriesCreationMessage> updateSeriesMessage() {
        return seriesUpdationgMessage -> seriesService.save(seriesUpdationgMessage);
    }

    @Bean Consumer<Long> seriesDeletionMessage() {
        return id -> seriesService.delete(id);
    }

}
