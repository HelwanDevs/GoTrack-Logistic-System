package com.gotrack.core_logistic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.core_logistic.Service.finance.TransactionService;
import com.gotrack.core_logistic.model.dto.FinancialSummaryDTO;
import com.gotrack.core_logistic.model.dto.TransactionDTO;

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

    @GetMapping
    public ResponseEntity <FinancialSummaryDTO> ReportTransaction(){
             return ResponseEntity.ok(transactionService.ReportTransaction());

                 
    }



    
    

    





}