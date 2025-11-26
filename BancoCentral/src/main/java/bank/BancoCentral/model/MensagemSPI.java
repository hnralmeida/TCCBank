package bank.BancoCentral.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "MensagemSPI")
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

    @ManyToOne
    private TransacaoLiquidacao transacao;
}
