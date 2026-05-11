export enum ComplaintStatus {
  OPEN = "OPEN",
  IN_PROGRESS = "IN_PROGRESS",
  RESOLVED = "RESOLVED",
  REJECTED = "REJECTED",
  CLOSED = "CLOSED",
}

export enum ComplaintType {
  SHIPMENT_ISSUE = "SHIPMENT_ISSUE",
  PRODUCT_ISSUE = "PRODUCT_ISSUE",
  SERVICE_ISSUE = "SERVICE_ISSUE",
  OTHER = "OTHER",
}

export interface CreateComplaintRequest {
  type: ComplaintType;
  subject: string;
  description: string;
  shipmentId?: number;
}

export interface UpdateComplaintStatusRequest {
  status: ComplaintStatus;
  note?: string;
}

export interface ComplaintResponse {
  id: string;
  type: ComplaintType;
  subject: string;
  description: string;
  status: ComplaintStatus;
  shipmentId?: number;
  createdAt?: string;
  updatedAt?: string;
  createdBy?: string;
}

export interface SearchComplaintParams {
  subject?: string;
  status?: ComplaintStatus;
  shipmentId?: number;
}
