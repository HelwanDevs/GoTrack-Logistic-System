import { useState } from "react";
import { Header } from "@/components/Header";
import { Button } from "@/components/Button";
import { Card } from "@/components/Card";
import { Input } from "@/components/Input";
import { Modal } from "@/components/Modal";
import { LoadingSpinner } from "@/components/LoadingSpinner";
import {
  useUpdateAccountMutation,
  useChangePasswordMutation,
} from "@/features/accounts";
import { getStoredAuth, removeStoredTokens } from "@/utils/storage";
import { UserRole } from "@/types/enums";
import { useLoginMutation } from "@/features/auth";

interface EditEmailForm {
  newEmail: string;
  confirmEmail: string;
}

interface ChangePasswordForm {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

export const SettingsPage = () => {
  const auth = getStoredAuth();
  const currentUser = auth.user;

  const [editEmailForm, setEditEmailForm] = useState<EditEmailForm>({
    newEmail: currentUser?.email || "",
    confirmEmail: currentUser?.email || "",
  });
  const [editEmailErrors, setEditEmailErrors] = useState<
    Record<string, string>
  >({});
  const [showEditEmailModal, setShowEditEmailModal] = useState(false);

  const [changePasswordForm, setChangePasswordForm] =
    useState<ChangePasswordForm>({
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
    });
  const [changePasswordErrors, setChangePasswordErrors] = useState<
    Record<string, string>
  >({});
  const [showChangePasswordModal, setShowChangePasswordModal] = useState(false);

  const updateAccountMutation = useUpdateAccountMutation();
  const changePasswordMutation = useChangePasswordMutation();
  const validateCurrentPasswordMutation = useLoginMutation();

  if (!currentUser) {
    return (
      <main className="flex-1 mr-0 p-6 lg:p-10 flex flex-col gap-8">
        <div className="text-center py-12">
          <p className="text-error text-body-md">
            خطأ: لم يتم العثور على بيانات المستخدم
          </p>
        </div>
      </main>
    );
  }

  // Edit Email Handlers
  const validateEditEmail = (): boolean => {
    const errors: Record<string, string> = {};

    if (!editEmailForm.newEmail) {
      errors.newEmail = "البريد الإلكتروني الجديد مطلوب";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(editEmailForm.newEmail)) {
      errors.newEmail = "البريد الإلكتروني غير صحيح";
    }

    if (editEmailForm.newEmail !== editEmailForm.confirmEmail) {
      errors.confirmEmail = "البريد الإلكتروني غير متطابق";
    }

    setEditEmailErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleEditEmail = async () => {
    if (!validateEditEmail()) {
      return;
    }

    if (
      !confirm(
        `هل تأكد من رغبتك في تغيير بريدك الإلكتروني إلى ${editEmailForm.newEmail}؟\nسيتم تسجيل خروجك تلقائياً بعد التغيير.`,
      )
    ) {
      return;
    }

    try {
      await updateAccountMutation.mutateAsync({
        accountId: currentUser.accountId,
        data: {
          email: editEmailForm.newEmail,
        },
      });
      setShowEditEmailModal(false);
      alert("تم تغيير البريد الإلكتروني بنجاح. يرجى تسجيل الدخول مرة أخرى.");
      removeStoredTokens();
      window.location.href = "/login";
    } catch (error: any) {
      setEditEmailErrors({
        submit: error.message || "فشل تغيير البريد الإلكتروني. حاول مرة أخرى",
      });
    }
  };

  // Change Password Handlers
  const validateChangePassword = (): boolean => {
    const errors: Record<string, string> = {};

    if (!changePasswordForm.currentPassword) {
      errors.currentPassword = "كلمة المرور الحالية مطلوبة";
    }

    if (!changePasswordForm.newPassword) {
      errors.newPassword = "كلمة المرور الجديدة مطلوبة";
    } else if (changePasswordForm.newPassword.length < 6) {
      errors.newPassword = "كلمة المرور يجب أن تكون 6 أحرف على الأقل";
    }

    if (changePasswordForm.newPassword !== changePasswordForm.confirmPassword) {
      errors.confirmPassword = "كلمات المرور غير متطابقة";
    }

    if (changePasswordForm.currentPassword === changePasswordForm.newPassword) {
      errors.newPassword = "كلمة المرور الجديدة يجب أن تكون مختلفة عن الحالية";
    }

    setChangePasswordErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleChangePassword = async () => {
    if (!validateChangePassword()) {
      return;
    }

    if (!confirm("هل تأكد من رغبتك في تغيير كلمة المرور؟")) {
      return;
    }

    try {
      try {
        await validateCurrentPasswordMutation.mutateAsync({
          email: currentUser.email,
          password: changePasswordForm.currentPassword,
        });
      } catch (error) {
        setChangePasswordErrors({
          currentPassword: "كلمة المرور الحالية غير صحيحة",
        });
        return;
      }
      await changePasswordMutation.mutateAsync({
        accountId: currentUser.accountId,
        newPassword: changePasswordForm.newPassword,
      });
      setShowChangePasswordModal(false);
      alert("تم تغيير كلمة المرور بنجاح. يرجى تسجيل الدخول مرة أخرى.");
      removeStoredTokens();
      window.location.href = "/login";
    } catch (error: any) {
      setChangePasswordErrors({
        submit: error.message || "فشل تغيير كلمة المرور. حاول مرة أخرى",
      });
    }
  };

  const getRoleLabel = (role: UserRole): string => {
    const roleMap: Record<UserRole, string> = {
      [UserRole.ADMIN]: "مسؤول",
      [UserRole.EMPLOYEE]: "موظف",
      [UserRole.MERCHANT]: "تاجر",
    };
    return roleMap[role];
  };

  return (
    <main className="flex-1 mr-0 p-6 lg:p-10 flex flex-col gap-8">
      <Header title="الإعدادات" subtitle="إدارة بيانات حسابك الشخصية" />

      {/* Account Info Card */}
      <Card>
        <h3 className="font-headline-md text-headline-md text-on-background mb-6">
          معلومات الحساب
        </h3>

        <div className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="font-label-md text-label-md text-on-surface-variant block mb-2">
                البريد الإلكتروني
              </label>
              <p className="text-body-md text-on-surface">
                {currentUser.email}
              </p>
            </div>

            <div>
              <label className="font-label-md text-label-md text-on-surface-variant block mb-2">
                الدور
              </label>
              <span className="inline-block px-3 py-1 rounded-full text-label-md font-label-md bg-primary text-on-primary">
                {getRoleLabel(currentUser.role)}
              </span>
            </div>
          </div>
        </div>
      </Card>

      {/* Edit Account Settings */}
      <Card>
        <h3 className="font-headline-md text-headline-md text-on-background mb-6">
          تحديث البيانات
        </h3>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Button
            variant="secondary"
            size="md"
            onClick={() => {
              setEditEmailForm({
                newEmail: currentUser.email,
                confirmEmail: currentUser.email,
              });
              setEditEmailErrors({});
              setShowEditEmailModal(true);
            }}
          >
            تغيير البريد الإلكتروني
          </Button>

          <Button
            variant="secondary"
            size="md"
            onClick={() => {
              setChangePasswordForm({
                currentPassword: "",
                newPassword: "",
                confirmPassword: "",
              });
              setChangePasswordErrors({});
              setShowChangePasswordModal(true);
            }}
          >
            تغيير كلمة المرور
          </Button>
        </div>
      </Card>

      {/* Edit Email Modal */}
      <Modal
        isOpen={showEditEmailModal}
        title="تغيير البريد الإلكتروني"
        onClose={() => {
          setShowEditEmailModal(false);
          setEditEmailErrors({});
        }}
      >
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleEditEmail();
          }}
          className="space-y-4"
        >
          <Input
            label="البريد الإلكتروني الجديد"
            type="email"
            placeholder="example@domain.com"
            value={editEmailForm.newEmail}
            onChange={(e) => {
              setEditEmailForm({
                ...editEmailForm,
                newEmail: e.target.value,
              });
              if (editEmailErrors.newEmail) {
                setEditEmailErrors({ ...editEmailErrors, newEmail: "" });
              }
            }}
            error={editEmailErrors.newEmail}
            required
          />

          <Input
            label="تأكيد البريد الإلكتروني"
            type="email"
            placeholder="example@domain.com"
            value={editEmailForm.confirmEmail}
            onChange={(e) => {
              setEditEmailForm({
                ...editEmailForm,
                confirmEmail: e.target.value,
              });
              if (editEmailErrors.confirmEmail) {
                setEditEmailErrors({
                  ...editEmailErrors,
                  confirmEmail: "",
                });
              }
            }}
            error={editEmailErrors.confirmEmail}
            required
          />

          {editEmailErrors.submit && (
            <div className="p-3 bg-error/10 border border-error rounded-lg">
              <p className="text-error text-body-sm">
                {editEmailErrors.submit}
              </p>
            </div>
          )}

          <div className="flex gap-3 pt-2">
            <Button
              type="submit"
              variant="primary"
              size="md"
              disabled={updateAccountMutation.isPending}
              isLoading={updateAccountMutation.isPending}
            >
              {updateAccountMutation.isPending ? "جاري التحديث..." : "تحديث"}
            </Button>
            <Button
              type="button"
              variant="outline"
              size="md"
              onClick={() => setShowEditEmailModal(false)}
            >
              إلغاء
            </Button>
          </div>
        </form>
      </Modal>

      {/* Change Password Modal */}
      <Modal
        isOpen={showChangePasswordModal}
        title="تغيير كلمة المرور"
        onClose={() => {
          setShowChangePasswordModal(false);
          setChangePasswordErrors({});
        }}
      >
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleChangePassword();
          }}
          className="space-y-4"
        >
          <Input
            label="كلمة المرور الحالية"
            type="password"
            placeholder="••••••••"
            value={changePasswordForm.currentPassword}
            onChange={(e) => {
              setChangePasswordForm({
                ...changePasswordForm,
                currentPassword: e.target.value,
              });
              if (changePasswordErrors.currentPassword) {
                setChangePasswordErrors({
                  ...changePasswordErrors,
                  currentPassword: "",
                });
              }
            }}
            error={changePasswordErrors.currentPassword}
            required
          />

          <Input
            label="كلمة المرور الجديدة"
            type="password"
            placeholder="••••••••"
            value={changePasswordForm.newPassword}
            onChange={(e) => {
              setChangePasswordForm({
                ...changePasswordForm,
                newPassword: e.target.value,
              });
              if (changePasswordErrors.newPassword) {
                setChangePasswordErrors({
                  ...changePasswordErrors,
                  newPassword: "",
                });
              }
            }}
            error={changePasswordErrors.newPassword}
            required
          />

          <Input
            label="تأكيد كلمة المرور الجديدة"
            type="password"
            placeholder="••••••••"
            value={changePasswordForm.confirmPassword}
            onChange={(e) => {
              setChangePasswordForm({
                ...changePasswordForm,
                confirmPassword: e.target.value,
              });
              if (changePasswordErrors.confirmPassword) {
                setChangePasswordErrors({
                  ...changePasswordErrors,
                  confirmPassword: "",
                });
              }
            }}
            error={changePasswordErrors.confirmPassword}
            required
          />

          {changePasswordErrors.submit && (
            <div className="p-3 bg-error/10 border border-error rounded-lg">
              <p className="text-error text-body-sm">
                {changePasswordErrors.submit}
              </p>
            </div>
          )}

          <div className="flex gap-3 pt-2">
            <Button
              type="submit"
              variant="primary"
              size="md"
              disabled={changePasswordMutation.isPending}
              isLoading={changePasswordMutation.isPending}
            >
              {changePasswordMutation.isPending ? "جاري التحديث..." : "تحديث"}
            </Button>
            <Button
              type="button"
              variant="outline"
              size="md"
              onClick={() => setShowChangePasswordModal(false)}
            >
              إلغاء
            </Button>
          </div>
        </form>
      </Modal>
    </main>
  );
};
