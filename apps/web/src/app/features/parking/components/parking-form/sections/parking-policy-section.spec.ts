import { Component, signal } from "@angular/core";
import type { ComponentFixture } from "@angular/core/testing";
import { TestBed } from "@angular/core/testing";
import { form, min, required } from "@angular/forms/signals";
import { By } from "@angular/platform-browser";
import { DurationInputComponent } from "@shared/components/duration-input/duration-input";

import { ParkingPolicySectionComponent } from "./parking-policy-section";

@Component({
  imports: [ParkingPolicySectionComponent],
  standalone: true,
  template: `
    <app-parking-policy-section
      [gracePeriodMinutes]="policyForm.gracePeriodMinutes"
      [gracePeriodPrice]="policyForm.gracePeriodPrice"
      [ivaRate]="policyForm.ivaRate"
    />
  `,
})
class TestHostComponent {
  public policyModel = signal({
    gracePeriodMinutes: 15,
    gracePeriodPrice: 500,
    ivaRate: 0.19,
  });

  public policyForm = form(this.policyModel, (path) => {
    required(path.gracePeriodMinutes, {
      message: "El tiempo de gracia es requerido",
    });
    min(path.gracePeriodMinutes, 0, {
      message: "El tiempo de gracia no puede ser negativo",
    });
    required(path.gracePeriodPrice);
    min(path.gracePeriodPrice, 0);
    required(path.ivaRate);
    min(path.ivaRate, 0);
  });
}

describe("ParkingPolicySectionComponent", () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let host: TestHostComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestHostComponent, ParkingPolicySectionComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    host = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should render policy title and description", () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain("Políticas de liquidación");
    expect(compiled.textContent).toContain("Reglas de tiempo de gracia");
  });

  it("should render duration-input for grace period with initial form values", () => {
    const durationInput = fixture.debugElement.query(
      By.directive(DurationInputComponent)
    );
    expect(durationInput).toBeTruthy();
    expect(durationInput.componentInstance.label()).toBe("Tiempo de gracia");
    expect(durationInput.componentInstance.amount()).toBe(15);
    expect(durationInput.componentInstance.unit()).toBe("MINUTES");
  });

  it("should propagate duration input updates back to signal form model", () => {
    const durationInput = fixture.debugElement.query(
      By.directive(DurationInputComponent)
    );
    const durationComp: DurationInputComponent = durationInput.componentInstance;

    durationComp.onUnitChange("HOURS");
    durationComp.onAmountChange(2);
    fixture.detectChanges();

    expect(host.policyModel().gracePeriodMinutes).toBe(120);
  });

  it("should propagate validation errors to duration-input when field is invalid and touched", () => {
    const durationInput = fixture.debugElement.query(
      By.directive(DurationInputComponent)
    );
    const durationComp: DurationInputComponent = durationInput.componentInstance;

    expect(durationComp.error()).toEqual([]);

    host.policyForm.gracePeriodMinutes().value.set(-5);
    host.policyForm.gracePeriodMinutes().markAsTouched();
    fixture.detectChanges();

    const err = durationComp.error();
    expect(err).toBeDefined();
    expect(Array.isArray(err)).toBe(true);
    if (Array.isArray(err)) {
      expect(err[0]?.message).toBe(
        "El tiempo de gracia no puede ser negativo"
      );
    }
  });

  it("should render all three numeric inputs across policy section", () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const inputs = compiled.querySelectorAll("input");
    expect(inputs.length).toBe(3);
  });
});
