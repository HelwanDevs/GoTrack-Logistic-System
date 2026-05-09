package com.gotrack.core_logistic.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.core_logistic.Service.PickupService;
import com.gotrack.core_logistic.model.dto.PickupRequestDTO;
import com.gotrack.core_logistic.model.dto.DTOFilters.PickupFilter;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pickups")
public class PickupController {

       @Autowired
        private  PickupService pickupService;

       @PostMapping
       @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'MERCHANT')")
       public ResponseEntity<PickupRequestDTO> createPickup(@Valid @RequestBody PickupRequestDTO Request) {

           PickupRequestDTO response = pickupService.createPickup(Request);
              return ResponseEntity.ok(response);
       }

       @PutMapping("/{id}")
       @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
       public ResponseEntity<PickupRequestDTO> updatePickup(@PathVariable Long id, @RequestBody PickupRequestDTO Request) {
              PickupRequestDTO response = pickupService.updatePickup(id, Request);
              return ResponseEntity.ok(response);
       }

       @PutMapping("/{id}/assign")
       @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
       public ResponseEntity<PickupRequestDTO> assignCourier(@Valid @PathVariable Long id, @RequestParam Long courierId) {
              PickupRequestDTO response = pickupService.assignCourier(id, courierId);
              return ResponseEntity.ok(response);
       }
       
      @GetMapping("/search")
       @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'MERCHANT')")
       public ResponseEntity<Page<PickupRequestDTO>> searchPickup(
        PickupFilter filter,
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable
) {

    Page<PickupRequestDTO> response = pickupService.searchPickups(filter, pageable);
    return ResponseEntity.ok(response);
}
       
}