import { ReactNode } from "react";

interface CardProps {
  children: ReactNode;
  className?: string;
  hover?: boolean;
}

export const BlueCard = ({
  children,
  className = "",
  hover = false,
}: CardProps) => {
  const hoverClass = hover
    ? "hover:shadow-md hover:bg-surface-container-high transition-all duration-200 cursor-pointer"
    : "";

  return (
    <div
      className={`bg-primary rounded-lg border border-outline-variant shadow-sm p-6 ${hoverClass} ${className}`.trim()}
    >
      {children}
    </div>
  );
};
