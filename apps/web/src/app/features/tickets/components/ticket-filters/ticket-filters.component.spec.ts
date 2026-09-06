import type { ComponentFixture } from "@angular/core/testing";
import { TestBed } from "@angular/core/testing";

import type { TicketFilterCriteria } from "../../models/ticket-filter.model";
import { INITIAL_TICKET_FILTERS } from "../../models/ticket-filter.model";
import { TicketFiltersComponent } from "./ticket-filters.component";

describe("TicketFiltersComponent", () => {
  let component: TicketFiltersComponent;
  let fixture: ComponentFixture<TicketFiltersComponent>;

  beforeEach(async () => {
    vi.useFakeTimers();

    await TestBed.configureTestingModule({
      imports: [TicketFiltersComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TicketFiltersComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput("filters", INITIAL_TICKET_FILTERS);
    fixture.detectChanges();
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it("should debounce license plate search by 300ms", () => {
    let emittedFilters: TicketFilterCriteria | null = null;
    component.filtersChange.subscribe((f) => {
      emittedFilters = f;
    });

    const event = new Event("input");
    const input = document.createElement("input");
    input.value = "ABC";
    Object.defineProperty(event, "target", { value: input });

    component.onPlateInput(event);

    expect(emittedFilters).toBeNull();

    vi.advanceTimersByTime(150);
    expect(emittedFilters).toBeNull();

    vi.advanceTimersByTime(150);
    expect(emittedFilters).toEqual({
      ...INITIAL_TICKET_FILTERS,
      plate: "ABC",
    });
  });

  it("should emit filter change on status selection", () => {
    let emittedFilters: TicketFilterCriteria | null = null;
    component.filtersChange.subscribe((f) => {
      emittedFilters = f;
    });

    component.onStatusChange("OPEN");

    expect(emittedFilters).toEqual({
      ...INITIAL_TICKET_FILTERS,
      status: "OPEN",
    });
  });

  it("should emit filter change on vehicle type selection", () => {
    let emittedFilters: TicketFilterCriteria | null = null;
    component.filtersChange.subscribe((f) => {
      emittedFilters = f;
    });

    component.onVehicleChange("CAR");

    expect(emittedFilters).toEqual({
      ...INITIAL_TICKET_FILTERS,
      vehicleType: "CAR",
    });
  });

  it("should emit reset output on reset button click", () => {
    let resetEmitted = false;
    component.reset.subscribe(() => {
      resetEmitted = true;
    });

    component.onReset();
    expect(resetEmitted).toBe(true);
  });
});
