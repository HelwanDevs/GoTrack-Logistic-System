import { useState } from "react";
import { Header } from "@/components/Header";
import { Button } from "@/components/Button";
import { Card } from "@/components/Card";
import { Input } from "@/components/Input";
import { Select } from "@/components/Select";
import { LoadingSpinner } from "@/components/LoadingSpinner";
import {
  useAccountsQuery,
  useCreateAccountMutation,
  useUpdateAccountMutation,
  useDeleteAccountMutation,
  type Account,
  type CreateAccountRequest,
  type UpdateAccountRequest,
  type ListAccountsParams,
} from "@/features/accounts";
import { UserRole } from "@/types/enums";

interface FormData {
  email: string;
  password: string;
  role: UserRole | "";
}

interface EditingAccount {
  id: string;
  email: string;
  password: string;
  role: UserRole | "";
}

export const AccountsPage = () => {
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [formErrors, setFormErrors] = useState<Record<string, string>>({});

  // Pagination
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);

  // Filters
  const [emailSearch, setEmailSearch] = useState("");
  const [roleFilter, setRoleFilter] = useState<UserRole | "">();

  // Fetch accounts from API with pagination and filters
  const queryParams: ListAccountsParams = {
    page,
    size,
    ...(emailSearch && { email: emailSearch }),
    ...(roleFilter && { role: roleFilter as UserRole }),
  };

  const {
    data: accountsData,
    isLoading,
    error: fetchError,
  } = useAccountsQuery(queryParams);

  const accounts = accountsData?.accounts || [];
  const totalCount = accountsData?.totalCount || 0;
  const totalPages = accountsData?.totalPages || 0;

  const createMutation = useCreateAccountMutation();
  const updateMutation = useUpdateAccountMutation();
  const deleteMutation = useDeleteAccountMutation();

  const [createForm, setCreateForm] = useState<FormData>({
    email: "",
    password: "",
    role: "",
  });

  const [editForm, setEditForm] = useState<EditingAccount>({
    id: "",
    email: "",
    password: "",
    role: "",
  });

  const roleOptions = [
    { value: UserRole.ADMIN, label: "مسؤول" },
    { value: UserRole.EMPLOYEE, label: "موظف" },
    { value: UserRole.MERCHANT, label: "تاجر" },
  ];

  const validateCreateForm = (): boolean => {
    const errors: Record<string, string> = {};

    if (!createForm.email) {
      errors.email = "البريد الإلكتروني مطلوب";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(createForm.email)) {
      errors.email = "البريد الإلكتروني غير صحيح";
    }

    if (!createForm.password) {
      errors.password = "كلمة المرور مطلوبة";
    } else if (createForm.password.length < 6) {
      errors.password = "كلمة المرور يجب أن تكون 6 أحرف على الأقل";
    }

    if (!createForm.role) {
      errors.role = "الدور مطلوب";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const validateEditForm = (): boolean => {
    const errors: Record<string, string> = {};

    if (!editForm.email) {
      errors.email = "البريد الإلكتروني مطلوب";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(editForm.email)) {
      errors.email = "البريد الإلكتروني غير صحيح";
    }

    if (editForm.password && editForm.password.length < 6) {
      errors.password = "كلمة المرور يجب أن تكون 6 أحرف على الأقل";
    }

    if (!editForm.role) {
      errors.role = "الدور مطلوب";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleCreateAccount = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateCreateForm()) {
      return;
    }

    try {
      await createMutation.mutateAsync(createForm as CreateAccountRequest);
      setCreateForm({ email: "", password: "", role: "" });
      setShowCreateForm(false);
      setFormErrors({});
    } catch (error: any) {
      setFormErrors({
        submit: error.message || "فشل إنشاء الحساب. حاول مرة أخرى",
      });
    }
  };

  const handleEditAccount = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateEditForm()) {
      return;
    }

    try {
      const updateData: UpdateAccountRequest = {
        email: editForm.email,
        role: editForm.role as UserRole,
      };

      if (editForm.password) {
        updateData.password = editForm.password;
      }

      await updateMutation.mutateAsync({
        accountId: editForm.id,
        data: updateData,
      });

      setEditingId(null);
      setEditForm({ id: "", email: "", password: "", role: "" });
      setFormErrors({});
    } catch (error: any) {
      setFormErrors({
        submit: error.message || "فشل تحديث الحساب. حاول مرة أخرى",
      });
    }
  };

  const handleDeleteAccount = async (accountId: string) => {
    if (!confirm("هل تأكد من رغبتك في حذف هذا الحساب؟")) {
      return;
    }

    try {
      await deleteMutation.mutateAsync(accountId);
    } catch (error: any) {
      alert(error.message || "فشل حذف الحساب. حاول مرة أخرى");
    }
  };

  const handleEditClick = (account: Account) => {
    setEditingId(account.id);
    setEditForm({
      id: account.id,
      email: account.email,
      password: "",
      role: account.role,
    });
    setFormErrors({});
  };

  const getRoleLabel = (role: UserRole): string => {
    const roleMap: Record<UserRole, string> = {
      [UserRole.ADMIN]: "مسؤول",
      [UserRole.EMPLOYEE]: "موظف",
      [UserRole.MERCHANT]: "تاجر",
    };
    return roleMap[role];
  };

  const getRoleBadgeColor = (
    role: UserRole,
  ): "primary" | "secondary" | "error" => {
    switch (role) {
      case UserRole.ADMIN:
        return "primary";
      case UserRole.EMPLOYEE:
        return "secondary";
      case UserRole.MERCHANT:
        return "error";
      default:
        return "primary";
    }
  };

  return (
    <main className="flex-1 mr-0 md:mr-64 p-6 lg:p-10 flex flex-col gap-8">
      <Header
        title="إدارة الحسابات"
        subtitle="إنشاء وتحديث وحذف حسابات المستخدمين"
        actions={
          <Button
            variant="primary"
            size="sm"
            onClick={() => {
              setShowCreateForm(!showCreateForm);
              setEditingId(null);
              setCreateForm({ email: "", password: "", role: "" });
              setFormErrors({});
            }}
          >
            {showCreateForm ? "إلغاء" : "+ حساب جديد"}
          </Button>
        }
      />

      {/* Create Account Form */}
      {showCreateForm && (
        <Card className="bg-surface-container-low border-2 border-secondary-container/20">
          <h3 className="font-headline-md text-headline-md text-on-background mb-6">
            إنشاء حساب جديد
          </h3>

          <form onSubmit={handleCreateAccount} className="space-y-4">
            <Input
              label="البريد الإلكتروني"
              type="email"
              placeholder="example@domain.com"
              value={createForm.email}
              onChange={(e) => {
                setCreateForm({
                  ...createForm,
                  email: e.target.value,
                });
                if (formErrors.email) {
                  setFormErrors({ ...formErrors, email: "" });
                }
              }}
              error={formErrors.email}
              required
            />

            <Input
              label="كلمة المرور"
              type="password"
              placeholder="••••••••"
              value={createForm.password}
              onChange={(e) => {
                setCreateForm({
                  ...createForm,
                  password: e.target.value,
                });
                if (formErrors.password) {
                  setFormErrors({ ...formErrors, password: "" });
                }
              }}
              error={formErrors.password}
              required
            />

            <Select
              label="الدور"
              value={createForm.role}
              onChange={(e) => {
                setCreateForm({
                  ...createForm,
                  role: (e.target.value as UserRole) || "",
                });
                if (formErrors.role) {
                  setFormErrors({ ...formErrors, role: "" });
                }
              }}
              options={roleOptions}
              error={formErrors.role}
              required
            />

            {formErrors.submit && (
              <div className="p-3 bg-error/10 border border-error rounded-lg">
                <p className="text-error text-body-sm">{formErrors.submit}</p>
              </div>
            )}

            <div className="flex gap-3 pt-2">
              <Button
                type="submit"
                variant="primary"
                size="md"
                disabled={createMutation.isPending}
                isLoading={createMutation.isPending}
              >
                {createMutation.isPending ? "جاري الإنشاء..." : "إنشاء الحساب"}
              </Button>
              <Button
                type="button"
                variant="outline"
                size="md"
                onClick={() => {
                  setShowCreateForm(false);
                  setCreateForm({ email: "", password: "", role: "" });
                  setFormErrors({});
                }}
              >
                إلغاء
              </Button>
            </div>
          </form>
        </Card>
      )}

      {/* Accounts List */}
      <Card>
        <div className="mb-6">
          <h3 className="font-headline-md text-headline-md text-on-background mb-1">
            الحسابات
          </h3>
          <p className="text-body-sm text-on-surface-variant">
            عدد الحسابات: {totalCount}
          </p>
        </div>

        {/* Search and Filter Section */}
        <div className="mb-6 space-y-4 p-4 bg-surface-container-low rounded-lg">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Input
              label="البحث حسب البريد الإلكتروني"
              type="text"
              placeholder="ابحث..."
              value={emailSearch}
              onChange={(e) => {
                setEmailSearch(e.target.value);
                setPage(0); // Reset to first page on search
              }}
            />

            <Select
              label="تصفية حسب الدور"
              value={roleFilter || ""}
              onChange={(e) => {
                setRoleFilter((e.target.value as UserRole) || "");
                setPage(0); // Reset to first page on filter change
              }}
              options={[
                { value: "", label: "جميع الأدوار" },
                { value: UserRole.ADMIN, label: "مسؤول" },
                { value: UserRole.EMPLOYEE, label: "موظف" },
                { value: UserRole.MERCHANT, label: "تاجر" },
              ]}
            />

            <div className="flex items-end">
              <Button
                variant="outline"
                size="md"
                fullWidth
                onClick={() => {
                  setEmailSearch("");
                  setRoleFilter("");
                  setPage(0);
                }}
              >
                إعادة تعيين الفلاتر
              </Button>
            </div>
          </div>
        </div>

        {/* Loading State */}
        {isLoading && (
          <div className="flex justify-center items-center py-12">
            <LoadingSpinner />
          </div>
        )}

        {/* Error State */}
        {fetchError && !isLoading && (
          <div className="p-4 bg-error/10 border border-error rounded-lg mb-4">
            <p className="text-error text-body-md">
              خطأ في تحميل الحسابات. يرجى المحاولة مرة أخرى.
            </p>
          </div>
        )}

        {/* Empty State */}
        {!isLoading && accounts.length === 0 && !fetchError && (
          <div className="text-center py-12">
            <p className="text-on-surface-variant text-body-md mb-4">
              لا توجد حسابات حالياً
            </p>
            <Button
              variant="outline"
              size="md"
              onClick={() => setShowCreateForm(true)}
            >
              إنشاء حساب الآن
            </Button>
          </div>
        )}

        {/* Accounts Table */}
        {!isLoading && accounts.length > 0 && (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-outline-variant">
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    البريد الإلكتروني
                  </th>
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    الدور
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
                {accounts.map((account) => (
                  <tr
                    key={account.id}
                    className="border-b border-surface-variant hover:bg-surface-container-low transition"
                  >
                    <td className="p-4">
                      {editingId === account.id ? (
                        <Input
                          type="email"
                          value={editForm.email}
                          onChange={(e) =>
                            setEditForm({
                              ...editForm,
                              email: e.target.value,
                            })
                          }
                          className="text-body-sm"
                        />
                      ) : (
                        <p className="text-body-md text-on-surface">
                          {account.email}
                        </p>
                      )}
                    </td>
                    <td className="p-4">
                      {editingId === account.id ? (
                        <Select
                          value={editForm.role}
                          onChange={(e) =>
                            setEditForm({
                              ...editForm,
                              role: (e.target.value as UserRole) || "",
                            })
                          }
                          options={roleOptions}
                        />
                      ) : (
                        <span
                          className={`px-3 py-1 rounded-full text-label-md font-label-md ${
                            account.role === UserRole.ADMIN
                              ? "bg-primary text-on-primary"
                              : account.role === UserRole.EMPLOYEE
                                ? "bg-secondary-container text-on-primary"
                                : "bg-surface-variant text-on-surface"
                          }`}
                        >
                          {getRoleLabel(account.role)}
                        </span>
                      )}
                    </td>
                    <td className="p-4 text-body-sm text-on-surface-variant">
                      {new Date(account.createdAt ?? new Date()).toLocaleDateString("ar-SA")}
                    </td>
                    <td className="p-4">
                      <div className="flex gap-2">
                        {editingId === account.id ? (
                          <>
                            <Button
                              variant="primary"
                              size="sm"
                              onClick={handleEditAccount}
                              disabled={updateMutation.isPending}
                              isLoading={updateMutation.isPending}
                            >
                              حفظ
                            </Button>
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => {
                                setEditingId(null);
                                setFormErrors({});
                              }}
                            >
                              إلغاء
                            </Button>
                          </>
                        ) : (
                          <>
                            <Button
                              variant="secondary"
                              size="sm"
                              onClick={() => handleEditClick(account)}
                            >
                              تعديل
                            </Button>
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => handleDeleteAccount(account.id)}
                              disabled={deleteMutation.isPending}
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

            {/* Pagination Controls */}
            <div className="mt-6 flex items-center justify-between p-4 bg-surface-container-low rounded-lg">
              <div className="text-body-sm text-on-surface-variant">
                عرض {accounts.length} من {totalCount} حساب
                {totalPages > 1 && ` (الصفحة ${page + 1} من ${totalPages})`}
              </div>

              <div className="flex gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  disabled={page === 0 || isLoading}
                  onClick={() => setPage(Math.max(0, page - 1))}
                >
                  السابق
                </Button>

                {/* Page Info */}
                <div className="flex items-center gap-2 px-4 py-2 bg-surface-container rounded-lg">
                  <span className="text-body-sm text-on-surface">
                    {page + 1} / {Math.max(1, totalPages)}
                  </span>
                </div>

                <Button
                  variant="outline"
                  size="sm"
                  disabled={page >= totalPages - 1 || isLoading}
                  onClick={() => setPage(page + 1)}
                >
                  التالي
                </Button>
              </div>

              {/* Page Size Selector */}
              <Select
                value={size.toString()}
                onChange={(e) => {
                  setSize(parseInt(e.target.value));
                  setPage(0);
                }}
                options={[
                  { value: "5", label: "5 عناصر" },
                  { value: "10", label: "10 عناصر" },
                  { value: "25", label: "25 عنصر" },
                  { value: "50", label: "50 عنصر" },
                ]}
              />
            </div>
          </div>
        )}
      </Card>
    </main>
  );
};
