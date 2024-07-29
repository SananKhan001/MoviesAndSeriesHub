package com.Stream_Service.service;

import com.Stream_Service.models.User;
import com.Stream_Service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Mono<Void> createUser(UserCreationMessage userCreationMessage){
        User user = User.builder()
                .id(userCreationMessage.getId())
                .username(userCreationMessage.getUsername())
                .password(userCreationMessage.getPassword())
                .authority(userCreationMessage.getAuthority())
                .isNew(userCreationMessage.isNew()).build();
        userRepository.save(user).subscribe();
        return Mono.empty();
    }
}
