import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  createShipmentApi,
  updateShipmentStatusApi,
  getMyShipmentsApi,
  searchShipmentsApi,
} from "./api";
import { shipmentQueryKeys } from "./query-keys";
import type { ShipmentFilter } from "./types";

export const useMyShipmentsQuery = (page?: number, size?: number) => {
  return useQuery({
    queryKey: shipmentQueryKeys.myShipments(page, size),
    queryFn: () => getMyShipmentsApi(page, size),
    staleTime: 30 * 1000,
  });
};

export const useSearchShipmentsQuery = (params: ShipmentFilter) => {
  return useQuery({
    queryKey: shipmentQueryKeys.search(params),
    queryFn: () => searchShipmentsApi(params),
    staleTime: 30 * 1000,
    enabled:
      Boolean(params.status) ||
      params.pickupRequestId !== undefined ||
      params.branchId !== undefined,
  });
};

export const useCreateShipmentMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createShipmentApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: shipmentQueryKeys.all });
    },
  });
};

export const useUpdateShipmentStatusMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      id,
      data,
    }: {
      id: number;
      data: ShipmentDTO;
    }) => updateShipmentStatusApi(id, data),
    onSuccess: (_, { id }) => {
      queryClient.invalidateQueries({
        queryKey: shipmentQueryKeys.detail(id),
      });
      queryClient.invalidateQueries({ queryKey: shipmentQueryKeys.all });
    },
  });
};
