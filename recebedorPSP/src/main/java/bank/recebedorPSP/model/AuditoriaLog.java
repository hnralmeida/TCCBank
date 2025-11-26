package bank.recebedorPSP.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "AuditoriaLog")
public class AuditoriaLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 250)
    private String evento;

    @Column(length = 250)
    private String detalhe;

    private LocalDate data;

    private UUID referenciaId;
}
