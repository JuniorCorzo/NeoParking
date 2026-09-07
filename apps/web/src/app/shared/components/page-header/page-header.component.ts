import { ChangeDetectionStrategy, Component, input } from "@angular/core";
import { RouterLink } from "@angular/router";
import { NgIcon, provideIcons } from "@ng-icons/core";
import { lucideArrowLeft } from "@ng-icons/lucide";
import {
  BadgeComponent,
  TypographyH1,
  TypographyMuted,
} from "@nivo-sass/design-system";

export type PageHeaderBadgeVariant =
  | "default"
  | "secondary"
  | "destructive"
  | "success"
  | "warning"
  | "info"
  | "outline";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    class: "block w-full",
  },
  imports: [RouterLink, NgIcon, BadgeComponent, TypographyH1, TypographyMuted],
  providers: [
    provideIcons({
      lucideArrowLeft,
    }),
  ],
  selector: "app-page-header",
  standalone: true,
  styleUrl: "./page-header.component.css",
  templateUrl: "./page-header.component.html",
})
export class PageHeaderComponent {
  readonly title = input.required<string>();
  readonly subtitle = input<string | null>(null);
  readonly backLink = input<string | unknown[] | null>(null);
  readonly backAriaLabel = input<string>("Volver");
  readonly badge = input<string | null>(null);
  readonly badgeVariant = input<PageHeaderBadgeVariant>("info");
}
