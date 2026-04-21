package com.gotrack.core_logistic.finance_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.gotrack.core_logistic.finance_service.enums.ShipmentStatus;
import com.gotrack.core_logistic.finance_service.mapper.shipmentMapper.ShipmentMapper;
import com.gotrack.core_logistic.finance_service.model.dto.ShipmentDTO;
import com.gotrack.core_logistic.finance_service.model.entity.Pickup;
import com.gotrack.core_logistic.finance_service.model.entity.Shipment;
import com.gotrack.core_logistic.finance_service.repository.PickupRepo;
import com.gotrack.core_logistic.finance_service.repository.ShipmentRepo;

@Service    
public class ShipmentService {
    
    @Autowired
    private ShipmentRepo shipmentRepo;
    @Autowired
    private PickupRepo pickupRepo;
    

    
    public ShipmentDTO createShipment(ShipmentDTO shipmentRequest) {
        Pickup pickupRequest = pickupRepo.findById(shipmentRequest.getPickupRequestId())
            .orElseThrow(() -> new RuntimeException("Pickup not found "));

        Shipment shipment = ShipmentMapper.toEntity(shipmentRequest, pickupRequest);
        shipment.setStatus(ShipmentStatus.PendingPickup);
        Shipment savedShipment = shipmentRepo.save(shipment);
        return ShipmentMapper.toDto(savedShipment);

        }


    public ShipmentDTO updateShipmentStatus(Long id, ShipmentDTO shipmentRequest) {
        Shipment existingShipment = shipmentRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Shipment not found "));
        
            ShipmentStatus currentStatus = existingShipment.getStatus();   
            ShipmentStatus newStatus = shipmentRequest.getStatus();   

        if(! currentStatus.canTransitionToS(newStatus)){
            throw new RuntimeException("Invalid status transition ");
        }

        existingShipment.setStatus(newStatus);

        System.out.println("Current: " + currentStatus);
        System.out.println("New: " + newStatus);
        
        Shipment updatedShipment = shipmentRepo.save(existingShipment);
        return ShipmentMapper.toDto(updatedShipment);
    }
    
    
    public List<ShipmentDTO> searchShipments(Long customerId, Long courierId, String status, Long flayerNumber, Long id) {
                       List<Shipment> shipments = shipmentRepo.findAll(); 
                return shipments.stream()
                        .filter(s -> (id == null || s.getId().equals(id)) &&
                                     (customerId == null || s.getCustomerId().equals(customerId)) &&
                                     (courierId == null || s.getCourierId().equals(courierId)) &&
                                     (status == null || s.getStatus().name().equalsIgnoreCase(status)))
                        .map(ShipmentMapper::toDto)
                        .toList();
    }

    
    


}