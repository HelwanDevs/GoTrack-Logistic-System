import { useMutation, useQueryClient } from "@tanstack/react-query";
import { accountQueryKeys } from "./query-keys";
import { createAccountApi, updateAccountApi, deleteAccountApi } from "./api";
import { UpdateAccountRequest } from "./types";

export const useCreateAccountMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createAccountApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: accountQueryKeys.list() });
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
      queryClient.invalidateQueries({ queryKey: accountQueryKeys.list() });
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
      queryClient.invalidateQueries({ queryKey: accountQueryKeys.list() });
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
