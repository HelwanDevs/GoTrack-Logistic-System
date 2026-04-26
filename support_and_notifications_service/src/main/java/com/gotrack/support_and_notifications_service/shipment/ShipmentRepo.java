package com.gotrack.support_and_notifications_service.shipment;

import org.springframework.stereotype.Component;

@Component
public class ShipmentRepo {

    public boolean existsById(Long shipmentId) {

        return true;
    }
}
