package com.Stream_Service.config_kafka.consume;

import com.Stream_Service.service.MovieService;
import org.commonDTO.EpisodeCreationMessage;
import org.commonDTO.MovieBuyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MovieTransactionMessages {

    @Autowired
    private MovieService movieService;

    @Bean
    public Consumer<Long> movieDeletionMessage(){
        return movieId -> {
            movieService.deleteUserMovieMapping(movieId);
        };
    }

    @Bean
    public Consumer<MovieBuyMessage> movieBuyMessage(){
        return movieBuyMessage -> {
            movieService.createUserMovieMapping(movieBuyMessage);
        };
    }

}
