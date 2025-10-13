package dev.angelcorzo.neoparking.model.tenants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Tenants {
    private UUID id;
    private String companyName;
    private OffsetDateTime createdAt;
}