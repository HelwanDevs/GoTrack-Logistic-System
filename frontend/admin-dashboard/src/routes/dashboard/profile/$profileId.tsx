import { createFileRoute } from "@tanstack/react-router";
import { ProfileDetailPage } from "@/pages/ProfileDetailPage";

export const Route = createFileRoute("/dashboard/profile/$profileId")({
  component: ProfileDetailPage,
});
