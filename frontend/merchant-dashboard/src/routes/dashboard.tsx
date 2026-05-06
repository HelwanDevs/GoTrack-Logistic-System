import { createFileRoute, Outlet, redirect } from "@tanstack/react-router";
import { checkIsMerchant, checkIsAuthenticated } from "@/features/auth";
import { Sidebar } from "@/components/Sidebar";

export const Route = createFileRoute("/dashboard")({
  beforeLoad: async () => {
    if (!checkIsAuthenticated() || !checkIsMerchant()) {
      throw redirect({ to: "/login" });
    }
  },
  component: () => (
    <div className="min-h-screen bg-background flex" dir="rtl">
      {/* Sidebar */}
      <Sidebar />
      {/* Main Content */}
      <Outlet />
    </div>
  ),
});
