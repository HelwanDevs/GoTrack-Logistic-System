import apiClient from "@/utils/axios";
import {
  LoginRequest,
  LoginResponse,
  LogoutRequest,
  RefreshTokenRequest,
  RefreshTokenResponse,
} from "./types";

export const loginApi = async (
  credentials: LoginRequest,
): Promise<LoginResponse> => {
  const response = await apiClient.post<LoginResponse>(
    "/api/auth/login",
    credentials,
  );
  return response.data;
};

export const refreshTokenApi = async (
  refreshToken: string,
): Promise<RefreshTokenResponse> => {
  const response = await apiClient.post<RefreshTokenResponse>(
    "/api/auth/refresh-token",
    {
      refreshToken,
    },
  );
  return response.data;
};

export const logoutApi = async (refreshToken: string): Promise<void> => {
  await apiClient.post("/api/auth/logout", {
    refreshToken,
  } as LogoutRequest);
};
