package dev.angelcorzo.neoparking.jpa.payments;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface PaymentsRepositoryData extends JpaRepository<PaymentsData, UUID> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
  @Query(value = "SELECT p FROM PaymentsData p WHERE  p.checkoutSessionId = :checkoutSessionId")
  Optional<PaymentsData> findByCheckoutSessionId(
      @Param("checkoutSessionId") String checkoutSessionId);

  @Query(
      value =
          """
          SELECT p FROM PaymentsData p
          WHERE p.parkingTicket.id = :parkingTicketId
          AND p.status IN ('PENDING_PAYMENT', 'PENDING_CHECKOUT')
          """)
  Optional<PaymentsData> findByParkingTicketId(@Param("parkingTicketId") UUID parkingTicketId);

  boolean existsByParkingTicketId(UUID parkingTicketId);
}
