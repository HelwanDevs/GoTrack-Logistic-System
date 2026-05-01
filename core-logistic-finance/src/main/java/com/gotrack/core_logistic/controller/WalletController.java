package com.gotrack.core_logistic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.core_logistic.Service.finance.WalletService;
import com.gotrack.core_logistic.model.dto.WalletDTO;



@RestController
@RequestMapping("/api/finance/wallets")
public class WalletController {
    
   @Autowired
   WalletService walletService ;

   @GetMapping
   public List<WalletDTO> GetAllWallets(){
           List<WalletDTO> response = walletService.GetAllWallets();
           return response ;
   }
   

   @GetMapping("/me")
   public ResponseEntity<WalletDTO> GetMyWallets(@RequestParam(required = false) Long id){

           WalletDTO response = walletService.GetMyWallets(id);
           return ResponseEntity.ok(response);
   }
   

}
