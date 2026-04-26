import { useState } from "react";
import { useNavigate } from "@tanstack/react-router";
import { Button } from "@/components/Button";
import { Input } from "@/components/Input";
import { Card } from "@/components/Card";
import { useLoginMutation } from "@/features/auth";

export const LoginPage = () => {
  const navigate = useNavigate();
  const loginMutation = useLoginMutation();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [errors, setErrors] = useState<Record<string, string>>({});

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

  const handleSubmit = async (e: React.ChangeEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      await loginMutation.mutateAsync({
        email: formData.email,
        password: formData.password,
      });

      await navigate({ to: "/dashboard" });

    } catch (error: any) {
      switch (error.status || error.cause?.status) {
        case 400:
          setErrors({ submit: "البريد الإلكتروني أو كلمة المرور غير صحيحة" });
          break;
        case 401:
          setErrors({ submit: "ليس لدي صلاحية الوصول لهذه الصفحه" });
          break;
        case 500:
          setErrors({ submit: "خطأ في الخادم. حاول مرة أخرى لاحقًا" });
          break;
        default:
          setErrors({ submit: "فشل تسجيل الدخول. حاول مرة أخرى" });
      }
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
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


        <div className="text-center mb-8">
          <img
            src="./gotrack_logo.png"
            alt="GoTrack Logo"
            className="img-fluid mx-auto mb-4 w-64"
          />
        </div>


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


            <Button
              type="submit"
              variant="primary"
              size="lg"
              fullWidth
              isLoading={loginMutation.isPending}
            >
              {loginMutation.isPending
                ? "جاري تسجيل الدخول..."
                : "تسجيل الدخول"}
            </Button>
          </form>
        </Card>

      </div>
    </div>
  );
};
