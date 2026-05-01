import apiClient from "@/utils/axios";
import type {
  CreateAccountRequest,
  UpdateAccountRequest,
  AccountResponse,
  ListAccountsParams,
  ListAccountsResponse,
} from "./types";

export const listAccountsApi = async (
  params: ListAccountsParams,
): Promise<ListAccountsResponse> => {
  try {
    const queryParams = new URLSearchParams();
    queryParams.append("page", params.page.toString());
    queryParams.append("size", params.size.toString());

    if (params.role) {
      queryParams.append("role", params.role);
    }
    if (params.email) {
      queryParams.append("email", params.email);
    }

    if (params.includeDeleted) {
      queryParams.append("includeDeleted", "true");
    }

    const response = await apiClient.get<ListAccountsResponse>(
      `/api/auth/accounts?${queryParams.toString()}`,
    );
    return response.data;
  } catch (error: any) {
    // If the endpoint doesn't exist, return empty array with metadata
    if (error.response?.status === 404) {
      return {
        accounts: [],
        totalCount: 0,
        page: params.page,
        size: params.size,
        totalPages: 0,
      };
    }
    throw error;
  }
};

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
