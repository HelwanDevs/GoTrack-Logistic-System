export enum InventoryStatus {
  IN_STOCK = "IN_STOCK",
  RESERVED = "RESERVED",
  SHIPPED = "SHIPPED",
}

export interface ProductDTO {
  name: string;
  merchantId: number;
  baseSku: string;
}

export interface UpdateProductRequest {
  id?: number;
  name?: string;
  merchantId?: number;
}

export interface ProductPageResponse {
  content: {
    id: number;
    name: string;
    merchantId: number;
    baseSku: string;
  }[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface InventoryItemRequest {
  productId: number;
  branchId: number;
  pickupRequestId: number;
  uniqueSkus: string[];
}

export interface InventoryItemPageResponse {
  content: {
    id: number;
    productId: number;
    productName: string;
    branchId: number;
    uniqueSku: string;
    status: InventoryStatus;
    MerchantId: number;
  }[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface InventorySearchParams {
  branchId?: number;
  uniqueSku?: string;
  MerchantId?: number;
  page?: number;
  size?: number;
}
