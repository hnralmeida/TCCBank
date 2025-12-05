package bank.recebedorPSP.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MensagemSPI")
@Getter
@Setter
public class MensagemSPI {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private MensagemTipo tipo;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private LocalDate dataEnvio;

    @Enumerated(EnumType.STRING)
    private StatusSPI statusEnvio;

    private UUID transacaoId;
}
