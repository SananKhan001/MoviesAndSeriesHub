package com.payment.service.functions;

import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class TestCloudFunction {

    @Bean
    public Function<String, String> test(){
        return name -> "My name is " + name;
    }

    @Bean
    public Supplier<String> anotherTest() {
        return () -> "Response from anotherTest";
    }

}
