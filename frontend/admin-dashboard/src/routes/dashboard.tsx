import {
  createFileRoute,
  Outlet,
  redirect,
} from "@tanstack/react-router";
import { checkIsEmployee, checkIsAuthenticated } from "@/features/auth";
import { Sidebar } from "@/components/Sidebar";
export const Route = createFileRoute("/dashboard")({
  beforeLoad: async () => {
    if (!checkIsAuthenticated() || !checkIsEmployee()) {
      throw redirect({ to: "/login" });
    }
  },
  component: () => (
    <div className="min-h-screen bg-background flex flex-row" dir="rtl">
      {/* Sidebar */}
      <Sidebar />
      {/* Main Content */}
      <div className="flex-col flex-1 p-0">
      
        <Outlet />
      </div>
    </div>
  ),
});
