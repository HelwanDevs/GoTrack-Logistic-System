# Admin Dashboard

A modern admin dashboard built with React, TanStack Query (v5), TanStack Router (v1), and Tailwind CSS.

## Features

- ✅ **Authentication System** - Login page with protected routes
- ✅ **TanStack Query** - Server state management with fake API functions
- ✅ **TanStack Router** - Type-safe routing with route protection
- ✅ **Tailwind CSS** - Modern utility-first CSS styling
- ✅ **Zustand** - Lightweight state management for auth
- ✅ **TypeScript** - Full type safety

## Project Structure

```
src/
├── pages/              # Page components (Login, Dashboard)
├── stores/             # Zustand store for authentication
├── hooks/              # Custom React hooks (useApi, etc.)
├── router.tsx          # TanStack Router configuration
├── App.tsx             # Main app component
├── main.tsx            # Entry point
└── index.css           # Tailwind CSS import

vite.config.ts          # Vite configuration
tsconfig.json           # TypeScript configuration
index.html              # HTML entry point
package.json            # Dependencies
```

## Getting Started

### Prerequisites
- Node.js 16+ 
- npm or yarn

### Installation

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm run dev
```

3. Open [http://localhost:5173](http://localhost:5173) in your browser

## Login Credentials

Demo credentials (any email/password works):
- **Email:** demo@example.com
- **Password:** any password

## Development

### Available Commands

```bash
# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

## Key Components

### Authentication
- Login page with form validation
- Auth state management using Zustand
- Protected routes via TanStack Router beforeLoad hooks

### Data Fetching
- Fake API functions in `src/hooks/useApi.ts`
- TanStack Query with custom hooks (useUserProfile, useDashboardData)
- Easy to replace with real API calls

### Dashboard
- Displays statistics with TanStack Query
- Shows recent activity
- Includes user logout functionality

## Customization

### Replace Fake API with Real Backend

Edit `src/hooks/useApi.ts` and replace the fake functions with real API calls:

```typescript
export const fakeApiFunctions = {
  fetchUserProfile: async (userId: string) => {
    // Replace with your actual API call
    const response = await fetch(`/api/users/${userId}`)
    return response.json()
  },
  // ... other functions
}
```

### Add More Routes

Edit `src/router.tsx` to add new routes:

```typescript
export const newRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: '/new-page',
  component: NewPage,
})
```

### Styling

Tailwind CSS is configured. Add any custom styles in `src/index.css` or use Tailwind classes.

## Documentation

- [TanStack Query Docs](https://tanstack.com/query/latest)
- [TanStack Router Docs](https://tanstack.com/router/latest)
- [Tailwind CSS Docs](https://tailwindcss.com/docs)
- [React Docs](https://react.dev)

## License

MIT

This project was created using `bun init` in bun v1.3.6. [Bun](https://bun.com) is a fast all-in-one JavaScript runtime.
