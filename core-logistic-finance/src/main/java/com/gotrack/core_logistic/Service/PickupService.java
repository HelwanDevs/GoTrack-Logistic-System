package com.gotrack.core_logistic.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.ExceptionHandling.ConflictException;
import com.gotrack.core_logistic.ExceptionHandling.ResourceNotFoundException;
import com.gotrack.core_logistic.Specifications.PickupSpecification;
import com.gotrack.core_logistic.enums.PickupStatus;
import com.gotrack.core_logistic.mapper.PickupRequestMapper;
import com.gotrack.core_logistic.model.dto.PickupFilter;
import com.gotrack.core_logistic.model.dto.PickupRequestDTO;
import com.gotrack.core_logistic.model.entity.Pickup;
import com.gotrack.core_logistic.repository.PickupRepo;



@Service
public class PickupService {

        @Autowired
        private PickupRepo pickupRepository;
        @Autowired
        private PickupRequestMapper pickupMapper;
        
        
    
    
        public PickupRequestDTO createPickup(PickupRequestDTO pickupRequest) {
             Pickup pickup = pickupMapper.toEntity(pickupRequest);

             //TODO: integrate with customer service to validate customerId
             pickup.setStatus(PickupStatus.Pending);
             pickup.setStatus(PickupStatus.Pending);
             Pickup savedPickup = pickupRepository.save(pickup);
             return pickupMapper.toDTO(savedPickup);

        }



        public PickupRequestDTO updatePickup(Long id, PickupRequestDTO pickupRequest) {
                   Pickup existingPickup = pickupRepository.findById(id)
                         .orElseThrow(() -> new RuntimeException("Pickup not found with id: " + id));

                   PickupStatus currentStatus = existingPickup.getStatus();   
                   PickupStatus newStatus = pickupRequest.getStatus();      
                   
                   if(currentStatus == PickupStatus.Completed || currentStatus == PickupStatus.Cancelled) {
                        throw new ConflictException("Pickup is already completed or cancelled");
                }
                                    
                    if(! currentStatus.canTransitionTo(newStatus)){
                        throw new ConflictException("Pickup cannot be transitioned from " + currentStatus + " to " + newStatus);
                    }

                    existingPickup.setPickupTime(pickupRequest.getPickupTime());
                    existingPickup.setStatus(newStatus);

                   
                   Pickup updatedPickup = pickupRepository.save(existingPickup);
                   return pickupMapper.toDTO(updatedPickup);


        }
        


        public PickupRequestDTO assignCourier(Long id, Long courierId) {
                Pickup existingPickup = pickupRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Pickup not found with id: " + id));
                //TODO: integrate with courier service to validate courierId and check availability
                existingPickup.setCourierId(courierId);
                existingPickup.setStatus(PickupStatus.CurierAssigned);

                Pickup updatedPickup = pickupRepository.save(existingPickup);
                return pickupMapper.toDTO(updatedPickup);
        }
                 
        
        public Page<PickupRequestDTO> searchPickups(PickupFilter filter, Pageable pageable) {

        Specification<Pickup> spec = PickupSpecification.filterPickups(filter);

        return pickupRepository.findAll(spec, pageable)
                    .map(pickupMapper::toDTO);
}
        
    
}