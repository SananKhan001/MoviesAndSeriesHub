package com.Stream_Service.config_kafka.consume;

import com.Stream_Service.service.UserService;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class UserTransactionMessages {

    @Autowired
    private UserService userService;

    @Bean
    public Consumer<UserCreationMessage> userCreationMessage(){
        return userMessage -> userService.createUser(userMessage);
    }
}
