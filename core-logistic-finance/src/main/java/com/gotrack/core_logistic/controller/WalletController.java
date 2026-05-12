package com.gotrack.core_logistic.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @GetMapping("/search")
  @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
  public ResponseEntity<Page<WalletDTO>> getWallet(
        @RequestParam(required = false) Long id,
        @PageableDefault(size = 10, direction = Sort.Direction.DESC)
        Pageable pageable) {

    Page<WalletDTO> response = walletService.getWallets(id, pageable);
    return ResponseEntity.ok(response);
}
   
   @GetMapping("/me")
   @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'MERCHANT')")
   public ResponseEntity<WalletDTO> GetMyWallets() {
   
           WalletDTO response = walletService.GetMyWallets();
           return ResponseEntity.ok(response);
   }
   

}
