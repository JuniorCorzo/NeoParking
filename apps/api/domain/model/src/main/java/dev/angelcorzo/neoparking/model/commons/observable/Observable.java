package dev.angelcorzo.neoparking.model.commons.observable;

public interface Observable<T> {
  void update(T event);
}
