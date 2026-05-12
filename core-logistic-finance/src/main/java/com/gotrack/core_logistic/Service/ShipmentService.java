package com.gotrack.core_logistic.Service;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.ExceptionHandling.ConflictException;
import com.gotrack.core_logistic.ExceptionHandling.ResourceNotFoundException;
import com.gotrack.core_logistic.Service.finance.FinanceService;
import com.gotrack.core_logistic.Specifications.ShipmentSpecification;
import com.gotrack.core_logistic.enums.PickupStatus;
import com.gotrack.core_logistic.enums.ShipmentStatus;
import com.gotrack.core_logistic.filter.AuthenticationDetails;
import com.gotrack.core_logistic.mapper.shipmentMapper;
import com.gotrack.core_logistic.model.dto.ProfileResponse;
import com.gotrack.core_logistic.model.dto.ShipmentDTO;
import com.gotrack.core_logistic.model.dto.DTOFilters.ShipmentFilter;
import com.gotrack.core_logistic.model.entity.Pickup;
import com.gotrack.core_logistic.model.entity.Shipment;
import com.gotrack.core_logistic.model.entity.Wallet;
import com.gotrack.core_logistic.repository.PickupRepo;
import com.gotrack.core_logistic.repository.ShipmentRepo;
import com.gotrack.core_logistic.repository.WalletRepo;



@Service    
public class ShipmentService {
    
    @Autowired
        private ShipmentRepo shipmentRepo;
    @Autowired
        private PickupRepo pickupRepo;
    @Autowired
        private shipmentMapper ShipmentMapper;
    @Autowired
        private FinanceService financeService;
    @Autowired
        private ProfileBranchService profileBranchService;
    @Autowired
        private WalletRepo walletRepo;
    
    
    public ShipmentDTO createShipment(ShipmentDTO shipmentRequest) {
        Pickup pickup = pickupRepo.findById(shipmentRequest.getPickupRequest().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Pickup request not found with id: " + shipmentRequest.getPickupRequest().getId()));
        
        if(pickup.getStatus() != PickupStatus.Pending){
            throw new ConflictException("Pickup request is not in pending status , it should be in pending status to create a shipment");
        }

        Shipment shipment = ShipmentMapper.toEntity(shipmentRequest);
        
          ProfileResponse profile = profileBranchService.getProfileById(shipmentRequest.getCourierId());
                if(profile.getType().toString() != "COURIER")
                    throw new ConflictException("This is not a courier profile");

        
        shipment.setCourierId(shipmentRequest.getCourierId());
        pickup.setStatus(PickupStatus.Accepted);
        shipment.setStatus(ShipmentStatus.PendingPickup);

        pickupRepo.save(pickup);
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
            financeService.shipmentCalculation(existingShipment.getShipmentFee(), existingShipment.getTotalPrice());

            Wallet merchantWallet = walletRepo.findByProfileId(existingShipment.getMERCHANTId())
            .orElseThrow(() -> new ResourceNotFoundException("Customer wallet not found"));

            if (merchantWallet.getBalance() == null) 
                merchantWallet.setBalance(BigDecimal.ZERO);
            
            merchantWallet.setBalance(merchantWallet.getBalance().add(existingShipment.getTotalPrice().subtract(existingShipment.getShipmentFee())));
            walletRepo.save(merchantWallet);
            
        }

        if(currentStatus == ShipmentStatus.PendingPickup ){
            

                if (shipmentRequest.getCourierId() != null) {

                    ProfileResponse profile = profileBranchService.getProfileById(shipmentRequest.getCourierId());
                    
                    if (!"COURIER".equals(profile.getType().toString())) 
                        throw new ConflictException("This is not a courier profile");
                
                    existingShipment.setCourierId(shipmentRequest.getCourierId());
                }
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
    
    public Page<ShipmentDTO> GetMyShipments(Pageable pageable){
        
         
        AuthenticationDetails authDetails = new AuthenticationDetails();
        String accountId = authDetails.getAccountId();
        ProfileResponse profile = profileBranchService.getProfileByAccountId(accountId);
        
         return shipmentRepo
             .findByMERCHANTId(profile.getId(), pageable)
             .map(ShipmentMapper::toDto);
   }
    
    


}