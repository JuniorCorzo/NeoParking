package dev.angelcorzo.neoparking.paymentprovider.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreatePayLink(
		Integer id,
    String title,
    String description,
    int quantity,
    String currency,
    BigDecimal amount,
    byte typeSell,
    String email,
    String mobilePhone,
    String indicative,
    String urlConfirmation,
    String methodConfirmation,
    String expirationDate) {}
