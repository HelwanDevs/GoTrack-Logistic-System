import { useQuery } from "@tanstack/react-query";
import type { AuthUser, AuthState } from "./types";
// Auth state stored in localStorage
const AUTH_STORAGE_KEY = "auth_state";

const setStoredAuth = (auth: AuthState) => {
  localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(auth));
};

// Helper functions for localStorage
const getStoredAuth = (): AuthState => {
  try {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    return stored ? JSON.parse(stored) : { isAuthenticated: false, user: null };
  } catch {
    return { isAuthenticated: false, user: null };
  }
};

// Query hook for current auth state
export const useAuthState = () => {
  return useQuery<AuthState>({
    queryKey: ["auth"],
    queryFn: async () => {
      // Simulate a small delay to check auth on mount
      await new Promise((resolve) => setTimeout(resolve, 100));
      return getStoredAuth();
    },
    staleTime: Infinity, // Auth state doesn't go stale
  });
};
