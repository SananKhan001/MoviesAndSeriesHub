package com.payment.service.config_kafka.consumer;

import org.commonDTO.MovieCreationMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MovieTransactionMessage {

    @Bean
    public Consumer<MovieCreationMessage> movieCreationMessage() {
        return movieCreationMessage -> System.out.println(movieCreationMessage.toString());
    }

}
