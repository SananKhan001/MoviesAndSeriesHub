package NotificationService.config_kafka;

import NotificationService.service.UserService;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class UserCreationMessageTransaction {

    @Autowired
    private UserService userService;

    @Bean
    public Consumer<UserCreationMessage> userCreationMessage() {
        return userCreationMessage -> userService.createUser(userCreationMessage);
    }

}
