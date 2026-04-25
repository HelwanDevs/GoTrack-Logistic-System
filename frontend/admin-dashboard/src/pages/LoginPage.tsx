import { useState } from "react";
import { useNavigate } from "@tanstack/react-router";
import { Button } from "@/components/Button";
import { Input } from "@/components/Input";
import { Card } from "@/components/Card";

export const LoginPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    rememberMe: false,
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isLoading, setIsLoading] = useState(false);

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.email) {
      newErrors.email = "البريد الإلكتروني مطلوب";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = "البريد الإلكتروني غير صحيح";
    }

    if (!formData.password) {
      newErrors.password = "كلمة المرور مطلوبة";
    } else if (formData.password.length < 6) {
      newErrors.password = "كلمة المرور يجب أن تكون 6 أحرف على الأقل";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setIsLoading(true);
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1500));
      // Mock successful login
      await navigate({ to: "/dashboard" });
    } catch (error) {
      setErrors({
        submit: "فشل تسجيل الدخول. حاول مرة أخرى",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
    // Clear error for this field
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  };

  return (
    <div
      className="min-h-screen bg-primary flex items-center justify-center p-4"
      dir="rtl"
    >
      <div className="w-full max-w-md">
        {/* Logo Section */}
        <div className="text-center mb-8">
          <img
            src="./gotrack_logo.png"
            alt="GoTrack Logo"
            className="img-fluid mx-auto mb-4 w-64"
          />
        </div>

        {/* Login Card */}
        <Card>
          <div className="mb-6">
            <h2 className="text-xl font-headline-md text-on-background mb-1">
              تسجيل الدخول
            </h2>
            <p className="text-body-sm text-on-surface-variant">
              أدخل بيانات تسجيل الدخول الخاصة بك
            </p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            {errors.submit && (
              <div className="p-3 bg-error-container border border-error rounded-lg">
                <p className="text-body-sm text-on-error-container">
                  {errors.submit}
                </p>
              </div>
            )}

            <Input
              label="البريد الإلكتروني"
              type="email"
              name="email"
              placeholder="your@email.com"
              value={formData.email}
              onChange={handleInputChange}
              error={errors.email}
              required
            />

            <Input
              label="كلمة المرور"
              type="password"
              name="password"
              placeholder="••••••••"
              value={formData.password}
              onChange={handleInputChange}
              error={errors.password}
              required
            />

            {/* Remember Me & Forgot Password */}
            <div className="flex items-center justify-between">
              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="checkbox"
                  name="rememberMe"
                  checked={formData.rememberMe}
                  onChange={handleInputChange}
                  className="w-4 h-4 rounded border-outline-variant text-secondary-container focus:ring-secondary-container"
                />
                <span className="font-body-sm text-body-sm text-on-surface">
                  تذكرني
                </span>
              </label>
            </div>

            {/* Submit Button */}
            <Button
              type="submit"
              variant="primary"
              size="lg"
              fullWidth
              isLoading={isLoading}
            >
              {isLoading ? "جاري تسجيل الدخول..." : "تسجيل الدخول"}
            </Button>
          </form>
        </Card>

        {/* Demo Credentials */}
        <div className="bg-surface-container-lowest mt-6 p-4 bg-primary-container/10 rounded-lg border border-primary-container/20">
          <p className="text-body-sm text-on-surface-variant mb-2">
            بيانات التجربة:
          </p>
          <p className="text-body-sm text-on-surface">
            البريد: demo@gotrack.com
          </p>
          <p className="text-body-sm text-on-surface">كلمة المرور: 123456</p>
        </div>
      </div>
    </div>
  );
};
