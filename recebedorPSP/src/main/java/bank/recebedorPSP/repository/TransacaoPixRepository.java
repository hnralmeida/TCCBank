package bank.recebedorPSP.repository;

import bank.recebedorPSP.model.TransacaoPix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransacaoPixRepository extends JpaRepository<TransacaoPix, UUID> {

}
