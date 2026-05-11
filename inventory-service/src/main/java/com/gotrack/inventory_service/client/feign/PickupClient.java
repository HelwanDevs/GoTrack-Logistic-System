package com.gotrack.inventory_service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gotrack.inventory_service.Dto.PickupResponceDTO;


@FeignClient(name = "core-logistic-finance-service" )

public interface PickupClient {

    @GetMapping("/api/pickups/{id}")
    PickupResponceDTO getPickup(@PathVariable Long id);

}
