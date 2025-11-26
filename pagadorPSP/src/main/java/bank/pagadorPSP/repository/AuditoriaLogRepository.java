package bank.pagadorPSP.repository;

import bank.pagadorPSP.model.AuditoriaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditoriaLogRepository extends JpaRepository<AuditoriaLog, UUID> {

}
