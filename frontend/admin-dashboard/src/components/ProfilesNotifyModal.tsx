import { useState } from "react";
import { Button } from "@/components/Button";
import { Select } from "@/components/Select";
import { Checkbox } from "@/components/Checkbox";
import { Modal } from "@/components/Modal";

// ─── Types ───────────────────────────────────────────────────────────────────

export interface Profile {
  id: string;
  full_name: string;
  phone_number: string;
}

interface ProfilesNotifyModalProps {
  isOpen: boolean;
  profile: Profile | null;
  onClose: () => void;
  onSubmit: (data: NotifyData) => void;
}

export interface NotifyData {
  message: string;
  methods: string[];
  messageType: string;
}

interface NotificationMessage {
  value: string;
  label: string;
  type: string;
  content: string;
}

// ─── Preconfigured Messages ──────────────────────────────────────────────────

const notificationMessages: NotificationMessage[] = [
  {
    value: "info",
    label: "ℹ️ معلومات",
    type: "info",
    content: "معلومة جديدة تريد مشاركتها.",
  },
  {
    value: "success",
    label: "✅ نجاح",
    type: "success",
    content: "تمت العملية بنجاح!",
  },
  {
    value: "warning",
    label: "⚠️ تحذير",
    type: "warning",
    content: "يرجى الانتباه إلى أمر مهم.",
  },
  {
    value: "danger",
    label: "🚨 خطير",
    type: "danger",
    content: "يوجد خطأ يتطلب انتباهك الفوري.",
  },
  {
    value: "welcome",
    label: "👋 ترحيب",
    type: "info",
    content: "مرحباً بك! نرحب بك في منصتنا ونتمنى لك تجربة مميزة.",
  },
  {
    value: "reminder",
    label: "⏰ تذكير",
    type: "warning",
    content: "تذكير: لديك مهمة مستحقة. يرجى الإنهاء في أقرب وقت.",
  },
  {
    value: "update",
    label: "📢 تحديث",
    type: "info",
    content: "لدينا تحديث مهم نود إعلامك به. يرجى مراجعة التحديثات الجديدة.",
  },
  {
    value: "approval",
    label: "🎉 قبول",
    type: "success",
    content: "مباركة! تم قبول طلبك بنجاح. شكراً لك.",
  },
  { value: "custom", label: "✏️ رسالة مخصصة", type: "info", content: "" },
];

const messageTypeOptions = [
  { value: "info", label: "ℹ️ معلومات" },
  { value: "success", label: "✅ نجاح" },
  { value: "warning", label: "⚠️ تحذير" },
  { value: "danger", label: "🚨 خطير" },
];

// ─── Component ───────────────────────────────────────────────────────────────

export const ProfilesNotifyModal = ({
  isOpen,
  profile,
  onClose,
  onSubmit,
}: ProfilesNotifyModalProps) => {
  const [selectedMessageType, setSelectedMessageType] = useState("");
  const [selectedTemplate, setSelectedTemplate] = useState("");
  const [customMessage, setCustomMessage] = useState("");
  const [selectedMethods, setSelectedMethods] = useState<string[]>([]);
  const [submitError, setSubmitError] = useState("");

  const methodOptions = [
    { value: "email", label: "📧 بريد إلكتروني" },
    { value: "site", label: "🌐 إشعار الموقع" },
    { value: "whatsapp", label: "📱 واتساب" },
    { value: "sms", label: "📨 رسالة نصية" },
  ];

  const toggleMethod = (method: string) => {
    setSelectedMethods((prev) =>
      prev.includes(method)
        ? prev.filter((m) => m !== method)
        : [...prev, method],
    );
  };

  const getMessageTypeLabel = (type: string) => {
    const map: Record<string, string> = {
      info: "معلومات",
      success: "نجاح",
      warning: "تحذير",
      danger: "خطر",
    };
    return map[type] || type;
  };

  const getCurrentMessageType = () => {
    if (selectedMessageType) return selectedMessageType;
    const found = notificationMessages.find(
      (m) => m.value === selectedTemplate,
    );
    return found ? found.type : "info";
  };

  const getSelectedMessageContent = () => {
    if (selectedTemplate === "custom") return customMessage;
    const found = notificationMessages.find(
      (m) => m.value === selectedTemplate,
    );
    return found ? found.content : "";
  };

  const handleSend = () => {
    const message = getSelectedMessageContent();
    const messageType = getCurrentMessageType();

    if (!message.trim()) {
      setSubmitError("يرجى كتابة أو اختيار رسالة");
      return;
    }
    if (selectedMethods.length === 0) {
      setSubmitError("يرجى اختيار طريقة إرسال واحدة على الأقل");
      return;
    }

    onSubmit({
      message,
      methods: [...selectedMethods],
      messageType,
    });

    setSelectedMessageType("");
    setSelectedTemplate("");
    setCustomMessage("");
    setSelectedMethods([]);
    setSubmitError("");
    onClose();
  };

  const handleTemplateChange = (val: string) => {
    setSelectedTemplate(val);
    if (val === "custom") {
      setCustomMessage("");
    }
    setSubmitError("");
  };

  return (
    <Modal
      isOpen={isOpen}
      title={profile ? `إشعار لـ ${profile.full_name}` : "إرسال إشعار"}
      onClose={onClose}
      size="lg"
    >
      <div className="space-y-4">
        <div className="flex flex-row gap-3">
          {/* Message Type */}
          <div className="w-full">
            <label className="font-label-md text-label-md text-on-surface block mb-2">
              نوع الإشعار
            </label>
            <Select
              value={selectedMessageType}
              onChange={(e) => {
                setSelectedMessageType(e.target.value as string);
                setSubmitError("");
              }}
              options={messageTypeOptions}
            />
          </div>

          {/* Template Dropdown */}
          <div className="w-full">
            <label className="font-label-md text-label-md text-on-surface block mb-2">
              قالب جاهز
            </label>
            <Select
              value={selectedTemplate}
              onChange={(e) => handleTemplateChange(e.target.value)}
              options={notificationMessages.map((m) => ({
                value: m.value,
                label: m.label,
              }))}
            />
          </div>
        </div>

        {/* Message Content */}
        <div>
          <label className="font-label-md text-label-md text-on-surface block mb-2">
            محتوى الرسالة
          </label>
          <textarea
            className="w-full bg-surface-container-low border-2 border-outline-variant text-on-surface font-body-md text-body-md rounded-lg px-4 py-3 focus:ring-2 focus:ring-orange-500 focus:border-orange-500 transition-all resize-y min-h-[100px]"
            placeholder="اكتب رسالتك هنا..."
            value={getSelectedMessageContent()}
            onChange={(e) => {
              setCustomMessage(e.target.value);
              if (
                selectedTemplate &&
                e.target.value !== getSelectedMessageContent()
              ) {
                setSelectedTemplate("custom");
              }
              setSubmitError("");
            }}
          />
        </div>

        {/* Notification Methods */}
        <div>
          <label className="font-label-md text-label-md text-on-surface block mb-2">
            طريقة الإرسال
          </label>
          <div className="space-y-2">
            {methodOptions.map((method) => (
              <div
                key={method.value}
                className="flex justify-start gap-3 p-2 rounded-lg hover:bg-surface-container-low transition"
              >
                <div>
                  <Checkbox
                    checked={selectedMethods.includes(method.value)}
                    size="md"
                    onChange={() => toggleMethod(method.value)}
                  />
                </div>
                <span className="text-on-surface text-body-md">
                  {method.label}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Error */}
        {submitError && (
          <div className="p-3 bg-error/10 border border-error rounded-lg">
            <p className="text-error text-body-sm">{submitError}</p>
          </div>
        )}

        {/* Actions */}
        <div className="flex gap-3 pt-2">
          <Button variant="primary" size="md" onClick={handleSend}>
            إرسال الإشعار
          </Button>
          <Button variant="outline" size="md" onClick={onClose}>
            إلغاء
          </Button>
        </div>
      </div>
    </Modal>
  );
};
