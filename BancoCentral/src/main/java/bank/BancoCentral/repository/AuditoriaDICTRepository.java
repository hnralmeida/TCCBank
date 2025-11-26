package bank.BancoCentral.repository;

import bank.BancoCentral.model.AuditoriaDICT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditoriaDICTRepository extends JpaRepository<AuditoriaDICT, UUID> {

}
