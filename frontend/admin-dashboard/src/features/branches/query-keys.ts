import type { ListBranchesParams, SearchBranchParams } from "./types";

export const branchQueryKeys = {
  all: ["branches"] as const,
  list: (params: ListBranchesParams) =>
    [...branchQueryKeys.all, "list", params] as const,
  detail: (id: number) => [...branchQueryKeys.all, "detail", id] as const,
  search: (params: SearchBranchParams) =>
    [...branchQueryKeys.all, "search", params] as const,
  create: () => [...branchQueryKeys.all, "create"] as const,
  update: (id: number) => [...branchQueryKeys.all, "update", id] as const,
  delete: (id: number) => [...branchQueryKeys.all, "delete", id] as const,
};
