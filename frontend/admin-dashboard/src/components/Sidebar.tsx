import { ReactNode, useEffect, useState } from "react";
import { Button } from "./Button";
import { useNavigate } from "@tanstack/react-router";
import { useAuthState, useLogoutMutation } from "@/features/auth";
import { getStoredRefreshToken } from "@/utils/storage";

export const Sidebar = () => {
  const navigate = useNavigate();
  const logoutMutation = useLogoutMutation();
  const authState = useAuthState();
  const [activeNav, setActiveNav] = useState(
    location.pathname.split("/").pop() || "dashboard",
  );

  const handleLogout = async () => {
    try {
      const refreshToken = getStoredRefreshToken();
      if (refreshToken) {
        await logoutMutation.mutateAsync(refreshToken);
      }
      await navigate({ to: "/login" });
    } catch (error) {
      console.error("Logout error:", error);
      await navigate({ to: "/login" });
    }
  };

  const sidebarItems = [
    {
      id: "dashboard",
      label: "لوحة التحكم",
      icon: "📊",
      onClick: () => setActiveNav("dashboard"),
      Roles: ["admin", "employee"],
    },
    {
      id: "accounts",
      label: "إدارة الحسابات",
      icon: "👥",
      onClick: () => setActiveNav("accounts"),
      Roles: ["admin"],
    },
    {
      id: "profiles",
      label: "إدارة الملفات الشخصية",
      icon: "👤",
      onClick: () => setActiveNav("profiles"),
      Roles: ["admin", "employee"],
    },
    {
      id: "branches",
      label: "إدارة الفروع",
      icon: "🏢",
      onClick: () => setActiveNav("branches"),
      Roles: ["admin"],
    },
    {
      id: "shipments",
      label: "إدارة الشحنات",
      icon: "📦",
      onClick: () => setActiveNav("shipments"),
      badge: 5,
      Roles: ["admin", "employee"],
    },
    {
      id: "tracking",
      label: "تتبع مباشر",
      icon: "🗺️",
      onClick: () => setActiveNav("tracking"),
      Roles: ["admin", "employee"],
    },
    {
      id: "reports",
      label: "التقارير",
      icon: "📈",
      onClick: () => setActiveNav("reports"),
      Roles: ["admin"],
    },
    {
      id: "settings",
      label: "الإعدادات",
      icon: "⚙️",
      onClick: () => setActiveNav("settings"),
      Roles: ["admin", "employee"],
    },
  ];
  useEffect(() => {
    navigate({ to: `/dashboard/${activeNav == "dashboard" ? "" : activeNav}` });
  }, [activeNav]);

  return (
    <nav className="sticky right-0 top-0 h-screen w-64 bg-primary-container flex-col z-40 border-l border-white/10 shadow-xl hidden md:flex">
      {/* Logo Section */}
      <div className="p-6 flex flex-col items-center border-b  border-white/10 mb-10">
        <div className="flex items-center justify-center mb-4">
          <div className="text-center mt-4 mb-0">
            <img
              src="/gotrack_logo.png"
              alt="GoTrack Logo"
              className="img-fluid mx-auto w-64"
            />
          </div>
        </div>
      </div>

      {/* Navigation Items */}

      <div className="flex-1 flex flex-col gap-2 pl-4 pr-2 font-sans text-right">
        {sidebarItems
          .filter(
            (item) =>
              authState.data?.user?.role &&
              item.Roles.includes(authState.data.user.role.toLocaleLowerCase()),
          )
          .map((item) => (
            <button
              key={item.id}
              onClick={item.onClick}
              className={`flex flex-row-reverse items-center p-3 gap-3 rounded-l-lg transition-all focus:ring-2 focus:ring-secondary-container outline-none ${
                activeNav === item.id
                  ? "bg-secondary-container text-on-secondary rounded-l-lg"
                  : "text-slate-300 hover:bg-white/10"
              }`}
            >
              <span className="text-xl">{item.icon}</span>
              <span className="font-label-md text-label-md">{item.label}</span>
              {item.badge && item.badge > 0 && (
                <span className="ml-auto bg-error text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                  {item.badge}
                </span>
              )}
            </button>
          ))}
      </div>

      <div className="p-3">
        <Button
          variant="primary"
          size="lg"
          fullWidth
          onClick={handleLogout}
          isLoading={logoutMutation.isPending}
        >
          {logoutMutation.isPending ? "جاري تسجيل الخروج..." : "تسجيل الخروج"}
        </Button>
      </div>
    </nav>
  );
};
