import { ProfileType, ProfileStatus } from "@/types/enums";
import { Button } from "@/components/Button";
import { Card } from "@/components/Card";
import { Input } from "@/components/Input";
import { Select } from "@/components/Select";
import { SearchableSelect } from "@/components/SearchableSelect";

// ─── Types ───────────────────────────────────────────────────────────────────

interface Branch {
  id: string;
  name: string;
}

interface ProfilesFiltersProps {
  nameSearch: string;
  onNameSearchChange: (value: string) => void;
  typeFilter: ProfileType | "";
  onTypeFilterChange: (value: ProfileType | "") => void;
  statusFilter: ProfileStatus | "";
  onStatusFilterChange: (value: ProfileStatus | "") => void;
  branchFilter: string;
  onBranchFilterChange: (value: string) => void;
  onResetFilters: () => void;
  totalCount: number;
  branches: Branch[];
}

// ─── Component ───────────────────────────────────────────────────────────────

export const ProfilesFilters = ({
  nameSearch,
  onNameSearchChange,
  typeFilter,
  onTypeFilterChange,
  statusFilter,
  onStatusFilterChange,
  branchFilter,
  onBranchFilterChange,
  onResetFilters,
  totalCount,
  branches,
}: ProfilesFiltersProps) => {
  const typeOptions = [
    { value: "", label: "جميع الأنواع" },
    { value: ProfileType.EMPLOYEE, label: "موظف" },
    { value: ProfileType.COURIER, label: "سائق توصيل" },
    { value: ProfileType.CUSTOMER, label: "تاجر" },
    { value: ProfileType.ADMIN, label: "مسؤول" },
  ];

  const statusOptions = [
    { value: "", label: "جميع الحالات" },
    { value: ProfileStatus.ACTIVE, label: "نشط" },
    { value: ProfileStatus.INACTIVE, label: "غير نشط" },
  ];

  const branchOptions = [
    { value: "", label: "جميع الفروع" },
    ...branches.map((b) => ({ value: b.id, label: b.name })),
  ];

  const handleBranchSelect = (opt: { value: string; label: string }) => {
    if (opt.value) {
      onBranchFilterChange(opt.label);
    } else {
      onBranchFilterChange("");
    }
  };

  const handleBranchClear = () => {
    onBranchFilterChange("");
  };

  return (
    <Card>
      <div className="mb-6">
        <h3 className="font-headline-md text-headline-md text-on-background mb-1">
          الملف الشخصي
        </h3>
        <p className="text-body-sm text-on-surface-variant">
          عدد الملفات الشخصية: {totalCount}
        </p>
      </div>

      <div className="mb-6 space-y-4 p-4 bg-surface-container-low rounded-lg">
        <div className="grid grid-cols-1 md:grid-cols-12 gap-4">
          <div className="col-span-1 md:col-span-3">
            <Input
              label="البحث حسب الاسم , رقم الهاتف أو البريد الإلكتروني"
              type="text"
              placeholder="ابحث..."
              value={nameSearch}
              onChange={(e) => onNameSearchChange(e.target.value)}
            />
          </div>

          <div className="col-span-1 md:col-span-2">
            <Select
              label="تصفية حسب النوع"
              value={typeFilter || ""}
              onChange={(e) =>
                onTypeFilterChange((e.target.value as ProfileType) || "")
              }
              options={typeOptions}
            />
          </div>

          <div className="col-span-1 md:col-span-2">
            <Select
              label="تصفية حسب الحالة"
              value={statusFilter || ""}
              onChange={(e) =>
                onStatusFilterChange((e.target.value as ProfileStatus) || "")
              }
              options={statusOptions}
            />
          </div>

          <div className="col-span-1 md:col-span-2">
            <SearchableSelect
              options={branchOptions}
              searchQuery={branchFilter}
              setSearchQuery={onBranchFilterChange}
              onSelect={handleBranchSelect}
              onClear={handleBranchClear}
              hasClear
              label="تصفية حسب الفرع"
            />
          </div>

          <div className="col-span-1 md:col-span-2 flex items-end">
            <Button
              variant="outline"
              size="md"
              fullWidth
              onClick={onResetFilters}
            >
              إعادة تعيين الفلاتر
            </Button>
          </div>
        </div>
      </div>
    </Card>
  );
};
