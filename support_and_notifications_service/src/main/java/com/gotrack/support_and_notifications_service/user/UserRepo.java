package com.gotrack.support_and_notifications_service.user;

import org.springframework.stereotype.Component;

@Component
public class UserRepo {

    public boolean existsById(String profileId) {

        return true;
    }
}
