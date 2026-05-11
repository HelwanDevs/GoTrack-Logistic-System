import apiClient from "@/utils/axios";
import type {
  UpdateAccountRequest,
  AccountResponse,
} from "./types";



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

export const changeAccountPasswordApi = async (
  accountId: string,
  newPassword: string,
): Promise<AccountResponse> => {
  const response = await apiClient.put<AccountResponse>(
    `/api/auth/accounts/${accountId}`,
    { password: newPassword },
  );
  return response.data;
};
