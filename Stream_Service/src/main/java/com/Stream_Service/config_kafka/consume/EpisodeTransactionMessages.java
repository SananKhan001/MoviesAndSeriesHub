package com.Stream_Service.config_kafka.consume;

import com.Stream_Service.service.EpisodeService;
import org.commonDTO.EpisodeCreationMessage;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EpisodeTransactionMessages {

    @Autowired
    private EpisodeService episodeService;

    @Bean
    public Consumer<String> episodeDeletionMessage(){
        return uniquePosterId -> {
            System.out.println("Got deletion request");
            episodeService.deleteEpisode(uniquePosterId);
        };
    }

    @Bean
    public Consumer<EpisodeCreationMessage> episodeCreationMessage(){
        return episodeCreationMessage -> {
            episodeService.createEpisode(episodeCreationMessage);
        };
    }
}
