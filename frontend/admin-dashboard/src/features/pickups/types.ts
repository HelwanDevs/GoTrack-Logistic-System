export enum PickupStatus {
  PENDING = "PENDING",
  ASSIGNED = "ASSIGNED",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED",
}

export interface PickupRequestDTO {
  id?: number;
  merchantProfileId: number;
  branchId: number;
  status?: PickupStatus;
  courierProfileId?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface PickupFilter {
  status?: PickupStatus;
  merchantProfileId?: number;
  courierProfileId?: number;
  branchId?: number;
  page?: number;
  size?: number;
}

export interface PickupPageResponse {
  content: PickupRequestDTO[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
