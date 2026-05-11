import { ReactNode } from "react";

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: "primary" | "secondary" | "outline" | "ghost";
  size?: "sm" | "md" | "lg";
  isLoading?: boolean;
  leftIcon?: ReactNode;
  rightIcon?: ReactNode;
  fullWidth?: boolean;
}

export const Button = ({
  variant = "primary",
  size = "md",
  isLoading = false,
  leftIcon,
  rightIcon,
  fullWidth = false,
  children,
  disabled,
  className = "",
  ...props
}: ButtonProps) => {
  const baseStyles =
    "font-label-md text-label-md rounded-lg transition-all duration-200 flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed focus:outline-none";

  const variantStyles = {
    primary:
      "bg-orange-600 text-white hover:bg-orange-700 focus:ring-2 focus:ring-orange-500",
    secondary:
      "bg-indigo-900 text-white hover:bg-indigo-950 focus:ring-2 focus:ring-indigo-900",
    outline:
      "border-2 border-gray-300 text-gray-900 hover:bg-gray-50 focus:ring-2 focus:ring-indigo-600",
    ghost:
      "text-indigo-900 hover:bg-gray-100 focus:ring-2 focus:ring-indigo-600 bg-transparent",
  };

  const sizeStyles = {
    sm: "px-3 py-2 text-body-sm",
    md: "px-4 py-3 text-body-md",
    lg: "px-6 py-4 text-body-lg",
  };

  const widthStyle = fullWidth ? "w-full" : "";

  return (
    <button
      disabled={disabled || isLoading}
      className={`${baseStyles} ${variantStyles[variant]} ${sizeStyles[size]} ${widthStyle} ${className}`.trim()}
      {...props}
    >
      {leftIcon && <span>{leftIcon}</span>}
      {isLoading ? <span className="animate-spin">⟳</span> : children}
      {rightIcon && <span>{rightIcon}</span>}
    </button>
  );
};
