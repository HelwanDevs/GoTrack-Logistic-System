export type ButtonVariant = "primary" | "secondary" | "outline" | "ghost";
export type ButtonSize = "sm" | "md" | "lg";
export type MetricCardColor = "primary" | "secondary" | "error" | "success";

export interface SidebarItem {
  id: string;
  label: string;
  icon: React.ReactNode;
  onClick?: () => void;
  badge?: number;
}

export interface TrendIndicator {
  value: number;
  direction: "up" | "down";
}

export interface FormField {
  name: string;
  label: string;
  type?: "text" | "email" | "password" | "number" | "tel";
  placeholder?: string;
  required?: boolean;
  validation?: (value: string) => string | undefined;
}

export interface DashboardMetric {
  label: string;
  value: string | number;
  icon?: React.ReactNode;
  trend?: TrendIndicator;
  color?: MetricCardColor;
}

export interface Shipment {
  id: string;
  client: string;
  status: "pending" | "in-transit" | "delivered" | "delayed";
  date: Date;
  destination: string;
}

export const SHIPMENT_STATUS = {
  pending: { label: "قيد الانتظار", color: "bg-surface-variant" },
  "in-transit": { label: "في الطريق", color: "bg-secondary-container" },
  delivered: { label: "تم التسليم", color: "bg-primary-container" },
  delayed: { label: "متأخر", color: "bg-error-container" },
} as const;
