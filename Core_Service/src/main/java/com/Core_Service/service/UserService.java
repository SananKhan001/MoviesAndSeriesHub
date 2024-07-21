package com.Core_Service.service;

import com.Core_Service.config_jwt.JwtHelper;
import com.Core_Service.enums.Authorities;
import com.Core_Service.model.User;
import com.Core_Service.model_request.JWTRequest;
import com.Core_Service.model_request.UserCreateRequest;
import com.Core_Service.model_response.JWTResponse;
import com.Core_Service.model_response.UserResponse;
import com.Core_Service.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@EnableTransactionManagement
public class UserService implements UserDetailsManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(UserCreateRequest userCreateRequest) {
        User.UserBuilder userBuilder = User.builder()
                .username(userCreateRequest.getUsername())
                .password(passwordEncoder.encode(userCreateRequest.getPassword()));

        if (userCreateRequest.getAdmin() != null){
            userBuilder.authority(Authorities.ADMIN.toString());
        }
        else userBuilder.authority(Authorities.VIEWER.toString());

        return userRepository.save(userBuilder.build());
    }

    @Override
    public void createUser(UserDetails user) {
        // Already Implemented
    }

    @Override
    public void updateUser(UserDetails user) {
        // TODO:: updateUser To Be Implemented
    }

    @Override
    public void deleteUser(String username) {
        // TODO:: deleteUser To Be Implemented
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO:: changePassword To Be Implemented
    }

    @Override
    public boolean userExists(String username) {
        // TODO:: user should first load from cache
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO:: user should first load from cache
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(!optionalUser.isEmpty()) return optionalUser.get();
        throw new UsernameNotFoundException("There are no user with provided username");
    }
}
