export const accountQueryKeys = {
  all: ["accounts"] as const,
  list: () => [...accountQueryKeys.all, "list"] as const,
  detail: (id: string) => [...accountQueryKeys.all, "detail", id] as const,
  create: () => [...accountQueryKeys.all, "create"] as const,
  update: (id: string) => [...accountQueryKeys.all, "update", id] as const,
  delete: (id: string) => [...accountQueryKeys.all, "delete", id] as const,
};
