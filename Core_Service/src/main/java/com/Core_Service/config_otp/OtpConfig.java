package com.Core_Service.config_otp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class OtpConfig {

    @Bean
    public SecureRandom getSecureRandom() {
        return new SecureRandom();
    }

}
