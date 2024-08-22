package com.Core_Service.config_kafka.consumer;

import com.Core_Service.service.TransactionService;
import org.commonDTO.TransactionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class TransactionDetailsConsumer {

    @Autowired
    private TransactionService transactionService;

    @Bean
    public Consumer<TransactionDetails> transactionDetails(){
        return transactionDetails -> transactionService.save(transactionDetails);
    }

}
