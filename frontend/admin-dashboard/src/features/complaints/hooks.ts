import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  createComplaintApi,
  updateComplaintStatusApi,
  searchComplaintsApi,
  getMyComplaintsApi,
} from "./api";
import { complaintQueryKeys } from "./query-keys";
import type { SearchComplaintParams } from "./types";

export const useMyComplaintsQuery = () => {
  return useQuery({
    queryKey: complaintQueryKeys.myComplaints(),
    queryFn: getMyComplaintsApi,
    staleTime: 30 * 1000,
  });
};

export const useSearchComplaintsQuery = (params: SearchComplaintParams) => {
  return useQuery({
    queryKey: complaintQueryKeys.search(params),
    queryFn: () => searchComplaintsApi(params),
    staleTime: 30 * 1000,
    enabled:
      Boolean(params.subject) ||
      Boolean(params.status) ||
      params.shipmentId !== undefined,
  });
};

export const useCreateComplaintMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createComplaintApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: complaintQueryKeys.all });
    },
  });
};

export const useUpdateComplaintStatusMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      complaintId,
      data,
    }: {
      complaintId: string;
      data: UpdateComplaintStatusRequest;
    }) => updateComplaintStatusApi(complaintId, data),
    onSuccess: (_, { complaintId }) => {
      queryClient.invalidateQueries({
        queryKey: complaintQueryKeys.detail(complaintId),
      });
      queryClient.invalidateQueries({ queryKey: complaintQueryKeys.all });
    },
  });
};
