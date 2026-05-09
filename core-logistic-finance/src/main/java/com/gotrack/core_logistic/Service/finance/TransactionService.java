package com.gotrack.core_logistic.Service.finance;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.ExceptionHandling.ConflictException;
import com.gotrack.core_logistic.ExceptionHandling.ResourceNotFoundException;
import com.gotrack.core_logistic.Service.ProfileBranchService;
import com.gotrack.core_logistic.Specifications.TransactionSpecification;
import com.gotrack.core_logistic.enums.ReportPeriod;
import com.gotrack.core_logistic.filter.AuthenticationDetails;
import com.gotrack.core_logistic.mapper.TransactionMapper;
import com.gotrack.core_logistic.model.dto.DTOFilters.TransactionFilter;
import com.gotrack.core_logistic.model.dto.FinancialSummaryDTO;
import com.gotrack.core_logistic.model.dto.ProfileResponse;
import com.gotrack.core_logistic.model.dto.TransactionDTO;
import com.gotrack.core_logistic.model.entity.Transaction;
import com.gotrack.core_logistic.model.entity.Wallet;
import com.gotrack.core_logistic.repository.TransactionRepo;
import com.gotrack.core_logistic.repository.WalletRepo;

import jakarta.transaction.Transactional;


@Service
public class TransactionService {


    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    TransactionMapper transactionMapper ;
    @Autowired
    FinanceService financeService;
    @Autowired 
    WalletRepo walletRepo;
    @Autowired
    ProfileBranchService profileBranchService;
    @Autowired
    AuthenticationDetails AuthenticationDetails;
    


   @Transactional
    public TransactionDTO createTransaction(TransactionDTO request){

    BigDecimal amount = request.getAmount();

    Wallet fromWallet = walletRepo.findByProfileId(request.getTransacteFrom())
        .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

    Wallet toWallet = walletRepo.findByProfileId(request.getTransacteTo())
        .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

    if(fromWallet.getBalance().compareTo(amount) < 0){
        throw new ConflictException("Insufficient balance");
    }
    
    fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
    toWallet.setBalance(toWallet.getBalance().add(amount));

    walletRepo.save(fromWallet);
    walletRepo.save(toWallet);


    Transaction transaction = transactionMapper.toEntity(request);
    transaction.setWallet(fromWallet);
    Transaction savedTransaction = transactionRepo.save(transaction);
    financeService.calculate(request);
    return transactionMapper.toDTO(savedTransaction);
}
    


   
    public  FinancialSummaryDTO ReportTransaction(ReportPeriod period){

    return financeService.buildSummary(period);

}


   public Page<TransactionDTO> getTransactions(TransactionFilter filter, Pageable pageable){

       String accountId = AuthenticationDetails.getAccountId();
       ProfileResponse profile = profileBranchService.getProfileByAccountId(accountId);
       
       if(filter.getFromProfileId() != null && filter.getFromProfileId() != profile.getId() && profile.getType().toString() == "EMPLOYEE")
          throw new ConflictException("You are not authorized to search transactions for this profile");

        if(filter.getToProfileId() != null && filter.getToProfileId() != profile.getId() && profile.getType().toString() == "EMPLOYEE")
          throw new ConflictException("You are not authorized to search transactions for this profile");

       Specification<Transaction> spec = TransactionSpecification.filterTransactions(filter);
       return transactionRepo.findAll(spec, pageable);
       
   }
    


   public Page<TransactionDTO> GetMyTransactions(Pageable pageable){
    
    String accountId = AuthenticationDetails.getAccountId();
    ProfileResponse profile = profileBranchService.getProfileByAccountId(accountId);
    
         return transactionRepo
             .findByTransacteFromOrTransacteTo(profile.getId(), profile.getId(), pageable)
             .map(transactionMapper::toDTO);
   }


}