package com.Stream_Service.service;

import com.Stream_Service.models.User;
import com.Stream_Service.repository.UserRepository;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUser(UserCreationMessage userCreationMessage){
        User user = User.builder()
                .id(userCreationMessage.getId())
                .username(userCreationMessage.getUsername())
                .password(userCreationMessage.getPassword())
                .authority(userCreationMessage.getAuthority()).build();
        userRepository.save(user).subscribe(
                u -> System.out.println(u.getUsername())
        );
    }
}
