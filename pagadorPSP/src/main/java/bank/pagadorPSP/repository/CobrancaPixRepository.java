package bank.pagadorPSP.repository;

import bank.pagadorPSP.model.CobrancaPix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CobrancaPixRepository extends JpaRepository<CobrancaPix, UUID> {

}
