package com.example.mybookshopapp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    public Object aroundApiExecutionTimeAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        Object returnValue = null;
        logger.info("Выполняется метод {} в сервисе {}", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName());
        long durationMills = new Date().getTime();
        try {
            returnValue = proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            logger.error("При вызове метода {} из класса {} произошла ошибка {}", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName(), t.getMessage());
        }
        durationMills = new Date().getTime() - durationMills;

        if (durationMills > 10_000L) {
            logger.warn("При вызове метода {} из класса {} запрос в БД превысил 10 секунд!", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName());
        }

        return returnValue;
    }

    @Around(value = "logMethodPointcut()")
    public Object aroundDurationTrackingAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        long durationMils = new Date().getTime();
        logger.info("Выполняется метод {} в сервисе {}", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName());
        Object returnValue = null;
        try {
            returnValue = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("При вызове метода {} из класса {} произошла ошибка {}", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName(), throwable.getMessage());
        }

        durationMils = new Date().getTime() - durationMils;
        if (durationMils > 10_000L) {
            logger.warn("При вызове метода {} из класса {} запрос в БД превысил 10 секунд!", proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getThis().getClass().getSimpleName());
        }

        return returnValue;
    }
}
