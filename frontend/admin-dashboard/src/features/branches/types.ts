export interface CreateBranchRequest {
  name: string;
  location: string;
  phone: string;
}

export interface UpdateBranchRequest {
  name?: string;
  location?: string;
  phone?: string;
}

export interface BranchDTO {
  id: number;
  name: string;
  location: string;
  phone: string;
  isDeleted?: boolean;
}

export interface BranchResponse {
  content: BranchDTO[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ListBranchesParams {
  page?: number;
  size?: number;
}

export interface SearchBranchParams {
  name?: string;
  location?: string;
  phone?: string;
  isDeleted?: boolean;
  page?: number;
  size?: number;
}
