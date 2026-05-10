import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  listProfilesApi,
  getProfileApi,
  createProfileApi,
  updateProfileApi,
  getProfileByAccountIdApi,
  searchProfilesApi,
} from "./api";
import { profileQueryKeys } from "./query-keys";
import type {
  ListProfilesParams,
  SearchProfilesParams,
  CreateProfileRequest,
  UpdateProfileRequest,
} from "./types";

export const useProfilesQuery = (params: ListProfilesParams) => {
  return useQuery({
    queryKey: profileQueryKeys.list(params),
    queryFn: () => listProfilesApi(params),
    staleTime: 30 * 1000,
  });
};

export const useProfileDetailQuery = (id: number) => {
  return useQuery({
    queryKey: profileQueryKeys.detail(id),
    queryFn: () => getProfileApi(id),
    enabled: id > 0,
  });
};

export const useProfileByAccountQuery = (accountId: string) => {
  return useQuery({
    queryKey: profileQueryKeys.byAccount(accountId),
    queryFn: () => getProfileByAccountIdApi(accountId),
    enabled: Boolean(accountId),
  });
};

export const useSearchProfilesQuery = (params: SearchProfilesParams) => {
  return useQuery({
    queryKey: profileQueryKeys.search(params),
    queryFn: () => searchProfilesApi(params),
    staleTime: 30 * 1000,
    enabled:
      Boolean(params.name) ||
      Boolean(params.phoneNumber) ||
      Boolean(params.type) ||
      params.branchId !== undefined ||
      Boolean(params.status),
  });
};

export const useCreateProfileMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createProfileApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: profileQueryKeys.all });
    },
  });
};

export const useUpdateProfileMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      id,
      data,
    }: {
      id: number;
      data: UpdateProfileRequest;
    }) => updateProfileApi(id, data),
    onSuccess: (_, { id }) => {
      queryClient.invalidateQueries({ queryKey: profileQueryKeys.detail(id) });
      queryClient.invalidateQueries({ queryKey: profileQueryKeys.all });
    },
  });
};
