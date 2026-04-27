import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { accountQueryKeys } from "./query-keys";
import {
  updateAccountApi,
  changeAccountPasswordApi,
  getAccountApi,
} from "./api";
import {
  AccountResponse,
  UpdateAccountRequest,

} from "./types";


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

export const useAccountQuery = (id: string) => {
  return useQuery<AccountResponse>({
    queryKey: accountQueryKeys.detail(id),
    queryFn: () => getAccountApi(id),
    staleTime: 0,
    refetchInterval:  1000, // Refetch every 5 minutes
    refetchIntervalInBackground: false,
    gcTime: 10 * 60 * 1000, //  cache Time if not used
  });
};