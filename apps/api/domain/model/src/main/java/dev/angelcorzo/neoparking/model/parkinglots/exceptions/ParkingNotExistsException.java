package dev.angelcorzo.neoparking.model.parkinglots.exceptions;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;

import java.util.UUID;

public class ParkingNotExistsException extends RuntimeException {
  public ParkingNotExistsException(UUID id) {
    super(ErrorMessagesModel.PARKING_NOT_EXISTS.format(id));
  }
}
