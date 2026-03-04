package dev.angelcorzo.neoparking.websocket.publisher;

import dev.angelcorzo.neoparking.model.commons.observable.Observable;
import dev.angelcorzo.neoparking.model.payments.observer.PaymentEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RequiredArgsConstructor
public class PaymentEventObservable implements Observable<PaymentEvent> {
  private final UUID paymentId;

  private final SimpMessagingTemplate template;

  @Override
  public void update(PaymentEvent event) {
    String destination = "/topic/payment/" + this.paymentId;

    this.template.convertAndSend(destination, event);
  }
}
