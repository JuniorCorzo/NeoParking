package dev.angelcorzo.neoparking.usecase.showratesbyparkinglot;

import dev.angelcorzo.neoparking.model.rates.Rates;
import dev.angelcorzo.neoparking.model.rates.gateways.RatesRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ShowRatesByParkingLotUseCase {
  private final RatesRepository ratesRepository;

  public List<Rates> execute(UUID parkingLots) {
    return this.ratesRepository.findAllByParkingLotId(parkingLots);
  }
}
