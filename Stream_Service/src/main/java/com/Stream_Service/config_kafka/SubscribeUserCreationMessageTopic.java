package com.Stream_Service.config_kafka;

import com.Stream_Service.service.UserService;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;

import java.util.function.Consumer;

@Configuration
public class SubscribeUserCreationMessageTopic {

    @Autowired
    private UserService userService;

    @Bean
    public Consumer<UserCreationMessage> userCreationMessage(){
        return userMessage -> userService.createUser(userMessage);
    }
}
