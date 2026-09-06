import type { OnInit } from "@angular/core";
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  inject,
  input,
  output,
} from "@angular/core";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { NgIcon, provideIcons } from "@ng-icons/core";
import { lucideFilterX, lucideRotateCcw, lucideSearch } from "@ng-icons/lucide";
import {
  ButtonComponent,
  InputComponent,
  SelectComponent,
} from "@nivo-sass/design-system";
import { debounceTime, distinctUntilChanged, Subject } from "rxjs";

import type {
  TicketFilterCriteria,
  TicketStatusFilter,
} from "../../models/ticket-filter.model";

export interface FilterSelectOption<T = string> {
  label: string;
  value: T;
}

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [InputComponent, SelectComponent, ButtonComponent, NgIcon],
  providers: [
    provideIcons({
      lucideFilterX,
      lucideRotateCcw,
      lucideSearch,
    }),
  ],
  selector: "app-ticket-filters",
  standalone: true,
  templateUrl: "./ticket-filters.component.html",
})
export class TicketFiltersComponent implements OnInit {
  private readonly destroyRef = inject(DestroyRef);
  private readonly plateSubject = new Subject<string>();

  readonly filters = input.required<TicketFilterCriteria>();

  readonly filtersChange = output<TicketFilterCriteria>();
  readonly reset = output();

  readonly statusOptions: FilterSelectOption<TicketStatusFilter>[] = [
    { label: "Todos los estados", value: "ALL" },
    { label: "Activo (Abierto)", value: "OPEN" },
    { label: "Finalizado (Cerrado)", value: "CLOSED" },
  ];

  readonly vehicleOptions: FilterSelectOption<string>[] = [
    { label: "Todos los tipos", value: "ALL" },
    { label: "Carro", value: "CAR" },
    { label: "Moto", value: "MOTORCYCLE" },
    { label: "Bicicleta", value: "BICYCLE" },
  ];

  static displayOptionFn(opt: FilterSelectOption): string {
    return opt?.label ?? "";
  }

  static valueOptionFn(opt: FilterSelectOption): string {
    return opt?.value ?? "";
  }

  readonly displayOptionFn = TicketFiltersComponent.displayOptionFn;
  readonly valueOptionFn = TicketFiltersComponent.valueOptionFn;

  ngOnInit(): void {
    this.plateSubject
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((plate) => {
        this.filtersChange.emit({
          ...this.filters(),
          plate,
        });
      });
  }

  onPlateInput(event: Event): void {
    const { target } = event;
    if (target instanceof HTMLInputElement) {
      this.plateSubject.next(target.value);
    }
  }

  onStatusChange(val: string): void {
    if (!val) {
      return;
    }
    this.filtersChange.emit({
      ...this.filters(),
      /* SAFETY: val is selected from statusOptions which only contains TicketStatusFilter values */
      status: val as TicketStatusFilter,
    });
  }

  onVehicleChange(val: string): void {
    if (!val) {
      return;
    }
    this.filtersChange.emit({
      ...this.filters(),
      vehicleType: val,
    });
  }

  onReset(): void {
    this.reset.emit();
  }
}
