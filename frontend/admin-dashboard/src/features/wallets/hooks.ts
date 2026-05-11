import { useQuery } from "@tanstack/react-query";
import { getMyWalletApi, searchWalletsApi } from "./api";
import { walletQueryKeys } from "./query-keys";

export const useMyWalletQuery = () => {
  return useQuery({
    queryKey: walletQueryKeys.myWallet(),
    queryFn: getMyWalletApi,
    staleTime: 30 * 1000,
  });
};

export const useWalletsSearchQuery = (id?: number, page?: number, size?: number) => {
  return useQuery({
    queryKey: walletQueryKeys.search(id, page, size),
    queryFn: () => searchWalletsApi(id, page, size),
    enabled: id !== undefined && id > 0,
    staleTime: 30 * 1000,
  });
};
