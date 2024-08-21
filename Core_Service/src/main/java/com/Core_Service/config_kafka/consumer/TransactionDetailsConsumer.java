package com.Core_Service.config_kafka.consumer;

import org.commonDTO.TransactionDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class TransactionDetailsConsumer {

    @Bean
    public Consumer<TransactionDetails> transactionDetails(){
        return transactionDetails -> System.out.println(transactionDetails.toString());
    }

}
