package bank.BancoCentral.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.BancoCentral.model.ParticipanteSPI;

@Repository
public interface ParticipanteSPIRepository extends JpaRepository<ParticipanteSPI, UUID> {
    java.util.Optional<ParticipanteSPI> findByCodigoISPB(String codigoISPB);
}
