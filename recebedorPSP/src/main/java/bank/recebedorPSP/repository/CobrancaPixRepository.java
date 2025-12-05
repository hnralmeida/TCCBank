package bank.recebedorPSP.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.recebedorPSP.model.CobrancaPix;

@Repository
public interface CobrancaPixRepository extends JpaRepository<CobrancaPix, UUID> {
    java.util.Optional<CobrancaPix> findByTxid(String txid);
}
