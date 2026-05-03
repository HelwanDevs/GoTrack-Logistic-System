package com.gotrack.support_and_notifications_service.shipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.support_and_notifications_service.error.ResourceNotFoundException;

@Service
public class CheckShipment {
    @Autowired
    private ShipmentRepo repo;

    public void checkShipmentExists(Long shipmentId) {
        if (!repo.existsById(shipmentId)) {
            throw new ResourceNotFoundException("Shipment " + shipmentId + " does not exist");
        }
    }

}
