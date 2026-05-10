import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  createTransactionApi,
  getMyTransactionsApi,
  getTransactionReportApi,
  searchTransactionsApi,
} from "./api";
import { transactionQueryKeys } from "./query-keys";
import type { TransactionFilter } from "./types";

export const useMyTransactionsQuery = (page?: number, size?: number) => {
  return useQuery({
    queryKey: transactionQueryKeys.myTransactions(page, size),
    queryFn: () => getMyTransactionsApi(page, size),
    staleTime: 30 * 1000,
  });
};

export const useTransactionReportQuery = (period?: string) => {
  return useQuery({
    queryKey: transactionQueryKeys.report(period),
    queryFn: () => getTransactionReportApi(period),
    enabled: Boolean(period),
    staleTime: 60 * 1000,
  });
};

export const useSearchTransactionsQuery = (params: TransactionFilter) => {
  return useQuery({
    queryKey: transactionQueryKeys.search(params),
    queryFn: () => searchTransactionsApi(params),
    staleTime: 30 * 1000,
    enabled:
      Boolean(params.type) ||
      Boolean(params.status) ||
      Boolean(params.accountId) ||
      Boolean(params.fromDate) ||
      Boolean(params.toDate),
  });
};

export const useCreateTransactionMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createTransactionApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: transactionQueryKeys.all });
    },
  });
};
