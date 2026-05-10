import type { ListProfilesParams, SearchProfilesParams } from "./types";

export const profileQueryKeys = {
  all: ["profiles"] as const,
  list: (params: ListProfilesParams) =>
    [...profileQueryKeys.all, "list", params] as const,
  detail: (id: number) => [...profileQueryKeys.all, "detail", id] as const,
  byAccount: (accountId: string) =>
    [...profileQueryKeys.all, "byAccount", accountId] as const,
  search: (params: SearchProfilesParams) =>
    [...profileQueryKeys.all, "search", params] as const,
  create: () => [...profileQueryKeys.all, "create"] as const,
  update: (id: number) => [...profileQueryKeys.all, "update", id] as const,
};
