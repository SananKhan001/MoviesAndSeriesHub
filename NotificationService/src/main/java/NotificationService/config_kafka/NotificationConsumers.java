package NotificationService.config_kafka;

import NotificationService.dto.NewNotification;
import NotificationService.dto.UserStatusMessage;
import NotificationService.enums.Status;
import NotificationService.repository.redis_db_repo.ActiveUserRepository;
import NotificationService.service.WSservice;
import org.commonDTO.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Fuseable;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class NotificationConsumers {

    @Autowired
    private WSservice wSservice;

    @Autowired
    private ActiveUserRepository activeUserRepository;

    @Bean
    public Consumer<String> globalNotificationConsumer() {
        return notification -> wSservice.sendMessage(notification);
    }

    @Bean
    public Consumer<NotificationMessage> privateNotificationConsumer() {
        return notification -> wSservice.sendUser(notification);
    }

    @Bean
    public Supplier<Long> activeUserSupplier() {

        return () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return activeUserRepository.getActiveUsersCount();
        };
    }

    @Bean
    public Consumer<Long> activeUserConsumer() {
        return count -> {
            wSservice.sendActiveUserCount(count);
        };
    }

    @Bean
    public Consumer<Integer> updateActiveUserCountConsumer() {
        return num -> {
            Boolean isFirstUser = activeUserRepository.isFirstUser();
            if(isFirstUser) {
                activeUserRepository.updateActiveUserCount(0L);
                activeUserRepository.isFirstUser(false);
            }

            Long activeUsers = activeUserRepository.getActiveUsersCount() + num;
            activeUserRepository.updateActiveUserCount(activeUsers);
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
        };
    }

    @Bean
    public Consumer<NewNotification> newNotificationConsumer() {
        return newNotification -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            wSservice.sendNewNotificationCount(newNotification);
        };
    }

}
