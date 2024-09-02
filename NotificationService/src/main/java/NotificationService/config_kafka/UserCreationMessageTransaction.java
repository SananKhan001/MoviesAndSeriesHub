package NotificationService.config_kafka;

import NotificationService.service.EmailService;
import NotificationService.service.UserService;
import org.commonDTO.OtpNotificationMessage;
import org.commonDTO.UserCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class UserCreationMessageTransaction {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Value("${mail.notification.greeting}")
    private String greeting;

    @Bean
    public Consumer<UserCreationMessage> userCreationMessage() {
        return userCreationMessage -> userService.createUser(userCreationMessage);
    }

    @Bean
    public Consumer<OtpNotificationMessage> otpNotificationMessage() {
        return otpNotificationMessage -> emailService.sendEmail("OTP Notification",
                greeting, otpNotificationMessage.getMessage(), otpNotificationMessage.getUsername());
    }

}
