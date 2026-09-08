package dev.angelcorzo.nivo.domain.usecase.rate;

import dev.angelcorzo.nivo.domain.model.rates.gateways.RatesRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteRateUseCase {
  private final RatesRepository ratesRepository;

  public void execute(UUID id) {
    this.ratesRepository.deleteById(id);
  }
}
