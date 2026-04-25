// Export types
export type { AuthUser, AuthState } from "./types";

// Export API functions and hooks
export {
  fakeLoginApi,
  fakeLogoutApi,
  fakeCheckAuthApi,
  useLogin,
  useLogout,
} from "./api";

// Export query utilities
export { useAuthState } from "./query-keys";
