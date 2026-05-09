package com.gotrack.inventory_service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.inventory_service.Dto.PickupResponceDTO;
import com.gotrack.inventory_service.client.feign.PickupClient;

@Service
public class PickupService {
    
    @Autowired
    private  PickupClient pickupClient;

    public PickupResponceDTO getPickup(Long id) {
        try {
          return pickupClient.getPickup(id);
        } catch (Exception e) {
            return null;
        }
    }
    

}
