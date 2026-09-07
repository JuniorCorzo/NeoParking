export type TicketStatusFilter = "ALL" | "OPEN" | "CLOSED";

export interface TicketDateRange {
  from?: string;
  to?: string;
}

export interface TicketFilterCriteria {
  dateRange: TicketDateRange | null;
  parkingLotId: string;
  plate: string;
  status: TicketStatusFilter;
  vehicleType: string;
}

export const INITIAL_TICKET_FILTERS: TicketFilterCriteria = {
  dateRange: null,
  parkingLotId: "ALL",
  plate: "",
  status: "ALL",
  vehicleType: "ALL",
};
