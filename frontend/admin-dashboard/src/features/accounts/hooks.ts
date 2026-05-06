import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { accountQueryKeys } from "./query-keys";
import {
  listAccountsApi,
  createAccountApi,
  updateAccountApi,
  deleteAccountApi,
  changeAccountPasswordApi,
} from "./api";
import {
  UpdateAccountRequest,
  ListAccountsParams,
  ListAccountsResponse,
} from "./types";

export const useAccountsQuery = (params: ListAccountsParams) => {
  return useQuery<ListAccountsResponse>({
    queryKey: accountQueryKeys.list(params),
    queryFn: () => listAccountsApi(params),
    staleTime: 0,
    refetchInterval: 5 * 60 * 1000, // Refetch every 5 minutes
    refetchIntervalInBackground: false,
    gcTime: 10 * 60 * 1000, //  cache Time if not used
  });
};

export const useCreateAccountMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createAccountApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["accounts", "list"] });
    },
    onError: (error: any) => {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to create account";
      throw new Error(message);
    },
  });
};

export const useUpdateAccountMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      accountId,
      data,
    }: {
      accountId: string;
      data: UpdateAccountRequest;
    }) => updateAccountApi(accountId, data),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: accountQueryKeys.detail(data.id),
      });
      queryClient.invalidateQueries({ queryKey: ["accounts", "list"] });
    },
    onError: (error: any) => {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to update account";
      throw new Error(message);
    },
  });
};

export const useDeleteAccountMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteAccountApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["accounts", "list"] });
    },
    onError: (error: any) => {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to delete account";
      throw new Error(message);
    },
  });
};

export const useChangePasswordMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      accountId,
      newPassword,
    }: {
      accountId: string;
      newPassword: string;
    }) => changeAccountPasswordApi(accountId, newPassword),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: accountQueryKeys.detail(data.id),
      });
      queryClient.invalidateQueries({ queryKey: ["accounts", "list"] });
    },
    onError: (error: any) => {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to change password";
      throw new Error(message);
    },
  });
};
