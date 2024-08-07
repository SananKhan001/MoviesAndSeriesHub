package com.Search_Service.Search_Service.config_kafka.consume;

import org.commonDTO.MovieCreationMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MovieTransactionMessages {

    @Bean
    public Consumer<MovieCreationMessage> movieCreationMessage(){
        return movieCreationMessage -> System.out.println(movieCreationMessage);
    }

    @Bean
    public Consumer<Long> movieDeletionMessage(){
        return (id) -> System.out.println("Movie Id : " + id);
    }

}
