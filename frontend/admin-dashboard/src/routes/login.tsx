import { LoadingSpinner } from "@/components/LoadingSpinner";
import { fakeCheckAuthApi } from "@/features/auth/api";
import { LoginPage } from "@/pages/LoginPage";
import { createFileRoute, redirect } from "@tanstack/react-router";
import { Suspense } from "react";

export const Route = createFileRoute("/login")({
  beforeLoad: async () => {
    const authState = await fakeCheckAuthApi();
    // if (authState.isAuthenticated) {
    //   throw redirect({ to: "/dashboard" });
    // }
  },
  component: () => (
    <Suspense fallback={<LoadingSpinner />}>
      <LoginPage />
    </Suspense>
  ),
});
