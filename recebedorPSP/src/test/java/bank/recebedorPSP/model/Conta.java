package bank.recebedorPSP.model;

import jakarta.persistence.*;

import java.util.UUID;

public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
