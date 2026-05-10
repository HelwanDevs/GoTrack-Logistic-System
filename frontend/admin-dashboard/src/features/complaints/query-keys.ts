import type { SearchComplaintParams } from "./types";

export const complaintQueryKeys = {
  all: ["complaints"] as const,
  myComplaints: () => [...complaintQueryKeys.all, "me"] as const,
  detail: (id: string) => [...complaintQueryKeys.all, "detail", id] as const,
  search: (params: SearchComplaintParams) =>
    [...complaintQueryKeys.all, "search", params] as const,
  create: () => [...complaintQueryKeys.all, "create"] as const,
  updateStatus: (id: string) =>
    [...complaintQueryKeys.all, "status", id] as const,
};
