package com.gotrack.core_logistic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.core_logistic.Service.finance.TransactionService;
import com.gotrack.core_logistic.enums.ReportPeriod;
import com.gotrack.core_logistic.model.dto.FinancialSummaryDTO;
import com.gotrack.core_logistic.model.dto.TransactionDTO;
import com.gotrack.core_logistic.model.dto.Filters.TransactionFilter;


import jakarta.validation.Valid;




@RestController
@RequestMapping("/api/finance/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;    
    
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction (@Valid @RequestBody TransactionDTO request) {
         TransactionDTO response = transactionService.createTransaction(request);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/report")
    public ResponseEntity <FinancialSummaryDTO> ReportTransaction(@RequestParam(required = false) ReportPeriod period){
         FinancialSummaryDTO response = transactionService.ReportTransaction(period);
              return ResponseEntity.ok(response);
        
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TransactionDTO>> getTransactions(
        TransactionFilter filter,
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable
    ){
        Page<TransactionDTO> response = transactionService.getTransactions(filter, pageable);
            return ResponseEntity.ok(response);

    }

    @GetMapping("/me")
    public ResponseEntity<Page<TransactionDTO>> GetMyTransactions(
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable
    ){
        Page<TransactionDTO> response = transactionService.GetMyTransactions( pageable);
            return ResponseEntity.ok(response);
    
    }
    


    
    

    





}