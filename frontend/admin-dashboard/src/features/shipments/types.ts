export enum ShipmentStatus {
  PENDING = "PENDING",
  IN_TRANSIT = "IN_TRANSIT",
  DELIVERED = "DELIVERED",
  CANCELLED = "CANCELLED",
  RETURNED = "RETURNED",
}

export interface ShipmentDTO {
  id?: number;
  pickupRequestId?: number;
  status?: ShipmentStatus;
  branchId?: number;
  courierId?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface ShipmentFilter {
  status?: ShipmentStatus;
  pickupRequestId?: number;
  branchId?: number;
  page?: number;
  size?: number;
}

export interface ShipmentPageResponse {
  content: ShipmentDTO[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
