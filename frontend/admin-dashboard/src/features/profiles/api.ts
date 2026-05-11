import apiClient from "@/utils/axios";
import type {
  ProfileResponseDTO,
  CreateProfileRequest,
  UpdateProfileRequest,
  SearchProfilesParams,
  ListProfilesParams,
  ProfilePageResponse,
} from "./types";

export const listProfilesApi = async (
  params: ListProfilesParams,
): Promise<ProfilePageResponse> => {
  const response = await apiClient.get("/api/users/profiles", { params });
  return response.data;
};

export const getProfileApi = async (id: number): Promise<ProfileResponseDTO> => {
  const response = await apiClient.get(`/api/users/profiles/${id}`);
  return response.data;
};

export const createProfileApi = async (
  data: CreateProfileRequest,
): Promise<{ message: string; data: null }> => {
  const response = await apiClient.post("/api/users/profiles", data);
  return response.data;
};

export const updateProfileApi = async (
  id: number,
  data: UpdateProfileRequest,
): Promise<{ message: string; data: null }> => {
  const response = await apiClient.put(`/api/users/profiles/${id}`, data);
  return response.data;
};

export const getProfileByAccountIdApi = async (
  accountId: string,
): Promise<ProfileResponseDTO> => {
  const response = await apiClient.get(
    `/api/users/profiles/account/${accountId}`,
  );
  return response.data;
};

export const searchProfilesApi = async (
  params: SearchProfilesParams,
): Promise<ProfilePageResponse> => {
  const queryParams = new URLSearchParams();

  if (params.name) queryParams.append("name", params.name);
  if (params.phoneNumber) queryParams.append("phoneNumber", params.phoneNumber);
  if (params.type) queryParams.append("type", params.type);
  if (params.branchId !== undefined)
    queryParams.append("branchId", String(params.branchId));
  if (params.status) queryParams.append("status", params.status);
  if (params.page !== undefined) queryParams.append("page", String(params.page));
  if (params.size !== undefined) queryParams.append("size", String(params.size));
  if (params.sortBy) queryParams.append("sortBy", params.sortBy);

  const response = await apiClient.get("/api/users/profiles/search", {
    params: queryParams,
  });
  return response.data;
};
