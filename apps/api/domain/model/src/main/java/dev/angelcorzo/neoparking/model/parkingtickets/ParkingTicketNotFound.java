package dev.angelcorzo.neoparking.model.parkingtickets;

import dev.angelcorzo.neoparking.model.exceptions.ErrorMessagesModel;
import java.util.UUID;

public class ParkingTicketNotFound extends RuntimeException {
  public ParkingTicketNotFound(UUID parkingTicketId) {
    super(ErrorMessagesModel.PARKING_TICKET_NOT_FOUND.format(parkingTicketId));
  }
}
