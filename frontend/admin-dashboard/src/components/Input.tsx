import { ReactNode, useState } from "react";

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  helpText?: string;
  icon?: ReactNode;
  prefix?: string;
}

export const Input = ({
  label,
  error,
  helpText,
  icon,
  prefix,
  className = "",
  ...props
}: InputProps) => {
  const [value, setValue] = useState(props.value || "");
  return (
    <div className="w-full">
      {label && (
        <label className="font-label-md text-label-md text-on-surface block mb-2">
          {label}

          {props.required && <span className="text-error ml-1">*</span>}
        </label>
      )}
      <div className="relative">
        {prefix && (
          <div className="absolute left-3 top-1/2 -translate-y-1/2 text-on-surface-variant pointer-events-none">
            {prefix}
          </div>
        )}
        <input
          className={`w-full bg-surface-container-low border-2 border-outline-variant text-on-surface font-body-md text-body-md rounded-lg px-4 py-3 focus:ring-2 focus:ring-orange-500 focus:border-orange-500 transition-all ${
            error ? "border-error" : ""
          } ${icon ? "pr-10" : ""} ${prefix ? "pr-8" : ""} ${className}`.trim()}
          {...props}
        />
        {icon && (
          <div className="absolute left-3 top-1/2 -translate-y-1/2 text-on-surface-variant">
            {icon}
          </div>
        )}
      </div>
      {error && (
        <p className="font-body-sm text-body-sm text-error mt-1">{error}</p>
      )}
      {helpText && !error && (
        <p className="font-body-sm text-body-sm text-on-surface-variant mt-1">
          {helpText}
        </p>
      )}
    </div>
  );
};
