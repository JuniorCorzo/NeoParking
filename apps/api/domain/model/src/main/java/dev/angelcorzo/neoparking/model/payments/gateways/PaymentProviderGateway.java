package dev.angelcorzo.neoparking.model.payments.gateways;

import dev.angelcorzo.neoparking.model.commons.result.Result;
import dev.angelcorzo.neoparking.model.parkingtickets.ParkingTickets;
import dev.angelcorzo.neoparking.model.payments.exceptions.PaymentError;
import dev.angelcorzo.neoparking.model.payments.valueobject.ProviderMetadata;
import dev.angelcorzo.neoparking.model.payments.valueobject.check_out.CheckOut;
import dev.angelcorzo.neoparking.model.transactions.Transactions;
import java.math.BigDecimal;
import java.util.Map;

public interface PaymentProviderGateway {
  Result<Transactions, PaymentError> getTransactionDetails(String checkoutSessionId);

  Result<ProviderMetadata, PaymentError> processPayment(
      ParkingTickets tickets, BigDecimal amount, CheckOut command);

  Result<Transactions, PaymentError> confirmationPay(Map<String, String> receipt);
}
