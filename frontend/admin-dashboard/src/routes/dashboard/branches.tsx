import { createFileRoute } from '@tanstack/react-router'
import { BranchesPage } from "@/pages/BranchesPage";

export const Route = createFileRoute('/dashboard/branches')({
  component: () => <BranchesPage />,
});

// function RouteComponent() {
//   return <div>Hello "/dashboard/branches"!</div>
// }
