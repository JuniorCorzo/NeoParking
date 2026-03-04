package dev.angelcorzo.neoparking.websocket.config;

import dev.angelcorzo.neoparking.model.payments.gateways.PaymentsRepository;
import dev.angelcorzo.neoparking.model.payments.observer.PaymentEventBroker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservableConfig {
  @Bean
  PaymentEventBroker transactionEventBroker(PaymentsRepository paymentsRepository) {
    return PaymentEventBroker.getInstance(paymentsRepository);
  }
}
