package com.gotrack.core_logistic.Service.finance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.core_logistic.ExceptionHandling.ResourceNotFoundException;
import com.gotrack.core_logistic.enums.ReportPeriod;
import com.gotrack.core_logistic.enums.TransactionCatg;
import com.gotrack.core_logistic.model.dto.FinancialSummaryDTO;
import com.gotrack.core_logistic.model.dto.TransactionDTO;
import com.gotrack.core_logistic.model.entity.FinancialSummary;
import com.gotrack.core_logistic.repository.FinanceRepo;



@Service
public class FinanceService {


     @Autowired
     FinanceRepo financeRepo;


     
      private FinancialSummary getTodaySummary() {

    LocalDate today = LocalDate.now();

    return financeRepo.findByCreated_at(today)
        .orElseGet(() -> {
            FinancialSummary newSummary = new FinancialSummary();
            newSummary.setCreatedAt(today);

            newSummary.setNetCash(BigDecimal.ZERO);
            newSummary.setExpenses(BigDecimal.ZERO);
            newSummary.setNetProfit(BigDecimal.ZERO);
            newSummary.setProfitMargin(BigDecimal.ZERO);
            newSummary.setCurierCommission(BigDecimal.ZERO);
            newSummary.setShippingCost(BigDecimal.ZERO);
            newSummary.setVendorDue(BigDecimal.ZERO);
            newSummary.setVenderPaid(BigDecimal.ZERO);
            newSummary.setTotalCommission(BigDecimal.ZERO);

            return financeRepo.save(newSummary);
        });
}
     
 
      public void  calculate(TransactionDTO transaction){
            
           FinancialSummary financialSummary =getTodaySummary();

         if(transaction.getType().getCatg() == TransactionCatg.REVENUE ){
            
            financialSummary.setNetCash(financialSummary.getNetCash().add(transaction.getAmount()));
            }
        


         else if(transaction.getType().getCatg() == TransactionCatg.EXPENSE)
         {
                financialSummary.setExpenses(financialSummary.getExpenses().add(transaction.getAmount()));
                financialSummary.setNetCash(financialSummary.getNetCash().subtract(transaction.getAmount()));
         }   
      
          else if(transaction.getType().getCatg() == TransactionCatg.COURIER_COMMISSIONS){

            financialSummary.setCurierCommission(financialSummary.getCurierCommission().add(transaction.getAmount()));
         

          }


          else if (transaction.getType().getCatg() == TransactionCatg.PICKUP_COMMISSION_EXPENSES){
             financialSummary.setTotalCommission(financialSummary.getTotalCommission().add(transaction.getAmount()));
          }


          else if (transaction.getType().getCatg() == TransactionCatg.CASH_COLLECTION_CUSTODY){
            financialSummary.setVenderPaid(financialSummary.getVenderPaid().add(transaction.getAmount()));
            financialSummary.setVendorDue(financialSummary.getVendorDue().subtract(transaction.getAmount()));
            financialSummary.setNetCash(financialSummary.getNetCash().subtract(transaction.getAmount()));

          }

          else if(transaction.getType().getCatg()  == TransactionCatg.CUSTODY_LIABILITY){
            
              financialSummary.setVenderPaid(financialSummary.getVenderPaid().add(transaction.getAmount()));
          }


          
          financialSummary.setNetProfit(financialSummary.getShippingCost().subtract(financialSummary.getExpenses()));
          financialSummary.setProfitMargin(financialSummary.getShippingCost().subtract(financialSummary.getCurierCommission()));  


          financeRepo.save(financialSummary);
       

      }
       
       public void shipmentCalculation(BigDecimal shippingFee ,BigDecimal totalAmount){
         
         FinancialSummary financialSummary =getTodaySummary();
   
         financialSummary.setNetProfit(financialSummary.getNetProfit().add(totalAmount));
         financialSummary.setProfitMargin(financialSummary.getProfitMargin().add(shippingFee));
         financialSummary.setShippingCost(financialSummary.getShippingCost().add(shippingFee));

          
          financialSummary.setNetProfit(financialSummary.getShippingCost().subtract(financialSummary.getExpenses()));

          financialSummary.setProfitMargin(financialSummary.getShippingCost().subtract(financialSummary.getCurierCommission()));  
   
   
         financeRepo.save(financialSummary);
       }



      public FinancialSummaryDTO buildSummary( ReportPeriod period) {

      LocalDate now = LocalDate.now();
      LocalDate start = period.getStartDate(now);

      List<FinancialSummary> summaries =
               financeRepo.findByCreatedAtBetween(start, now);

       if (summaries.isEmpty()) {
           throw new ResourceNotFoundException("No Financial Summary Found");
       }

       FinancialSummaryDTO dto = new FinancialSummaryDTO();

         dto.setNetCash(summaries.stream().map(FinancialSummary::getNetCash).reduce(BigDecimal.ZERO, BigDecimal::add));

         dto.setVendorDue(summaries.stream().map(FinancialSummary::getVendorDue).reduce(BigDecimal.ZERO, BigDecimal::add));

         dto.setVenderPaid(summaries.stream().map(FinancialSummary::getVenderPaid).reduce(BigDecimal.ZERO, BigDecimal::add));

         dto.setShippingCost(summaries.stream().map(FinancialSummary::getShippingCost).reduce(BigDecimal.ZERO, BigDecimal::add));

         dto.setCurierCommission(summaries.stream().map(FinancialSummary::getCurierCommission).reduce(BigDecimal.ZERO, BigDecimal::add));
      
         dto.setTotalCommission(summaries.stream().map(FinancialSummary::getTotalCommission).reduce(BigDecimal.ZERO, BigDecimal::add));
      
         dto.setExpenses(summaries.stream().map(FinancialSummary::getExpenses).reduce(BigDecimal.ZERO, BigDecimal::add));
     
         dto.setProfitMargin(summaries.stream().map(FinancialSummary::getProfitMargin).reduce(BigDecimal.ZERO, BigDecimal::add));
     
         dto.setNetProfit(summaries.stream().map(FinancialSummary::getNetProfit).reduce(BigDecimal.ZERO, BigDecimal::add));
      
         dto.setCreatedAt(now);

    return dto;
}
      
   }




      



