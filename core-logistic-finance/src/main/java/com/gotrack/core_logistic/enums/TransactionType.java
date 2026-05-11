package com.gotrack.core_logistic.enums;

import lombok.Getter;

@Getter
public enum TransactionType {

    CASH(TransactionCatg.REVENUE),
    FAWRY(TransactionCatg.REVENUE),
    BANK_TRANSFER(TransactionCatg.REVENUE),
    MOBILE_WALLET(TransactionCatg.REVENUE),
    BANK_CHECK(TransactionCatg.REVENUE),
    SALARIES_AND_WAGES(TransactionCatg.EXPENSE),
    COURIER_COMMISSIONS(TransactionCatg.COURIER_COMMISSIONS),
    RENT(TransactionCatg.EXPENSE),
    UTILITIES(TransactionCatg.EXPENSE),
    OTHER_EXPENSES(TransactionCatg.EXPENSE),
    ADVANCES(TransactionCatg.EXPENSE),
    COMPANY_BANK_DEPOSIT(TransactionCatg.REVENUE),
    OTHER_REVENUES(TransactionCatg.REVENUE),
    FUEL(TransactionCatg.EXPENSE),
    VEHICLE_MAINTENANCE(TransactionCatg.EXPENSE),
    PICKUP_COMMISSION_EXPENSES(TransactionCatg.PICKUP_COMMISSION_EXPENSES),
    CASH_COLLECTION_CUSTODY(TransactionCatg.CASH_COLLECTION_CUSTODY),
    CUSTODY_LIABILITY(TransactionCatg.CUSTODY_LIABILITY);

    private final TransactionCatg catg;

    TransactionType(TransactionCatg catg) {
        this.catg = catg;
    }
}