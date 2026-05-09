package com.gotrack.support_and_notifications_service.notification.strategy;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;

@Component
public class NotificationChannelResolver {
    private final Map<NotificationChannel, NotificationChannelStrategy> strategies = new EnumMap<>(
            NotificationChannel.class);

    public NotificationChannelResolver(List<NotificationChannelStrategy> strategyList) {
        for (NotificationChannelStrategy strategy : strategyList) {
            strategies.put(strategy.channel(), strategy);
        }
    }

    public NotificationChannelStrategy get(NotificationChannel channel) {
        NotificationChannelStrategy strategy = strategies.get(channel);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported channel: " + channel);
        }
        return strategy;
    }
}
