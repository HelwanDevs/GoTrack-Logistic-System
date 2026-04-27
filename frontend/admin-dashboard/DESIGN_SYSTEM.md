# GoTrack Design System - Kinetic Logistics

A professional Arabic RTL-first design system for the GoTrack logistics management platform, built with React, TailwindCSS, and TypeScript.

## Overview

This design system implements the "Kinetic Logistics" theme designed for high-stakes logistics operations in Egypt and the Middle East. The system prioritizes:

- **Right-to-Left (RTL) Native Support**: All layouts and components are built with RTL in mind
- **High Contrast & Legibility**: Ensures readability on any screen, from warehouse monitors to mobile devices
- **Arabic Typography**: Uses IBM Plex Sans Arabic for optimal Arabic text rendering
- **Operational Clarity**: Clear visual hierarchy for rapid information processing

## Color Palette

### Primary Colors
- **Deep Navy Blue** (`#070d45` / `#1e245a`): Sidebar, primary headers, institutional trust
- **Vibrant Orange** (`#fc9430`): Primary actions, active states, delivery alerts
- **Light Gray** (`#fbf8fe`): Background, low-strain surface for data tables

### Usage
```typescript
import { COLORS } from "@/config/theme";

// Use colors directly
className={`bg-primary text-on-primary`}
```

## Components

### Button
Reusable button component with multiple variants and sizes.

```typescript
import { Button } from "@/components";

<Button variant="primary" size="lg" fullWidth>
  شحنة جديدة
</Button>

// Variants: primary, secondary, outline, ghost
// Sizes: sm, md, lg
// Props: isLoading, leftIcon, rightIcon, disabled
```

### Input
Form input with label, error state, and help text.

```typescript
import { Input } from "@/components";

<Input
  label="البريد الإلكتروني"
  type="email"
  error={errors.email}
  helpText="أدخل بريدك الصحيح"
  required
/>
```

### Card
Container component for grouped content.

```typescript
import { Card } from "@/components";

<Card hover>
  {/* Content */}
</Card>
```

### Sidebar
Navigation sidebar with logo, menu items, and footer section.

```typescript
import { Sidebar } from "@/components";

<Sidebar
  logoText="GoTrack"
  logoSubtext="نظام إدارة اللوجستيات"
  items={[
    { id: "dashboard", label: "لوحة التحكم", icon: "📊" },
    { id: "shipments", label: "إدارة الشحنات", icon: "📦", badge: 5 }
  ]}
  activeItem={activeNav}
  footer={<Button>تسجيل الخروج</Button>}
/>
```

### Header
Page header with title, subtitle, and action buttons.

```typescript
import { Header } from "@/components";

<Header
  title="لوحة التحكم"
  subtitle="عرض إحصائيات الشحنات"
  actions={<Button>تصدير</Button>}
/>
```

### MetricCard
Dashboard metric card with value, icon, and trend indicator.

```typescript
import { MetricCard } from "@/components";

<MetricCard
  label="إجمالي الشحنات"
  value="1,245"
  icon="📦"
  trend={{ value: 12, direction: "up" }}
  color="primary"
/>
```

## Typography Scale

```
headline-xl:  36px bold
headline-lg:  28px bold
headline-md:  22px semibold
body-lg:      18px regular
body-md:      16px regular (default)
body-sm:      14px regular
label-md:     14px semibold
label-sm:     12px medium
```

## Spacing System

Based on 8px base unit:
- `xs`: 4px
- `sm`: 12px
- `md`: 24px (standard)
- `lg`: 40px
- `xl`: 64px
- `gutter`: 20px (grid gutter)
- `margin-edge`: 32px (page margin)

## Border Radius

- Default: 8px
- md: 12px
- lg: 16px
- full: 9999px (circles)

## Page Structure

### Login Page (`src/pages/LoginPage.tsx`)
Professional login interface with:
- Centered form layout
- Email & password validation
- Remember me checkbox
- Demo credentials section
- Arabic error messages

### Dashboard Page (`src/pages/DashboardPage.tsx`)
Comprehensive logistics dashboard featuring:
- RTL sidebar navigation with active state
- Metric cards with trend indicators
- Recent shipments list
- Quick action buttons
- Performance chart
- Color-coded status badges

## File Structure

```
src/
├── components/           # Reusable components
│   ├── Button.tsx
│   ├── Input.tsx
│   ├── Card.tsx
│   ├── Sidebar.tsx
│   ├── Header.tsx
│   ├── MetricCard.tsx
│   └── index.ts
├── config/
│   └── theme.ts         # Design tokens
├── pages/
│   ├── LoginPage.tsx
│   └── DashboardPage.tsx
└── routes/
    └── ...
```

## Customization

### Updating Colors
Edit `src/config/theme.ts` to modify the color palette:

```typescript
export const COLORS = {
  primary: "#070d45",
  secondary_container: "#fc9430",
  // ... other colors
};
```

### Tailwind Configuration
The `tailwind.config.ts` is pre-configured with all design tokens. To extend:

```typescript
theme: {
  extend: {
    colors: COLORS,
    // ... other extensions
  }
}
```

## Best Practices

1. **Always use RTL layout**: Add `dir="rtl"` to root containers
2. **Use Arabic labels**: Provide Arabic text for all UI elements
3. **Semantic colors**: Use `primary`, `secondary`, `error` for consistency
4. **Responsive design**: All components are mobile-first
5. **Accessibility**: Maintain focus states and proper contrast ratios
6. **Performance**: Use the component library for consistency

## Browser Support

- Chrome/Edge (latest)
- Firefox (latest)
- Safari (latest)
- Mobile browsers (iOS Safari, Chrome Mobile)

## Resources

- [Material Design 3](https://m3.material.io) - Design foundation
- [IBM Plex Sans Arabic](https://fonts.google.com/specimen/IBM+Plex+Sans+Arabic) - Typography
- [TailwindCSS](https://tailwindcss.com) - Styling framework
- [React Router](https://tanstack.com/router) - Navigation

## Demo Credentials

For testing the login page:
- **Email**: demo@gotrack.com
- **Password**: 123456

## Support

For design system questions or component issues, refer to the theme configuration in `src/config/theme.ts` or update component files in `src/components/`.
