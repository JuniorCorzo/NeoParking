package dev.angelcorzo.neoparking.api.parkingtickets.controller;

import dev.angelcorzo.neoparking.api.commons.dto.Response;
import dev.angelcorzo.neoparking.api.parkingtickets.dto.CreateTicket;
import dev.angelcorzo.neoparking.api.parkingtickets.dto.ParkingTicketsDTO;
import dev.angelcorzo.neoparking.api.parkingtickets.mapper.ParkingTicketMapper;
import dev.angelcorzo.neoparking.model.authentication.gateway.AuthenticationContextGateway;
import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.users.UserAuthentication;
import dev.angelcorzo.neoparking.usecase.checkinvehiclewithoureservation.CheckInVehicleWithoutReservationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class ParkingTicketsController {
  private final CheckInVehicleWithoutReservationUseCase checkInVehicleWithoutReservationUseCase;

  private final AuthenticationContextGateway authenticationContext;
  private final ParkingTicketMapper parkingTicketMapper;

  @PostMapping("/check-in")
  @PreAuthorize("hasRole('OPERATOR')")
  public Response<ParkingTicketsDTO> createTicket(@RequestBody CreateTicket createTicket) {
    final UserAuthentication userAuthentication =
        this.authenticationContext.getCurrentlyAuthenticatedUser();

    final ParkingTickets ticket =
        this.checkInVehicleWithoutReservationUseCase.execute(
            this.parkingTicketMapper.toModel(createTicket).toBuilder()
                .tenantId(userAuthentication.tenantId())
                .userId(userAuthentication.userId())
                .build());

    return Response.created(this.parkingTicketMapper.toDto(ticket), "");
  }
}
