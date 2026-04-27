export const accountQueryKeys = {
  all: ["accounts"] as const,
  detail: (id: string) => [...accountQueryKeys.all, "detail", id] as const,
  update: (id: string) => [...accountQueryKeys.all, "update", id] as const,
};

