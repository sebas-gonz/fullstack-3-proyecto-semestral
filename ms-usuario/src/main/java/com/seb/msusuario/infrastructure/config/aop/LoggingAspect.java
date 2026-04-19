package com.seb.msusuario.infrastructure.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class LoggingAspect {

    @Pointcut("execution(* com.seb.msusuario.domain.service.*.*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Metodo: {} ejecutado con los argumentos: {}", joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Metodo: {} ejecutado con el resultado: {}", joinPoint.getSignature().getName(), result);
    }

}
