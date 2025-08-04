package bank.pagadorPSP.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
