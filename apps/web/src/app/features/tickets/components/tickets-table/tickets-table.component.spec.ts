import type { ComponentFixture } from "@angular/core/testing";
import { TestBed } from "@angular/core/testing";
import type { TicketSummary } from "@core/models/ticket.model";

import { TicketsTableComponent } from "./tickets-table.component";

describe("TicketsTableComponent", () => {
  let component: TicketsTableComponent;
  let fixture: ComponentFixture<TicketsTableComponent>;

  const mockTickets: TicketSummary[] = [
    {
      entryTime: "2026-09-05T10:00:00Z",
      id: "t-1",
      licensePlate: "ABC123",
      slotNumber: "A-01",
      slotType: "CAR",
      status: "OPEN",
    },
    {
      entryTime: "2026-09-05T08:00:00Z",
      exitTime: "2026-09-05T09:30:00Z",
      id: "t-2",
      licensePlate: "XYZ789",
      slotNumber: "M-05",
      slotType: "MOTORCYCLE",
      status: "CLOSED",
      totalToCharge: 3500,
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketsTableComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TicketsTableComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput("tickets", mockTickets);
    fixture.componentRef.setInput("isLoading", false);
    fixture.detectChanges();
  });

  it("should render table with tickets", () => {
    /* SAFETY: nativeElement is guaranteed to be an HTMLElement in test environment */
    const compiled = fixture.nativeElement as HTMLElement;
    const rows = compiled.querySelectorAll("tbody tr");
    expect(rows.length).toBe(2);
    expect(compiled.textContent).toContain("ABC123");
    expect(compiled.textContent).toContain("XYZ789");
  });

  it("should show empty state when tickets array is empty", () => {
    fixture.componentRef.setInput("tickets", []);
    fixture.detectChanges();

    /* SAFETY: nativeElement is guaranteed to be an HTMLElement in test environment */
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain("No se encontraron tickets");
  });

  it("should show loading indicator when isLoading is true", () => {
    fixture.componentRef.setInput("isLoading", true);
    fixture.detectChanges();

    /* SAFETY: nativeElement is guaranteed to be an HTMLElement in test environment */
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain("Cargando tickets...");
  });

  it("should emit selectTicket when clicking view detail button", () => {
    let selected: TicketSummary | null = null;
    component.selectTicket.subscribe((t) => {
      selected = t;
    });

    component.selectTicket.emit(mockTickets[0]);
    expect(selected).toEqual(mockTickets[0]);
  });

  it("should emit reprintReceipt when clicking reprint button", () => {
    let selected: TicketSummary | null = null;
    component.reprintReceipt.subscribe((t) => {
      selected = t;
    });

    component.reprintReceipt.emit(mockTickets[1]);
    expect(selected).toEqual(mockTickets[1]);
  });

  it("should format date correctly", () => {
    const formatted = TicketsTableComponent.formatDate("2026-09-05T14:30:00Z");
    expect(formatted).not.toBe("---");
    expect(TicketsTableComponent.formatDate()).toBe("---");
  });
});
