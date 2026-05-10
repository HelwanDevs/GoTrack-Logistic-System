export enum NotificationChannel {
  IN_APP = "IN_APP",
  EMAIL = "EMAIL",
  SMS = "SMS",
}

export enum NotificationType {
  SYSTEM = "SYSTEM",
  COMPLIANT = "COMPLIANT",
  TRANSACTION = "TRANSACTION",
  SHIPMENT = "SHIPMENT",
  PICKUP = "PICKUP",
}

export interface Notifications {
  id: string;
  type: NotificationType;
  title?: string;
  message: string;
  channel: NotificationChannel;
  read: boolean;
  profileId?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface AdminNotificationRequest {
  profileId: string;
  message: string;
  channel?: NotificationChannel;
  email?: string;
}
