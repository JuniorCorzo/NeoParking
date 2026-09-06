import { CommonModule, DecimalPipe } from "@angular/common";
import {
  ChangeDetectionStrategy,
  Component,
  input,
  output,
} from "@angular/core";
import type { TicketSummary } from "@core/models/ticket.model";
import { NgIcon, provideIcons } from "@ng-icons/core";
import {
  lucideEye,
  lucideInbox,
  lucidePrinter,
  lucideRotateCcw,
} from "@ng-icons/lucide";
import {
  BadgeComponent,
  ButtonComponent,
  CardComponent,
  CardContentComponent,
  TableBodyComponent,
  TableCellComponent,
  TableComponent,
  TableHeadComponent,
  TableHeaderComponent,
  TableRowComponent,
  TypographyH4,
  TypographyMuted,
} from "@nivo-sass/design-system";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    DecimalPipe,
    NgIcon,
    TableComponent,
    TableHeaderComponent,
    TableBodyComponent,
    TableRowComponent,
    TableHeadComponent,
    TableCellComponent,
    BadgeComponent,
    ButtonComponent,
    CardComponent,
    CardContentComponent,
    TypographyH4,
    TypographyMuted,
  ],
  providers: [
    provideIcons({
      lucideEye,
      lucideInbox,
      lucidePrinter,
      lucideRotateCcw,
    }),
  ],
  selector: "app-tickets-table",
  standalone: true,
  templateUrl: "./tickets-table.component.html",
})
export class TicketsTableComponent {
  readonly tickets = input.required<TicketSummary[]>();
  readonly isLoading = input<boolean>(false);

  readonly selectTicket = output<TicketSummary>();
  readonly reprintReceipt = output<TicketSummary>();
  readonly resetFilters = output();

  static formatDate(dateStr?: string): string {
    if (!dateStr) {
      return "---";
    }
    const d = new Date(dateStr);
    if (Number.isNaN(d.getTime())) {
      return dateStr;
    }
    const day = String(d.getDate()).padStart(2, "0");
    const month = String(d.getMonth() + 1).padStart(2, "0");
    const year = d.getFullYear();
    const hours = String(d.getHours()).padStart(2, "0");
    const minutes = String(d.getMinutes()).padStart(2, "0");
    return `${day}/${month}/${year} ${hours}:${minutes}`;
  }

  readonly formatDate = TicketsTableComponent.formatDate;
}
