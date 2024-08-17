package com.Core_Service.aspect;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(public * com.Core_Service.service..*(..))")
    public void serviceOperationLog(JoinPoint joinPoint){
        logger.info("Data Not Present In Cache, Request Went To Main DB: {}", joinPoint.getSignature());
    }

    @Before("execution(* com.Core_Service.repository.cache_repository..*(..))")
    public void cacheOperationLog(JoinPoint joinPoint){
        logger.info("Useless Cache Has Been Cleared: {}", joinPoint.getSignature());
    }

    @Before("execution(* com.Core_Service.repository.db_repository..*(..))")
    public void dbOperationLog(JoinPoint joinPoint) {
        logger.info("Calling Repository Method: {}", joinPoint.getSignature());
    }

}
