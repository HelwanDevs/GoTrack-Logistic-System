import apiClient from "@/utils/axios";
import type {
  ProductDTO,
  UpdateProductRequest,
  ProductPageResponse,
  InventoryItemRequest,
  InventoryItemPageResponse,
  InventorySearchParams,
} from "./types";

export const createProductApi = async (
  data: ProductDTO,
): Promise<{ status: number; message: string; data: { id: number; name: string; merchantId: number; baseSku: string } }> => {
  const response = await apiClient.post("/api/inventory/products", data);
  return response.data;
};

export const updateProductApi = async (
  id: number,
  data: UpdateProductRequest,
): Promise<{ status: number; message: string; data: null }> => {
  const response = await apiClient.put(`/api/inventory/products/${id}`, data);
  return response.data;
};

export const getProductsByMerchantApi = async (
  merchantId: number,
  page?: number,
  size?: number,
): Promise<{ status: number; message: string; data: ProductPageResponse }> => {
  const response = await apiClient.get(
    `/api/inventory/products/merchent/${merchantId}`,
    { params: { page: page ?? 0, size: size ?? 10 } },
  );
  return response.data;
};

export const getMyProductsApi = async (
  page?: number,
  size?: number,
): Promise<{ status: number; message: string; data: ProductPageResponse }> => {
  const response = await apiClient.get("/api/inventory/products/myProducts", {
    params: { page: page ?? 0, size: size ?? 10 },
  });
  return response.data;
};

export const receiveInventoryItemsApi = async (
  data: InventoryItemRequest,
): Promise<{ status: number; message: string; data: null }> => {
  const response = await apiClient.post(
    "/api/inventory/items/receive",
    data,
  );
  return response.data;
};

export const listInventoryItemsApi = async (
  page?: number,
  size?: number,
): Promise<{ status: number; message: string; data: InventoryItemPageResponse }> => {
  const response = await apiClient.get("/api/inventory/items", {
    params: { page: page ?? 0, size: size ?? 10 },
  });
  return response.data;
};

export const searchInventoryItemsApi = async (
  params: InventorySearchParams,
): Promise<{ status: number; message: string; data: InventoryItemPageResponse }> => {
  const queryParams = new URLSearchParams();

  if (params.branchId !== undefined)
    queryParams.append("branchId", String(params.branchId));
  if (params.uniqueSku) queryParams.append("uniqueSku", params.uniqueSku);
  if (params.MerchantId !== undefined)
    queryParams.append("MerchantId", String(params.MerchantId));
  if (params.page !== undefined) queryParams.append("page", String(params.page));
  if (params.size !== undefined) queryParams.append("size", String(params.size));

  const response = await apiClient.get("/api/inventory/items/search", {
    params: queryParams,
  });
  return response.data;
};
