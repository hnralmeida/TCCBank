package bank.pagadorPSP.repository;

import bank.pagadorPSP.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {
    java.util.Optional<Pagamento> findByTxid(String txid);
}
