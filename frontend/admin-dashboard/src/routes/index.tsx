import { fakeCheckAuthApi } from "@/features/auth";
import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/")({
  beforeLoad: async () => {
    const authState = await fakeCheckAuthApi();
    if (!authState.isAuthenticated) {
      throw redirect({ to: "/login" });
    } else {
      // throw redirect({ to: "/dashboard" });
    }
  },
});
