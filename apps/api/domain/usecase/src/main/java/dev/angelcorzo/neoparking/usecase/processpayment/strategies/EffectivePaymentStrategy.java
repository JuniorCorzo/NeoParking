package dev.angelcorzo.neoparking.usecase.processpayment.strategies;

import dev.angelcorzo.neoparking.model.commons.result.Result;
import dev.angelcorzo.neoparking.model.parkingtickets.gateways.ParkingTicketsRepository;
import dev.angelcorzo.neoparking.model.payments.Payments;
import dev.angelcorzo.neoparking.model.payments.exceptions.PaymentError;
import dev.angelcorzo.neoparking.model.payments.factories.PaymentFactory;
import dev.angelcorzo.neoparking.model.payments.gateways.PaymentsRepository;
import dev.angelcorzo.neoparking.model.transactions.Transactions;
import dev.angelcorzo.neoparking.model.transactions.enums.TransactionStatus;
import dev.angelcorzo.neoparking.model.transactions.gateways.TransactionsRepository;
import dev.angelcorzo.neoparking.usecase.processpayment.strategies.commands.PaymentCommand;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EffectivePaymentStrategy implements PaymentStrategy {
  private final PaymentsRepository paymentsRepository;
  private final ParkingTicketsRepository parkingTicketsRepository;
  private final TransactionsRepository transactionsRepository;

  @Override
  public Result<Payments, PaymentError> processPayment(PaymentCommand command) {
    final BigDecimal amountToCharge = command.amounts().getTotal();
    final Payments payment = PaymentFactory.registerEffective(command.ticket(), amountToCharge);

    return this.paymentsRepository.processPayment(payment).onSuccess(this::handlerSuccess);
  }

  private Result<Payments, PaymentError> handlerSuccess(Payments payment) {
    try {
      this.parkingTicketsRepository.prepareCheckout(
          payment.getParkingTicket().getId(), payment.getAmount());

      this.registerTransaction(payment);
      this.parkingTicketsRepository.closeTicket(payment.getParkingTicket().getId());

      return Result.success(payment);
    } catch (Exception e) {
      return Result.failure(new PaymentError.DatabaseError(e.getMessage()));
    }
  }

  private Transactions registerTransaction(Payments payment) {
    Transactions transaction =
        Transactions.builder()
            .payment(payment)
            .amount(payment.getAmount())
            .currency("COP")
            .status(TransactionStatus.APPROVED)
            .build();

    return this.transactionsRepository.save(transaction);
  }
}
