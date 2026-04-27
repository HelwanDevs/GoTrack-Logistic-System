import { ReactNode } from "react";

interface SelectOption {
  value: string;
  label: string;
}

interface SelectProps extends React.SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
  error?: string;
  helpText?: string;
  icon?: ReactNode;
  options: SelectOption[];
  placeholder?: string;
}

export const Select = ({
  label,
  error,
  helpText,
  icon,
  options,
  placeholder = "اختر...",
  className = "",
  ...props
}: SelectProps) => {
  return (
    <div className="w-full">
      {label && (
        <label className="font-label-md text-label-md text-on-surface block mb-2">
          {label}
          {props.required && <span className="text-error ml-1">*</span>}
        </label>
      )}
      <div className="relative">
        <select
          className={`w-full bg-surface-container-low border-2 border-outline-variant text-on-surface font-body-md text-body-md rounded-lg px-4 py-3 focus:ring-2 focus:ring-orange-500 focus:border-orange-500 transition-all appearance-none cursor-pointer ${
            error ? "border-error" : ""
          } ${icon ? "pr-10" : ""} ${className}`.trim()}
          {...props}
        >
          <option value="">{placeholder}</option>
          {options.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
        <div className="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none text-on-surface-variant">
          <svg
            className="w-5 h-5"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M19 14l-7 7m0 0l-7-7m7 7V3"
            />
          </svg>
        </div>
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
