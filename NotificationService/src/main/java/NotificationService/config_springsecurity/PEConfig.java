package NotificationService.config_springsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PEConfig {
    @Bean
    public PasswordEncoder getPE(){
        return new BCryptPasswordEncoder();
    }
}
