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
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.core_logistic.Service.ShipmentService;
import com.gotrack.core_logistic.model.dto.ShipmentDTO;
import com.gotrack.core_logistic.model.dto.DTOFilters.ShipmentFilter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ShipmentDTO> createShipment(@Valid @RequestBody ShipmentDTO request) {
        ShipmentDTO response = shipmentService.createShipment(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ShipmentDTO> updateShipmentStatus(@PathVariable Long id, @RequestBody ShipmentDTO request) {
        ShipmentDTO response = shipmentService.updateShipmentStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public ResponseEntity<Page<ShipmentDTO>> GetMyShipments(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ShipmentDTO> response = shipmentService.GetMyShipments(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Page<ShipmentDTO>> searchShipments(
            ShipmentFilter filter,
            @PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ShipmentDTO> response = shipmentService.searchShipments(filter, pageable);
        return ResponseEntity.ok(response);
    }



   @GetMapping("/{id}")
   @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN', 'EMPLOYEE')")
   public ResponseEntity<ShipmentDTO> getShipmentById(@PathVariable Long id) {
       ShipmentDTO dto = shipmentService.getShipmentById(id);
       return ResponseEntity.ok(dto);
   }
   
}