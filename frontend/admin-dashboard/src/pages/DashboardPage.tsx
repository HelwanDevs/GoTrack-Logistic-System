import { useState } from "react";
import { useNavigate } from "@tanstack/react-router";
import { Sidebar } from "@/components/Sidebar";
import { Header } from "@/components/Header";
import { Button } from "@/components/Button";
import { MetricCard } from "@/components/MetricCard";
import { Card } from "@/components/Card";

export const DashboardPage = () => {
  return (
    <main className="flex-1 mr-0 md:mr-64 p-6 lg:p-10 flex flex-col gap-8">
      <Header
        title="لوحة التحكم"
        subtitle="عرض إحصائيات الشحنات والعمليات الجارية"
        actions={
          <Button variant="outline" size="sm">
            تصدير البيانات
          </Button>
        }
      />

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <MetricCard
          label="إجمالي الشحنات"
          value="1,245"
          icon="📦"
          trend={{ value: 12, direction: "up" }}
          color="primary"
        />
        <MetricCard
          label="الشحنات النشطة"
          value="342"
          icon="🚚"
          trend={{ value: 8, direction: "up" }}
          color="secondary"
        />
        <MetricCard
          label="الشحنات المكتملة"
          value="892"
          icon="✓"
          trend={{ value: 5, direction: "up" }}
          color="success"
        />
        <MetricCard
          label="الشحنات المتأخرة"
          value="11"
          icon="⚠️"
          trend={{ value: 2, direction: "down" }}
          color="error"
        />
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recent Shipments */}
        <Card className="lg:col-span-2">
          <div className="mb-6">
            <h3 className="font-headline-md text-headline-md text-on-background mb-1">
              آخر الشحنات
            </h3>
            <p className="text-body-sm text-on-surface-variant">
              آخر 5 شحنات تم إضافتها
            </p>
          </div>

          <div className="space-y-3">
            {[
              {
                id: "#SHP-9824",
                client: "شركة الأمل للتجارة",
                status: "في الطريق",
                statusColor: "bg-secondary-container",
              },
              {
                id: "#SHP-9825",
                client: "مؤسسة البناء الحديث",
                status: "قيد الانتظار",
                statusColor: "bg-surface-variant",
              },
              {
                id: "#SHP-9810",
                client: "مصنع الأغذية الوطنية",
                status: "تم التسليم",
                statusColor: "bg-primary-container",
              },
            ].map((shipment) => (
              <div
                key={shipment.id}
                className="flex items-center justify-between p-4 bg-surface-container-low rounded-lg hover:bg-surface-container transition"
              >
                <div>
                  <p className="font-label-md text-label-md text-on-background">
                    {shipment.id}
                  </p>
                  <p className="text-body-sm text-on-surface-variant">
                    {shipment.client}
                  </p>
                </div>
                <span
                  className={`${shipment.statusColor} ${shipment.statusColor === "bg-primary-container" ? "text-on-primary" : "text-on-surface-variant"} text-body-sm font-label-md px-3 py-1 rounded-full`}
                >
                  {shipment.status}
                </span>
              </div>
            ))}
          </div>
        </Card>

        {/* Quick Actions */}
        <Card>
          <div className="mb-6">
            <h3 className="font-headline-md text-headline-md text-on-background">
              إجراءات سريعة
            </h3>
          </div>

          <div className="space-y-3">
            <Button variant="primary" fullWidth size="md">
              + شحنة جديدة
            </Button>
            <Button variant="outline" fullWidth size="md">
              جدول الشحنات
            </Button>
            <Button variant="outline" fullWidth size="md">
              تقرير الأداء
            </Button>
            <Button variant="outline" fullWidth size="md">
              الدعم الفني
            </Button>
          </div>

          {/* Stats Box */}
          <div className="mt-6 pt-6 border-t border-outline-variant space-y-3">
            <div className="flex justify-between items-center">
              <span className="text-body-sm text-on-surface-variant">
                معدل النجاح
              </span>
              <span className="text-label-md font-label-md text-primary">
                98.5%
              </span>
            </div>
            <div className="w-full bg-surface-container-low rounded-full h-2">
              <div
                className="bg-primary-container rounded-full h-2"
                style={{ width: "98.5%" }}
              ></div>
            </div>
          </div>
        </Card>
      </div>

      {/* Performance Chart */}
      <Card>
        <div className="mb-6">
          <h3 className="font-headline-md text-headline-md text-on-background mb-1">
            أداء التوصيل الشهري
          </h3>
          <p className="text-body-sm text-on-surface-variant">
            متوسط وقت التوصيل بالساعات
          </p>
        </div>

        <div className="flex items-end justify-between h-48 gap-2">
          {[
            { month: "يناير", value: 24, maxValue: 48 },
            { month: "فبراير", value: 22, maxValue: 48 },
            { month: "مارس", value: 19, maxValue: 48 },
            { month: "أبريل", value: 21, maxValue: 48 },
            { month: "مايو", value: 18, maxValue: 48 },
            { month: "يونيو", value: 16, maxValue: 48 },
          ].map((data) => (
            <div key={data.month} className="flex-1 flex flex-col items-center">
              <div className="w-full flex justify-center">
                <div
                  className="w-full bg-secondary-container rounded-t-lg"
                  style={{
                    height: `${(data.value / data.maxValue) * 250}px`,
                    maxWidth: "100%",
                  }}
                ></div>
              </div>
              <p className="text-body-sm text-on-surface-variant mt-2">
                {data.month}
              </p>
            </div>
          ))}
        </div>
      </Card>
    </main>
  );
};
