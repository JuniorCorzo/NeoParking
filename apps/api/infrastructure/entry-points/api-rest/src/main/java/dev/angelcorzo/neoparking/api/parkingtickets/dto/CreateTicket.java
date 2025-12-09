package dev.angelcorzo.neoparking.api.parkingtickets.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateTicket(UUID slotId, UUID rateId, String plate) {}
