import { UserRole } from "../../types/enums";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  role: UserRole;
  accountId: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface RefreshTokenResponse {
  accessToken: string;
  refreshToken: string;
}

export interface LogoutRequest {
  refreshToken: string;
}

export interface AuthUser {
  accountId: string;
  email: string;
  role: UserRole;
}

export interface AuthState {
  isAuthenticated: boolean;
  user: AuthUser | null;
  accessToken?: string;
}
