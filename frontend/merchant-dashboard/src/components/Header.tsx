import { ReactNode } from "react";

interface HeaderProps {
  title: string;
  subtitle?: string;
  actions?: ReactNode;
  rightContent?: ReactNode;
}

export const Header = ({
  title,
  subtitle,
  actions,
  rightContent,
}: HeaderProps) => {
  return (
    <header className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
      <div>
        <h2 className="font-headline-lg text-headline-lg text-on-background">
          {title}
        </h2>
        {subtitle && (
          <p className="font-body-sm text-body-sm text-on-surface-variant mt-1">
            {subtitle}
          </p>
        )}
      </div>
      <div className="flex items-center gap-4">
        {actions && <div className="flex gap-2">{actions}</div>}
        {rightContent && <div>{rightContent}</div>}
      </div>
    </header>
  );
};
