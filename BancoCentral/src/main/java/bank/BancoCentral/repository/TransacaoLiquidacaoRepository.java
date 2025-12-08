package bank.BancoCentral.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.BancoCentral.model.TransacaoLiquidacao;

@Repository
public interface TransacaoLiquidacaoRepository extends JpaRepository<TransacaoLiquidacao, UUID> {
    java.util.Optional<TransacaoLiquidacao> findByTxid(String txid);
}
