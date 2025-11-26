package dev.angelcorzo.neoparking.model.parkingtickets;

import dev.angelcorzo.neoparking.model.parkingtickets.enums.ParkingTicketStatus;
import dev.angelcorzo.neoparking.model.rates.valueobject.RateReference;
import dev.angelcorzo.neoparking.model.slots.Slots;
import dev.angelcorzo.neoparking.model.specialpolicies.valueobjects.SpecialPoliciesReference;
import dev.angelcorzo.neoparking.model.tenants.valueobject.TenantReference;
import dev.angelcorzo.neoparking.model.users.valueobject.UserReference;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ParkingTickets {
  private UUID id;
  private Slots slot;
  private TenantReference tenant;
  private UserReference user;
  private SpecialPoliciesReference specialPolicy;
  private String licensePlate;
  private OffsetDateTime entryDate;
  private OffsetDateTime exitDate;
  private RateReference rate;
  private Long totalToCharge;
  private ParkingTicketStatus status;
  private String paymentMethod;
  private String transactionReference;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private OffsetDateTime deletedAt;
}
