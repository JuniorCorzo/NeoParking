package dev.angelcorzo.neoparking.api.parkinglots.controller;

import dev.angelcorzo.neoparking.api.commons.dto.Response;
import dev.angelcorzo.neoparking.api.parkinglots.dto.ParkingLotsResponse;
import dev.angelcorzo.neoparking.api.parkinglots.dto.UpsertParkingLotsRequest;
import dev.angelcorzo.neoparking.api.parkinglots.enums.ParkingLotsMessages;
import dev.angelcorzo.neoparking.api.parkinglots.mappers.ParkingLotsMapper;
import dev.angelcorzo.neoparking.api.rates.dto.CreateRate;
import dev.angelcorzo.neoparking.api.rates.dto.RatesDTO;
import dev.angelcorzo.neoparking.api.rates.enums.RateMessages;
import dev.angelcorzo.neoparking.api.rates.mappers.RatesMapper;
import dev.angelcorzo.neoparking.model.authentication.gateway.AuthenticationGateway;
import dev.angelcorzo.neoparking.model.parkinglots.ParkingLots;
import dev.angelcorzo.neoparking.model.parkinglots.dto.UpsertParkingLotsDTO;
import dev.angelcorzo.neoparking.model.rates.Rates;
import dev.angelcorzo.neoparking.usecase.createparking.CreateParkingUseCase;
import dev.angelcorzo.neoparking.usecase.listparkinglots.ListParkingLotsUseCase;
import dev.angelcorzo.neoparking.usecase.rateconfiguration.RateConfigurationUseCase;
import dev.angelcorzo.neoparking.usecase.showratesbyparkinglot.ShowRatesByParkingLotUseCase;
import dev.angelcorzo.neoparking.usecase.updateparking.UpdateParkingLotsUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
  private final ParkingLotsMapper parkingLotsMapper;
  private final RatesMapper ratesMapper;

  private final CreateParkingUseCase createParkingUseCase;
  private final UpdateParkingLotsUseCase updateParkingLotsUseCase;
  private final ListParkingLotsUseCase listParkingLotsUseCase;
  private final RateConfigurationUseCase rateConfigurationUseCase;
  private final ShowRatesByParkingLotUseCase showRatesByParkingLotUseCase;

  @GetMapping("/list")
  @PreAuthorize("hasRole('MANAGER')")
  public Response<List<ParkingLotsResponse>> listParkingLots(
      @RequestHeader("Authorization") String accessToken) {
    final UUID tenantId = this.extractTenantId(accessToken);

    final List<ParkingLotsResponse> parkingLots =
        this.listParkingLotsUseCase.listParkingLots(tenantId).stream()
            .map(parkingLotsMapper::toDTO)
            .toList();

    return Response.ok(parkingLots, ParkingLotsMessages.PARKING_LOTS_LIST.format());
  }

  @GetMapping("/{parkingId}/rates")
  @PreAuthorize("hasRole('OPERATOR')")
  public Response<Iterable<RatesDTO>> showRatesByParkingId(@PathVariable UUID parkingId) {
    final List<RatesDTO> listRates =
        this.showRatesByParkingLotUseCase.execute(parkingId).stream()
            .map(this.ratesMapper::toDTO)
            .toList();

    return Response.ok(listRates, RateMessages.SHOW_RATES_BY_TENANT.format());
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('MANAGER')")
  @Transactional
  public Response<ParkingLotsResponse> createParkingLots(
      @Valid @RequestBody UpsertParkingLotsRequest parkingLots,
      @RequestHeader("Authorization") String accessToken) {

    final UpsertParkingLotsDTO newParkingLots =
        this.parkingLotsMapper.toModel(parkingLots).toBuilder()
            .tenantId(this.extractTenantId(accessToken))
            .build();

    final ParkingLots parkingLotsCreated = this.createParkingUseCase.created(newParkingLots);

    return Response.ok(
        this.parkingLotsMapper.toDTO(parkingLotsCreated),
        ParkingLotsMessages.PARKING_LOT_CREATED.format());
  }

  @PostMapping("/create-rate")
  @PreAuthorize("hasRole('OWNER')")
  @Transactional
  public Response<RatesDTO> createRateForParking(
      @Valid @RequestBody CreateRate rate, @RequestHeader("Authorization") String accessToken) {
    final RateConfigurationUseCase.CreateTariff rateModel =
        this.ratesMapper.toModel(rate).toBuilder()
            .tenantId(this.extractTenantId(accessToken))
            .build();

    final Rates rateCreated = this.rateConfigurationUseCase.execute(rateModel);

    return Response.created(
        this.ratesMapper.toDTO(rateCreated), RateMessages.RATE_CONFIGURATED_SUCCESSFULLY.format());
  }

  @PutMapping("/update")
  @PreAuthorize("hasRole('MANAGER')")
  @Transactional
  public Response<ParkingLotsResponse> updateParkingLots(
      @Valid @RequestBody UpsertParkingLotsRequest parkingLots,
      @RequestHeader("Authorization") String accessToken) {
    final UpsertParkingLotsDTO updateParkingLots =
        this.parkingLotsMapper.toModel(parkingLots).toBuilder()
            .tenantId(this.extractTenantId(accessToken))
            .build();

    return Response.ok(
        this.parkingLotsMapper.toDTO(this.updateParkingLotsUseCase.update(updateParkingLots)),
        ParkingLotsMessages.PARKING_LOTS_UPDATED.format());
  }

  private UUID extractTenantId(String accessToken) {
    final String token = accessToken.replace("Bearer ", "");
    final String tenantIdClaim =
        this.authenticationGateway.extractClaim(token, "tenantId").orElseThrow();

    return UUID.fromString(tenantIdClaim);
  }
}
