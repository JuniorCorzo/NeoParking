package dev.angelcorzo.neoparking.model.transactions.gateways;

import dev.angelcorzo.neoparking.model.transactions.Transactions;
import java.util.Optional;

public interface TransactionsRepository {
  Optional<Transactions> findBySupplierRef(String supplierRef);

  boolean existsBySupplierRef(String supplierRef);

  Transactions save(Transactions transaction);
}
