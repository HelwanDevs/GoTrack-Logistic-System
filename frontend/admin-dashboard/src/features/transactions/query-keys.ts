import type { TransactionFilter } from "./types";

export const transactionQueryKeys = {
  all: ["transactions"] as const,
  myTransactions: (page?: number, size?: number) =>
    [...transactionQueryKeys.all, "me", page, size] as const,
  report: (period?: string) =>
    [...transactionQueryKeys.all, "report", period] as const,
  search: (params: TransactionFilter) =>
    [...transactionQueryKeys.all, "search", params] as const,
  create: () => [...transactionQueryKeys.all, "create"] as const,
  detail: (id: number) =>
    [...transactionQueryKeys.all, "detail", id] as const,
};
