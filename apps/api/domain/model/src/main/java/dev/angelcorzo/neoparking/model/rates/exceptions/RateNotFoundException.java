package dev.angelcorzo.neoparking.model.rates.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;
import java.util.UUID;

public class RateNotFoundException extends RuntimeException {
  public RateNotFoundException(UUID id) {
    super(ErrorMessagesModel.RATE_NOT_FOUND.format(id));
  }
}
