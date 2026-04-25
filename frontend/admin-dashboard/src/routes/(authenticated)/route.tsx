import { createFileRoute, Outlet, redirect } from "@tanstack/react-router";
import { fakeCheckAuthApi } from "@/features/auth";

export const Route = createFileRoute("/(authenticated)")({
  beforeLoad: async () => {
    const authState = await fakeCheckAuthApi();
    if (!authState.isAuthenticated) {
      throw redirect({ to: "/login" });
    }
  },
  component: () => <Outlet />,
});
