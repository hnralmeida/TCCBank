package bank.BancoCentral.repository;

import bank.BancoCentral.model.ParticipanteSPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParticipanteSPIRepository extends JpaRepository<ParticipanteSPI, UUID> {

}
