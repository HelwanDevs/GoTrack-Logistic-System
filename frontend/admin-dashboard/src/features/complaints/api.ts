import apiClient from "@/utils/axios";
import type {
  ComplaintResponse,
  CreateComplaintRequest,
  UpdateComplaintStatusRequest,
  SearchComplaintParams,
} from "./types";

export const createComplaintApi = async (
  data: CreateComplaintRequest,
): Promise<{ id: string; message: string }> => {
  const response = await apiClient.post("/api/complaints", data);
  return response.data;
};

export const updateComplaintStatusApi = async (
  complaintId: string,
  data: UpdateComplaintStatusRequest,
): Promise<{ message: string; complaint: ComplaintResponse }> => {
  const response = await apiClient.put(
    `/api/complaints/${complaintId}/status`,
    data,
  );
  return response.data;
};

export const searchComplaintsApi = async (
  params: SearchComplaintParams,
): Promise<ComplaintResponse[]> => {
  const queryParams = new URLSearchParams();

  if (params.subject) queryParams.append("subject", params.subject);
  if (params.status) queryParams.append("status", params.status);
  if (params.shipmentId !== undefined)
    queryParams.append("shipmentId", String(params.shipmentId));

  const response = await apiClient.get("/api/complaints/search", {
    params: queryParams,
  });
  return response.data;
};

export const getMyComplaintsApi = async (): Promise<ComplaintResponse[]> => {
  const response = await apiClient.get("/api/complaints/me");
  return response.data;
};
