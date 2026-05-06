import { createFileRoute, redirect } from "@tanstack/react-router";
import { SettingsPage } from "@/pages/SettingsPage";
import { checkIsAuthenticated, checkIsMerchant } from "@/features/auth";

export const Route = createFileRoute("/dashboard/settings")({
  component: () => <SettingsPage />,
});
