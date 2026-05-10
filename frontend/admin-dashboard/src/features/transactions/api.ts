import apiClient from "@/utils/axios";
import type {
  TransactionDTO,
  TransactionFilter,
  TransactionPageResponse,
  FinancialSummaryDTO,
} from "./types";

export const createTransactionApi = async (
  data: TransactionDTO,
): Promise<TransactionDTO> => {
  const response = await apiClient.post("/api/finance/transactions", data);
  return response.data;
};

export const getMyTransactionsApi = async (
  page?: number,
  size?: number,
): Promise<TransactionPageResponse> => {
  const response = await apiClient.get("/api/finance/transactions/me", {
    params: { page: page ?? 0, size: size ?? 10 },
  });
  return response.data;
};

export const getTransactionReportApi = async (
  period?: string,
): Promise<FinancialSummaryDTO> => {
  const response = await apiClient.get("/api/finance/transactions/report", {
    params: period ? { period } : undefined,
  });
  return response.data;
};

export const searchTransactionsApi = async (
  params: TransactionFilter,
): Promise<TransactionPageResponse> => {
  const queryParams = new URLSearchParams();

  if (params.type) queryParams.append("type", params.type);
  if (params.status) queryParams.append("status", params.status);
  if (params.accountId) queryParams.append("accountId", params.accountId);
  if (params.fromDate) queryParams.append("fromDate", params.fromDate);
  if (params.toDate) queryParams.append("toDate", params.toDate);
  if (params.page !== undefined) queryParams.append("page", String(params.page));
  if (params.size !== undefined) queryParams.append("size", String(params.size));

  const response = await apiClient.get("/api/finance/transactions/search", {
    params: queryParams,
  });
  return response.data;
};
