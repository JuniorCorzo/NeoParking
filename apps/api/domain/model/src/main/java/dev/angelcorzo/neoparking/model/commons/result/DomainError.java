package dev.angelcorzo.neoparking.model.commons.result;

public interface DomainError {
  int status();

  String code();

  String message();

  Severity severity();

  enum Severity {
    INFO,
    WARNING,
    ERROR,
    CRITICAL
  }
}
