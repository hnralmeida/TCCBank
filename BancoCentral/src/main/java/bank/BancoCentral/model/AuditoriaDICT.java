package bank.BancoCentral.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "AuditoriaDICT")
public class AuditoriaDICT {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 250)
    private String evento;

    @Column(length = 250)
    private String detalhe;

    private LocalDate data;

    private UUID referenciaID;
}
