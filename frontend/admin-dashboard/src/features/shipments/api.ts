import apiClient from "@/utils/axios";
import type {
  ShipmentDTO,
  ShipmentFilter,
  ShipmentPageResponse,
} from "./types";

export const createShipmentApi = async (
  data: ShipmentDTO,
): Promise<ShipmentDTO> => {
  const response = await apiClient.post("/api/shipments", data);
  return response.data;
};

export const updateShipmentStatusApi = async (
  id: number,
  data: ShipmentDTO,
): Promise<ShipmentDTO> => {
  const response = await apiClient.put(`/api/shipments/${id}/status`, data);
  return response.data;
};

export const getMyShipmentsApi = async (
  page?: number,
  size?: number,
): Promise<ShipmentPageResponse> => {
  const response = await apiClient.get("/api/shipments/me", {
    params: { page: page ?? 0, size: size ?? 10 },
  });
  return response.data;
};

export const searchShipmentsApi = async (
  params: ShipmentFilter,
): Promise<ShipmentPageResponse> => {
  const queryParams = new URLSearchParams();

  if (params.status) queryParams.append("status", params.status);
  if (params.pickupRequestId !== undefined)
    queryParams.append("pickupRequestId", String(params.pickupRequestId));
  if (params.branchId !== undefined)
    queryParams.append("branchId", String(params.branchId));
  if (params.page !== undefined) queryParams.append("page", String(params.page));
  if (params.size !== undefined) queryParams.append("size", String(params.size));

  const response = await apiClient.get("/api/shipments/search", {
    params: queryParams,
  });
  return response.data;
};
