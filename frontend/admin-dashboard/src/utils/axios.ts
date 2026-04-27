import axios from "axios";
import {
  getStoredToken,
  getStoredRefreshToken,
  removeStoredTokens,
} from "./storage";
import { refreshTokenApi } from "@/features/auth";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8081";

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

    const isAuthEndpoint = originalRequest?.url?.includes("/auth/");
    if (isAuthEndpoint || originalRequest?._retry) {
      return Promise.reject(error);
    }

    if (error.response?.status === 401) {
      originalRequest._retry = true;

      try {
        const refreshToken = getStoredRefreshToken();
        if (!refreshToken) {
          removeStoredTokens();
          window.location.href = "/login";
          return Promise.reject(new Error("No refresh token available"));
        }

        const { accessToken, refreshToken: newRefreshToken } =
          await refreshTokenApi(refreshToken);

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
