package dev.angelcorzo.neoparking.api.commons.dto;

public class Response extends RuntimeException {
  public Response(String message) {
    super(message);
  }
}
