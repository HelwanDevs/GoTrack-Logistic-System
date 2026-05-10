import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  getMyNotificationsApi,
  markNotificationAsReadApi,
  markNotificationAsUnreadApi,
  createAdminNotificationApi,
} from "./api";
import { notificationQueryKeys } from "./query-keys";

export const useMyNotificationsQuery = (readStatus?: boolean) => {
  return useQuery({
    queryKey: notificationQueryKeys.myNotifications(readStatus),
    queryFn: () => getMyNotificationsApi(readStatus),
    staleTime: 30 * 1000,
    refetchInterval: 60 * 1000,
  });
};

export const useMarkAsReadMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: markNotificationAsReadApi,
    onSuccess: (_, notificationId) => {
      queryClient.invalidateQueries({
        queryKey: notificationQueryKeys.detail(notificationId),
      });
      queryClient.invalidateQueries({
        queryKey: notificationQueryKeys.all,
      });
    },
  });
};

export const useMarkAsUnreadMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: markNotificationAsUnreadApi,
    onSuccess: (_, notificationId) => {
      queryClient.invalidateQueries({
        queryKey: notificationQueryKeys.detail(notificationId),
      });
      queryClient.invalidateQueries({
        queryKey: notificationQueryKeys.all,
      });
    },
  });
};

export const useCreateAdminNotificationMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createAdminNotificationApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: notificationQueryKeys.all });
    },
  });
};
