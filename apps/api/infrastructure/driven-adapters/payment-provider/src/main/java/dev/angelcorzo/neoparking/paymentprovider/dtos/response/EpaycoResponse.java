package dev.angelcorzo.neoparking.paymentprovider.dtos.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.angelcorzo.neoparking.paymentprovider.config.json.SuccessTypeIdResolver;
import java.util.function.Function;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import tools.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "success", visible = true)
@JsonTypeIdResolver(value = SuccessTypeIdResolver.class)
@JsonSubTypes({
  @JsonSubTypes.Type(value = EpaycoResponse.Success.class, name = "true"),
  @JsonSubTypes.Type(value = EpaycoResponse.Failure.class, name = "false")
})
public sealed interface EpaycoResponse<T> permits EpaycoResponse.Success, EpaycoResponse.Failure {
  boolean success();

  String titleResponse();

  String textResponse();

  String lastResponse();

  default T get() {
    if (this.isFailure()) return null;

    return ((Success<T>) this).data;
  }

  default EpaycoError error() {
    if (this.isSuccess()) return null;

    return ((Failure<T>) this).data;
  }

  default <R> R fold(Function<T, R> onSuccess, Function<EpaycoError, R> onFailure) {
    return isSuccess() ? onSuccess.apply(get()) : onFailure.apply(error());
  }

  default boolean isSuccess() {
    return this instanceof EpaycoResponse.Success;
  }

  default boolean isFailure() {
    return this instanceof EpaycoResponse.Failure;
  }

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  record Success<T>(
      boolean success, String titleResponse, String textResponse, String lastResponse, T data)
      implements EpaycoResponse<T> {}

  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  record Failure<T>(
      boolean success,
      String titleResponse,
      String textResponse,
      String lastResponse,
      EpaycoError data)
      implements EpaycoResponse<T> {}
}
