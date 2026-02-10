package dev.angelcorzo.neoparking.api.parkingtickets.controller;

import dev.angelcorzo.neoparking.api.commons.dto.Response;
import dev.angelcorzo.neoparking.api.parkingtickets.dto.CreateTicket;
import dev.angelcorzo.neoparking.api.parkingtickets.dto.ParkingTicketsDTO;
import dev.angelcorzo.neoparking.api.parkingtickets.mapper.ParkingTicketMapper;
import dev.angelcorzo.neoparking.api.payments.dtos.request.check_out.check_out.CheckOutCommand;
import dev.angelcorzo.neoparking.api.payments.mappers.PaymentsMapper;
import dev.angelcorzo.neoparking.model.authentication.gateway.AuthenticationContextGateway;
import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.usecase.checkinvehiclewithoureservation.CheckInVehicleWithoutReservationUseCase;
import java.util.UUID;

import dev.angelcorzo.neoparking.usecase.checkoutvehicle.CheckOutVehicleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class ParkingTicketsController {
  private final CheckInVehicleWithoutReservationUseCase checkInVehicleWithoutReservationUseCase;
  private final CheckOutVehicleUseCase checkOutVehicleUseCase;

  private final AuthenticationContextGateway authenticationContext;
  private final ParkingTicketMapper parkingTicketMapper;
  private final PaymentsMapper paymentsMapper;

  @PostMapping("/check-in")
  @PreAuthorize("hasRole('OPERATOR')")
  public Response<ParkingTicketsDTO> createTicket(@RequestBody CreateTicket createTicket) {
    final UUID tenantId = this.authenticationContext.getCurrentTenantId();

    final ParkingTickets ticket =
        this.checkInVehicleWithoutReservationUseCase.execute(
            this.parkingTicketMapper.toModel(createTicket).toBuilder()
                .tenantId(tenantId)
                .email(createTicket.email())
                .build());

    return Response.created(this.parkingTicketMapper.toDto(ticket), "");
  }

  @PostMapping("/check-out")
  @PreAuthorize("hasRole('OPERATOR')")
  public Response<Void> checkOutVehicle(@RequestBody CheckOutCommand checkOutCommand) {
    this.checkOutVehicleUseCase.execute(paymentsMapper.toModel(checkOutCommand));
    return Response.ok(null, "");
  }
}
