import { AuthState } from "@/features/auth";

const ACCESS_TOKEN_KEY = "access_token";
const REFRESH_TOKEN_KEY = "refresh_token";
const AUTH_STATE_KEY = "auth_state";

export const setStoredTokens = (accessToken: string, refreshToken: string) => {
  localStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
  localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
};

export const getStoredToken = (): string | null => {
  return localStorage.getItem(ACCESS_TOKEN_KEY);
};

export const getStoredRefreshToken = (): string | null => {
  return localStorage.getItem(REFRESH_TOKEN_KEY);
};

export const removeStoredTokens = () => {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
  localStorage.removeItem(AUTH_STATE_KEY);
};

export const setStoredAuth = (auth: AuthState) => {
  localStorage.setItem(AUTH_STATE_KEY, JSON.stringify(auth));
};

export const getStoredAuth = (): AuthState => {
  try {
    const stored = localStorage.getItem(AUTH_STATE_KEY);
    return stored ? JSON.parse(stored) : { isAuthenticated: false, user: null };
  } catch {
    return { isAuthenticated: false, user: null };
  }
};
