package com.Search_Service.Search_Service.config_kafka.consume;

import org.commonDTO.SeriesCreationMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SeriesTransactionMessages {

    @Bean
    public Consumer<SeriesCreationMessage> seriesCreationMessage(){
        return seriesCreationMessage -> System.out.println(seriesCreationMessage);
    }

    @Bean
    public Consumer<Long> seriesDeletionMessage(){
        return id -> System.out.println("Series Id: " + id);
    }

}
