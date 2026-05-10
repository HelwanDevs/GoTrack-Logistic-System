package com.gotrack.support_and_notifications_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gotrack.support_and_notifications_service.client.dto.ShipmentDTO;

@FeignClient(name = "core-logistic-finance-service", contextId = "shipmentClient")
public interface ShipmentClient {

    @GetMapping("/api/shipments/{id}")
    ShipmentDTO getShipment(@PathVariable("id") Long id);
}
