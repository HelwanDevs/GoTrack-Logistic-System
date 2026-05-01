import { useRef, useEffect } from "react";

export interface AccountResult {
  id: string;
  email: string;
  role: string;
}

interface AccountSearchDropdownProps {
  accounts: AccountResult[];
  searchQuery: string;
  setSearchQuery: (val: string) => void;
  dropdownOpen: boolean;
  setDropdownOpen: (val: boolean) => void;
  onSelect: (account: AccountResult) => void;
  placeholder?: string;
  selectedId?: string;
}

export const AccountSearchDropdown = ({
  accounts,
  searchQuery,
  setSearchQuery,
  dropdownOpen,
  setDropdownOpen,
  onSelect,
  placeholder = "example@domain.com",
  selectedId,
}: AccountSearchDropdownProps) => {
  const wrapperRef = useRef<HTMLDivElement>(null);

  const filteredAccounts = searchQuery.trim()
    ? accounts.filter((a) =>
        a.email.toLowerCase().includes(searchQuery.toLowerCase()),
      )
    : accounts;

  const handleClickOutside = (e: MouseEvent) => {
    if (
      wrapperRef.current &&
      !wrapperRef.current.contains(e.target as Node)
    ) {
      setDropdownOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div ref={wrapperRef} className="relative">
      <input
        type="text"
        className={`w-full bg-surface-container-low border-2 border-outline-variant text-on-surface font-body-md text-body-md rounded-lg px-4 py-3 focus:ring-2 focus:ring-orange-500 focus:border-orange-500 transition-all appearance-none cursor-pointer ${
          dropdownOpen ? "border-orange-500" : ""
        }`}
        placeholder={placeholder}
        value={searchQuery}
        onChange={(e) => {
          setSearchQuery(e.target.value);
          setDropdownOpen(true);
        }}
        onFocus={() => setDropdownOpen(true)}
      />

      {dropdownOpen && filteredAccounts.length > 0 && (
        <div className="absolute top-full left-0 right-0 mt-1 bg-surface border-2 border-outline-variant rounded-lg max-h-48 overflow-y-auto z-10 shadow-lg">
          {filteredAccounts.map((account) => (
            <button
              key={account.id}
              type="button"
              className={`w-full text-right px-4 py-3 font-body-md text-body-md flex items-center gap-3 hover:bg-surface-container/50 transition ${
                selectedId === account.id
                  ? "bg-primary-container/30 text-on-primary"
                  : "text-on-surface"
              }`}
              onClick={() => {
                onSelect(account);
                setSearchQuery(account.email);
                setDropdownOpen(false);
              }}
            >
              <span className="text-on-surface text-body-sm">
                {account.email}
              </span>
              <span className="text-on-surface-variant text-body-sm">
                — {account.role}
              </span>
            </button>
          ))}
        </div>
      )}

      {dropdownOpen && filteredAccounts.length === 0 && (
        <div className="absolute top-full left-0 right-0 mt-1 bg-surface border-2 border-outline-variant rounded-lg z-10 p-3">
          <p className="text-error text-body-sm">لا توجد نتائج</p>
        </div>
      )}
    </div>
  );
};
