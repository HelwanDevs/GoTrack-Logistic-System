package com.gotrack.core_logistic.finance_service.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.core_logistic.finance_service.Service.PickupService;
import com.gotrack.core_logistic.finance_service.model.dto.PickupRequestDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pickups")
public class PickupController {

     private final PickupService pickupService;

     @PostMapping
       public ResponseEntity<PickupRequestDTO> createPickup(@RequestBody PickupRequestDTO Request) {
           PickupRequestDTO response = pickupService.createPickup(Request);
              return ResponseEntity.ok(response);
       }

     
       @PutMapping("/{id}")
       public ResponseEntity<PickupRequestDTO> updatePickup(
              @PathVariable Long id,
              @RequestBody PickupRequestDTO Request) {

              PickupRequestDTO response = pickupService.updatePickup(id, Request);
              return ResponseEntity.ok(response);
       }


       @PutMapping("/{id}/assign")
       public ResponseEntity<PickupRequestDTO> assignCourier(
              @PathVariable Long id,
              @RequestParam Long courierId) {

              PickupRequestDTO response = pickupService.assignCourier(id, courierId);
              return ResponseEntity.ok(response);
       }
       

       @GetMapping("/search")
       public List <PickupRequestDTO> searchPickup(
              @RequestParam(required = false) Long id,
              @RequestParam(required = false) Long customerId,
              @RequestParam(required = false) Long courierId,
              @RequestParam(required = false) String status) {
              
              List<PickupRequestDTO> response = pickupService.searchPickups(id, customerId, courierId, status);
              return response;
       }
}