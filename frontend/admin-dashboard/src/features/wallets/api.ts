import apiClient from "@/utils/axios";
import type {
  WalletDTO,
  WalletPageResponse,
} from "./types";

export const getMyWalletApi = async (): Promise<WalletDTO> => {
  const response = await apiClient.get("/api/finance/wallets/me");
  return response.data;
};

export const searchWalletsApi = async (
  id?: number,
  page?: number,
  size?: number,
): Promise<WalletPageResponse> => {
  const queryParams = new URLSearchParams();

  if (id !== undefined) queryParams.append("id", String(id));
  if (page !== undefined) queryParams.append("page", String(page));
  if (size !== undefined) queryParams.append("size", String(size));

  const response = await apiClient.get("/api/finance/wallets/search", {
    params: queryParams,
  });
  return response.data;
};
