export const notificationQueryKeys = {
  all: ["notifications"] as const,
  myNotifications: (readStatus?: boolean) =>
    [...notificationQueryKeys.all, "me", readStatus] as const,
  detail: (id: string) =>
    [...notificationQueryKeys.all, "detail", id] as const,
  markRead: (id: string) =>
    [...notificationQueryKeys.all, "read", id] as const,
  markUnread: (id: string) =>
    [...notificationQueryKeys.all, "unread", id] as const,
  adminCreate: () => [...notificationQueryKeys.all, "admin"] as const,
};
