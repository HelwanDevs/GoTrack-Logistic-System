import { ProfileType, ProfileStatus } from "../../types/enums";

export interface CreateProfileRequest {
  fullName: string;
  phoneNumber: string;
  type: ProfileType;
  accountId?: string;
  branchId?: number;
}

export interface UpdateProfileRequest {
  fullName?: string;
  phoneNumber?: string;
  type?: ProfileType;
  status?: ProfileStatus;
  branchId?: number;
  accountId?: string;
}

export interface ProfileResponseDTO {
  id: number;
  fullName: string;
  phoneNumber: string;
  type: ProfileType;
  accountId?: string;
  status: ProfileStatus;
  branchId?: number;
  createdBy?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface ProfilePageResponse {
  content: ProfileResponseDTO[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ListProfilesParams {
  page?: number;
  size?: number;
  sortBy?: string;
  type?: ProfileType;
  status?: ProfileStatus;
  branchId?: number;
}

export interface SearchProfilesParams {
  name?: string;
  phoneNumber?: string;
  type?: ProfileType;
  branchId?: number;
  status?: ProfileStatus;
  page?: number;
  size?: number;
  sortBy?: string;
}