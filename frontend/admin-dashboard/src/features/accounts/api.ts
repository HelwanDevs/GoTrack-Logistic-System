import apiClient from "@/utils/axios";
import type {
  CreateAccountRequest,
  UpdateAccountRequest,
  AccountResponse,
} from "./types";

export const createAccountApi = async (
  data: CreateAccountRequest,
): Promise<AccountResponse> => {
  const response = await apiClient.post<AccountResponse>(
    "/api/auth/accounts",
    data,
  );
  return response.data;
};

export const getAccountApi = async (
  accountId: string,
): Promise<AccountResponse> => {
  const response = await apiClient.get<AccountResponse>(
    `/api/auth/accounts/${accountId}`,
  );
  return response.data;
};

export const updateAccountApi = async (
  accountId: string,
  data: UpdateAccountRequest,
): Promise<AccountResponse> => {
  const response = await apiClient.put<AccountResponse>(
    `/api/auth/accounts/${accountId}`,
    data,
  );
  return response.data;
};

export const deleteAccountApi = async (accountId: string): Promise<void> => {
  await apiClient.delete(`/api/auth/accounts/${accountId}`);
};
