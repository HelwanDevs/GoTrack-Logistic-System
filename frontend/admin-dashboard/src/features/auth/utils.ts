import { getStoredAuth, getStoredToken } from "@/utils/storage";
import { AuthState, AuthUser } from "./types";
import { UserRole } from "../../types/enums";
import { jwtDecode } from "jwt-decode";

export const checkIsAuthenticated = (): boolean => {
  const authState = getStoredAuth();
  return authState.isAuthenticated === true;
};

export const getAuthState = () => {
  return getStoredAuth();
};

export const checkIsEmployee = (): boolean => {
  const authState = getStoredAuth();
  let token = getStoredToken();
  const { role: userRole } = jwtDecode<AuthUser>(token || "") || {};
  return (
    authState.isAuthenticated === true &&
    userRole == authState.user?.role &&
    (authState.user?.role === UserRole.ADMIN || userRole == UserRole.EMPLOYEE)
  );
};

export const checkUserRole = (requiredRole: UserRole): boolean => {
  const authState = getStoredAuth();
  return (
    authState.isAuthenticated === true && authState.user?.role === requiredRole
  );
};
