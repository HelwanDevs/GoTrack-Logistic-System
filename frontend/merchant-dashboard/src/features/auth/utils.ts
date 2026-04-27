import { getStoredAuth, getStoredToken } from "@/utils/storage";
import {  AuthUser } from "./types";
import { UserRole } from "../../types/enums";
import { jwtDecode } from "jwt-decode";

export const checkIsAuthenticated = (): boolean => {
  const authState = getStoredAuth();
  return authState.isAuthenticated === true;
};


export const checkIsMerchant = (): boolean => {
  const authState = getStoredAuth();
  let token = getStoredToken();
  const { role: userRole } = jwtDecode<AuthUser>(token || "") || {};
  return (
    authState.isAuthenticated === true &&
    userRole == authState.user?.role &&
    (authState.user?.role === UserRole.MERCHANT)
  );
};