package com.gotrack.core_logistic.Service.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.enums.TransactionType;
import com.gotrack.core_logistic.model.dto.TransactionDTO;
import com.gotrack.core_logistic.model.entity.FinancialSummary;
import com.gotrack.core_logistic.repository.FinanceRepo;



@Service
public class FinanceService {


     @Autowired
     FinanceRepo financeRepo;

     
 
      public void  calculate(TransactionDTO transaction){
            
           FinancialSummary financialSummary =financeRepo.findTopByOrderByIdDesc()
        .orElseThrow(() -> new RuntimeException("No Financial Summary Found"));

        

         if(transaction.getType() == TransactionType.CASH ||
            transaction.getType()  == TransactionType.FAWRY ||
            transaction.getType()  == TransactionType.BANK_CHECK ||
            transaction.getType()  == TransactionType.BANK_TRANSFER || 
            transaction.getType()  == TransactionType.MOBILE_WALLET ||
            transaction.getType()  == TransactionType.COMPANY_BANK_DEPOSIT ||
            transaction.getType()  == TransactionType.ADVANCES ||
            transaction.getType()  == TransactionType.OTHER_REVENUES){
            

            financialSummary.setNetCash(financialSummary.getNetCash().add(transaction.getAmount()));
            }
        


         else if(transaction.getType()  == TransactionType.RENT ||
            transaction.getType()  == TransactionType.UTILITIES ||
            transaction.getType()  == TransactionType.OTHER_EXPENSES ||
            transaction.getType()  == TransactionType.FUEL ||
            transaction.getType()  == TransactionType.VEHICLE_MAINTENANCE 
            
         ){
                financialSummary.setExpenses(financialSummary.getExpenses().add(transaction.getAmount()));
                financialSummary.setNetCash(financialSummary.getNetCash().subtract(transaction.getAmount()));
                financialSummary.setNetProfit(financialSummary.getNetProfit().subtract(transaction.getAmount()));
         }   
      
          else if(transaction.getType()  == TransactionType.COURIER_COMMISSIONS){

            financialSummary.setCurierCommission(financialSummary.getCurierCommission().add(transaction.getAmount()));
            financialSummary.setProfitMargin(financialSummary.getProfitMargin().subtract(transaction.getAmount()));

          }


          else if (transaction.getType()  == TransactionType.PICKUP_COMMISSION_EXPENSES){
             financialSummary.setTotalCommission(financialSummary.getTotalCommission().add(transaction.getAmount()));
          }


          else if (transaction.getType()  == TransactionType.CASH_COLLECTION_CUSTODY){
            financialSummary.setVenderPaid(financialSummary.getVenderPaid().add(transaction.getAmount()));
            financialSummary.setVendorDue(financialSummary.getVendorDue().subtract(transaction.getAmount()));
            financialSummary.setNetCash(financialSummary.getNetCash().subtract(transaction.getAmount()));

          }

          else if(transaction.getType()  == TransactionType.CUSTODY_LIABILITY){
            
              financialSummary.setVenderPaid(financialSummary.getVenderPaid().add(transaction.getAmount()));
          }

          financeRepo.save(financialSummary);
       

      }

      


}
