package com.gotrack.support_and_notifications_service.notification.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.config.AsyncConfig;
import com.gotrack.support_and_notifications_service.notification.event.NotificationCreated;
import com.gotrack.support_and_notifications_service.notification.strategy.NotificationChannelResolver;
import com.netflix.spectator.impl.PatternExpr.Not;

@Component
public class NotificationListener {// this class is for demonstration purposes, to show how to listen to events. In
                                   // a real application, you would have more complex logic here, such as sending
                                   // emails or SMS messages.

    @Autowired
    NotificationChannelResolver resolver;

    @Async
    @EventListener
    public void handleNotificationCreatedEvent(NotificationCreated event) {
        resolver.get(event.getChannel()).send(event);
    }

    public NotificationListener(NotificationChannelResolver resolver) {
        this.resolver = resolver;
    }

}
