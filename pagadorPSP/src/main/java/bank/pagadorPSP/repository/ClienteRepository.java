package bank.pagadorPSP.repository;

import bank.pagadorPSP.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    java.util.Optional<Cliente> findByRegistro(String registro);

}
