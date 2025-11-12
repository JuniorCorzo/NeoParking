package dev.angelcorzo.neoparking.api.slot.controller;

import dev.angelcorzo.neoparking.api.commons.dto.Response;
import dev.angelcorzo.neoparking.api.slot.dto.CreateSlotRequest;
import dev.angelcorzo.neoparking.api.slot.dto.SlotResponse;
import dev.angelcorzo.neoparking.api.slot.dto.UpdateSlotRequest;
import dev.angelcorzo.neoparking.api.slot.mappers.SlotsMapper;
import dev.angelcorzo.neoparking.model.authentication.gateway.AuthenticationGateway;
import dev.angelcorzo.neoparking.model.slots.Slots;
import dev.angelcorzo.neoparking.usecase.createslot.CreateSlotUseCase;
import dev.angelcorzo.neoparking.usecase.editslot.EditSlotUseCase;
import dev.angelcorzo.neoparking.usecase.listslots.ListSlotsUseCase;
import dev.angelcorzo.neoparking.usecase.removeslot.RemoveSlotUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/slots")
@RequiredArgsConstructor
public class SlotsController {
  private final SlotsMapper slotsMapper;
  private final AuthenticationGateway authenticationGateway;

  private final ListSlotsUseCase listSlotsUseCase;
  private final CreateSlotUseCase createSlotUseCase;
  private final EditSlotUseCase editSlotUseCase;
  private final RemoveSlotUseCase removeSlotUseCase;

  @GetMapping("/list")
  @PreAuthorize("hasRole('OPERATOR')")
  Response<List<SlotResponse>> listSlots(@RequestParam("parking") UUID parkingLotId) {
    List<SlotResponse> slots =
        this.listSlotsUseCase.execute(parkingLotId).stream().map(slotsMapper::toDto).toList();

    return Response.ok(slots, "Slots retrieved successfully");
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('MANAGER')")
  Response<SlotResponse> createSlot(
      @RequestBody CreateSlotRequest request, @RequestHeader("Authorization") String accessToken) {
    final CreateSlotUseCase.CreateSlotCommand slot =
        this.slotsMapper.toModel(request).toBuilder()
            .tenantId(this.extractTenantId(accessToken))
            .build();

    final Slots createdSlot = this.createSlotUseCase.execute(slot);

    return Response.created(this.slotsMapper.toDto(createdSlot), "Slot created successfully");
  }

  @PutMapping("/update")
  @PreAuthorize("hasRole('MANAGER')")
  Response<SlotResponse> updateSlot(@RequestBody UpdateSlotRequest request) {
    final EditSlotUseCase.UpdateSlotCommand slot = this.slotsMapper.toModel(request);
    final Slots updatedSlot = this.editSlotUseCase.execute(slot);

    return Response.ok(this.slotsMapper.toDto(updatedSlot), "Slot updated successfully");
  }

  @DeleteMapping("/delete/{slotId}")
  @PreAuthorize("hasRole('MANAGER')")
  Response<Void> deleteSlot(@PathVariable("slotId") UUID slotId) {
    this.removeSlotUseCase.execute(slotId);

    return Response.ok(null, "Slot deleted successfully");
  }

  private UUID extractTenantId(String accessToken) {
    final String token = accessToken.replace("Bearer ", "");
    final String tenantIdClaim =
        this.authenticationGateway.extractClaim(token, "tenantId").orElseThrow();

    return UUID.fromString(tenantIdClaim);
  }
}
