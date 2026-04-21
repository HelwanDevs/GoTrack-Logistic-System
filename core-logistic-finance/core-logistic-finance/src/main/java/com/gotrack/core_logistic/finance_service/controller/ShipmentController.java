package com.gotrack.core_logistic.finance_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.core_logistic.finance_service.Service.ShipmentService;
import com.gotrack.core_logistic.finance_service.model.dto.ShipmentDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

   @Autowired
   private ShipmentService shipmentService;


   @PostMapping
   public ResponseEntity<ShipmentDTO> createShipment(@RequestBody ShipmentDTO request) {
       ShipmentDTO response = shipmentService.createShipment(request);
       return ResponseEntity.ok(response);
   }
   


   @PutMapping("/{id}/status")
   public ResponseEntity<ShipmentDTO> updateShipmentStatus(@PathVariable Long id, @RequestBody ShipmentDTO request) {
       ShipmentDTO response = shipmentService.updateShipmentStatus(id, request);
       return ResponseEntity.ok(response);
   }



   @GetMapping("/search")
   public List<ShipmentDTO> searchShipments(
       @RequestParam(required = false) Long customerId,
       @RequestParam(required = false) Long courierId,
       @RequestParam(required = false) String status,
       @RequestParam(required = false) Long flayerNumber,
       @RequestParam(required = false) Long id
   ) {
       
       List<ShipmentDTO> response = shipmentService.searchShipments(customerId, courierId, status, flayerNumber, id);
       return response;
   }
   
}