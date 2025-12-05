package bank.BancoCentral.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.BancoCentral.model.ChaveDICT;

@Repository
public interface ChaveDICTRepository extends JpaRepository<ChaveDICT, UUID> {
    java.util.Optional<ChaveDICT> findByValor(String valor);
}
