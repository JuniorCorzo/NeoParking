package dev.angelcorzo.neoparking.jpa.payments;

import dev.angelcorzo.neoparking.jpa.helper.AdapterOperations;
import dev.angelcorzo.neoparking.jpa.payments.mapper.PaymentMapper;
import dev.angelcorzo.neoparking.model.commons.result.Result;
import dev.angelcorzo.neoparking.model.payments.Payments;
import dev.angelcorzo.neoparking.model.payments.exceptions.PaymentError;
import dev.angelcorzo.neoparking.model.payments.gateways.PaymentsRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class PaymentsRepositoryAdapter
    extends AdapterOperations<Payments, PaymentsData, UUID, PaymentsRepositoryData>
    implements PaymentsRepository {

  protected PaymentsRepositoryAdapter(PaymentsRepositoryData repository, PaymentMapper mapper) {
    super(repository, mapper);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Payments> findByCheckoutSessionId(String transactionId) {
    return super.repository.findByCheckoutSessionId(transactionId).map(super::toEntity);
  }

  @Override
  public Result<Payments, PaymentError> processPayment(Payments entity) {
    try {
      final Payments payment = super.save(entity);
      return Result.success(payment);
    } catch (DataIntegrityViolationException exception) {
      log.error("Payment duplicate: {}", exception.getMessage());
      return Result.failure(new PaymentError.Duplicate(entity.getParkingTicket().getId()));
    } catch (Exception e) {
      log.error("Error saving payment: {}", e.getMessage());
      return Result.failure(new PaymentError.DatabaseError(e.getMessage()));
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Payments getReferenceById(UUID id) {
    return super.mapper.toEntity(super.repository.getReferenceById(id));
  }

  @Override
  public boolean existsByParkingTicketId(UUID parkingTicketId) {
    return super.repository.existsByParkingTicketId(parkingTicketId);
  }

  @Override
  public Optional<Payments> findByParkingTicketId(UUID parkingTicketId) {
    return super.repository.findByParkingTicketId(parkingTicketId).map(super::toEntity);
  }
}
