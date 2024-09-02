package NotificationService.config_kafka;

import NotificationService.dto.NewNotification;
import NotificationService.dto.UserStatusMessage;
import NotificationService.enums.Status;
import NotificationService.repository.redis_db_repo.ActiveUserRepository;
import NotificationService.service.UserService;
import NotificationService.service.WSservice;
import org.commonDTO.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Fuseable;

import java.security.Principal;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class NotificationConsumers {

    @Autowired
    private WSservice wSservice;

    @Autowired
    private ActiveUserRepository activeUserRepository;

    @Autowired
    private UserService userService;

    @Bean
    public Consumer<String> globalNotificationConsumer() {
        return notification -> wSservice.sendMessage(notification);
    }

    @Bean
    public Consumer<NotificationMessage> privateNotificationConsumer() {
        return notification -> {
            wSservice.sendUser(notification);
        };
    }

    @Bean
    public Consumer<Long> activeUserConsumer() {
        return count -> {
            wSservice.sendActiveUserCount(count);
        };
    }

    @Bean
    public Consumer<UserStatusMessage> userStatusMessageConsumer() {
        return userStatusMessage -> {
            Set<Long> activeUserSet = activeUserRepository.getActiveUsersSet();
            if(userStatusMessage.getStatus().equals(Status.ACTIVE)) {
                activeUserSet.add(userStatusMessage.getUserId());
            } else {
                activeUserSet.remove(userStatusMessage.getUserId());
            }
            activeUserRepository.updateActiveUsersSet(activeUserSet);
            wSservice.sendActiveUserCount((long) activeUserRepository.getActiveUsersSet().size());
        };
    }

}
