import { UserRole } from "../../types/enums";
import { AuthUser } from "./types";
import { extractUserFromToken } from "@/utils/jwt-utils";
import { setStoredTokens, setStoredAuth, removeStoredTokens } from "@/utils";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { loginApi, logoutApi } from "./api";
import { authQueryKeys } from "./query-keys";

export const useLoginMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: loginApi,
    onSuccess: (data) => {
      if (!data.token) {
        throw new Error("Access token missing from login response");
      }
      if (!data.refreshToken) {
        throw new Error("Refresh token missing from login response");
      }

      const userInfo = extractUserFromToken(data.token);
      if (!userInfo) {
        throw new Error(
          "Failed to extract user information from token. Token may be invalid.",
        );
      }

      if (
        userInfo.role !== UserRole.ADMIN &&
        userInfo.role !== UserRole.EMPLOYEE
      ) {
        throw new Error(
          "Access denied. Only administrators and employees can access this application.",
          {
            cause: {
              status: 401,
            },
          },
        );
      }

      setStoredTokens(data.token, data.refreshToken);

      const authUser: AuthUser = {
        accountId: userInfo.id,
        email: userInfo?.email,
        role: userInfo.role as UserRole,
      };

      setStoredAuth({
        isAuthenticated: true,
        user: authUser,
      });

      // Invalidate auth queries to refetch
      queryClient.invalidateQueries({ queryKey: authQueryKeys.all });
    },
    onError: (error: any) => {
      const message =
        error.response?.data?.message ||
        error.response?.data?.error ||
        error.message ||
        "Login failed";

      throw new Error(message);
    },
  });
};

export const useLogoutMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (refreshToken: string) => {
      await logoutApi(refreshToken);
    },
    onSuccess: () => {
      removeStoredTokens();

      queryClient.removeQueries({ queryKey: authQueryKeys.all });
    },
    onError: (error: any) => {
      removeStoredTokens();
      queryClient.removeQueries({ queryKey: authQueryKeys.all });
    },
  });
};
