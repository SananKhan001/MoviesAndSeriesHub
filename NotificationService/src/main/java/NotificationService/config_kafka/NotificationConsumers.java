package NotificationService.config_kafka;

import NotificationService.repository.redis_db_repo.ActiveUserRepository;
import NotificationService.service.WSservice;
import org.commonDTO.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Fuseable;

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

}
