export interface WalletDTO {
  id: number;
  accountId: string;
  balance: number;
  currency?: string;
  status?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface WalletPageResponse {
  content: WalletDTO[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
