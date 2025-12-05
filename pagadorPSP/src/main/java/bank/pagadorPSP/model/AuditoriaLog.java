package bank.pagadorPSP.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "AuditoriaLog")
@Getter
@Setter
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
