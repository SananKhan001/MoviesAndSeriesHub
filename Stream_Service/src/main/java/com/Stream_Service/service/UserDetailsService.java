package com.Stream_Service.service;

import com.Stream_Service.models.User;
import com.Stream_Service.repository.UserRepository;
import lombok.SneakyThrows;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Mono<User> user = userRepository.findByUsername(username);
        return user
                .map(x -> x);
    }
}
