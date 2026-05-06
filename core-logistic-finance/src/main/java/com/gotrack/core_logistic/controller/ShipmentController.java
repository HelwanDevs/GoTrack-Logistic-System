package com.gotrack.core_logistic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.core_logistic.Service.ShipmentService;
import com.gotrack.core_logistic.model.dto.ShipmentDTO;
import com.gotrack.core_logistic.model.dto.Filters.ShipmentFilter;

import jakarta.validation.Valid;




@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

   @Autowired
   private ShipmentService shipmentService;


   @PostMapping
   public ResponseEntity<ShipmentDTO> createShipment(@Valid @RequestBody ShipmentDTO request) {
       ShipmentDTO response = shipmentService.createShipment(request);
       return ResponseEntity.ok(response);
   }
   
   @PutMapping("/{id}/status")
   public ResponseEntity<ShipmentDTO> updateShipmentStatus(@PathVariable Long id, @Valid @RequestBody ShipmentDTO request) {
       ShipmentDTO response = shipmentService.updateShipmentStatus(id, request);
       return ResponseEntity.ok(response);
   }

   @GetMapping("/search")
    public ResponseEntity<Page<ShipmentDTO>> searchShipments(
        ShipmentFilter filter,
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable
) {

    Page<ShipmentDTO> response = shipmentService.searchShipments(filter, pageable);
    return ResponseEntity.ok(response);
}
   
}