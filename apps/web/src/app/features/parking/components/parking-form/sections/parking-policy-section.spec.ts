import { Component, signal } from "@angular/core";
import type { ComponentFixture } from "@angular/core/testing";
import { TestBed } from "@angular/core/testing";
import { form, min, required } from "@angular/forms/signals";

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
    required(path.gracePeriodMinutes);
    min(path.gracePeriodMinutes, 0);
    required(path.gracePeriodPrice);
    min(path.gracePeriodPrice, 0);
    required(path.ivaRate);
    min(path.ivaRate, 0);
  });
}

describe("ParkingPolicySectionComponent", () => {
  let fixture: ComponentFixture<TestHostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestHostComponent, ParkingPolicySectionComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    fixture.detectChanges();
  });

  it("should render policy title and description", () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain("Políticas de liquidación");
    expect(compiled.textContent).toContain("Reglas de tiempo de gracia");
  });

  it("should render all three numeric inputs", () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const inputs = compiled.querySelectorAll("input");
    expect(inputs.length).toBe(3);
  });
});
