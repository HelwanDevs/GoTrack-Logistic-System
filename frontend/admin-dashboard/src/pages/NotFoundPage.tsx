import { useNavigate } from "@tanstack/react-router";
import { Button } from "@/components/Button";

export const NotFoundPage = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-primary flex items-center justify-center p-4">
      <div className="max-w-md w-full text-center">
        <div className="mb-8 relative">
          <h1 className="text-9xl font-bold text-secondary-container">404</h1>
          <div className="absolute inset-0 flex items-center justify-center">
            <div className="text-6xl animate-bounce">🔍</div>
          </div>
        </div>

        <h2 className="text-3xl font-bold text-white mb-3">
          الصفحة غير موجودة
        </h2>

        <p className="text-white text-body-lg mb-2">
          عذراً، الصفحة التي تبحث عنها غير موجودة أو تم حذفها.
        </p>
        <p className="text-white text-body-md mb-8">
          قد يكون الرابط غير صحيح .
        </p>

        <div className="flex flex-col gap-3 sm:flex-row sm:justify-center">
          <Button
            variant="primary"
            size="md"
            onClick={() => navigate({ to: "/" })}
            className="flex-1 sm:flex-none"
          >
            العودة للرئيسية
          </Button>
          <Button
            size="md"
            onClick={() => navigate({ to: "/dashboard" })}
            className="flex-1 sm:flex-none bg-white text-primary! hover:bg-primary-container hover:outline-1 hover:outline-white hover:text-white!"
          >
            لوحة التحكم
          </Button>
        </div>
      </div>
    </div>
  );
};
