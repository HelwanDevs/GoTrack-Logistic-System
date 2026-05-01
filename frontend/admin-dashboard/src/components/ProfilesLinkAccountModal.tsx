import { useState } from "react";
import { Button } from "@/components/Button";
import { Input } from "@/components/Input";
import { Select } from "@/components/Select";
import { Modal } from "@/components/Modal";
import { UserRole } from "@/types/enums";
import { AccountSearchDropdown } from "@/components/AccountSearchDropdown";

// ─── Types ───────────────────────────────────────────────────────────────────

export interface ProfileAccount {
  id: string;
  email: string;
  role: string;
}
interface LinkedAccountForm {
  email: string;
  password: string;
  role: UserRole | "";
}

interface Profile {
  id: string;
  full_name: string;
  phone_number: string;
  type: string;
  account_id: string | null;
  branch_id: string | null;
  created_by: string;
  status: string;
  created_at: string;
  account?: ProfileAccount | null;
}

interface ProfilesLinkAccountModalProps {
  isOpen: boolean;
  profile: Profile | null;
  onClose: () => void;
  onConfirmLink: (accountId: string, accountData?: ProfileAccount) => void;
}

// ─── Fake Data ───────────────────────────────────────────────────────────────

const fakeAccounts: ProfileAccount[] = [
  { id: "1", email: "ahmed@company.com", role: "موظف" },
  { id: "2", email: "noura@company.com", role: "مسؤول" },
  { id: "3", email: "sara@company.com", role: "تاجر" },
  { id: "4", email: "khalid@company.com", role: "موظف" },
  { id: "5", email: "faisal@company.com", role: "تاجر" },
];

// ─── Component ───────────────────────────────────────────────────────────────

export const ProfilesLinkAccountModal = ({
  isOpen,
  profile,
  onClose,
  onConfirmLink,
}: ProfilesLinkAccountModalProps) => {
  const [linkStep, setLinkStep] = useState<"search" | "create">("search");
  const [searchQuery, setSearchQuery] = useState("");
  const [searchDropdownOpen, setSearchDropdownOpen] = useState(false);
  const [linkForm, setLinkForm] = useState<LinkedAccountForm>({
    email: "",
    password: "",
    role: "",
  });
  const [linkedAccount, setLinkedAccount] = useState<ProfileAccount | null>(
    null,
  );
  const [linkSuccess, setLinkSuccess] = useState("");
  const [linkError, setLinkError] = useState("");
  const [createEmailDropdownOpen, setCreateEmailDropdownOpen] = useState(false);

  const handleSearchSelect = (account: ProfileAccount) => {
    setLinkedAccount(account);
    setSearchQuery(account.email);
    setSearchDropdownOpen(false);
  };

  const handleCreateSelect = (account: ProfileAccount) => {
    setLinkForm({
      ...linkForm,
      email: account.email,
      role: account.role as UserRole | "",
    });
    setCreateEmailDropdownOpen(false);
    setSearchQuery(account.email);
  };

  const handleConfirmLink = () => {
    setLinkError("");

    if (!linkedAccount) {
      const accErrs: Record<string, string> = {};
      if (!linkForm.email.trim()) accErrs.email = "البريد الإلكتروني مطلوب";
      else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(linkForm.email))
        accErrs.email = "البريد الإلكتروني غير صحيح";
      if (!linkForm.password) accErrs.password = "كلمة المرور مطلوبة";
      else if (linkForm.password.length < 6)
        accErrs.password = "كلمة المرور 6 أحرف على الأقل";
      if (!linkForm.role) accErrs.role = "الدور مطلوب";
      if (Object.keys(accErrs).length) {
        setLinkError(accErrs.email || accErrs.password || accErrs.role || "");
        return;
      }
      // TODO: await createAccountApi(linkForm);
    }

    // TODO: await linkProfileToAccountApi(profile!.id, linkedAccount?.id || newAccountId);
    const accountId = linkedAccount?.id || String(Date.now());

    setLinkSuccess("تم ربط الحساب بالملف الشخصي بنجاح");
    onConfirmLink(accountId, linkedAccount || undefined);
  };

  const handleClose = () => {
    setLinkStep("search");
    setSearchQuery("");
    setSearchDropdownOpen(false);
    setCreateEmailDropdownOpen(false);
    setLinkForm({ email: "", password: "", role: "" });
    setLinkedAccount(null);
    setLinkSuccess("");
    setLinkError("");
    onClose();
  };

  const title =
    linkStep === "search"
      ? "ربط حساب موجود"
      : linkSuccess
        ? "تم الربط بنجاح"
        : "إنشاء حساب وربطه";

  return (
    <Modal isOpen={isOpen} title={title} onClose={handleClose}>
      <div className="space-y-4">
        {linkSuccess ? (
          <>
            <p className="text-success text-body-md">{linkSuccess}</p>
            <div className="flex justify-end">
              <Button variant="primary" size="md" onClick={handleClose}>
                إغلاق
              </Button>
            </div>
          </>
        ) : (
          <>
            {linkStep === "search" ? (
              <div className="space-y-4">
                <div>
                  <AccountSearchDropdown
                    accounts={fakeAccounts}
                    searchQuery={searchQuery}
                    setSearchQuery={setSearchQuery}
                    dropdownOpen={searchDropdownOpen}
                    setDropdownOpen={setSearchDropdownOpen}
                    onSelect={handleSearchSelect}
                    selectedId={linkedAccount?.id || undefined}
                  />
                </div>

                {linkError && (
                  <div className="p-3 bg-error/10 border border-error rounded-lg">
                    <p className="text-error text-body-sm">{linkError}</p>
                  </div>
                )}

                <div className="flex gap-3">
                  <Button
                    variant="primary"
                    size="md"
                    disabled={!linkedAccount}
                    onClick={() => {
                      if (linkedAccount) {
                        handleConfirmLink();
                      }
                    }}
                  >
                    تأكيد الربط
                  </Button>
                  <Button
                    variant="secondary"
                    size="md"
                    onClick={() => setLinkStep("create")}
                  >
                    إنشاء حساب جديد
                  </Button>
                  <Button variant="outline" size="md" onClick={handleClose}>
                    إلغاء
                  </Button>
                </div>
              </div>
            ) : (
              <div className="space-y-4">
                {linkedAccount && (
                  <div className="p-3 bg-primary-container/20 border border-primary/30 rounded-lg">
                    <p className="text-body-sm text-on-surface">
                      الحساب:{" "}
                      <span className="font-label-md">
                        {linkedAccount.email}
                      </span>
                    </p>
                  </div>
                )}

                {!linkedAccount && (
                  <div>
                    <Input
                      label="البريد الإلكتروني"
                      type="email"
                      placeholder="example@domain.com"
                      value={linkForm.email}
                      onChange={(e) => {
                        setLinkForm({ ...linkForm, email: e.target.value });
                        setCreateEmailDropdownOpen(true);
                      }}
                      onFocus={() => setCreateEmailDropdownOpen(true)}
                      error={
                        linkForm.email &&
                        !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(linkForm.email)
                          ? "البريد الإلكتروني غير صحيح"
                          : undefined
                      }
                    />
                    <p className="text-body-sm text-on-surface-variant mt-2">
                    </p>
                  </div>
                )}

                <Input
                  label="كلمة المرور"
                  type="password"
                  placeholder="••••••••"
                  value={linkForm.password}
                  onChange={(e) =>
                    setLinkForm({
                      ...linkForm,
                      password: e.target.value,
                    })
                  }
                />

                <Select
                  label="الدور"
                  value={linkForm.role}
                  onChange={(e) =>
                    setLinkForm({
                      ...linkForm,
                      role: (e.target.value as UserRole) || "",
                    })
                  }
                  options={[
                    { value: "", label: "اختر الدور" },
                    { value: UserRole.ADMIN, label: "مسؤول" },
                    { value: UserRole.EMPLOYEE, label: "موظف" },
                    { value: UserRole.MERCHANT, label: "تاجر" },
                  ]}
                />

                {linkError && (
                  <div className="p-3 bg-error/10 border border-error rounded-lg">
                    <p className="text-error text-body-sm">{linkError}</p>
                  </div>
                )}

                <div className="flex gap-3">
                  <Button
                    variant="primary"
                    size="md"
                    onClick={handleConfirmLink}
                  >
                    تأكيد الربط
                  </Button>
                  <Button variant="outline" size="md" onClick={handleClose}>
                    إلغاء
                  </Button>
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </Modal>
  );
};
