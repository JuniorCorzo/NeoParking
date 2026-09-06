import { TestBed } from "@angular/core/testing";
import { ActivatedRoute } from "@angular/router";
import { TicketService } from "@core/services/ticket-service";
import { of } from "rxjs";

import { TicketsPageComponent } from "./tickets-page";

interface MockPageTicketService {
  calculatePrice: ReturnType<typeof vi.fn>;
  listTicketsByParkingLot: ReturnType<typeof vi.fn>;
}

describe("TicketsPageComponent", () => {
  let mockTicketService: MockPageTicketService;

  beforeEach(async () => {
    mockTicketService = {
      calculatePrice: vi.fn().mockReturnValue(of(null)),
      listTicketsByParkingLot: vi.fn().mockReturnValue(of([])),
    };

    await TestBed.configureTestingModule({
      imports: [TicketsPageComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: vi.fn().mockReturnValue("p-123"),
              },
            },
          },
        },
        { provide: TicketService, useValue: mockTicketService },
      ],
    }).compileComponents();
  });

  it("should create and load tickets on init", () => {
    const fixture = TestBed.createComponent(TicketsPageComponent);
    const component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component).toBeTruthy();
    expect(mockTicketService.listTicketsByParkingLot).toHaveBeenCalledWith(
      "p-123"
    );
  });
});
