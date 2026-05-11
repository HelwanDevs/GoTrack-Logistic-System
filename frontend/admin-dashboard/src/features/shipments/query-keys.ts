import type { ShipmentFilter } from "./types";

export const shipmentQueryKeys = {
  all: ["shipments"] as const,
  myShipments: (page?: number, size?: number) =>
    [...shipmentQueryKeys.all, "myShipments", page, size] as const,
  detail: (id: number) => [...shipmentQueryKeys.all, "detail", id] as const,
  search: (params: ShipmentFilter) =>
    [...shipmentQueryKeys.all, "search", params] as const,
  create: () => [...shipmentQueryKeys.all, "create"] as const,
  updateStatus: (id: number) =>
    [...shipmentQueryKeys.all, "status", id] as const,
};
