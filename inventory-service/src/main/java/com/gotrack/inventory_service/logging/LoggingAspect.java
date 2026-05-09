package com.gotrack.inventory_service.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
// import org.springframework.stereotype.Component;

@Aspect
// @Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private static final String BASE_PACKAGE = "com.gotrack";

    private static final String APPLICATION_PACKAGE = "within(" + BASE_PACKAGE + "..*)";

    private static final String EXCLUDED_PACKAGES = "!within(" + BASE_PACKAGE + "..config..*) " +
            "&& !within(" + BASE_PACKAGE + "..logging..*)" + "&& !within(" + BASE_PACKAGE + "..filter..*)";

    @Pointcut(APPLICATION_PACKAGE + " && " + EXCLUDED_PACKAGES)
    public void app() {
    }

    @Pointcut("app() && within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
    }

    @Pointcut("app() && within(@org.springframework.stereotype.Service *)")
    public void service() {
    }

    @Pointcut("app() && within(@org.springframework.stereotype.Repository *)")
    public void repository() {
    }

    @Around("controller() || service() || repository()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("[EXECUTION TIME] IN {}.{}() | args = ( {} ) | execution time = {} ms",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()),
                    (endTime - startTime));
            return result;
        } catch (Throwable ex) {
            long endTime = System.currentTimeMillis();
            log.info("[EXECUTION TIME] IN {}.{}() | args = ( {} ) | cause = {} | execution time = {} ms",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()),
                    (ex.getCause() != null ? ex.getCause() : "NULL"),
                    (endTime - startTime));
            throw ex;
        }
    }

    @AfterThrowing(pointcut = "app()", throwing = "ex")
    public void logException(JoinPoint jpoint, Throwable ex) {
        log.error("[EXCEPTION] " + "IN {}.{}() | cause={} | message={}",
                jpoint.getTarget().getClass().getSimpleName(),
                jpoint.getSignature().getName(),
                (ex.getCause() != null ? ex.getCause() : "NULL"),
                ex.getMessage());
    }

}
