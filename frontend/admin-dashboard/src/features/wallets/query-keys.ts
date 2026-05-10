export const walletQueryKeys = {
  all: ["wallets"] as const,
  myWallet: () => [...walletQueryKeys.all, "me"] as const,
  search: (id?: number, page?: number, size?: number) =>
    [...walletQueryKeys.all, "search", id, page, size] as const,
  detail: (id: number) => [...walletQueryKeys.all, "detail", id] as const,
};
