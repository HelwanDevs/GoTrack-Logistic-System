package com.gotrack.support_and_notifications_service.shipment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.support_and_notifications_service.client.dto.ShipmentDTO;
import com.gotrack.support_and_notifications_service.client.feign.ShipmentClient;
import com.gotrack.support_and_notifications_service.error.ResourceNotFoundException;

@Service
public class CheckShipment {

    private static final Logger log = LoggerFactory.getLogger(CheckShipment.class);

    @Autowired
    private ShipmentClient shipmentClient;

    public void checkShipmentExists(Long shipmentId) {
        if (shipmentId == null) {
            return;
        }

        ShipmentDTO shipment = shipmentClient.getShipment(shipmentId);

        if (shipment == null || shipment.getId() == null) {
            log.warn("[SHIPMENT] Empty response for shipmentId={}", shipmentId);
            throw new ResourceNotFoundException("Shipment " + shipmentId + " does not exist");
        }

        log.debug("[SHIPMENT] Verified shipment id={}", shipmentId);
    }

}
