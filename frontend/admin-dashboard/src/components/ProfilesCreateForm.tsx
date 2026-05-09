import { useState } from "react";
import { Button } from "@/components/Button";
import { Card } from "@/components/Card";
import { Input } from "@/components/Input";
import { Select } from "@/components/Select";
import { ProfileType, ProfileStatus } from "@/types/enums";
import { AccountSearchDropdown } from "@/components/AccountSearchDropdown";
import { SearchableSelect } from "@/components/SearchableSelect";

// ─── Types ───────────────────────────────────────────────────────────────────

interface CreateProfileForm {
  full_name: string;
  phone_number: string;
  type: ProfileType | "";
  status: ProfileStatus | "";
  account_id: string | null;
  branch_id: string | null;
}

interface ProfilesCreateFormProps {
  form: CreateProfileForm;
  setForm: React.Dispatch<React.SetStateAction<CreateProfileForm>>;
  errors: Record<string, string>;
  setErrors: React.Dispatch<React.SetStateAction<Record<string, string>>>;
  onSubmit: () => void;
  onCancel: () => void;
  branches: { id: string; name: string }[];
}

interface AccountResult {
  id: string;
  email: string;
  role: string;
}

// ─── Fake Data ───────────────────────────────────────────────────────────────

const fakeAccounts: AccountResult[] = [
  { id: "1", email: "ahmed@company.com", role: "موظف" },
  { id: "2", email: "noura@company.com", role: "مسؤول" },
  { id: "3", email: "sara@company.com", role: "تاجر" },
  { id: "4", email: "khalid@company.com", role: "موظف" },
  { id: "5", email: "faisal@company.com", role: "تاجر" },
];

// ─── Component ───────────────────────────────────────────────────────────────

export const ProfilesCreateForm = ({
  form,
  setForm,
  errors,
  setErrors,
  onSubmit,
  onCancel,
  branches,
}: ProfilesCreateFormProps) => {
  const [localErrors, setLocalErrors] = useState<Record<string, string>>({});
  const [accountSearchQuery, setAccountSearchQuery] = useState("");
  const [linkedAccount, setLinkedAccount] = useState<AccountResult | null>(
    null,
  );

  const validate = (): boolean => {
    const errs: Record<string, string> = {};
    if (!form.full_name.trim()) errs.full_name = "الاسم الكامل مطلوب";
    if (!form.phone_number.trim()) errs.phone_number = "رقم الهاتف مطلوب";
    else if (!/^\+?[0-9]{10,15}$/.test(form.phone_number.trim()))
      errs.phone_number = "رقم الهاتف غير صحيح";
    if (!form.type) errs.type = "النوع مطلوب";
    if (!form.status) errs.status = "الحالة مطلوبة";
    setLocalErrors(errs);
    return Object.keys(errs).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validate()) {
      setForm({ ...form, account_id: linkedAccount?.id ?? form.account_id });
      onSubmit();
    }
  };

  const handleSelectAccount = (account: AccountResult) => {
    setLinkedAccount(account);
    setForm({ ...form, account_id: account.id });
    setAccountSearchQuery(account.email);
    setAccountSearchQuery("");
  };

  const handleRemoveAccount = () => {
    setLinkedAccount(null);
    setForm({ ...form, account_id: null });
    setAccountSearchQuery("");
  };

  const branchOptions = [
    { value: "", label: "بدون فرع" },
    ...branches.map((b) => ({ value: b.name, label: b.name })),
  ];

  const handleBranchSelect = (opt: { value: string; label: string }) => {
    const branch = branches.find((b) => b.name === opt.label);
    setForm({ ...form, branch_id: branch ? branch.id : null });
  };

  const handleBranchClear = () => {
    setForm({ ...form, branch_id: null });
  };

  const typeOptions = [
    { value: "", label: "اختر النوع" },
    { value: ProfileType.EMPLOYEE, label: "موظف" },
    { value: ProfileType.COURIER, label: "سائق توصيل" },
    { value: ProfileType.MERCHANT, label: "عميل" },
    { value: ProfileType.ADMIN, label: "مسؤول" },
  ];

  const statusOptions = [
    { value: "", label: "اختر الحالة" },
    { value: ProfileStatus.ACTIVE, label: "نشط" },
    { value: ProfileStatus.INACTIVE, label: "غير نشط" },
  ];

  const clearError = (field: string) => {
    setErrors((prev) => {
      const next = { ...prev };
      delete next[field];
      return next;
    });
  };

  return (
    <Card className="bg-surface-container-low border-2 border-secondary-container/20">
      <h3 className="font-headline-md text-headline-md text-on-background mb-6">
        إنشاء ملف شخصي جديد
      </h3>

      <form onSubmit={handleSubmit} className="space-y-4">
        <Input
          label="الاسم الكامل"
          placeholder="أدخل الاسم الكامل"
          value={form.full_name}
          onChange={(e) => {
            setForm({ ...form, full_name: e.target.value });
            clearError("full_name");
          }}
          error={errors.full_name || localErrors.full_name}
          required
        />
        <div className="grid grid-cols-12 gap-3">
          <div className="col-span-3">
            <Input
              label="رقم الهاتف"
              placeholder="501234567"
              value={form.phone_number}
              onChange={(e) => {
                if (/^\+?[0-9]*$/.test(e.target.value)) {
                  setForm({ ...form, phone_number: e.target.value });
                  clearError("phone_number");
                } else {
                  setLocalErrors((prev) => ({
                    ...prev,
                    phone_number: "رقم الهاتف يجب أن يحتوي على أرقام فقط",
                  }));
                }
              }}
              className="pl-6"
              type="tel"
              error={errors.phone_number || localErrors.phone_number}
              required
              prefix="+"
            />
          </div>
          <div className="col-span-3">
            <Select
              label="النوع"
              value={form.type}
              onChange={(e) => {
                setForm({
                  ...form,
                  type: (e.target.value as ProfileType) || "",
                });
                clearError("type");
              }}
              options={typeOptions}
              error={errors.type || localErrors.type}
              required
            />
          </div>
          <div className="col-span-3">
            <Select
              label="الحالة"
              value={form.status}
              onChange={(e) => {
                setForm({
                  ...form,
                  status: (e.target.value as ProfileStatus) || "",
                });
                clearError("status");
              }}
              options={statusOptions}
              error={errors.status || localErrors.status}
              required
            />
          </div>
          <div className="col-span-3">
            <SearchableSelect
              options={branchOptions}
              searchQuery={
                form.branch_id
                  ? branches.find((b) => b.id === form.branch_id)?.name || ""
                  : ""
              }
              setSearchQuery={(v) => {
                const branch = branches.find((b) => b.name === v);
                setForm({ ...form, branch_id: branch ? branch.id : null });
              }}
              onSelect={handleBranchSelect}
              onClear={handleBranchClear}
              hasClear
              label="الفرع"
            />
          </div>
        </div>

        {/* ── Linked Account Dropdown ── */}
        {linkedAccount ? (
          <div>
            <label className="font-label-md text-label-md text-on-surface block mb-2">
              الحساب المرتبط
            </label>
            <div className="flex gap-3 items-start">
              <div className="flex-1 p-3 bg-primary-container/20 border border-primary/30 rounded-lg">
                <p className="text-body-sm text-on-surface">
                  <span className="font-label-md">{linkedAccount.email}</span> —{" "}
                  <span className="text-on-surface-variant">
                    {linkedAccount.role}
                  </span>
                </p>
              </div>
              <Button
                variant="outline"
                size="sm"
                type="button"
                onClick={handleRemoveAccount}
                className="my-auto"
              >
                إزالة
              </Button>
            </div>
          </div>
        ) : (
          <div>
            <label className="font-label-md text-label-md text-on-surface block mb-2">
              الحساب المرتبط
            </label>
            <AccountSearchDropdown
              accounts={fakeAccounts}
              searchQuery={accountSearchQuery}
              setSearchQuery={setAccountSearchQuery}
              onSelect={handleSelectAccount}
              placeholder="example@domain.com"
              selectedId={form.account_id || undefined}
            />
            <p className="text-body-sm text-on-surface-variant mt-2">
              اختياري — ابحث عن حساب لربطه بالملف الشخصي
            </p>
          </div>
        )}

        {errors.submit && (
          <div className="p-3 bg-error/10 border border-error rounded-lg">
            <p className="text-error text-body-sm">{errors.submit}</p>
          </div>
        )}

        <div className="flex gap-3 pt-2">
          <Button variant="primary" size="md" type="submit">
            إنشاء الملف الشخصي
          </Button>
          <Button variant="outline" size="md" type="button" onClick={onCancel}>
            إلغاء
          </Button>
        </div>
      </form>
    </Card>
  );
};
