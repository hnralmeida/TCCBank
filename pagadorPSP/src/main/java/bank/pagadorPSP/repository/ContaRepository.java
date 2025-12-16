package bank.pagadorPSP.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.pagadorPSP.model.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, UUID> {
    java.util.List<Conta> findAllByNumero(String numero);
    java.util.Optional<Conta> findByAgenciaAndNumero(String agencia, String numero);

}

