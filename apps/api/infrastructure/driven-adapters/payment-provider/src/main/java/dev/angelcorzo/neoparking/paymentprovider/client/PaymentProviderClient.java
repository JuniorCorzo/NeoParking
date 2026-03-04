package dev.angelcorzo.neoparking.paymentprovider.client;

import dev.angelcorzo.neoparking.model.commons.result.Result;
import dev.angelcorzo.neoparking.model.payments.exceptions.PaymentError;
import dev.angelcorzo.neoparking.paymentprovider.config.PaymentProviderProperties;
import dev.angelcorzo.neoparking.paymentprovider.dtos.request.CreatePayLink;
import dev.angelcorzo.neoparking.paymentprovider.dtos.response.EpaycoResponse;
import dev.angelcorzo.neoparking.paymentprovider.dtos.response.PayLinkResponse;
import io.netty.handler.timeout.ReadTimeoutException;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProviderClient {
  private static final Duration TIMEOUT = Duration.ofSeconds(30);
  private final WebClient webClient;
  private final PaymentProviderProperties properties;

  public Result<PayLinkResponse, PaymentError> createPayLink(CreatePayLink createPayLink) {
    try {
      return Optional.ofNullable(
              webClient
                  .post()
                  .uri("/collection/link/create")
                  .bodyValue(createPayLink)
                  .retrieve()
                  .bodyToMono(new ParameterizedTypeReference<EpaycoResponse<PayLinkResponse>>() {})
                  .timeout(TIMEOUT)
                  .block())
          .map(this::handleResponse)
          .orElseGet(
              () ->
                  Result.failure(
                      new PaymentError.ProviderEmptyResponse(properties.getProviderName())));
    } catch (Exception ex) {
      return Result.failure(handleException(ex));
    }
  }

  private Result<PayLinkResponse, PaymentError> handleResponse(
      EpaycoResponse<PayLinkResponse> response) {
    return response.fold(
        Result::success,
        error -> {
          log.error("Failed to create pay link: {}", error.errors().toString());
          return Result.failure(
              new PaymentError.ProviderValidation("Error creando el link de pago"));
        });
  }

  private PaymentError handleException(Exception ex) {
    log.error("Error inesperado en PaymentProviderClient ({})", properties.getProviderName(), ex);
    if (ex instanceof WebClientResponseException webClientEx) {
      return switch (webClientEx.getStatusCode().value()) {
        case 400 ->
            new PaymentError.ProviderValidation(
                "Bad Request: " + webClientEx.getResponseBodyAsString(), 400);
        case 429 -> new PaymentError.ProviderRateLimited(properties.getProviderName());
        default ->
            new PaymentError.ProviderServerError(
                properties.getProviderName(), webClientEx.getResponseBodyAsString());
      };
    }

    if (ex instanceof ReadTimeoutException) {
      return new PaymentError.ProviderTimeout(properties.getProviderName());
    }

    if (ex instanceof DecodingException) {
      return new PaymentError.ProviderDeserialization(
          properties.getProviderName(), ex.getMessage());
    }

    return new PaymentError.ProviderUnexpectedError(properties.getProviderName(), ex.getMessage());
  }
}
