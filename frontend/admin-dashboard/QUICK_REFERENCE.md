# GoTrack Design System - Quick Reference

## 🎯 Component Usage Examples

### Button Component

```tsx
import { Button } from "@/components";

// Primary button (orange)
<Button variant="primary" size="lg">
  شحنة جديدة
</Button>

// Secondary button (navy)
<Button variant="secondary">
  تحديث
</Button>

// Outline button
<Button variant="outline">
  إلغاء
</Button>

// Ghost button
<Button variant="ghost">
  المزيد
</Button>

// With loading state
<Button isLoading>
  جاري الحفظ...
</Button>

// Full width
<Button fullWidth>
  تسجيل الدخول
</Button>
```

### Input Component

```tsx
import { Input } from "@/components";

<Input
  label="البريد الإلكتروني"
  type="email"
  placeholder="your@email.com"
  required
  error={errors.email}
/>

<Input
  label="كلمة المرور"
  type="password"
  helpText="يجب أن تكون 6 أحرف على الأقل"
/>
```

### Card Component

```tsx
import { Card } from "@/components";

<Card>
  <h3>المحتوى</h3>
  <p>وصف المحتوى</p>
</Card>

// With hover effect
<Card hover>
  يمكن النقر عليها
</Card>
```

### Sidebar Component

```tsx
import { Sidebar } from "@/components";

<Sidebar
  logoText="GoTrack"
  items={[
    { id: "dashboard", label: "لوحة التحكم", icon: "📊" },
    { id: "shipments", label: "الشحنات", icon: "📦", badge: 5 }
  ]}
  activeItem={activeNav}
  footer={<Button>تسجيل الخروج</Button>}
/>
```

### Header Component

```tsx
import { Header } from "@/components";

<Header
  title="لوحة التحكم"
  subtitle="عرض إحصائيات الشحنات"
  actions={<Button>تصدير</Button>}
/>
```

### MetricCard Component

```tsx
import { MetricCard } from "@/components";

<MetricCard
  label="إجمالي الشحنات"
  value="1,245"
  icon="📦"
  trend={{ value: 12, direction: "up" }}
  color="primary"
/>
```

## 🎨 Color Usage

```tsx
// Use Tailwind color classes directly
className="bg-primary text-on-primary"           // Navy
className="bg-orange-600 text-white"            // Orange
className="bg-red-600 text-white"               // Error
className="bg-green-600 text-white"             // Success

// Custom color variables from theme.ts
import { COLORS } from "@/config/theme";
const primaryColor = COLORS.primary; // "#070d45"
```

## 📐 Spacing Usage

```tsx
// Tailwind spacing classes
className="p-md"        // 24px padding
className="gap-sm"      // 12px gap
className="mb-lg"       // 40px margin-bottom

// Or use raw values
className="p-6"         // Standard padding
className="space-y-4"   // Vertical spacing
```

## 🔤 Typography Usage

```tsx
// Font sizes
<h1 className="font-headline-xl">صفحة رئيسية</h1>
<h2 className="font-headline-lg">عنوان</h2>
<p className="font-body-md">نص عادي</p>
<label className="font-label-md">تسمية</label>

// Or inline styles
<p className="text-headline-md font-bold">النص</p>
```

## 📱 Responsive Patterns

```tsx
// Mobile first (default)
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
  {/* Cards */}
</div>

// Sidebar responsive
<main className="flex-1 mr-0 md:mr-64">
  {/* Content */}
</main>
```

## ✅ Best Practices

1. **Always use RTL**: Add `dir="rtl"` to containers
2. **Use components**: Import from `@/components`
3. **Consistent spacing**: Use the 8px scale
4. **Semantic colors**: primary, secondary, error, success
5. **Arabic text**: All labels should be Arabic
6. **Loading states**: Always show loading feedback
7. **Validation**: Provide error messages in Arabic

## 🚀 Common Patterns

### Form with Validation

```tsx
const [errors, setErrors] = useState({});

const handleSubmit = async (e) => {
  e.preventDefault();
  const newErrors = validateForm();
  
  if (Object.keys(newErrors).length > 0) {
    setErrors(newErrors);
    return;
  }
  
  // Submit
};

return (
  <form onSubmit={handleSubmit}>
    <Input
      name="email"
      error={errors.email}
      onChange={(e) => setFormData({...formData, [e.target.name]: e.target.value})}
    />
    <Button type="submit">إرسال</Button>
  </form>
);
```

### Dashboard Layout

```tsx
<div className="min-h-screen bg-background flex" dir="rtl">
  <Sidebar items={navItems} activeItem={activeNav} />
  
  <main className="flex-1 mr-0 md:mr-64 p-10">
    <Header title="الصفحة" />
    
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <MetricCard ... />
    </div>
    
    <Card>
      {/* Content */}
    </Card>
  </main>
</div>
```

### Login Form

```tsx
<div className="min-h-screen bg-background flex items-center justify-center" dir="rtl">
  <Card className="w-full max-w-md">
    <form onSubmit={handleSubmit}>
      <Input label="البريد الإلكتروني" type="email" required />
      <Input label="كلمة المرور" type="password" required />
      <Button fullWidth variant="primary">تسجيل الدخول</Button>
    </form>
  </Card>
</div>
```

## 🎨 Theme Customization

Edit `src/config/theme.ts`:

```typescript
export const COLORS = {
  primary: "#070d45",              // Change main color
  secondary_container: "#fc9430",  // Change accent
  background: "#fbf8fe",           // Change background
  // ... other colors
};
```

Then update `tailwind.config.ts` to use new tokens.

## 📚 File References

- Components: `src/components/*.tsx`
- Theme: `src/config/theme.ts`
- Types: `src/types/design-system.ts`
- Pages: `src/pages/*.tsx`
- Config: `tailwind.config.ts`
