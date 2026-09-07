import { Directive } from "@angular/core";

@Directive({
  selector: "[emptyState]",
  standalone: true,
})
export class EmptyStateDirective {
  readonly isMarker = true;
}
