package com.gotrack.core_logistic.finance_service.Service.Pickup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.finance_service.enums.PickupStatus;
import com.gotrack.core_logistic.finance_service.mapper.PickupRequestMapper;
import com.gotrack.core_logistic.finance_service.model.dto.PickupRequestDTO;
import com.gotrack.core_logistic.finance_service.model.entity.Pickup;
import com.gotrack.core_logistic.finance_service.repository.PickupRepo;



@Service
public class PickupService {

        @Autowired
        private PickupRepo pickupRepository;
    
    
        public PickupRequestDTO createPickup(PickupRequestDTO pickupRequest) {
             Pickup pickup = PickupRequestMapper.toEntity(pickupRequest);
             pickup.setStatus(PickupStatus.Pending);
             Pickup savedPickup = pickupRepository.save(pickup);
             return PickupRequestMapper.toDTO(savedPickup);

        }



        public PickupRequestDTO updatePickup(Long id, PickupRequestDTO pickupRequest) {
                   Pickup existingPickup = pickupRepository.findById(id)
                         .orElseThrow(() -> new RuntimeException("Pickup not found with id: " + id));
                   
                   if(existingPickup.getStatus() == PickupStatus.Completed || existingPickup.getStatus() == PickupStatus.Cancelled) {
                        throw new RuntimeException("Cannot update a pickup that is already Completed or Cancelled");
                }
                
                    existingPickup.setStatus(pickupRequest.getStatus());
                    existingPickup.setPickupTime(pickupRequest.getPickupTime());

                   Pickup updatedPickup = pickupRepository.save(existingPickup);
                   return PickupRequestMapper.toDTO(updatedPickup);
        }
        


        public PickupRequestDTO assignCourier(Long id, Long courierId) {
                Pickup existingPickup = pickupRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Pickup request not found"));
                
                existingPickup.setCourierId(courierId);
                existingPickup.setStatus(PickupStatus.CurierAssigned);

                Pickup updatedPickup = pickupRepository.save(existingPickup);
                return PickupRequestMapper.toDTO(updatedPickup);
        }
                 
        
        public List<PickupRequestDTO> searchPickups(Long id, Long customerId, Long courierId, String status) {
                List<Pickup> pickups = pickupRepository.findAll(); 
                return pickups.stream()
                        .filter(p -> (id == null || p.getId().equals(id)) &&
                                     (customerId == null || p.getCustomerId().equals(customerId)) &&
                                     (courierId == null || p.getCourierId().equals(courierId)) &&
                                     (status == null || p.getStatus().name().equalsIgnoreCase(status)))
                        .map(PickupRequestMapper::toDTO)
                        .toList();
        }
        
    
}