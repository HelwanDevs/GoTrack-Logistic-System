export type {
  ProductDTO,
  UpdateProductRequest,
  ProductPageResponse,
  InventoryItemRequest,
  InventoryItemPageResponse,
  InventorySearchParams,
  InventoryStatus,
} from "./types";

export {
  createProductApi,
  updateProductApi,
  getProductsByMerchantApi,
  getMyProductsApi,
  receiveInventoryItemsApi,
  listInventoryItemsApi,
  searchInventoryItemsApi,
} from "./api";

export {
  useMyProductsQuery,
  useProductsByMerchantQuery,
  useInventoryItemsQuery,
  useSearchInventoryItemsQuery,
  useCreateProductMutation,
  useUpdateProductMutation,
  useReceiveItemsMutation,
} from "./hooks";

export { inventoryQueryKeys } from "./query-keys";
