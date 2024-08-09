package com.Search_Service.Search_Service.config_kafka.consume;

import com.Search_Service.Search_Service.service.MovieService;
import org.commonDTO.MovieCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MovieTransactionMessages {

    @Autowired
    private MovieService movieService;

    @Bean
    public Consumer<MovieCreationMessage> movieCreationMessage(){
        return movieCreationMessage -> movieService.save(movieCreationMessage);
    }

    @Bean
    public Consumer<Long> movieDeletionMessage(){
        return (id) -> movieService.delete(id);
    }

    @Bean
    public Consumer<MovieCreationMessage> updateMovieMessage() {
        return updateMovieMessage -> movieService.save(updateMovieMessage);
    }

}
