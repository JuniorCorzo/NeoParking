package dev.angelcorzo.neoparking.api.parkinglots.controller;

import dev.angelcorzo.neoparking.api.commons.dto.Response;
import dev.angelcorzo.neoparking.api.parkinglots.dto.ParkingLotsResponse;
import dev.angelcorzo.neoparking.api.parkinglots.dto.UpsertParkingLotsRequest;
import dev.angelcorzo.neoparking.api.parkinglots.mappers.ParkingLotsMapper;
import dev.angelcorzo.neoparking.model.authentication.gateway.AuthenticationGateway;
import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;
import dev.angelcorzo.neoparking.model.parkinglots.dto.UpsertParkingLotsDTO;
import dev.angelcorzo.neoparking.usecase.createparking.CreateParkingUseCase;
import dev.angelcorzo.neoparking.usecase.listparkinglots.ListParkingLotsUseCase;
import dev.angelcorzo.neoparking.usecase.updateparking.UpdateParkingLotsUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking-lots")
@Tag(name = "Parking Lots", description = "Parking Lots API")
@RequiredArgsConstructor
public class ParkingLotsController {
  private final AuthenticationGateway authenticationGateway;

  private final CreateParkingUseCase createParkingUseCase;
  private final UpdateParkingLotsUseCase updateParkingLotsUseCase;
  private final ListParkingLotsUseCase listParkingLotsUseCase;
  private final ParkingLotsMapper parkingLotsMapper;

  @GetMapping("/list")
  public Response<List<ParkingLotsResponse>> listParkingLots(
      @RequestHeader("Authorization") String accessToken) {
    final UUID tenantId = this.extractTenantId(accessToken);

    final List<ParkingLotsResponse> parkingLots =
        this.listParkingLotsUseCase.listParkingLots(tenantId).stream()
            .map(parkingLotsMapper::toDTO)
            .toList();

    return Response.ok(parkingLots, "");
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('MANAGER')")
  @Transactional
  public Response<ParkingLotsResponse> createParkingLots(
      @RequestBody UpsertParkingLotsRequest parkingLots,
      @RequestHeader("Authorization") String accessToken) {

    final UpsertParkingLotsDTO newParkingLots =
        this.parkingLotsMapper.toModel(parkingLots).toBuilder()
            .tenantId(this.extractTenantId(accessToken))
            .build();

    final ParkingLots parkingLotsCreated = this.createParkingUseCase.created(newParkingLots);

    return Response.ok(
        this.parkingLotsMapper.toDTO(parkingLotsCreated), "Parking Lot created successfully");
  }

  @PutMapping("/update")
  @PreAuthorize("hasRole('MANAGER')")
  @Transactional
  public Response<ParkingLotsResponse> updateParkingLots(
      @RequestBody UpsertParkingLotsRequest parkingLots,
      @RequestHeader("Authorization") String accessToken) {
    final UpsertParkingLotsDTO updateParkingLots =
        this.parkingLotsMapper.toModel(parkingLots).toBuilder()
            .tenantId(this.extractTenantId(accessToken))
            .build();

    return Response.ok(
        this.parkingLotsMapper.toDTO(this.updateParkingLotsUseCase.update(updateParkingLots)), "");
  }

  private UUID extractTenantId(String accessToken) {
    final String token = accessToken.replace("Bearer ", "");
    final String tenantIdClaim =
        this.authenticationGateway.extractClaim(token, "tenantId").orElseThrow();

    return UUID.fromString(tenantIdClaim);
  }
}
