import axios from "axios";
import {
  getStoredToken,
  getStoredRefreshToken,
  removeStoredTokens,
} from "./storage";
import { refreshTokenApi } from "@/features/auth";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Adding token to all requests if token exists
apiClient.interceptors.request.use(
  (config) => {
    const token = getStoredToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// handle refresh token or logout
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (originalRequest?._retry) {
      return Promise.reject(error);
    }

    if (error.response?.status === 401) {
      originalRequest._retry = true;
      if (originalRequest.url?.includes("/api/auth/refresh-token")) {
        removeStoredTokens();
        window.location.href = "/login";
        return Promise.reject(new Error("Refresh token expired or invalid"));
      }
      try {
        const refreshToken = getStoredRefreshToken();
        if (!refreshToken) {
          removeStoredTokens();
          window.location.href = "/login";
          return Promise.reject(new Error("No refresh token available"));
        }

        let refreshResponse;
        try {
          refreshResponse = await refreshTokenApi(refreshToken);
        } catch (refreshApiError: any) {
          // Handle refresh token API errors (e.g., token deleted from DB, expired, invalid)
          console.error("Refresh token API error:", refreshApiError);
          removeStoredTokens();
          window.location.href = "/login";
          return Promise.reject(
            new Error(
              refreshApiError?.response?.data?.message ||
                "Failed to refresh token",
            ),
          );
        }

        const { status, data } = refreshResponse;

        if (!data?.accessToken || status !== 200) {
          removeStoredTokens();
          window.location.href = "/login";
          return Promise.reject(new Error("Failed to refresh token"));
        }

        const { accessToken, refreshToken: newRefreshToken } = data;
        localStorage.setItem("access_token", accessToken);

        if (newRefreshToken) {
          localStorage.setItem("refresh_token", newRefreshToken);
        }

        // Retry original request with new token
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return apiClient(originalRequest);
      } catch (refreshError) {
        removeStoredTokens();
        window.location.href = "/login";
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  },
);

export default apiClient;
