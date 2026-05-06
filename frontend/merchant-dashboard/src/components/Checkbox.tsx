import { ReactNode } from "react";

interface CheckboxProps extends Omit<
  React.InputHTMLAttributes<HTMLInputElement>,
  "size"
> {
  label?: string | ReactNode;
  error?: string;
  helpText?: string;
  size?: "sm" | "md" | "lg" | "xl" | "2xl" | "3xl";
}

export const Checkbox = ({
  label,
  error,
  helpText,
  size = "md",
  className = "",
  checked,
  disabled,
  ...props
}: CheckboxProps) => {
  const sizeStyles = {
    sm: "w-4 h-4",
    md: "w-5 h-5",
    lg: "w-6 h-6",
    xl: "w-7 h-7",
    "2xl": "w-8 h-8",
    "3xl": "w-11 h-11",
  };

  const labelSizeStyles = {
    sm: "text-label-sm",
    md: "text-label-md",
    lg: "text-body-md",
    xl: "text-body-lg",
    "2xl": "text-body-xl",
    "3xl": "text-body-2xl",
  };

  return (
    <div className="w-full h-full">
      <div className="flex items-center flex-col-reverse gap-2 h-full">
        <div className="relative flex w-full h-full text-center justify-center">
          <div className="w-fit">
            <input
              type="checkbox"
              checked={checked}
              disabled={disabled}
              className={`mt-0.5 appearance-none cursor-pointer rounded transition-all duration-200 ${sizeStyles[size]} ${
                checked
                  ? "bg-orange-600 border-2 border-orange-600"
                  : "bg-surface-container-low border-2 border-outline-variant hover:border-orange-500"
              } ${
                error ? "border-error" : ""
              } focus:ring-2 focus:ring-orange-500 focus:ring-offset-0 ${
                disabled ? "opacity-50 cursor-not-allowed" : ""
              } ${className}`.trim()}
              {...props}
            />
          </div>
          {checked && (
            <svg
              className={`absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-white pointer-events-none ${
                size === "sm"
                  ? "w-2.5 h-2.5"
                  : size === "md"
                    ? "w-3 h-3"
                    : size === "lg"
                      ? "w-3.5 h-3.5"
                      : size === "xl"
                        ? "w-4 h-4"
                        : size === "2xl"
                          ? "w-5 h-5"
                          : "w-6 h-6"
              }`.trim()}
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fillRule="evenodd"
                d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                clipRule="evenodd"
              />
            </svg>
          )}
        </div>

        {label && (
          <label
            className={`font-label-md ${labelSizeStyles[size]} text-on-surface cursor-pointer select-none ${
              disabled ? "opacity-50" : ""
            }`}
          >
            {label}
            {props.required && <span className="text-error mr-1">*</span>}
          </label>
        )}
      </div>

      {error && (
        <p className="font-body-sm text-body-sm text-error mt-1 mr-7">
          {error}
        </p>
      )}
      {helpText && !error && (
        <p className="font-body-sm text-body-sm text-on-surface-variant mt-1 mr-7">
          {helpText}
        </p>
      )}
    </div>
  );
};
