import { useState } from "react";
import { Header } from "@/components/Header";
import { Button } from "@/components/Button";
import { ProfileType, ProfileStatus, UserRole } from "@/types/enums";
import { ProfilesCreateForm } from "../components/ProfilesCreateForm";
import { ProfilesFilters } from "../components/ProfilesFilters";
import {
  ProfilesTable,
  type EditingProfile,
  type ProfileAccount,
} from "../components/ProfilesTable";
import { ProfilesLinkAccountModal } from "../components/ProfilesLinkAccountModal";
import { ProfileAccount as ProfileAccountDisplaying } from "../components/ProfilesLinkAccountModal";
import {
  ProfilesNotifyModal,
  type NotifyData,
} from "../components/ProfilesNotifyModal";
// ─── Types ───────────────────────────────────────────────────────────────────

interface Profile {
  id: string;
  full_name: string;
  phone_number: string;
  type: ProfileType;
  account_id: string | null;
  branch_id: string | null;
  created_by: string;
  status: ProfileStatus;
  created_at: string;
  account?: ProfileAccount | null;
}

interface CreateProfileForm {
  full_name: string;
  phone_number: string;
  type: ProfileType | "";
  status: ProfileStatus | "";
  account_id: string | null;
  branch_id: string | null;
}

// ─── Fake Data ───────────────────────────────────────────────────────────────

const fakeProfiles: Profile[] = [
  {
    id: "1",
    full_name: "أحمد محمد",
    phone_number: "966501234567",
    type: ProfileType.EMPLOYEE,
    account_id: "1",
    branch_id: "1",
    created_by: "system",
    status: ProfileStatus.ACTIVE,
    created_at: "2025-01-15T10:00:00Z",
    account: { id: "1", email: "ahmed_mohamed2023@company.com", role: UserRole.EMPLOYEE },
  },
  {
    id: "2",
    full_name: "سارة العلي",
    phone_number: "966509876543",
    type: ProfileType.COURIER,
    account_id: null,
    branch_id: null,
    created_by: "admin",
    status: ProfileStatus.ACTIVE,
    created_at: "2025-02-20T14:30:00Z",
  },
  {
    id: "3",
    full_name: "خالد عبدالله",
    phone_number: "966507654321",
    type: ProfileType.CUSTOMER,
    account_id: null,
    branch_id: null,
    created_by: "system",
    status: ProfileStatus.INACTIVE,
    created_at: "2025-03-10T08:00:00Z",
  },
  {
    id: "4",
    full_name: "نورة السالم",
    phone_number: "966502345678",
    type: ProfileType.ADMIN,
    account_id: "2",
    branch_id: null,
    created_by: "system",
    status: ProfileStatus.ACTIVE,
    created_at: "2025-04-05T12:00:00Z",
    account: { id: "2", email: "noura@company.com", role: UserRole.ADMIN },
  },
];

// ─── Fake Branch Data ────────────────────────────────────────────────────────

interface Branch {
  id: string;
  name: string;
}

const fakeBranches: Branch[] = [
  { id: "1", name: "الفرع الرئيسي" },
  { id: "2", name: "فرع الشمال" },
  { id: "3", name: "فرع الجنوب" },
  { id: "4", name: "فرع الشرق" },
  { id: "5", name: "فرع الغرب" },
];

// ─── Page Component ──────────────────────────────────────────────────────────

export const ProfilesPage = () => {
  // ── Fake data state ──
  const [profiles, setProfiles] = useState<Profile[]>(fakeProfiles);

  // ── UI State ──
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [editingProfile, setEditingProfile] = useState<Profile | null>(null);
  const [linkingProfile, setLinkingProfile] = useState<Profile | null>(null);
  const [notifyingProfile, setNotifyingProfile] = useState<Profile | null>(null);

  // ── Forms ──
  const [createForm, setCreateForm] = useState<CreateProfileForm>({
    full_name: "",
    phone_number: "",
    type: "",
    status: "",
    account_id: null,
    branch_id: null,
  });
  const [formErrors, setFormErrors] = useState<Record<string, string>>({});
  const [editForm, setEditForm] = useState<EditingProfile>({
    id: "",
    full_name: "",
    phone_number: "",
    type: "",
    status: "",
    account_id: null,
    branch_id: null,
    created_by: "",
    created_at: "",
    account: null,
  });

  // ── Filters ──
  const [nameSearch, setNameSearch] = useState("");
  const [typeFilter, setTypeFilter] = useState<ProfileType | "">("");
  const [statusFilter, setStatusFilter] = useState<ProfileStatus | "">("");
  const [branchFilter, setBranchFilter] = useState("");
  const [searchPage, setSearchPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  // ── Derived data ──
  const filtered = profiles.filter((p) => {
    if (nameSearch && !p.full_name.includes(nameSearch)) return false;
    if (typeFilter && p.type !== typeFilter) return false;
    if (statusFilter && p.status !== statusFilter) return false;
    if (branchFilter) {
      const branchName = fakeBranches.find((b) => b.id === p.branch_id)?.name;
      if (branchName !== branchFilter) return false;
    }
    return true;
  });

  // ── Create handlers ──
  const handleCreate = () => {
    const newProfile: Profile = {
      id: String(Date.now()),
      full_name: createForm.full_name,
      phone_number: createForm.phone_number,
      type: createForm.type as ProfileType,
      status: createForm.status as ProfileStatus,
      account_id: createForm.account_id,
      branch_id: createForm.branch_id,
      created_at: new Date().toISOString(),
      created_by: "current-user",
      account: undefined,
    };
    setProfiles((prev) => [newProfile, ...prev]);
    setCreateForm({
      full_name: "",
      phone_number: "",
      type: "",
      status: "",
      account_id: null,
      branch_id: null,
    });
    setShowCreateForm(false);
    setFormErrors({});
  };

  // ── Edit handlers ──
  const handleEditClick = (profile: Profile) => {
    setEditingId(profile.id);
    setEditingProfile(profile);
    setEditForm({
      id: profile.id,
      full_name: profile.full_name,
      phone_number: profile.phone_number,
      type: profile.type,
      status: profile.status,
      account_id: profile.account_id,
      branch_id: profile.branch_id,
      created_by: profile.created_by,
      created_at: profile.created_at,
      account: profile.account ?? undefined,
    });
    setFormErrors({});
  };

  const handleSaveEdit = () => {
    setProfiles((prev) =>
      prev.map((p) => {
        if (p.id === editForm.id) {
          return {
            ...p,
            full_name: editForm.full_name,
            phone_number: editForm.phone_number,
            type: editForm.type as ProfileType,
            status: editForm.status as ProfileStatus,
            branch_id: editForm.branch_id,
          };
        }
        return p;
      }),
    );
    setEditingId(null);
    setEditingProfile(null);
    setFormErrors({});
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setEditingProfile(null);
    setFormErrors({});
  };

  // ── Delete handler ──
  const handleDelete = (id: string) => {
    if (!confirm("هل متأكد من رغبتك في حذف هذا الملف الشخصي؟")) return;
    setProfiles((prev) => prev.filter((p) => p.id !== id));
  };

  // ── Link account handlers ──
  const handleOpenLink = (profile: Profile) => {
    setLinkingProfile(profile);
  };

  const handleCloseLink = () => {
    setLinkingProfile(null);
  };

  const handleNotifySubmit = (data: NotifyData) => {
    console.log("Notification sent:", {
      profile: notifyingProfile,
      ...data,
    });
    setNotifyingProfile(null);
  };

  const handleOpenNotify = (profile: Profile) => {
    setNotifyingProfile(profile);
  };

  const handleCloseNotify = () => {
    setNotifyingProfile(null);
  };

  const handleConfirmLink = (
    accountId: string,
    accountData?: ProfileAccountDisplaying,
  ) => {
    setProfiles((prev) =>
      prev.map((p) => {
        if (p.id === linkingProfile?.id) {
          return {
            ...p,
            account_id: accountId,
            account: accountData
              ? {
                  ...accountData,
                  role: accountData.role as UserRole,
                }
              : {
                  id: accountId,
                  email: "linked@example.com",
                  role: UserRole.EMPLOYEE as unknown as UserRole,
                },
          };
        }
        return p;
      }),
    );
    // setLinkingProfile(null);
  };

  // ── Filter reset ──
  const handleResetFilters = () => {
    setNameSearch("");
    setTypeFilter("");
    setStatusFilter("");
    setBranchFilter("");
    setSearchPage(0);
  };

  return (
    <main className="flex-1 p-6 flex flex-col gap-8">
      <Header
        title="إدارة الملفات الشخصية"
        subtitle="إنشاء وتعديل وإدارة ملفات المستخدمين"
        actions={
          <Button
            variant="primary"
            size="sm"
            onClick={() => {
              setShowCreateForm(!showCreateForm);
              setEditingId(null);
              setCreateForm({
                full_name: "",
                phone_number: "",
                type: "",
                status: "",
                account_id: null,
                branch_id: null,
              });
              setFormErrors({});
            }}
          >
            {showCreateForm ? "إلغاء" : " ملف شخصي جديد"}
          </Button>
        }
      />

      {/* ── Create Form ── */}
      {showCreateForm && (
        <ProfilesCreateForm
          form={createForm}
          setForm={setCreateForm}
          errors={formErrors}
          setErrors={setFormErrors}
          onSubmit={handleCreate}
          onCancel={() => {
            setShowCreateForm(false);
            setCreateForm({
              full_name: "",
              phone_number: "",
              type: "",
              status: "",
              account_id: null,
              branch_id: null,
            });
            setFormErrors({});
          }}
          branches={fakeBranches}
        />
      )}

      {/* ── Filters ── */}
      <ProfilesFilters
        nameSearch={nameSearch}
        onNameSearchChange={(v) => {
          setNameSearch(v);
          setSearchPage(0);
        }}
        typeFilter={typeFilter}
        onTypeFilterChange={(v) => {
          setTypeFilter(v);
          setSearchPage(0);
        }}
        statusFilter={statusFilter}
        onStatusFilterChange={(v) => {
          setStatusFilter(v);
          setSearchPage(0);
        }}
        branchFilter={branchFilter}
        onBranchFilterChange={(v) => {
          setBranchFilter(v);
          setSearchPage(0);
        }}
        onResetFilters={handleResetFilters}
        totalCount={filtered.length}
        branches={fakeBranches}
      />

      {/* ── Table ── */}
      <ProfilesTable
        profiles={profiles}
        filtered={filtered}
        totalCount={filtered.length}
        searchPage={searchPage}
        setSearchPage={setSearchPage}
        size={pageSize}
        setSize={setPageSize}
        editingId={editingId}
        editForm={editForm}
        setEditForm={setEditForm}
        onEditClick={handleEditClick}
        onSaveEdit={handleSaveEdit}
        onCancelEdit={handleCancelEdit}
        onDelete={handleDelete}
        onOpenLink={handleOpenLink}
        onNotify={handleOpenNotify}
        isLoading={false}
        branches={fakeBranches}
      />

      {/* ── Link Account Modal ── */}
      <ProfilesLinkAccountModal
        isOpen={!!linkingProfile}
        profile={linkingProfile}
        onClose={handleCloseLink}
        onConfirmLink={handleConfirmLink}
      />

      {/* ── Notify Modal ── */}
      <ProfilesNotifyModal
        isOpen={!!notifyingProfile}
        profile={notifyingProfile}
        onClose={handleCloseNotify}
        onSubmit={handleNotifySubmit}
      />
    </main>
  );
};
