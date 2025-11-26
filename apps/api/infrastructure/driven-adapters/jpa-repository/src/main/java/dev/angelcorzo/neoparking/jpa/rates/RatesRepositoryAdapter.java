package dev.angelcorzo.neoparking.jpa.rates;

import dev.angelcorzo.neoparking.jpa.helper.AdapterOperations;
import dev.angelcorzo.neoparking.jpa.rates.mapper.RatesMapper;
import dev.angelcorzo.neoparking.model.rates.Rates;
import dev.angelcorzo.neoparking.model.rates.gateways.RatesRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class RatesRepositoryAdapter
    extends AdapterOperations<Rates, RateData, UUID, RatesRepositoryData>
    implements RatesRepository {

  /**
   * Constructor for AdapterOperations.
   *
   * @param repository The JPA repository instance.
   * @param mapper The mapper for converting between domain and data entities.
   */
  protected RatesRepositoryAdapter(RatesRepositoryData repository, RatesMapper mapper) {
    super(repository, mapper);
  }

  @Override
  public List<Rates> findAllByParkingLotId(UUID parkingLotId) {
    return super.repository.findAllByParking_Id(parkingLotId).stream()
        .map(super::toEntity)
        .toList();
  }

  @Override
  public Rates getReferenceById(UUID uuid) {
    return super.toEntity(super.repository.getReferenceById(uuid));
  }

  @Override
  public Boolean existsById(UUID uuid) {
    return super.repository.existsById(uuid);
  }

  @Override
  public void deleteById(UUID uuid) {
    super.repository.deleteById(uuid);
  }
}
