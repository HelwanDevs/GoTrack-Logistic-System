import { createFileRoute, redirect } from "@tanstack/react-router";
import { checkIsMerchant, checkIsAuthenticated } from "@/features/auth";

export const Route = createFileRoute("/")({
  beforeLoad: async () => {
    if (checkIsAuthenticated() && checkIsMerchant()) {
      throw redirect({ to: "/dashboard" });
    } else {
      throw redirect({ to: "/login" });
    }
  },
});
