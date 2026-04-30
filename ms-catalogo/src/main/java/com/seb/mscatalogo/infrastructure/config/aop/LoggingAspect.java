package com.seb.mscatalogo.infrastructure.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(* com.seb.mscatalogo.application.service.*.*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void logBefore(@NotNull JoinPoint joinPoint) {
        log.info("Metodo: {} ejecutado con los argumentos: {}", joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(@NotNull JoinPoint joinPoint, Object result) {
        log.info("Metodo: {} ejecutado con el resultado: {}", joinPoint.getSignature().getName(), result);
    }
}
