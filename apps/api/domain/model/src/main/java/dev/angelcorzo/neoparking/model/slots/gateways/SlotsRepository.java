package dev.angelcorzo.neoparking.model.slots.gateways;

import dev.angelcorzo.neoparking.model.slots.Slots;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SlotsRepository {
  Optional<Slots> findById(UUID id);

  List<Slots> findAllByParkingLotsId(UUID parkingLotsId);

  Slots getReferenceById(UUID id);

  Boolean existsById(UUID id);

  Slots save(Slots slot);

  void deleteById(UUID id);
}
