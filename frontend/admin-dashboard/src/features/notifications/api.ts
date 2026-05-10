import apiClient from "@/utils/axios";
import type {
  Notifications,
  AdminNotificationRequest,
} from "./types";

export const getMyNotificationsApi = async (
  readStatus?: boolean,
): Promise<Notifications[]> => {
  const queryParams = new URLSearchParams();
  if (readStatus !== undefined)
    queryParams.append("readStatus", String(readStatus));

  const response = await apiClient.get("/api/notifications/me", {
    params: queryParams,
  });
  return response.data;
};

export const markNotificationAsReadApi = async (
  notificationId: string,
): Promise<{ message: string }> => {
  const response = await apiClient.put(
    `/api/notifications/${notificationId}/read`,
  );
  return response.data;
};

export const markNotificationAsUnreadApi = async (
  notificationId: string,
): Promise<{ message: string }> => {
  const response = await apiClient.put(
    `/api/notifications/${notificationId}/unread`,
  );
  return response.data;
};

export const createAdminNotificationApi = async (
  data: AdminNotificationRequest,
): Promise<Notifications[]> => {
  const response = await apiClient.post("/api/notifications/admin", data);
  return response.data;
};
