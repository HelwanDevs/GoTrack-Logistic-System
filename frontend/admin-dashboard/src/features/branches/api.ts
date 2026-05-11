import apiClient from "@/utils/axios";
import type {
  BranchDTO,
  CreateBranchRequest,
  UpdateBranchRequest,
  SearchBranchParams,
  ListBranchesParams,
  BranchResponse,
} from "./types";

export const listBranchesApi = async (
  params: ListBranchesParams,
): Promise<BranchResponse> => {
  const queryParams = new URLSearchParams();
  queryParams.append("page", String(params.page ?? 0));
  queryParams.append("size", String(params.size ?? 5));

  const response = await apiClient.get("/api/branches", {
    params,
  });
  return response.data;
};

export const getBranchApi = async (id: number): Promise<BranchDTO> => {
  const response = await apiClient.get(`/api/branches/${id}`);
  return response.data;
};

export const createBranchApi = async (
  data: CreateBranchRequest,
): Promise<BranchDTO> => {
  const response = await apiClient.post("/api/branches", data);
  return response.data;
};

export const updateBranchApi = async (
  id: number,
  data: UpdateBranchRequest,
): Promise<BranchDTO> => {
  const response = await apiClient.put(`/api/branches/${id}`, data);
  return response.data;
};

export const deleteBranchApi = async (id: number): Promise<{ message: string }> => {
  const response = await apiClient.delete(`/api/branches/${id}`);
  return response.data;
};

export const searchBranchesApi = async (
  params: SearchBranchParams,
): Promise<BranchResponse> => {
  const queryParams = new URLSearchParams();

  if (params.name) queryParams.append("name", params.name);
  if (params.location) queryParams.append("location", params.location);
  if (params.phone) queryParams.append("phone", params.phone);
  if (params.isDeleted !== undefined)
    queryParams.append("isDeleted", String(params.isDeleted));
  if (params.page !== undefined) queryParams.append("page", String(params.page));
  if (params.size !== undefined) queryParams.append("size", String(params.size));

  const response = await apiClient.get("/api/branches/search", {
    params: queryParams,
  });
  return response.data;
};
