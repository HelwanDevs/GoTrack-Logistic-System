import { LoadingSpinner } from "@/components/LoadingSpinner";
import { LoginPage } from "@/pages/LoginPage";
import { createFileRoute, redirect } from "@tanstack/react-router";
import { checkIsMerchant, checkIsAuthenticated } from "@/features/auth";
import { Suspense } from "react";

export const Route = createFileRoute("/login")({
  beforeLoad: async () => {
    if (checkIsAuthenticated() && checkIsMerchant()) {
      throw redirect({ to: "/dashboard" });
    }
  },
  component: () => (
    <Suspense fallback={<LoadingSpinner />}>
      <LoginPage />
    </Suspense>
  ),
});
