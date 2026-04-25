import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { AuthUser, AuthState } from "./types";
import { useAuthState } from "./query-keys";

const AUTH_STORAGE_KEY = "auth_state";

// ============================================================================
// Fake API Functions
// ============================================================================

/**
 * Fake login API function
 * Simulates authentication API call with delay
 */
export const fakeLoginApi = async (credentials: {
  email: string;
  password: string;
}): Promise<AuthUser> => {
  // Simulate network delay
  await new Promise((resolve) => setTimeout(resolve, 500));

  // Validation
  if (!credentials.email || !credentials.password) {
    throw new Error("Please fill in all fields");
  }

  if (!credentials.email.includes("@")) {
    throw new Error("Please enter a valid email");
  }

  if (credentials.password.length < 1) {
    throw new Error("Please enter a password");
  }

  // Return fake user data
  return {
    id: "1",
    email: credentials.email,
    name: credentials.email.split("@")[0] || "User",
  };
};

/**
 * Fake logout API function
 * Simulates logout API call with delay
 */
export const fakeLogoutApi = async (): Promise<void> => {
  // Simulate network delay
  await new Promise((resolve) => setTimeout(resolve, 300));
};

/**
 * Fake auth check API function
 * Simulates checking if user is authenticated on app load
 */
export const fakeCheckAuthApi = async (): Promise<AuthState> => {
  // Simulate network delay
  await new Promise((resolve) => setTimeout(resolve, 100));

  // Check localStorage for stored auth state
  try {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    if (stored) {
      const authState = JSON.parse(stored);
      if (authState.isAuthenticated && authState.user) {
        return authState;
      }
    }
  } catch {
    // Silent fail on parse error
  }
  return { isAuthenticated: false, user: null };
};

// ============================================================================
// React Query Hooks
// ============================================================================

/**
 * Login mutation hook
 * Handles user login with error handling and cache updates
 */
export const useLogin = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: fakeLoginApi,
    onSuccess: (user) => {
      // Update auth state in cache
      const authState: AuthState = {
        isAuthenticated: true,
        user,
      };
      queryClient.setQueryData(["auth"], authState);
      // Persist to localStorage
      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(authState));
    },
  });
};

/**
 * Logout mutation hook
 * Handles user logout with cache invalidation
 */
export const useLogout = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: fakeLogoutApi,
    onSuccess: () => {
      // Clear auth state
      const authState: AuthState = {
        isAuthenticated: false,
        user: null,
      };
      queryClient.setQueryData(["auth"], authState);
      // Clear localStorage
      localStorage.removeItem(AUTH_STORAGE_KEY);
      // Invalidate all queries to clear cached data
      queryClient.clear();
    },
  });
};

export const useCheckAuth = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: fakeCheckAuthApi,
    onSuccess: (authState) => {
      // Update auth state in cache
      queryClient.setQueryData(["auth"], authState);
    },
  });
};
