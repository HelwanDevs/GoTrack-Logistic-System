package com.gotrack.core_logistic.Service.finance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.mapper.TransactionMapper;
import com.gotrack.core_logistic.mapper.WalletMapper;
import com.gotrack.core_logistic.model.dto.TransactionDTO;
import com.gotrack.core_logistic.model.dto.WalletDTO;
import com.gotrack.core_logistic.model.entity.Wallet;
import com.gotrack.core_logistic.repository.WalletRepo;

@Service
public class WalletService {
      
      @Autowired
       WalletRepo walletRepo ;
      @Autowired 
         WalletMapper walletMapper;   
      @Autowired
      TransactionMapper transactionMapper;


        public List<WalletDTO> GetAllWallets() {
        List<Wallet> Wallets = walletRepo.findAll();
        return Wallets.stream().map(walletMapper :: toDto).toList();
        }

       
        public WalletDTO GetMyWallets(Long id) {
            Wallet wallet = walletRepo.findByProfileId(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
                
                List<TransactionDTO> transactions = wallet.getTransactions().stream()
                    .map(transactionMapper :: toDTO)
                    .limit(10) 
                    .toList();
 
                WalletDTO response = walletMapper.toDto(wallet);
                response.setBalance(wallet.getBalance());
                response.setId(wallet.getId());
                response.setTransactions(transactions);
                
                return response;
        }

}     
