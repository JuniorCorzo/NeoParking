import { Component, signal } from "@angular/core";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FormControl, ReactiveFormsModule } from "@angular/forms";
import { By } from "@angular/platform-browser";
import { DurationInputComponent } from "./duration-input";

@Component({
  imports: [DurationInputComponent, ReactiveFormsModule],
  standalone: true,
  template: `
    <app-duration-input
      [formControl]="control"
      [label]="label()"
      [id]="id()"
      [error]="error()"
    />
  `,
})
class TestHostComponent {
  readonly control = new FormControl<number>(0);
  readonly label = signal("Tiempo de gracia");
  readonly id = signal("grace-duration");
  readonly error = signal<string | undefined>(undefined);
}

describe("DurationInputComponent", () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let host: TestHostComponent;
  let component: DurationInputComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestHostComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    host = fixture.componentInstance;
    component = fixture.debugElement.query(
      By.directive(DurationInputComponent)
    ).componentInstance;
    fixture.detectChanges();
  });

  it("should initialize with default 0 minutes", () => {
    expect(component.amount()).toBe(0);
    expect(component.unit()).toBe("MINUTES");
  });

  it("should populate amount and unit on writeValue with hours divisible value", () => {
    host.control.setValue(120);
    fixture.detectChanges();

    expect(component.amount()).toBe(2);
    expect(component.unit()).toBe("HOURS");
  });

  it("should populate amount and unit on writeValue with days divisible value", () => {
    host.control.setValue(2880);
    fixture.detectChanges();

    expect(component.amount()).toBe(2);
    expect(component.unit()).toBe("DAYS");
  });

  it("should populate amount and unit on writeValue with non-divisible minutes", () => {
    host.control.setValue(45);
    fixture.detectChanges();

    expect(component.amount()).toBe(45);
    expect(component.unit()).toBe("MINUTES");
  });

  it("should propagate total minutes to form control when amount changes", () => {
    component.onUnitChange("HOURS");
    component.onAmountChange(3);
    fixture.detectChanges();

    expect(host.control.value).toBe(180);
  });

  it("should propagate total minutes to form control when unit changes", () => {
    component.onAmountChange(1);
    component.onUnitChange("DAYS");
    fixture.detectChanges();

    expect(host.control.value).toBe(1440);
  });

  it("should update disabled state", () => {
    host.control.disable();
    fixture.detectChanges();

    expect(component.disabled()).toBe(true);

    host.control.enable();
    fixture.detectChanges();

    expect(component.disabled()).toBe(false);
  });
});
