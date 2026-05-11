import { useState, useCallback } from "react";
import { Header } from "@/components/Header";
import { Button } from "@/components/Button";
import { Card } from "@/components/Card";
import { Input } from "@/components/Input";
import { Select } from "@/components/Select";
import { LoadingSpinner } from "@/components/LoadingSpinner";
import {
  useBranchesQuery,
  useCreateBranchMutation,
  useUpdateBranchMutation,
  useDeleteBranchMutation,
  useSearchBranchesQuery,
  type BranchDTO,
  type CreateBranchRequest,
  type UpdateBranchRequest,
  type ListBranchesParams,
  type SearchBranchParams,
} from "@/features/branches";

interface BranchFormData {
  name: string;
  location: string;
  phone: string;
}

interface EditingBranch {
  id: number;
  name: string;
  location: string;
  phone: string;
  isDeleted: boolean;
}

export const BranchesPage = () => {
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [formErrors, setFormErrors] = useState<Record<string, string>>({});

  // Pagination
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);

  // Filters
  const [nameSearch, setNameSearch] = useState("");
  const [locationSearch, setLocationSearch] = useState("");
  const [phoneSearch, setPhoneSearch] = useState("");
  const [deletedFilter, setDeletedFilter] = useState(false);

  // Create Form State
  const [createForm, setCreateForm] = useState<BranchFormData>({
    name: "",
    location: "",
    phone: "",
  });

  // Edit Form State
  const [editForm, setEditForm] = useState<EditingBranch>({
    id: 0,
    name: "",
    location: "",
    phone: "",
    isDeleted: false,
  });

  // Mutations
  const createMutation = useCreateBranchMutation();
  const updateMutation = useUpdateBranchMutation();
  const deleteMutation = useDeleteBranchMutation();

  // Fetch branches from API with pagination and filters
  const queryParams: ListBranchesParams = {
    page,
    size,
  };

  const searchParams: SearchBranchParams = {
    name: nameSearch || undefined,
    location: locationSearch || undefined,
    phone: phoneSearch || undefined,
    isDeleted: deletedFilter,
  };

  const {
    data: branchesData,
    isLoading,
    error: fetchError,
  } = useBranchesQuery(queryParams);

  const {
    data: searchData,
    isLoading: isSearching,
    error: searchError,
  } = useSearchBranchesQuery(searchParams);

  const isSearchActive = Boolean(nameSearch || locationSearch || phoneSearch);
  const branches = isSearchActive
    ? searchData?.content || []
    : branchesData?.content || [];
  const totalCount = isSearchActive
    ? searchData?.totalElements || 0
    : branchesData?.totalElements || 0;
  const totalPages = isSearchActive
    ? searchData?.totalPages || 0
    : branchesData?.totalPages || 0;

  const validateCreateForm = useCallback((): boolean => {
    const errors: Record<string, string> = {};

    if (!createForm.name.trim()) {
      errors.name = "اسم الفرع مطلوب";
    } else if (createForm.name.length < 3) {
      errors.name = "يجب أن يكون اسم الفرع 3 أحرف على الأقل";
    }

    if (!createForm.location.trim()) {
      errors.location = "الموقع مطلوب";
    }

    if (!createForm.phone.trim()) {
      errors.phone = "رقم الهاتف مطلوب";
    } else if (!/^\d{11}$/.test(createForm.phone)) {
      errors.phone = "رقم الهاتف يجب أن يكون 11 رقم";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  }, [createForm]);

  const validateEditForm = useCallback((): boolean => {
    const errors: Record<string, string> = {};

    if (!editForm.name.trim()) {
      errors.name = "اسم الفرع مطلوب";
    }

    if (!editForm.location.trim()) {
      errors.location = "الموقع مطلوب";
    }

    if (!editForm.phone.trim()) {
      errors.phone = "رقم الهاتف مطلوب";
    } else if (!/^\d{11}$/.test(editForm.phone)) {
      errors.phone = "رقم الهاتف يجب أن يكون 11 رقم";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  }, [editForm]);

  const handleCreateBranch = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateCreateForm()) {
      return;
    }

    try {
      const requestData: CreateBranchRequest = {
        name: createForm.name,
        location: createForm.location,
        phone: createForm.phone,
      };

      await createMutation.mutateAsync(requestData);

      setCreateForm({ name: "", location: "", phone: "" });
      setShowCreateForm(false);
      setFormErrors({});
    } catch (error: any) {
      setFormErrors({
        submit:
          error.response?.data?.message ||
          error.message ||
          "فشل إنشاء الفرع. حاول مرة أخرى",
      });
    }
  };

  const handleEditBranch = async () => {
    if (!validateEditForm()) {
      return;
    }

    try {
      const updateData: UpdateBranchRequest = {};

      if (editForm.name) updateData.name = editForm.name;
      if (editForm.location) updateData.location = editForm.location;
      if (editForm.phone) updateData.phone = editForm.phone;

      if (editingId) {
        await updateMutation.mutateAsync({ id: editingId, data: updateData });

        setEditingId(null);
        setEditForm({
          id: 0,
          name: "",
          location: "",
          phone: "",
          isDeleted: false,
        });
        setFormErrors({});
      }
    } catch (error: any) {
      setFormErrors({
        submit:
          error.response?.data?.message ||
          error.message ||
          "فشل تحديث الفرع. حاول مرة أخرى",
      });
    }
  };

  const handleDeleteBranch = async (id: number) => {
    if (!confirm("هل متأكد من رغبتك في حذف هذا الفرع؟")) {
      return;
    }

    try {
      await deleteMutation.mutateAsync(id);
    } catch (error: any) {
      alert(
        error.response?.data?.message ||
          error.message ||
          "فشل حذف الفرع. حاول مرة أخرى",
      );
    }
  };

  const handleEditClick = (branch: BranchDTO) => {
    setEditingId(branch.id);
    setEditForm({
      id: branch.id,
      name: branch.name,
      location: branch.location,
      phone: branch.phone,
      isDeleted: branch.isDeleted || false,
    });
    setFormErrors({});
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setEditForm({
      id: 0,
      name: "",
      location: "",
      phone: "",
      isDeleted: false,
    });
    setFormErrors({});
  };

  const handleCancelCreate = () => {
    setShowCreateForm(false);
    setCreateForm({ name: "", location: "", phone: "" });
    setFormErrors({});
  };

  const getStatusBadge = (isDeleted: boolean) => {
    if (isDeleted) {
      return (
        <span className="px-3 py-1 rounded-full text-label-sm font-label-md bg-error text-on-error">
          معطل
        </span>
      );
    }
    return (
      <span className="px-3 py-1 rounded-full text-label-sm font-label-md bg-green-100 text-green-700 inline-flex items-center gap-1">
        <span className="w-1.5 h-1.5 rounded-full bg-green-600"></span>
        نشط
      </span>
    );
  };

  const isSearchingOrLoading = isLoading || isSearching;

  const handleResetFilters = () => {
    setNameSearch("");
    setLocationSearch("");
    setPhoneSearch("");
    setDeletedFilter(false);
    setPage(0);
  };

  return (
    <main className="flex-1 p-6 lg:p-10 flex flex-col gap-8">
      {/* Page Header */}
      <Header
        title="إدارة الفروع"
        subtitle="إنشاء وتحديث وحذف فروع ومراكز التوزيع"
        actions={
          <Button
            variant="primary"
            size="sm"
            onClick={() => {
              setShowCreateForm(!showCreateForm);
              setEditingId(null);
              setCreateForm({ name: "", location: "", phone: "" });
              setFormErrors({});
            }}
          >
            {showCreateForm ? "إلغاء" : "+ فرع جديد"}
          </Button>
        }
      />

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="p-6 shadow-sm border border-outline-variant">
          <div className="flex justify-between items-start">
            <div className="p-2 bg-primary/10 rounded-lg text-primary">
              <svg
                className="w-6 h-6"
                fill="currentColor"
                viewBox="0 0 24 24"
              >
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.66 1.34 3 3 3s3-1.34 3-3v-1l5.21-2.08c.13.58.21 1.19.21 1.84 0 4.08-3.05 7.44-7 7.93z" />
              </svg>
            </div>
          </div>
          <div className="mt-4">
            <div className="font-headline-xl text-headline-xl text-primary">
              {totalCount}
            </div>
            <div className="font-label-md text-label-md text-on-surface-variant">
              إجمالي الفروع
            </div>
          </div>
        </Card>

        <Card className="p-6 shadow-sm border border-outline-variant">
          <div className="flex justify-between items-start">
            <div className="p-2 bg-green-100 rounded-lg text-green-700">
              <svg
                className="w-6 h-6"
                fill="currentColor"
                viewBox="0 0 24 24"
              >
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z" />
              </svg>
            </div>
          </div>
          <div className="mt-4">
            <div className="font-headline-xl text-headline-xl text-primary">
              {branches.filter((b) => !b.isDeleted).length}
            </div>
            <div className="font-label-md text-label-md text-on-surface-variant">
              فروع نشطة حالياً
            </div>
          </div>
        </Card>

        <Card className="p-6 shadow-sm border border-outline-variant">
          <div className="flex justify-between items-start">
            <div className="p-2 bg-error-container rounded-lg text-error">
              <svg
                className="w-6 h-6"
                fill="currentColor"
                viewBox="0 0 24 24"
              >
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z" />
              </svg>
            </div>
          </div>
          <div className="mt-4">
            <div className="font-headline-xl text-headline-xl text-primary">
              {branches.filter((b) => b.isDeleted).length}
            </div>
            <div className="font-label-md text-label-md text-on-surface-variant">
              فروع معطلة
            </div>
          </div>
        </Card>
      </div>

      {/* Create Branch Form */}
      {showCreateForm && (
        <Card className="bg-surface-container-low border-2 border-secondary-container/20">
          <h3 className="font-headline-md text-headline-md text-on-background mb-6">
            إنشاء فرع جديد
          </h3>

          <form onSubmit={handleCreateBranch} className="space-y-4">
            <Input
              label="اسم الفرع"
              type="text"
              placeholder="مثال: فرع القاهرة الرئيسي"
              value={createForm.name}
              onChange={(e) => {
                setCreateForm({ ...createForm, name: e.target.value });
                if (formErrors.name) {
                  setFormErrors({ ...formErrors, name: "" });
                }
              }}
              error={formErrors.name}
              required
            />

            <Input
              label="الموقع"
              type="text"
              placeholder="مثال: مدينة نصر، القاهرة"
              value={createForm.location}
              onChange={(e) => {
                setCreateForm({
                  ...createForm,
                  location: e.target.value,
                });
                if (formErrors.location) {
                  setFormErrors({ ...formErrors, location: "" });
                }
              }}
              error={formErrors.location}
              required
            />

            <Input
              label="رقم الهاتف"
              type="tel"
              placeholder="01012345678"
              value={createForm.phone}
              onChange={(e) => {
                setCreateForm({ ...createForm, phone: e.target.value });
                if (formErrors.phone) {
                  setFormErrors({ ...formErrors, phone: "" });
                }
              }}
              error={formErrors.phone}
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
                {createMutation.isPending ? "جاري الإنشاء..." : "إنشاء الفرع"}
              </Button>
              <Button
                type="button"
                variant="outline"
                size="md"
                onClick={handleCancelCreate}
              >
                إلغاء
              </Button>
            </div>
          </form>
        </Card>
      )}

      {/* Filters Section */}
      <Card>
        <div className="mb-6">
          <h3 className="font-headline-md text-headline-md text-on-background mb-1">
            الفروع
          </h3>
          <p className="text-body-sm text-on-surface-variant">
            عدد الفروع: {totalCount}
          </p>
        </div>

        <div className="mb-6 space-y-4 p-4 bg-surface-container-low rounded-lg">
          <div className="grid grid-cols-1 md:grid-cols-12 gap-4">
            <div className="col-span-1 md:col-span-4">
              <Input
                label="البحث حسب اسم الفرع"
                type="text"
                placeholder="ابحث باسم الفرع..."
                value={nameSearch}
                onChange={(e) => {
                  setNameSearch(e.target.value);
                  setPage(0);
                }}
              />
            </div>

            <div className="col-span-1 md:col-span-4">
              <Input
                label="البحث حسب الموقع"
                type="text"
                placeholder="ابحث بالموقع..."
                value={locationSearch}
                onChange={(e) => {
                  setLocationSearch(e.target.value);
                  setPage(0);
                }}
              />
            </div>

            <div className="col-span-1 md:col-span-4">
              <Input
                label="البحث حسب رقم الهاتف"
                type="tel"
                placeholder="رقم الهاتف..."
                value={phoneSearch}
                onChange={(e) => {
                  setPhoneSearch(e.target.value);
                  setPage(0);
                }}
              />
            </div>

            <div className="col-span-1 md:col-span-2 flex items-center">
              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={deletedFilter}
                  onChange={(e) => {
                    setDeletedFilter(e.target.checked);
                    setPage(0);
                  }}
                  className="w-5 h-5 rounded border-outline-variant text-secondary focus:ring-secondary"
                />
                <span className="text-body-sm text-on-surface-variant">
                  تضمين المحذوف
                </span>
              </label>
            </div>

            <div className="col-span-1 md:col-span-2 flex items-end">
              <Button
                variant="outline"
                size="md"
                fullWidth
                onClick={handleResetFilters}
              >
                إعادة تعيين الفلاتر
              </Button>
            </div>
          </div>
        </div>
      </Card>

      {/* Branches Table */}
      <Card>
        {/* Loading State */}
        {isSearchingOrLoading && (
          <div className="flex justify-center items-center py-12">
            <LoadingSpinner />
          </div>
        )}

        {/* Error State */}
        {(fetchError || searchError) && !isSearchingOrLoading && (
          <div className="p-4 bg-error/10 border border-error rounded-lg mb-4">
            <p className="text-error text-body-md">
              خطأ في تحميل الفروع. يرجى المحاولة مرة أخرى.
            </p>
          </div>
        )}

        {/* Empty State */}
        {
          !isSearchingOrLoading &&
            branches.length === 0 &&
            !fetchError &&
            !searchError && (
              <div className="text-center py-12">
                <p className="text-on-surface-variant text-body-md mb-4">
                  لا توجد فروع حالياً
                </p>
                <Button
                  variant="outline"
                  size="md"
                  onClick={() => setShowCreateForm(true)}
                >
                  إنشاء فرع الآن
                </Button>
              </div>
            )
        }

        {/* Branches Table */}
        {!isSearchingOrLoading && branches.length > 0 && (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-outline-variant">
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    اسم الفرع
                  </th>
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    الموقع
                  </th>
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    رقم الهاتف
                  </th>
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    الحالة
                  </th>
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    الإجراءات
                  </th>
                </tr>
              </thead>
              <tbody>
                {branches.map((branch) => (
                  <tr
                    key={branch.id}
                    className="border-b border-surface-variant hover:bg-surface-container-low transition"
                  >
                    <td className="p-4">
                      {editingId === branch.id ? (
                        <Input
                          type="text"
                          value={editForm.name}
                          onChange={(e) =>
                            setEditForm({
                              ...editForm,
                              name: e.target.value,
                            })
                          }
                          className="text-body-sm"
                        />
                      ) : (
                        <p className="text-body-md text-on-surface font-headline-md">
                          {branch.name}
                        </p>
                      )}
                    </td>

                    <td className="p-4">
                      {editingId === branch.id ? (
                        <Input
                          type="text"
                          value={editForm.location}
                          onChange={(e) =>
                            setEditForm({
                              ...editForm,
                              location: e.target.value,
                            })
                          }
                          className="text-body-sm"
                        />
                      ) : (
                        <p className="text-body-sm text-on-surface-variant">
                          {branch.location}
                        </p>
                      )}
                    </td>

                    <td className="p-4">
                      {editingId === branch.id ? (
                        <Input
                          type="tel"
                          value={editForm.phone}
                          onChange={(e) =>
                            setEditForm({
                              ...editForm,
                              phone: e.target.value,
                            })
                          }
                          className="text-body-sm"
                        />
                      ) : (
                        <span
                          className="px-3 py-1 rounded-full text-label-sm font-label-md bg-secondary-container text-on-primary"
                          dir="ltr"
                        >
                          {branch.phone}
                        </span>
                      )}
                    </td>

                    <td className="p-4">
                      {getStatusBadge(branch.isDeleted || false)}
                    </td>

                    <td className="p-4">
                      <div className="flex gap-2">
                        {editingId === branch.id ? (
                          <>
                            <Button
                              variant="primary"
                              size="sm"
                              onClick={handleEditBranch}
                              disabled={updateMutation.isPending}
                              isLoading={updateMutation.isPending}
                            >
                              حفظ
                            </Button>
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={handleCancelEdit}
                            >
                              إلغاء
                            </Button>
                          </>
                        ) : (
                          <>
                            <Button
                              variant="secondary"
                              size="sm"
                              onClick={() => handleEditClick(branch)}
                            >
                              تعديل
                            </Button>
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => handleDeleteBranch(branch.id)}
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

            {/* Pagination */}
            {totalPages > 1 && (
              <div className="p-4 flex justify-between items-center border-t border-outline-variant">
                <div className="text-body-sm text-on-surface-variant">
                  عرض {page * size + 1} إلى{" "}
                  {Math.min((page + 1) * size, totalCount)} من إجمالي {totalCount}{" "}
                  فرع
                </div>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setPage((p) => Math.max(0, p - 1))}
                    disabled={page === 0}
                  >
                    السابق
                  </Button>
                  <span className="px-4 py-2 text-body-sm bg-secondary text-on-secondary rounded-lg">
                    {page + 1}
                  </span>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setPage((p) => p + 1)}
                    disabled={page >= totalPages - 1}
                  >
                    التالي
                  </Button>
                </div>
              </div>
            )}
          </div>
        )}
      </Card>

      {/* Error Display */}
      {formErrors.submit && (
        <div className="p-3 bg-error/10 border border-error rounded-lg">
          <p className="text-error text-body-sm">{formErrors.submit}</p>
        </div>
      )}
    </main>
  );
};
