import { useQuery } from "@tanstack/react-query";
import type { AuthState } from "./types";
import { getStoredAuth } from "@/utils/storage";

export const authQueryKeys = {
  all: ["auth"] as const,
  state: () => [...authQueryKeys.all, "state"] as const,
  user: () => [...authQueryKeys.all, "user"] as const,
  account: (id: string) => [...authQueryKeys.all, "account", id] as const,
};

export const useAuthState = () => {
  return useQuery<AuthState>({
    queryKey: authQueryKeys.state(),
    queryFn: async () => {
      return getStoredAuth();
    },
    staleTime: Infinity,
    gcTime: Infinity, // Keep in cache
  });
};
