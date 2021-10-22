package com.example.mybookshopapp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Aspect
@Component
public class ApiExecutionTimeLoggerAspect {

    private final Logger logger = LoggerFactory.getLogger(ApiExecutionTimeLoggerAspect.class);

    @Pointcut(value = "@annotation(com.example.mybookshopapp.annotations.ApiExecutionTimeLoggable)")
    public void logApiPointcut() {
    }

    @Pointcut(value = "@annotation(com.example.mybookshopapp.annotations.MethodExecutionTimeLoggable)")
    public void logMethodPointcut() {
    }

    @Around(value = "logApiPointcut()")
    public Object aroundExecutionApiAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        LocalDateTime beforeMethodStarted = LocalDateTime.now();
        logger.info("Выполняется api-запрос {} в сервисе {}", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName());
        Object returnValue = null;
        try {
            returnValue = proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            logger.error("При вызове api-запроса {} из класса {} произошла ошибка {}", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName(), t.getMessage());
        }
        LocalDateTime afterMethodExecution = LocalDateTime.now();

        Duration duration = Duration.between(beforeMethodStarted, afterMethodExecution);

        if (duration.getSeconds() > 10) {
            logger.warn("При вызове api-запроса {} из класса {} запрос в БД превысил 10 секунд!", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName());
        }

        return returnValue;
    }

    @Around(value = "logMethodPointcut()")
    public Object aroundExecutionMethodAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        LocalDateTime beforeMethodStarted = LocalDateTime.now();
        logger.info("Выполняется метод {} в сервисе {}", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName());
        Object returnValue = null;
        try {
            returnValue = proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            logger.error("При вызове метода {} из класса {} произошла ошибка {}", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName(), t.getMessage());
        }
        LocalDateTime afterMethodExecution = LocalDateTime.now();

        Duration duration = Duration.between(beforeMethodStarted, afterMethodExecution);

        if (duration.getSeconds() > 10) {
            logger.warn("При вызове метода {} из класса {} запрос в БД превысил 10 секунд!", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName());
        }

        return returnValue;
    }
}
