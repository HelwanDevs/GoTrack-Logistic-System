import type { InventorySearchParams } from "./types";

export const inventoryQueryKeys = {
  all: ["inventory"] as const,
  products: () => [...inventoryQueryKeys.all, "products"] as const,
  detail: (id: number) => [...inventoryQueryKeys.all, "products", id] as const,
  myProducts: (page?: number, size?: number) =>
    [...inventoryQueryKeys.all, "myProducts", page, size] as const,
  productsByMerchant: (merchantId: number, page?: number, size?: number) =>
    [
      ...inventoryQueryKeys.all,
      "products",
      "merchant",
      merchantId,
      page,
      size,
    ] as const,
  items: (page?: number, size?: number) =>
    [...inventoryQueryKeys.all, "items", page, size] as const,
  search: (params: InventorySearchParams) =>
    [...inventoryQueryKeys.all, "search", params] as const,
  receive: () => [...inventoryQueryKeys.all, "receive"] as const,
};
