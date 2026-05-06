import { useRef, useEffect, useState } from "react";

interface SearchableSelectOption {
  value: string;
  label: string;
}

interface SearchableSelectProps {
  options: SearchableSelectOption[];
  searchQuery: string;
  setSearchQuery: (val: string) => void;
  onSelect: (option: SearchableSelectOption) => void;
  onClear?: () => void;
  hasClear?: boolean;
  placeholder?: string;
  label?: string;
  className?: string;
}

export const SearchableSelect = ({
  options,
  searchQuery,
  setSearchQuery,
  onSelect,
  onClear,
  hasClear = false,
  placeholder = "اختر...",
  label,
  className = "",
}: SearchableSelectProps) => {
  const wrapperRef = useRef<HTMLDivElement>(null);
  const [isOpen, setIsOpen] = useState(false);

  const selectedOption = options.find((o) => o.value === searchQuery);

  const filteredOptions = searchQuery.trim()
    ? options.filter((o) =>
        o.label.toLowerCase().includes(searchQuery.toLowerCase()),
      )
    : options;

  const handleClickOutside = (e: MouseEvent) => {
    if (wrapperRef.current && !wrapperRef.current.contains(e.target as Node)) {
      setIsOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleSelect = (option: SearchableSelectOption) => {
    setSearchQuery(option.label);
    onSelect(option);
    setIsOpen(false);
  };

  const handleClear = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (onClear) onClear();
    setSearchQuery("");
    setIsOpen(false);
  };

  return (
    <div ref={wrapperRef} className={`flex flex-col justify-between relative w-full h-full ${className}`}>
      {label && (
        <label className="block text-body-sm text-on-surface-variant">
          {label}
        </label>
      )}
      <div className="relative">
        <input
          type="text"
          className={`w-full bg-surface-container-low border-2 border-outline-variant text-on-surface font-body-md text-body-md rounded-lg px-4 py-3 focus:ring-2 focus:ring-orange-500 focus:border-orange-500 transition-all appearance-none cursor-pointer ${
            isOpen ? "border-orange-500" : ""
          }`}
          placeholder={
            hasClear && searchQuery ? selectedOption?.label || "" : placeholder
          }
          value={searchQuery}
          onChange={(e) => {
            setSearchQuery(e.target.value);
            setIsOpen(true);
          }}
          onFocus={() => setIsOpen(true)}
        />
        {hasClear && searchQuery && (
          <button
            type="button"
            className="absolute left-3 top-1/2 -translate-y-1/2 text-on-surface-variant hover:text-error text-3xl z-10"
            onClick={handleClear}
          >
            ×
          </button>
        )}
      </div>

      {isOpen && filteredOptions.length > 0 && (
        <div className="absolute top-full left-0 right-0 mt-1 bg-surface border-2 border-outline-variant rounded-lg max-h-48 overflow-y-auto z-10 shadow-lg">
          {filteredOptions.map((option) => (
            <button
              key={option.value}
              type="button"
              className={`w-full text-right px-4 py-3 font-body-md text-body-md flex items-center gap-3 hover:bg-surface-container/50 transition ${
                searchQuery.toLowerCase() === option.value.toLowerCase() ||
                (searchQuery &&
                  searchQuery
                    .toLowerCase()
                    .includes(option.value.toLowerCase()))
                  ? "bg-primary-container/30 text-on-primary font-bold"
                  : "text-on-surface"
              }`}
              onClick={() => handleSelect(option)}
            >
              <span className="text-on-surface text-body-sm">
                {option.label}
              </span>
            </button>
          ))}
        </div>
      )}

      {isOpen && filteredOptions.length === 0 && (
        <div className="absolute top-full left-0 right-0 mt-1 bg-surface border-2 border-outline-variant rounded-lg z-10 p-3">
          <p className="text-error text-body-sm">لا توجد نتائج</p>
        </div>
      )}
    </div>
  );
};
