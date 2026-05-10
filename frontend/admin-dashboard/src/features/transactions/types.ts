export enum TransactionType {
  DEPOSIT = "DEPOSIT",
  WITHDRAWAL = "WITHDRAWAL",
  PAYMENT = "PAYMENT",
  REFUND = "REFUND",
  TRANSFER = "TRANSFER",
}

export enum TransactionStatus {
  PENDING = "PENDING",
  COMPLETED = "COMPLETED",
  FAILED = "FAILED",
  CANCELLED = "CANCELLED",
}

export interface TransactionDTO {
  id?: number;
  type: TransactionType;
  amount: number;
  currency?: string;
  status?: TransactionStatus;
  accountId?: string;
  description?: string;
  referenceId?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface TransactionFilter {
  type?: TransactionType;
  status?: TransactionStatus;
  accountId?: string;
  fromDate?: string;
  toDate?: string;
  page?: number;
  size?: number;
}

export interface TransactionPageResponse {
  content: TransactionDTO[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface FinancialSummaryDTO {
  period?: string;
  totalRevenue?: number;
  totalExpenses?: number;
  netProfit?: number;
  transactionCount?: number;
  periodStart?: string;
  periodEnd?: string;
}
