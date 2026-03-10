package dev.angelcorzo.neoparking.jpa.transactions;

import dev.angelcorzo.neoparking.jpa.helper.AdapterOperations;
import dev.angelcorzo.neoparking.jpa.transactions.mappers.TransactionMapper;
import dev.angelcorzo.neoparking.model.transactions.Transactions;
import dev.angelcorzo.neoparking.model.transactions.gateways.TransactionsRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryAdapter
    extends AdapterOperations<Transactions, TransactionsData, UUID, TransactionRepositoryData>
    implements TransactionsRepository {

  /**
   * Constructor for AdapterOperations.
   *
   * @param repository The JPA repository instance.
   * @param mapper The mapper for converting between domain and data entities.
   */
  protected TransactionRepositoryAdapter(
      TransactionRepositoryData repository, TransactionMapper mapper) {
    super(repository, mapper);
  }

  @Override
  public Optional<Transactions> findBySupplierRef(String supplierRef) {
    return super.repository.findBySupplierRef(supplierRef).map(super::toEntity);
  }

  @Override
  public boolean existsBySupplierRef(String supplierRef) {
    return super.repository.existsBySupplierRef(supplierRef);
  }
}
