package com.gotrack.core_logistic.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.core_logistic.Service.finance.WalletService;
import com.gotrack.core_logistic.model.dto.WalletDTO;



@RestController
@RequestMapping("/api/finance/wallets")
public class WalletController {
    
   @Autowired
   WalletService walletService ;

  @GetMapping
  public ResponseEntity<Page<WalletDTO>> getAllWallets(
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable) {

    Page<WalletDTO> response = walletService.getAllWallets(pageable);
    return ResponseEntity.ok(response);
}
   

   @GetMapping("/me")
   public ResponseEntity<WalletDTO> GetMyWallets(@PathVariable Long id) {
           //TODO: get user id from JWT
           WalletDTO response = walletService.GetMyWallets(id);
           return ResponseEntity.ok(response);
   }
   

}
