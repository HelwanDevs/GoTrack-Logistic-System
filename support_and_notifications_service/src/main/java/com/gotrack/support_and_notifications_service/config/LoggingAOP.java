package com.gotrack.support_and_notifications_service.config;

import org.springframework.stereotype.Component;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggingAOP {

        private static final Logger log = LoggerFactory.getLogger(LoggingAOP.class);

        @Pointcut("within(com.gotrack.support_and_notifications_service..*)&& !within(com.gotrack.support_and_notifications_service.config.LoggingAOP)&& !within(com.gotrack.support_and_notifications_service.security..*)")
        public void app() {
        }

        @AfterThrowing(pointcut = "app()", throwing = "ex")
        public void logException(JoinPoint jpoint, Throwable ex) {
                log.error("Exception in {}.{}() with cause = {} and message = {}",
                                jpoint.getTarget().getClass().getSimpleName(),
                                jpoint.getSignature().getName(),
                                (ex.getCause() != null ? ex.getCause() : "NULL"),
                                ex.getMessage());
        }

        @Pointcut("app() && within(@org.springframework.web.bind.annotation.RestController *)")
        public void controller() {
        }

        @Before("controller()")
        public void logControllerEnter(JoinPoint jpoint) {
                log.info("Entering Controller {} ",
                                jpoint.getSignature().toShortString());
                log.info("[CONTROLLER] --> {}.{}() | args: {}",
                                jpoint.getTarget().getClass().getSimpleName(),
                                jpoint.getSignature().getName(),
                                Arrays.toString(jpoint.getArgs()));

        }

        @AfterReturning(pointcut = "controller()", returning = "result")
        public void logControllerExit(JoinPoint jpoint, Object result) {
                log.info("Exiting controller {} ",
                                jpoint.getSignature().toShortString());
                log.info("[CONTROLLER] <-- {}.{}() | return: {}",
                                jpoint.getTarget().getClass().getSimpleName(),
                                jpoint.getSignature().getName(),
                                result);
        }

        @Pointcut("app() && within(@org.springframework.stereotype.Service *)")
        public void service() {
        }

        @Around("service()")
        public Object logService(ProceedingJoinPoint jpoint) throws Throwable {
                long startTime = System.currentTimeMillis();
                log.info("Within service method");
                log.info("[SERVICE] --> {}.{}() | args: {}",
                                jpoint.getTarget().getClass().getSimpleName(),
                                jpoint.getSignature().getName(),
                                Arrays.toString(jpoint.getArgs()));
                try {
                        Object result = jpoint.proceed();
                        long endTime = System.currentTimeMillis();
                        log.info("[SERVICE] <-- {}.{}() | time: {} ms | return: {}",
                                        jpoint.getTarget().getClass().getSimpleName(),
                                        jpoint.getSignature().getName(),
                                        endTime - startTime,
                                        result);
                        return result;
                } catch (Throwable ex) {
                        long endTime = System.currentTimeMillis();
                        log.info("[SERVICE] <-- {}.{}() | time: {} ms | exception: {}",
                                        jpoint.getTarget().getClass().getSimpleName(),
                                        jpoint.getSignature().getName(),
                                        endTime - startTime,
                                        ex.getClass().getSimpleName() + ": " + ex.getMessage());
                        throw ex;
                }
        }

        @Pointcut("app() && execution(* com.gotrack.support_and_notifications_service.notification.listeners.*.*(..))")
        public void listener() {
        }

        @Before("listener()")
        public void logListenerEnter(JoinPoint jpoint) {
                log.info("Entering Listener {} ",
                                jpoint.getSignature().toShortString());
                log.info("[LISTENER] --> {}.{}() | event: {}",
                                jpoint.getTarget().getClass().getSimpleName(),
                                jpoint.getSignature().getName(),
                                Arrays.toString(jpoint.getArgs()));

        }

        @AfterReturning("listener()")
        public void logListenerExit(JoinPoint jpoint) {
                log.info("Exiting Listener {} ",
                                jpoint.getSignature().toShortString());
                log.info("[LISTENER] <-- {}.{}()",
                                jpoint.getTarget().getClass().getSimpleName(),
                                jpoint.getSignature().getName());
        }

        @Pointcut("app()" + "&& within(@org.springframework.stereotype.Component *)" +
                        "&& !within(com.gotrack.support_and_notifications_service.config..*)")
        public void component() {
        }

        @Before("component()")
        public void logComponentEnter(JoinPoint jpoint) {
                log.info("Entering Component {} ",
                                jpoint.getSignature().toShortString());
                log.info("[COMPONENT] --> {}.{}() | args: {}",
                                jpoint.getTarget().getClass().getSimpleName(),
                                jpoint.getSignature().getName(),
                                Arrays.toString(jpoint.getArgs()));

        }

        @AfterReturning(pointcut = "component()", returning = "result")
        public void logComponentExit(JoinPoint jpoint, Object result) {
                log.info("Exiting component {} ",
                                jpoint.getSignature().toShortString());
                log.info("[COMPONENT] <-- {}.{}() | return: {}",
                                jpoint.getTarget().getClass().getSimpleName(),
                                jpoint.getSignature().getName(),
                                result);
        }

        @Pointcut("app() && within(com.gotrack.support_and_notifications_service..repo..*)")
        public void repository() {
        }

        @Around("repository()")
        public Object logRepository(ProceedingJoinPoint jpoint) throws Throwable {
                long startTime = System.currentTimeMillis();
                log.info("Within repository method");
                log.info("[REPOSITORY] --> {}.{}() | args: {}",
                                jpoint.getTarget().getClass().getSimpleName(),
                                jpoint.getSignature().getName(),
                                Arrays.toString(jpoint.getArgs()));
                try {
                        Object result = jpoint.proceed();
                        long endTime = System.currentTimeMillis();
                        log.info("[REPOSITORY] <-- {}.{}() | time: {} ms",
                                        jpoint.getTarget().getClass().getSimpleName(),
                                        jpoint.getSignature().getName(),
                                        endTime - startTime);
                        return result;
                } catch (Throwable ex) {
                        log.error("Exception in {}.{}() with cause = {} and message = {}",
                                        jpoint.getTarget().getClass().getSimpleName(),
                                        jpoint.getSignature().getName(),
                                        (ex.getCause() != null ? ex.getCause() : "NULL"),
                                        ex.getMessage());
                        throw ex;
                }
        }

}
