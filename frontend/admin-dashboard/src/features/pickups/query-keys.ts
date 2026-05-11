import type { PickupFilter } from "./types";

export const pickupQueryKeys = {
  all: ["pickups"] as const,
  list: (page?: number, size?: number) =>
    [...pickupQueryKeys.all, "list", page, size] as const,
  detail: (id: number) => [...pickupQueryKeys.all, "detail", id] as const,
  search: (params: PickupFilter) =>
    [...pickupQueryKeys.all, "search", params] as const,
  create: () => [...pickupQueryKeys.all, "create"] as const,
  update: (id: number) => [...pickupQueryKeys.all, "update", id] as const,
  assignCourier: (id: number) =>
    [...pickupQueryKeys.all, "assignCourier", id] as const,
};
