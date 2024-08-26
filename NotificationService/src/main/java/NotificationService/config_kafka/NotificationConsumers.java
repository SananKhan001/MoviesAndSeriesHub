package NotificationService.config_kafka;

import NotificationService.service.WSservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Fuseable;

import java.util.function.Consumer;

@Configuration
public class NotificationConsumers {

    @Autowired
    private WSservice wSservice;

    @Bean
    public Consumer<String> globalNotificationConsumer() {
        return notification -> wSservice.sendMessage(notification);
    }

}
