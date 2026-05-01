import { createFileRoute, redirect } from "@tanstack/react-router";
import { SettingsPage } from "@/pages/SettingsPage";
import { checkIsAuthenticated, checkIsEmployee } from "@/features/auth";

export const Route = createFileRoute("/dashboard/settings")({
  beforeLoad: async () => {
    // Only authenticated users (admin/employee) can access settings page
    if (!checkIsAuthenticated() || !checkIsEmployee()) {
      throw redirect({ to: "/dashboard" });
    }
  },
  component: () => <SettingsPage />,
});
