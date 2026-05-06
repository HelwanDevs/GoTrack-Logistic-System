import { jwtDecode } from "jwt-decode";

export const extractUserFromToken = (
  token: string,
): { id: string; role: string; email: string } | null => {
  try {
    const payload = jwtDecode<any>(token);

    const id = payload.accountId;
    const role = payload.role;
    const email = payload.sub;

    if (!id) {
      throw new Error("accountId claim missing in token");
    }
    if (!role) {
      throw new Error("role claim missing in token");
    }
    if (!email) {
      throw new Error("email claim missing in token");
    }
    return { id, role, email };
  } catch (error) {
    console.error("Failed to extract user from token:", error);
    return null;
  }
};
