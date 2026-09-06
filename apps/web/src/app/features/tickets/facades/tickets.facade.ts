import { computed, inject, Injectable, signal } from "@angular/core";
import type {
  PriceDetailedModel,
  TicketSummary,
} from "@core/models/ticket.model";
import { TicketService } from "@core/services/ticket-service";

import type { TicketFilterCriteria } from "../models/ticket-filter.model";
import { INITIAL_TICKET_FILTERS } from "../models/ticket-filter.model";

@Injectable()
export class TicketsFacade {
  private readonly ticketService = inject(TicketService);

  readonly parkingId = signal<string | null>(null);
  readonly tickets = signal<TicketSummary[]>([]);
  readonly filters = signal<TicketFilterCriteria>(INITIAL_TICKET_FILTERS);
  readonly selectedTicket = signal<TicketSummary | null>(null);
  readonly liveRatePreview = signal<PriceDetailedModel | null>(null);
  readonly isLoading = signal<boolean>(false);
  readonly isCalculatingRate = signal<boolean>(false);
  readonly isDrawerOpen = signal<boolean>(false);
  readonly isReceiptOpen = signal<boolean>(false);

  readonly filteredTickets = computed(() => {
    const all = this.tickets();
    const criteria = this.filters();
    const plateQuery = criteria.plate.trim().toUpperCase();

    return all.filter((ticket) => {
      // Plate filter
      if (
        plateQuery &&
        !ticket.licensePlate.toUpperCase().includes(plateQuery)
      ) {
        return false;
      }

      // Status filter
      if (criteria.status !== "ALL" && ticket.status !== criteria.status) {
        return false;
      }

      // Vehicle type filter
      if (
        criteria.vehicleType !== "ALL" &&
        ticket.slotType !== criteria.vehicleType
      ) {
        return false;
      }

      // Date range filter
      if (criteria.dateRange) {
        const ticketDate = ticket.entryTime
          ? new Date(ticket.entryTime).getTime()
          : 0;
        if (criteria.dateRange.from) {
          const fromTime = new Date(criteria.dateRange.from).getTime();
          if (ticketDate < fromTime) {
            return false;
          }
        }
        if (criteria.dateRange.to) {
          const toTime = new Date(criteria.dateRange.to).getTime();
          if (ticketDate > toTime) {
            return false;
          }
        }
      }

      return true;
    });
  });

  readonly ticketStats = computed(() => {
    const all = this.tickets();
    const open = all.filter((t) => t.status === "OPEN").length;
    const closed = all.filter((t) => t.status === "CLOSED").length;
    return { closed, open, total: all.length };
  });

  loadTickets(parkingId: string): void {
    this.parkingId.set(parkingId);
    this.isLoading.set(true);

    this.ticketService.listTicketsByParkingLot(parkingId).subscribe({
      error: () => {
        this.tickets.set([]);
        this.isLoading.set(false);
      },
      next: (data) => {
        this.tickets.set(data);
        this.isLoading.set(false);
      },
    });
  }

  updateFilters(criteria: Partial<TicketFilterCriteria>): void {
    this.filters.update((current) => ({
      ...current,
      ...criteria,
    }));
  }

  resetFilters(): void {
    this.filters.set(INITIAL_TICKET_FILTERS);
  }

  openDetail(ticket: TicketSummary): void {
    this.selectedTicket.set(ticket);
    this.isDrawerOpen.set(true);

    if (ticket.status === "OPEN") {
      this.isCalculatingRate.set(true);
      this.liveRatePreview.set(null);

      this.ticketService.calculatePrice(ticket.id).subscribe({
        error: () => {
          this.liveRatePreview.set(null);
          this.isCalculatingRate.set(false);
        },
        next: (rate) => {
          this.liveRatePreview.set(rate);
          this.isCalculatingRate.set(false);
        },
      });
    } else {
      this.liveRatePreview.set(null);
      this.isCalculatingRate.set(false);
    }
  }

  closeDetail(): void {
    this.isDrawerOpen.set(false);
    this.selectedTicket.set(null);
    this.liveRatePreview.set(null);
    this.isCalculatingRate.set(false);
  }

  openReceipt(ticket: TicketSummary): void {
    this.selectedTicket.set(ticket);
    this.isReceiptOpen.set(true);
  }

  closeReceipt(): void {
    this.isReceiptOpen.set(false);
  }
}
