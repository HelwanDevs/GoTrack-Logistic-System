import { ReactNode } from "react";

interface MetricCardProps {
  label: string;
  value: string | number;
  icon?: ReactNode;
  trend?: {
    value: number;
    direction: "up" | "down";
  };
  color?: "primary" | "secondary" | "error" | "success";
}

export const MetricCard = ({
  label,
  value,
  icon,
  trend,
  color = "primary",
}: MetricCardProps) => {
  const colorClasses = {
    primary: "text-primary",
    secondary: "text-secondary-container",
    error: "text-error",
    success: "text-primary-container",
  };

  return (
    <div className="bg-surface-container-lowest rounded-lg border border-outline-variant shadow-sm p-6">
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <p className="font-body-sm text-body-sm text-on-surface-variant mb-2">
            {label}
          </p>
          <p
            className={`font-headline-xl text-headline-xl ${colorClasses[color]}`}
          >
            {value}
          </p>
          {trend && (
            <div className="flex items-center gap-1 mt-3">
              <span
                className={`text-sm font-label-md ${
                  trend.direction === "up" ? "text-green-600" : "text-red-600"
                }`}
              >
                {trend.direction === "up" ? "↑" : "↓"} {Math.abs(trend.value)}%
              </span>
              <span className="text-on-surface-variant text-body-sm">
                من الشهر الماضي
              </span>
            </div>
          )}
        </div>
        {icon && (
          <div className={`text-4xl ${colorClasses[color]} opacity-80`}>
            {icon}
          </div>
        )}
      </div>
    </div>
  );
};
