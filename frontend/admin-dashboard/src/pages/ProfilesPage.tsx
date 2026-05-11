import { useState, useCallback } from "react";
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
import {
  ProfilesNotifyModal,
  type NotifyData,
} from "../components/ProfilesNotifyModal";
import {
  useProfilesQuery,
  useSearchProfilesQuery,
  useCreateProfileMutation,
  useUpdateProfileMutation,
  useProfileByAccountQuery,
  type ProfileResponseDTO,
  type ListProfilesParams,
  type SearchProfilesParams,
  type CreateProfileRequest,
  type UpdateProfileRequest,
} from "@/features/profiles";
import {
  useBranchesQuery,
  type BranchDTO,
} from "@/features/branches";
import {
  useMyWalletQuery,
} from "@/features/wallets";

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

export const ProfilesPage = () => {
  // ── UI State ──
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [linkingProfile, setLinkingProfile] = useState<Profile | null>(null);
  const [notifyingProfile, setNotifyingProfile] = useState<Profile | null>(
    null,
  );

  // ── Pagination ──
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);

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

  // ── API Hooks ──
  const {
    data: profilesData,
    isLoading: profilesLoading,
    error: profilesError,
  } = useProfilesQuery({ page, size: pageSize, sortBy: "fullName" });

  const {
    data: searchData,
    isLoading: isSearching,
    error: searchError,
  } = useSearchProfilesQuery({
    name: nameSearch || undefined,
    type: typeFilter || undefined,
    status: statusFilter || undefined,
    branchId: branchFilter ? Number(branchFilter) : undefined,
    page: 0,
    size: 50,
    sortBy: "fullName",
  });

  const createMutation = useCreateProfileMutation();
  const updateMutation = useUpdateProfileMutation();
  const {
    data: branchesData,
    isLoading: branchesLoading,
  } = useBranchesQuery({ page: 0, size: 100 });

  // ── Derived Data ──
  const isSearchActive = Boolean(
    nameSearch || typeFilter || statusFilter || branchFilter,
  );

  const profiles = (
    isSearchActive ? searchData?.content : profilesData?.content
  )
    ?.map((profile: ProfileResponseDTO) => ({
      id: String(profile.id),
      full_name: profile.fullName,
      phone_number: profile.phoneNumber,
      type: profile.type as ProfileType,
      status: profile.status as ProfileStatus,
      account_id: profile.accountId || null,
      branch_id: profile.branchId ? String(profile.branchId) : null,
      created_by: profile.createdBy || "system",
      created_at: profile.createdAt || new Date().toISOString(),
      account: profile.accountId
        ? {
            id: profile.accountId,
            email: "linked@example.com",
            role: UserRole.EMPLOYEE,
          }
        : null,
    })) || [];

  const totalCount =
    isSearchActive ? searchData?.totalElements : profilesData?.totalElements;
  const totalPages =
    isSearchActive ? searchData?.totalPages : profilesData?.totalPages;

  const branches: { id: string; name: string }[] =
    branchesData?.content?.map((branch: BranchDTO) => ({
      id: String(branch.id),
      name: branch.name,
    })) || [];

  // ── Handlers ──
  const handleCreate = async (formData: CreateProfileForm) => {
    const errors: Record<string, string> = {};

    if (!formData.full_name.trim()) {
      errors.full_name = "الاسم مطلوب";
    }
    if (!formData.phone_number.trim()) {
      errors.phone_number = "رقم الهاتف مطلوب";
    }
    if (!formData.type) {
      errors.type = "النوع مطلوب";
    }
    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      return;
    }

    try {
      const requestData: CreateProfileRequest = {
        fullName: formData.full_name,
        phoneNumber: formData.phone_number,
        type: formData.type as ProfileType,
        branchId: formData.branch_id ? Number(formData.branch_id) : undefined,
        accountId: formData.account_id || undefined,
      };

      const response = await createMutation.mutateAsync(requestData);

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
    } catch (error: any) {
      setFormErrors({
        submit:
          error.response?.data?.message ||
          error.message ||
          "فشل إنشاء الملف الشخصي",
      });
    }
  };

  const handleEditClick = (profile: Profile) => {
    setEditingId(profile.id);
    setEditForm({
      id: profile.id,
      full_name: profile.full_name,
      phone_number: profile.phone_number,
      type: profile.type as ProfileType | "",
      status: profile.status as ProfileStatus | "",
      account_id: profile.account_id,
      branch_id: profile.branch_id,
      created_by: profile.created_by,
      created_at: profile.created_at,
      account: profile.account || null,
    });
    setFormErrors({});
  };

  const handleSaveEdit = async () => {
    const errors: Record<string, string> = {};

    if (!editForm.full_name.trim()) {
      errors.full_name = "الاسم مطلوب";
    }
    if (!editForm.phone_number.trim()) {
      errors.phone_number = "رقم الهاتف مطلوب";
    }
    if (!editForm.type) {
      errors.type = "النوع مطلوب";
    }
    if (!editForm.status) {
      errors.status = "الحالة مطلوبة";
    }

    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      return;
    }

    try {
      const updateData: UpdateProfileRequest = {
        fullName: editForm.full_name,
        phoneNumber: editForm.phone_number,
        type: editForm.type as ProfileType,
        status: editForm.status as ProfileStatus,
        branchId: editForm.branch_id ? Number(editForm.branch_id) : undefined,
        accountId: editForm.account_id || undefined,
      };

      if (editingId) {
        await updateMutation.mutateAsync({
          id: Number(editingId),
          data: updateData,
        });

        setEditingId(null);
        setEditForm({
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
        setFormErrors({});
      }
    } catch (error: any) {
      setFormErrors({
        submit:
          error.response?.data?.message ||
          error.message ||
          "فشل تحديث الملف الشخصي",
      });
    }
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setEditForm({
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
    setFormErrors({});
  };

  const handleDelete = async (id: string) => {
    if (!confirm("هل متأكد من رغبتك في حذف هذا الملف الشخصي؟")) return;
    // Note: Profile deletion may require backend API implementation
    console.log("Delete profile:", id);
  };

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

  const handleResetFilters = () => {
    setNameSearch("");
    setTypeFilter("");
    setStatusFilter("");
    setBranchFilter("");
    setPage(0);
  };

  const isLoading = profilesLoading || branchesLoading || isSearching;

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
          branches={branches}
        />
      )}

      {/* ── Filters ── */}
      <ProfilesFilters
        nameSearch={nameSearch}
        onNameSearchChange={(v) => {
          setNameSearch(v);
          setPage(0);
        }}
        typeFilter={typeFilter}
        onTypeFilterChange={(v) => {
          setTypeFilter(v as ProfileType | "");
          setPage(0);
        }}
        statusFilter={statusFilter}
        onStatusFilterChange={(v) => {
          setStatusFilter(v as ProfileStatus | "");
          setPage(0);
        }}
        branchFilter={branchFilter}
        onBranchFilterChange={(v) => {
          setBranchFilter(v);
          setPage(0);
        }}
        onResetFilters={handleResetFilters}
        totalCount={totalCount || 0}
        branches={branches}
      />

      {/* ── Table ── */}
      <ProfilesTable
        profiles={profiles}
        filtered={profiles}
        totalCount={totalCount || 0}
        searchPage={page}
        setSearchPage={setPage}
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
        isLoading={isLoading}
        branches={branches}
      />

      {/* ── Link Account Modal ── */}
      <ProfilesLinkAccountModal
        isOpen={!!linkingProfile}
        profile={linkingProfile}
        onClose={handleCloseLink}
        onConfirmLink={(accountId, accountData) => {
          setFormErrors({});
        }}
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
