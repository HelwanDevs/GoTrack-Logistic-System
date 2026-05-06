import { UserRole } from "@/types/enums";


export interface UpdateAccountRequest {
  email?: string;
  password?: string;
  role?: UserRole;
}

export interface AccountResponse {
  id: string;
  email: string;
  role: UserRole;
  createdAt?: string;
  updatedAt?: string;
}

export interface Account extends AccountResponse {}