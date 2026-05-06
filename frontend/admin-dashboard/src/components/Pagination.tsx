import { Select } from "@/components/Select";
import { Button } from "@/components/Button";

interface PaginationProps {
  page: number;
  setPage: (page: number) => void;
  size: number;
  setSize: (size: number) => void;
  totalCount: number;
  isLoading?: boolean;
  totalPages?: number;
}

export const Pagination = ({
  page,
  setPage,
  size,
  setSize,
  totalCount,
  isLoading = false,
  totalPages,
}: PaginationProps) => {
  const displayTotalPages = totalPages != null ? Math.max(1, totalPages) : Math.max(1, Math.ceil(totalCount / size));

  return (
    <div className="mt-6 grid grid-cols-12 items-center justify-between p-4 bg-surface-container-low rounded-lg">
      <div className="col-span-3 text-body-sm text-on-surface-variant">
        عرض {size > totalCount ? totalCount : size} من {totalCount}
        {displayTotalPages > 1 && ` (الصفحة ${page + 1} من ${displayTotalPages})`}
      </div>

      <div className="flex col-span-6 gap-2 justify-center">
        <Button
          variant="outline"
          size="sm"
          disabled={page === 0 || isLoading}
          onClick={() => setPage(Math.max(0, page - 1))}
        >
          السابق
        </Button>

        <div className="flex items-center gap-2 px-4 py-2 bg-surface-container rounded-lg">
          <span className="text-body-sm text-on-surface">
            {page + 1} / {displayTotalPages}
          </span>
        </div>

        <Button
          variant="outline"
          size="sm"
          disabled={page >= displayTotalPages - 1 || isLoading}
          onClick={() => setPage(page + 1)}
        >
          التالي
        </Button>
      </div>

      <div className="col-span-3">
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
  );
};
