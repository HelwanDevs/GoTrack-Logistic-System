package com.gotrack.core_logistic.Service.finance;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.ExceptionHandling.ConflictException;
import com.gotrack.core_logistic.ExceptionHandling.ResourceNotFoundException;
import com.gotrack.core_logistic.mapper.FinancialSummaryMapper;
import com.gotrack.core_logistic.mapper.TransactionMapper;
import com.gotrack.core_logistic.model.dto.FinancialSummaryDTO;
import com.gotrack.core_logistic.model.dto.TransactionDTO;
import com.gotrack.core_logistic.model.entity.FinancialSummary;
import com.gotrack.core_logistic.model.entity.Transaction;
import com.gotrack.core_logistic.model.entity.Wallet;
import com.gotrack.core_logistic.repository.FinanceRepo;
import com.gotrack.core_logistic.repository.TransactionRepo;
import com.gotrack.core_logistic.repository.WalletRepo;


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
    FinanceRepo financeRepo;
    @Autowired
    FinancialSummaryMapper financialMapper;



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
    


   
    public  FinancialSummaryDTO ReportTransaction(){

    FinancialSummary finance = financeRepo.findTopByOrderByIdDesc()
        .orElseThrow(() -> new ResourceNotFoundException("No Financial Summary Found"));

    return financialMapper.toDTO(finance);
}
    


}