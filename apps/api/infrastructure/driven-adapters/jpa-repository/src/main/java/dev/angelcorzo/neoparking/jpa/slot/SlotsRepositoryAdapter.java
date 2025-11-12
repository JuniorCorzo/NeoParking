package dev.angelcorzo.neoparking.jpa.slot;

import dev.angelcorzo.neoparking.jpa.helper.AdapterOperations;
import dev.angelcorzo.neoparking.jpa.slot.mappers.SlotsMappers;
import dev.angelcorzo.neoparking.model.slots.Slots;
import dev.angelcorzo.neoparking.model.slots.gateways.SlotsRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class SlotsRepositoryAdapter
    extends AdapterOperations<Slots, SlotsData, UUID, SlotsRepositoryData>
    implements SlotsRepository {

  /**
   * Constructor for AdapterOperations.
   *
   * @param repository The JPA repository instance.
   * @param mapper The mapper for converting between domain and data entities.
   */
  protected SlotsRepositoryAdapter(SlotsRepositoryData repository, SlotsMappers mapper) {
    super(repository, mapper);
  }

  @Override
  public List<Slots> findAllByParkingLotsId(UUID parkingLotsId) {
    return super.repository.findAllByParking_Id(parkingLotsId).stream()
        .map(super::toEntity)
        .toList();
  }

  @Override
  public Boolean existsById(UUID id) {
    return super.repository.existsById(id);
  }

  @Override
  public void deleteById(UUID id) {
    super.repository.deleteById(id);
  }
}
