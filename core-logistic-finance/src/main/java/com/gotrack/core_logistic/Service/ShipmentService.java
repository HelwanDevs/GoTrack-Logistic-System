package com.gotrack.core_logistic.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.ExceptionHandling.ConflictException;
import com.gotrack.core_logistic.ExceptionHandling.ResourceNotFoundException;
import com.gotrack.core_logistic.Specifications.ShipmentSpecification;
import com.gotrack.core_logistic.enums.ShipmentStatus;
import com.gotrack.core_logistic.mapper.shipmentMapper;
import com.gotrack.core_logistic.model.dto.ShipmentDTO;
import com.gotrack.core_logistic.model.dto.ShipmentFilter;
import com.gotrack.core_logistic.model.entity.Shipment;
import com.gotrack.core_logistic.repository.PickupRepo;
import com.gotrack.core_logistic.repository.ShipmentRepo;



@Service    
public class ShipmentService {
    
    @Autowired
    private ShipmentRepo shipmentRepo;
    @Autowired
    private PickupRepo pickupRepo;
    @Autowired
    private shipmentMapper ShipmentMapper;

    
    public ShipmentDTO createShipment(ShipmentDTO shipmentRequest) {
        pickupRepo.findById(shipmentRequest.getPickupRequest().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Pickup request not found with id: " + shipmentRequest.getPickupRequest().getId()));

        Shipment shipment = ShipmentMapper.toEntity(shipmentRequest);

        //TODO: INtegrate with courier service to validate courierId
        shipment.setCourierId(shipmentRequest.getCourierId());
        shipment.setStatus(ShipmentStatus.PendingPickup);
        Shipment savedShipment = shipmentRepo.save(shipment);
        return ShipmentMapper.toDto(savedShipment);

        }


    public ShipmentDTO updateShipmentStatus(Long id, ShipmentDTO shipmentRequest) {
        Shipment existingShipment = shipmentRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        
            ShipmentStatus currentStatus = existingShipment.getStatus();   
            ShipmentStatus newStatus = shipmentRequest.getStatus();   
        
        if(currentStatus == ShipmentStatus.DELIVERED ) {
            throw new ConflictException("Shipment is already completed ");
        }

        if(! currentStatus.canTransitionToS(newStatus)){
            throw new ConflictException("Shipment cannot be transitioned from " + currentStatus + " to " + newStatus);
        }

        if(newStatus == ShipmentStatus.DELIVERED){
            //TODO:Add shipping profits and adjust financial accounts
        }

        existingShipment.setStatus(newStatus);
        Shipment updatedShipment = shipmentRepo.save(existingShipment);
        return ShipmentMapper.toDto(updatedShipment);
    }
    
    
    public Page<ShipmentDTO> searchShipments(ShipmentFilter filter, Pageable pageable) {

     Specification<Shipment> spec = ShipmentSpecification.filterShipments(filter);

     return shipmentRepo.findAll(spec, pageable)
                .map(ShipmentMapper::toDto);
}

    
    


}