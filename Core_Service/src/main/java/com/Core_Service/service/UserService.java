package com.Core_Service.service;

import com.Core_Service.enums.Authorities;
import com.Core_Service.model.User;
import com.Core_Service.model_request.UserCreateRequest;
import com.Core_Service.repository.db_repository.UserRepository;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableTransactionManagement
public class UserService implements UserDetailsManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StreamBridge streamBridge;

    @Transactional
    public User createUser(UserCreateRequest userCreateRequest) {
        User.UserBuilder userBuilder = User.builder()
                .username(userCreateRequest.getUsername())
                .password(passwordEncoder.encode(userCreateRequest.getPassword()));

        if (userCreateRequest.getAdmin() != null){
            userBuilder.authority(Authorities.ADMIN.toString());
        }
        else userBuilder.authority(Authorities.VIEWER.toString());

        User user = userRepository.save(userBuilder.build());

        /**                                ---------------------------
         *  UserCreationMessage ======>>> | UserCreationMessageTopic |
         *                                ---------------------------
         */
        UserCreationMessage userCreationMessage = UserCreationMessage.builder()
                .id(user.getId()).username(user.getUsername())
                .password("Password Will Not Be Sent Anywhere !!!")
                .authority(user.getAuthority()).isNew(true).build();
        streamBridge.send("UserCreationMessageTopic", userCreationMessage);

        return user;
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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("There are no user with provided username"));
    }
}
