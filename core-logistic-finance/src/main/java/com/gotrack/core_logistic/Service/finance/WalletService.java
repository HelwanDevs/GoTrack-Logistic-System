package com.gotrack.core_logistic.Service.finance;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.ExceptionHandling.ResourceNotFoundException;
import com.gotrack.core_logistic.mapper.TransactionMapper;
import com.gotrack.core_logistic.mapper.WalletMapper;
import com.gotrack.core_logistic.model.dto.ProfileResponse;
import com.gotrack.core_logistic.model.dto.TransactionDTO;
import com.gotrack.core_logistic.model.dto.WalletDTO;
import com.gotrack.core_logistic.model.entity.Wallet;
import com.gotrack.core_logistic.repository.WalletRepo;
import com.gotrack.core_logistic.Service.ProfileBranchService;


@Service
public class WalletService {
      
      @Autowired
       WalletRepo walletRepo ;
      @Autowired 
         WalletMapper walletMapper;   
      @Autowired
      TransactionMapper transactionMapper;
      @Autowired
      ProfileBranchService profileBranchService;
      

        
      public Page<WalletDTO> getWallets(Long id, Pageable pageable) {

         Page<Wallet> wallets;

         if (id == null) 
            wallets = walletRepo.findAll(pageable);
         else 
            wallets = walletRepo.findAllByProfileId(id, pageable)
                .orElseThrow(() -> new ResourceNotFoundException("No wallets found for profile id: " + id));

        return wallets.map(walletMapper::toDto);
}

       
        public WalletDTO GetMyWallets(String accountId) {
            ProfileResponse profile = profileBranchService.getProfileByAccountId(accountId);
            
            Wallet wallet = walletRepo.findByProfileId(profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for profile id: " + profile.getId()));
                
                List<TransactionDTO> transactions = wallet.getTransactions().stream()
                    .map(transactionMapper :: toDTO)
                    .sorted(Comparator.comparing(TransactionDTO::getCreatedAt).reversed())
                    .limit(10) 
                    .toList();
 
                WalletDTO response = walletMapper.toDto(wallet);
                response.setBalance(wallet.getBalance());
                response.setId(wallet.getId());
                response.setTransactions(transactions);
                
                return response;
        }

}     
