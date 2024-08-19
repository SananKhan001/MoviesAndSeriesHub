package com.payment.service.config_kafka.consumer;

import org.commonDTO.SeriesCreationMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SeriesTransactionMessage {

    @Bean
    public Consumer<SeriesCreationMessage> seriesCreationMessage() {
        return seriesCreationMessage -> System.out.println(seriesCreationMessage.toString());
    }

}
