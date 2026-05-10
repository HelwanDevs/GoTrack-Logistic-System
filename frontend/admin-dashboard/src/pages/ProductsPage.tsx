import { useState } from "react";
import { Header } from "@/components/Header";
import { Button } from "@/components/Button";
import { Card } from "@/components/Card";
import { Input } from "@/components/Input";
import { Select } from "@/components/Select";
import { LoadingSpinner } from "@/components/LoadingSpinner";
import { Pagination } from "@/components/Pagination";
import {
  useMyProductsQuery,
  useProductsByMerchantQuery,
  useCreateProductMutation,
  useUpdateProductMutation,
  type ProductDTO,
  type UpdateProductRequest,
  type ProductPageResponse,
} from "@/features/inventory";
import {
  useProfilesQuery,
  type ProfileResponseDTO,
} from "@/features/profiles";
import { ProfileType } from "@/types/enums";
   

interface Product {
  id: number;
  name: string;
  merchantId: number;
  merchantName: string;
  baseSku: string;
  createdAt?: string;
  updatedAt?: string;
}

interface EditingProduct {
  id: number;
  name: string;
  baseSku: string;
}

interface CreateProductForm {
  name: string;
  merchantId: string;
  baseSku: string;
}

export const ProductsPage = () => {
  // ── UI State ──
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [filterMerchantId, setFilterMerchantId] = useState<number | undefined>(undefined);

  // ── Edit Form State ──
  const [editForm, setEditForm] = useState<EditingProduct>({
    id: 0,
    name: "",
    baseSku: "",
  });

  // ── Pagination ──
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  // ── Filters ──
  const [nameSearch, setNameSearch] = useState("");
  const [skuSearch, setSkuSearch] = useState("");

  // ── Create Form State ──
  const [createForm, setCreateForm] = useState<CreateProductForm>({
    name: "",
    merchantId: "",
    baseSku: "",
  });

  // ── API Hooks ──
  const {
    data: productsData,
    isLoading: productsLoading,
    error: productsError,
  } = useMyProductsQuery(page, pageSize);

  const {
    data: merchantProductsData,
    isLoading: merchantProductsLoading,
  } = useProductsByMerchantQuery(
    filterMerchantId ?? 1,
    page,
    pageSize,
  );

  const createMutation = useCreateProductMutation();
  const updateMutation = useUpdateProductMutation();

  // ── Fetch Merchants ──
  const { data: merchantsData, isLoading: merchantsLoading } = useProfilesQuery({
    page: 0,
    size: 100,
    sortBy: "fullName",
    type: ProfileType.MERCHANT,
  });

  const merchants: { id: number; name: string }[] =
    merchantsData?.content?.map((p: ProfileResponseDTO) => ({
      id: p.id,
      name: p.fullName,
    })) || [];

  // ── Derived Data ──
  const isFilterActive = Boolean(filterMerchantId || nameSearch || skuSearch);

  const products = (isFilterActive
    ? merchantProductsData?.data?.content
    : productsData?.data?.content
  )?.map((product: ProductPageResponse["content"][0]) => ({
    id: product.id,
    name: product.name,
    merchantId: product.merchantId,
    merchantName: getMerchantName(product.merchantId),
    baseSku: product.baseSku,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
  })) || [];

  const totalCount = isFilterActive
    ? merchantProductsData?.data?.totalElements
    : productsData?.data?.totalElements;
  const totalPages = isFilterActive
    ? merchantProductsData?.data?.totalPages
    : productsData?.data?.totalPages;

  const getMerchantName = (merchantId: number) => {
    const merchant = merchants.find((m) => m.id === merchantId);
    return merchant?.name || "تاجر";
  };

  // ── Handlers ──
  const handleCreateProduct = async () => {
    const errors: Record<string, string> = {};

    if (!createForm.name.trim()) {
      errors.name = "اسم المنتج مطلوب";
    }
    if (!createForm.merchantId) {
      errors.merchantId = "التاجر مطلوب";
    }
    if (!createForm.baseSku.trim()) {
      errors.baseSku = "رمز SKU مطلوب";
    }

    if (Object.keys(errors).length > 0) {
      return;
    }

    try {
      await createMutation.mutateAsync({
        name: createForm.name,
        merchantId: Number(createForm.merchantId),
        baseSku: createForm.baseSku,
      } as ProductDTO);

      setCreateForm({ name: "", merchantId: "", baseSku: "" });
      setShowCreateForm(false);
    } catch (error: any) {
      console.error("Failed to create product:", error);
    }
  };

  const handleEditClick = (product: Product) => {
    setEditingId(product.id);
    setEditForm({
      id: product.id,
      name: product.name,
      baseSku: product.baseSku,
    });
  };

  const handleSaveEdit = async () => {
    const errors: Record<string, string> = {};

    if (!editForm.name.trim()) {
      errors.name = "اسم المنتج مطلوب";
    }
    if (!editForm.baseSku.trim()) {
      errors.baseSku = "رمز SKU مطلوب";
    }

    if (Object.keys(errors).length > 0) {
      return;
    }

    try {
      if (editingId) {
        await updateMutation.mutateAsync({
          id: editingId,
          data: {
            name: editForm.name,
            baseSku: editForm.baseSku,
          } as UpdateProductRequest,
        });

        setEditingId(null);
        setEditForm({ id: 0, name: "", baseSku: "" });
      }
    } catch (error: any) {
      console.error("Failed to update product:", error);
    }
  };

  const handleCancelEdit = () => {
    setEditingId(null);
    setEditForm({ id: 0, name: "", baseSku: "" });
  };

  const handleResetFilters = () => {
    setNameSearch("");
    setSkuSearch("");
    setFilterMerchantId(undefined);
    setPage(0);
  };

  const isSearching = productsLoading || merchantProductsLoading || merchantsLoading;

  return (
    <main className="flex-1 p-6 lg:p-10 flex flex-col gap-8">
      {/* Page Header */}
      <Header
        title="إدارة المنتجات"
        subtitle="إنشاء وتحديث وإدارة منتجات التجار"
        actions={
          <Button
            variant="primary"
            size="sm"
            onClick={() => {
              setShowCreateForm(!showCreateForm);
              setCreateForm({ name: "", merchantId: "", baseSku: "" });
            }}
          >
            {showCreateForm ? "إلغاء" : "+ منتج جديد"}
          </Button>
        }
      />

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="p-6 shadow-sm border border-outline-variant">
          <div>
            <div className="font-headline-xl text-headline-xl text-primary">
              {totalCount || 0}
            </div>
            <div className="font-label-md text-label-md text-on-surface-variant">
              إجمالي المنتجات
            </div>
          </div>
        </Card>

        <Card className="p-6 shadow-sm border border-outline-variant">
          <div>
            <div className="font-headline-xl text-headline-xl text-primary">
              {merchants.length || 0}
            </div>
            <div className="font-label-md text-label-md text-on-surface-variant">
              التجار النشطين
            </div>
          </div>
        </Card>

        <Card className="p-6 shadow-sm border border-outline-variant">
          <div>
            <div className="font-headline-xl text-headline-xl text-primary">
              {new Set(products.map((p) => p.merchantId)).size || 0}
            </div>
            <div className="font-label-md text-label-md text-on-surface-variant">
              منتجات متنوعة
            </div>
          </div>
        </Card>
      </div>

      {/* Filters Section */}
      <Card>
        <div className="mb-6">
          <h3 className="font-headline-md text-headline-md text-on-background mb-1">
            تصفية المنتجات
          </h3>
          <p className="text-body-sm text-on-surface-variant">
            عدد المنتجات: {totalCount || 0}
          </p>
        </div>

        <div className="mb-6 space-y-4 p-4 bg-surface-container-low rounded-lg">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div>
              <Select
                label="التاجر"
                value={filterMerchantId ? String(filterMerchantId) : ""}
                onChange={(e) => {
                  setFilterMerchantId(e.target.value ? Number(e.target.value) : undefined);
                  setPage(0);
                }}
                options={[
                  { value: "", label: "الكل" },
                  ...merchants.map((m) => ({
                    value: String(m.id),
                    label: m.name,
                  })),
                ]}
              />
            </div>

            <div>
              <Input
                label="البحث بالاسم"
                type="text"
                placeholder="ابحث باسم المنتج..."
                value={nameSearch}
                onChange={(e) => setNameSearch(e.target.value)}
              />
            </div>

            <div>
              <Input
                label="البحث برمز SKU"
                type="text"
                placeholder="ابحث برمز SKU..."
                value={skuSearch}
                onChange={(e) => setSkuSearch(e.target.value)}
              />
            </div>

            <div className="flex items-end">
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

      {/* Create Product Form */}
      {showCreateForm && (
        <Card className="bg-surface-container-low border-2 border-secondary-container/20">
          <h3 className="font-headline-md text-headline-md text-on-background mb-6">
            إنشاء منتج جديد
          </h3>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
            <Input
              label="اسم المنتج"
              type="text"
              placeholder="مثال:无线鼠标"
              value={createForm.name}
              onChange={(e) =>
                setCreateForm({ ...createForm, name: e.target.value })
              }
            />

            <Select
              label="التاجر"
              value={createForm.merchantId}
              onChange={(e) =>
                setCreateForm({ ...createForm, merchantId: e.target.value })
              }
              options={[
                { value: "", label: "اختر التاجر" },
                ...merchants.map((m) => ({
                  value: String(m.id),
                  label: m.name,
                })),
              ]}
            />

            <Input
              label="رمز SKU الأساسي"
              type="text"
              placeholder="WM-001"
              value={createForm.baseSku}
              onChange={(e) =>
                setCreateForm({ ...createForm, baseSku: e.target.value })
              }
            />
          </div>

          <div className="flex gap-3 pt-2">
            <Button
              variant="primary"
              size="md"
              onClick={handleCreateProduct}
              disabled={createMutation.isPending}
              isLoading={createMutation.isPending}
            >
              {createMutation.isPending ? "جاري الإنشاء..." : "إنشاء المنتج"}
            </Button>
            <Button
              variant="outline"
              size="md"
              onClick={() => {
                setShowCreateForm(false);
                setCreateForm({ name: "", merchantId: "", baseSku: "" });
              }}
            >
              إلغاء
            </Button>
          </div>
        </Card>
      )}

      {/* Products Table */}
      <Card>
        {/* Loading State */}
        {isSearching && (
          <div className="flex justify-center items-center py-12">
            <LoadingSpinner />
          </div>
        )}

        {/* Error State */}
        {productsError && !isSearching && (
          <div className="p-4 bg-error/10 border border-error rounded-lg mb-4">
            <p className="text-error text-body-md">
              خطأ في تحميل المنتجات. يرجى المحاولة مرة أخرى.
            </p>
          </div>
        )}

        {/* Empty State */}
        {!isSearching && products.length === 0 && !productsError && (
          <div className="text-center py-12">
            <p className="text-on-surface-variant text-body-md mb-4">
              لا توجد منتجات حالياً
            </p>
            <Button
              variant="outline"
              size="md"
              onClick={() => setShowCreateForm(true)}
            >
              إنشاء منتج الآن
            </Button>
          </div>
        )}

        {/* Products Table */}
        {!isSearching && products.length > 0 && (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-outline-variant">
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    اسم المنتج
                  </th>
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    رمز SKU
                  </th>
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    التاجر
                  </th>
                  <th className="text-right p-4 text-on-surface-variant font-label-md text-label-md">
                    الإجراءات
                  </th>
                </tr>
              </thead>
              <tbody>
                {products.map((product) => (
                  <tr
                    key={product.id}
                    className="border-b border-surface-variant hover:bg-surface-container-low transition"
                  >
                    <td className="p-4">
                      {editingId === product.id ? (
                        <Input
                          type="text"
                          value={editForm.name}
                          onChange={(e) =>
                            setEditForm({ ...editForm, name: e.target.value })
                          }
                          className="text-body-sm"
                        />
                      ) : (
                        <p className="text-body-md text-on-surface font-headline-md">
                          {product.name}
                        </p>
                      )}
                    </td>

                    <td className="p-4">
                      {editingId === product.id ? (
                        <Input
                          type="text"
                          value={editForm.baseSku}
                          onChange={(e) =>
                            setEditForm({ ...editForm, baseSku: e.target.value })
                          }
                          className="text-body-sm"
                        />
                      ) : (
                        <span className="px-3 py-1 rounded-full text-label-sm font-label-md bg-secondary-container text-on-primary">
                          {product.baseSku}
                        </span>
                      )}
                    </td>

                    <td className="p-4">
                      <span className="text-body-sm">{product.merchantName}</span>
                    </td>

                    <td className="p-4">
                      {editingId === product.id ? (
                        <div className="flex gap-2">
                          <Button
                            variant="primary"
                            size="sm"
                            onClick={handleSaveEdit}
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
                        </div>
                      ) : (
                        <Button
                          variant="secondary"
                          size="sm"
                          onClick={() => handleEditClick(product)}
                        >
                          تعديل
                        </Button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {/* Pagination */}
            {totalPages && totalPages > 1 && (
              <Pagination
                page={page}
                setPage={setPage}
                size={pageSize}
                setSize={setPageSize}
                totalCount={totalCount || 0}
                isLoading={isSearching}
                totalPages={totalPages}
              />
            )}
          </div>
        )}
      </Card>
    </main>
  );
};
