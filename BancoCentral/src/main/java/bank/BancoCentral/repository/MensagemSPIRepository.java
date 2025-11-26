package bank.BancoCentral.repository;

import bank.BancoCentral.model.MensagemSPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MensagemSPIRepository extends JpaRepository<MensagemSPI, UUID> {

}
