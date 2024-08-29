package NotificationService.service;

import NotificationService.model.User;
import NotificationService.repository.db_repo.UserRepository;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
@CacheConfig(cacheNames = "USER_DETAILS", cacheManager = "NotificationCacheManager")
public class UserService implements UserDetailsManager {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void createUser(UserCreationMessage userCreationMessage) {
        User user = User.builder()
                .id(userCreationMessage.getId())
                .username(userCreationMessage.getUsername())
                .password(userCreationMessage.getPassword())
                .authority(userCreationMessage.getAuthority()).build();

        userRepository.save(user);
    }

    @Override
    public void createUser(UserDetails user) {
        // Already Implemented
    }

    @Override
    public void updateUser(UserDetails user) {
        // TODO:: To Be Implemented
    }

    @Override
    public void deleteUser(String username) {
        // TODO:: To Be Implemented
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO:: To Be Implemented
    }

    @Override
    public boolean userExists(String username) {
        return true;
    }

    @Override
    @Cacheable(key = "'user::' + #username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).get();
    }

}
