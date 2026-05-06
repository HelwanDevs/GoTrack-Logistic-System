package com.gotrack.support_and_notifications_service.eventWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class WEventPublisher {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void publish(Object event) {
        publisher.publishEvent(event);
    }

}
