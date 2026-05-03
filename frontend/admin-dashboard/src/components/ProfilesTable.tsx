import { ProfileType, ProfileStatus, UserRole } from "@/types/enums";
import { Button } from "@/components/Button";
import { Card } from "@/components/Card";
import { Input } from "@/components/Input";
import { Select } from "@/components/Select";
import { LoadingSpinner } from "@/components/LoadingSpinner";
import { Pagination } from "@/components/Pagination";
import { SearchableSelect } from "@/components/SearchableSelect";

// ─── Types ───────────────────────────────────────────────────────────────────

export interface ProfileAccount {
  id: string;
  email: string;
  role: UserRole;
}

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

export interface EditingProfile {
  id: string;
  full_name: string;
  phone_number: string;
  type: ProfileType | "";
  status: ProfileStatus | "";
  account_id: string | null;
  branch_id: string | null;
  created_by: string;
  created_at: string;
  account?: ProfileAccount | null;
}

interface ProfilesTableProps {
  profiles: Profile[];
  filtered: Profile[];
  totalCount: number;
  searchPage: number;
  setSearchPage: (page: number) => void;
  size: number;
  setSize: (size: number) => void;
  editingId: string | null;
  editForm: EditingProfile;
  setEditForm: React.Dispatch<React.SetStateAction<EditingProfile>>;
  onEditClick: (profile: Profile) => void;
  onSaveEdit: () => void;
  onCancelEdit: () => void;
  onDelete: (id: string) => void;
  onOpenLink: (profile: Profile) => void;
  isLoading: boolean;
  branches: { id: string; name: string }[];
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

const getProfileTypeLabel = (type: ProfileType): string => {
  const map: Record<ProfileType, string> = {
    [ProfileType.EMPLOYEE]: "موظف",
    [ProfileType.COURIER]: "سائق توصيل",
    [ProfileType.CUSTOMER]: "عميل",
    [ProfileType.ADMIN]: "مسؤول",
  };
  return map[type];
};

const getProfileStatusLabel = (status: ProfileStatus): string => {
  const map: Record<ProfileStatus, string> = {
    [ProfileStatus.ACTIVE]: "نشط",
    [ProfileStatus.INACTIVE]: "غير نشط",
  };
  return map[status];
};

const getBranchName = (branchId: string | null, branches: { id: string; name: string }[]): string => {
  const branch = branches.find((b) => b.id === branchId);
  return branch ? branch.name : "غير مرتبط";
};

// ─── Component ───────────────────────────────────────────────────────────────

export const ProfilesTable = ({
  filtered,
  totalCount,
  searchPage,
  setSearchPage,
  size,
  setSize,
  editingId,
  editForm,
  setEditForm,
  onEditClick,
  onSaveEdit,
  onCancelEdit,
  onDelete,
  onOpenLink,
  isLoading,
  branches,
}: ProfilesTableProps) => {
  const paged = filtered.slice(searchPage * size, searchPage * size + size);

  const typeOptions = [
    { value: "", label: "اختر النوع" },
    { value: ProfileType.EMPLOYEE, label: "موظف" },
    { value: ProfileType.COURIER, label: "سائق توصيل" },
    { value: ProfileType.CUSTOMER, label: "عميل" },
    { value: ProfileType.ADMIN, label: "مسؤول" },
  ];

  const statusOptions = [
    { value: "", label: "اختر الحالة" },
    { value: ProfileStatus.ACTIVE, label: "نشط" },
    { value: ProfileStatus.INACTIVE, label: "غير نشط" },
  ];

  const branchOptions = [
    { value: "", label: "بدون فرع" },
    ...branches.map((b) => ({ value: b.name, label: b.name })),
  ];

  const handleBranchSelect = (opt: { value: string; label: string }) => {
    const branch = branches.find((b) => b.name === opt.label);
    setEditForm({
      ...editForm,
      branch_id: branch ? branch.id : null,
    });
  };

  const handleBranchClear = () => {
    setEditForm({ ...editForm, branch_id: null });
  };

  return (
    <Card>
      {filtered.length === 0 && !isLoading && (
        <div className="text-center py-12">
          <p className="text-on-surface-variant text-body-md mb-4">
            لا توجد ملف شخصي حالياً
          </p>
        </div>
      )}

      {isLoading && (
        <div className="flex justify-center items-center py-12">
          <LoadingSpinner />
        </div>
      )}

      {paged.length > 0 && (
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-outline-variant">
                <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                  الاسم الكامل
                </th>
                <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                  رقم الهاتف
                </th>
                <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                  النوع
                </th>
                <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                  الحالة
                </th>
                <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                  الحساب المرتبط
                </th>
                <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                  الفرع
                </th>
                <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                  تاريخ الإنشاء
                </th>
                <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                  الإجراءات
                </th>
              </tr>
            </thead>
            <tbody>
              {paged.map((profile) => (
                <tr
                  key={profile.id}
                  className="border-b border-surface-variant hover:bg-surface-container-low transition"
                >
                  <td className="p-4">
                    {editingId === profile.id ? (
                      <Input
                        value={editForm.full_name}
                        onChange={(e) =>
                          setEditForm({
                            ...editForm,
                            full_name: e.target.value,
                          })
                        }
                        className="text-body-sm w-50!"
                      />
                    ) : (
                      <p className="text-body-md text-on-surface">
                        {profile.full_name}
                      </p>
                    )}
                  </td>

                  <td className="p-4">
                    {editingId === profile.id ? (
                      <Input
                         value={editForm.phone_number}
                         onChange={(e) => {
                           if (/^\+?[0-9]*$/.test(e.target.value)) {
                             setEditForm({
                               ...editForm,
                               phone_number: e.target.value,
                             });
                           } else {
                             setEditForm((prev) => ({
                               ...prev,
                             }));
                           }
                         }}
                         type="tel"
                         className="text-body-sm pr-3!"
                         prefix="+"
                         width="w-40"
                       />
                    ) : (
                      <p className="text-body-md text-on-surface">
                        {profile.phone_number}+
                      </p>
                    )}
                  </td>

                  <td className="p-4">
                    {editingId === profile.id ? (
                      <Select
                        value={editForm.type}
                        onChange={(e) =>
                          setEditForm({
                            ...editForm,
                            type: (e.target.value as ProfileType) || "",
                          })
                        }
                        options={typeOptions}
                      />
                    ) : (
                      <span className="px-3 py-1 rounded-full text-label-md font-label-md bg-surface-variant text-on-surface">
                        {getProfileTypeLabel(profile.type)}
                      </span>
                    )}
                  </td>

                  <td className="p-4">
                    {editingId === profile.id ? (
                      <Select
                        value={editForm.status}
                        onChange={(e) =>
                          setEditForm({
                            ...editForm,
                            status: (e.target.value as ProfileStatus) || "",
                          })
                        }
                        options={statusOptions}
                      />
                    ) : (
                      <span
                        className={`px-3 py-1 rounded-full text-label-md font-label-md ${
                          profile.status === ProfileStatus.ACTIVE
                            ? "bg-primary-container text-on-primary"
                            : "bg-surface-variant text-on-surface"
                        }`}
                      >
                        {getProfileStatusLabel(profile.status)}
                      </span>
                    )}
                  </td>

                  <td className="p-4">
                    {editingId === profile.id ? (
                      <Button
                        variant="secondary"
                        size="sm"
                        onClick={() => onOpenLink(profile)}
                      >
                        {profile.account_id ? "تغيير الحساب" : "ربط حساب"}
                      </Button>
                    ) : (
                      <span
                        className={`px-3 py-1 rounded-full text-label-md font-label-md ${
                          profile.account_id
                            ? "bg-secondary-container text-on-secondary"
                            : "bg-surface-variant text-on-surface"
                        }`}
                      >
                        {profile.account?.email || "غير مرتبط"}
                      </span>
                    )}
                  </td>

                  <td className="p-4">
                    {editingId === profile.id ? (
                      <SearchableSelect
                        options={branchOptions}
                        searchQuery={getBranchName(editForm.branch_id, branches)}
                        setSearchQuery={(v) => {
                          const branch = branches.find((b) => b.name === v);
                          setEditForm({
                            ...editForm,
                            branch_id: branch ? branch.id : null,
                          });
                        }}
                        className="w-37.5!"
                        onSelect={handleBranchSelect}
                        onClear={handleBranchClear}
                        hasClear
                      />
                    ) : (
                      <span className="px-3 py-1 rounded-full text-label-md font-label-md bg-primary-container text-on-primary">
                        {getBranchName(profile.branch_id, branches)}
                      </span>
                    )}
                  </td>

                  <td className="p-4 text-body-sm text-on-surface-variant">
                    {new Date(profile.created_at).toLocaleDateString("ar-SA")}
                  </td>

                  <td className="p-4">
                    <div className="flex gap-2">
                      {editingId === profile.id ? (
                        <>
                          <Button
                            variant="primary"
                            size="sm"
                            onClick={onSaveEdit}
                          >
                            حفظ
                          </Button>
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={onCancelEdit}
                          >
                            إلغاء
                          </Button>
                        </>
                      ) : (
                        <>
                          <Button
                            variant="secondary"
                            size="sm"
                            onClick={() => onEditClick(profile)}
                          >
                            تعديل
                          </Button>
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={() => onOpenLink(profile)}
                          >
                            {profile.account_id ? "تغيير الحساب" : "ربط حساب"}
                          </Button>
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={() => onDelete(profile.id)}
                          >
                            حذف
                          </Button>
                        </>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <Pagination
            page={searchPage}
            setPage={setSearchPage}
            size={size}
            setSize={setSize}
            totalCount={totalCount}
            isLoading={isLoading}
          />
        </div>
      )}
    </Card>
  );
};
