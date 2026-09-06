import { CommonModule } from "@angular/common";
import type { OnInit } from "@angular/core";
import { ChangeDetectionStrategy, Component, inject } from "@angular/core";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { TicketReceiptComponent } from "@features/operations/components/ticket-receipt/ticket-receipt.component";
import { NgIcon, provideIcons } from "@ng-icons/core";
import {
  lucideArrowLeft,
  lucideCheckCircle2,
  lucideClock,
  lucideTicket,
} from "@ng-icons/lucide";
import {
  ButtonComponent,
  CardComponent,
  CardContentComponent,
  TypographyH1,
  TypographyMuted,
} from "@nivo-sass/design-system";
import { APP_ROUTES } from "@shared/constants/app-routes.constant";

import { TicketDetailDrawerComponent } from "../components/ticket-detail-drawer/ticket-detail-drawer.component";
import { TicketFiltersComponent } from "../components/ticket-filters/ticket-filters.component";
import { TicketsTableComponent } from "../components/tickets-table/tickets-table.component";
import { TicketsFacade } from "../facades/tickets.facade";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    RouterLink,
    NgIcon,
    ButtonComponent,
    CardComponent,
    CardContentComponent,
    TypographyH1,
    TypographyMuted,
    TicketFiltersComponent,
    TicketsTableComponent,
    TicketDetailDrawerComponent,
    TicketReceiptComponent,
  ],
  providers: [
    TicketsFacade,
    provideIcons({
      lucideArrowLeft,
      lucideCheckCircle2,
      lucideClock,
      lucideTicket,
    }),
  ],
  selector: "app-tickets-page",
  standalone: true,
  templateUrl: "./tickets-page.html",
})
export class TicketsPageComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  readonly facade = inject(TicketsFacade);

  protected readonly APP_ROUTES = APP_ROUTES;

  ngOnInit(): void {
    const parkingId = this.route.snapshot.paramMap.get("parkingId");
    if (parkingId) {
      this.facade.loadTickets(parkingId);
    }
  }
}
