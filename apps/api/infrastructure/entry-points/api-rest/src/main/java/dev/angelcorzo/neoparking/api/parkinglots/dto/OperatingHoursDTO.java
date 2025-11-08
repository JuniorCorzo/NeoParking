package dev.angelcorzo.neoparking.api.parkinglots.dto;

import java.time.OffsetTime;

public record OperatingHoursDTO(OffsetTime openTime, OffsetTime closeTime) {}
