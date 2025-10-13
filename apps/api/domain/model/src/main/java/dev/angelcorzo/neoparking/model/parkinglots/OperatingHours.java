package dev.angelcorzo.neoparking.model.parkinglots;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetTime;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class OperatingHours {
    private OffsetTime openTime;
    private OffsetTime closeTime;
}
