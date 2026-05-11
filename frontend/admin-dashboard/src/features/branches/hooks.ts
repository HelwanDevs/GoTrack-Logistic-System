import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  listBranchesApi,
  getBranchApi,
  createBranchApi,
  updateBranchApi,
  deleteBranchApi,
  searchBranchesApi,
} from "./api";
import { branchQueryKeys } from "./query-keys";
import type {
  ListBranchesParams,
  SearchBranchParams,
  CreateBranchRequest,
  UpdateBranchRequest,
} from "./types";

export const useBranchesQuery = (params: ListBranchesParams) => {
  return useQuery({
    queryKey: branchQueryKeys.list(params),
    queryFn: () => listBranchesApi(params),
    staleTime: 30 * 1000,
  });
};

export const useBranchDetailQuery = (id: number) => {
  return useQuery({
    queryKey: branchQueryKeys.detail(id),
    queryFn: () => getBranchApi(id),
    enabled: id > 0,
  });
};

export const useSearchBranchesQuery = (params: SearchBranchParams) => {
  return useQuery({
    queryKey: branchQueryKeys.search(params),
    queryFn: () => searchBranchesApi(params),
    staleTime: 30 * 1000,
    enabled: Boolean(params.name) || Boolean(params.location) || Boolean(params.phone),
  });
};

export const useCreateBranchMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createBranchApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: branchQueryKeys.all });
    },
  });
};

export const useUpdateBranchMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      id,
      data,
    }: {
      id: number;
      data: UpdateBranchRequest;
    }) => updateBranchApi(id, data),
    onSuccess: (_, { id }) => {
      queryClient.invalidateQueries({ queryKey: branchQueryKeys.detail(id) });
      queryClient.invalidateQueries({ queryKey: branchQueryKeys.all });
    },
  });
};

export const useDeleteBranchMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteBranchApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: branchQueryKeys.all });
    },
  });
};
