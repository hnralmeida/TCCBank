package bank.BancoCentral.repository;

import bank.BancoCentral.model.TransacaoLiquidacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransacaoLiquidacaoRepository extends JpaRepository<TransacaoLiquidacao, UUID> {

}
