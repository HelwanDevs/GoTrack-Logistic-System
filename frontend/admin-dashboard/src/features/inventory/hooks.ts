import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  createProductApi,
  updateProductApi,
  getProductsByMerchantApi,
  getMyProductsApi,
  receiveInventoryItemsApi,
  listInventoryItemsApi,
  searchInventoryItemsApi,
} from "./api";
import { inventoryQueryKeys } from "./query-keys";
import type {
  ProductDTO,
  UpdateProductRequest,
  InventoryItemRequest,
  InventorySearchParams,
} from "./types";

export const useMyProductsQuery = (page?: number, size?: number) => {
  return useQuery({
    queryKey: inventoryQueryKeys.myProducts(page, size),
    queryFn: () => getMyProductsApi(page, size),
    staleTime: 30 * 1000,
  });
};

export const useProductsByMerchantQuery = (
  merchantId: number,
  page?: number,
  size?: number,
) => {
  return useQuery({
    queryKey: inventoryQueryKeys.productsByMerchant(merchantId, page, size),
    queryFn: () => getProductsByMerchantApi(merchantId, page, size),
    enabled: merchantId > 0,
    staleTime: 30 * 1000,
  });
};

export const useInventoryItemsQuery = (page?: number, size?: number) => {
  return useQuery({
    queryKey: inventoryQueryKeys.items(page, size),
    queryFn: () => listInventoryItemsApi(page, size),
    staleTime: 30 * 1000,
  });
};

export const useSearchInventoryItemsQuery = (
  params: InventorySearchParams,
) => {
  return useQuery({
    queryKey: inventoryQueryKeys.search(params),
    queryFn: () => searchInventoryItemsApi(params),
    staleTime: 30 * 1000,
    enabled:
      params.branchId !== undefined ||
      Boolean(params.uniqueSku) ||
      params.MerchantId !== undefined,
  });
};

export const useCreateProductMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createProductApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: inventoryQueryKeys.all });
    },
  });
};

export const useUpdateProductMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      id,
      data,
    }: {
      id: number;
      data: UpdateProductRequest;
    }) => updateProductApi(id, data),
    onSuccess: (_, { id }) => {
      queryClient.invalidateQueries({
        queryKey: inventoryQueryKeys.detail(id),
      });
      queryClient.invalidateQueries({ queryKey: inventoryQueryKeys.all });
    },
  });
};

export const useReceiveItemsMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: receiveInventoryItemsApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: inventoryQueryKeys.all });
    },
  });
};
