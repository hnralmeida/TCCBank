package bank.BancoCentral.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TransacaoLiquidacao")
public class TransacaoLiquidacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double valor;

    @Column(length = 250)
    private String txid;

    @ManyToOne
    private ParticipanteSPI recebedorISPB;

    @ManyToOne
    private ParticipanteSPI pagadorISPB;

    @Enumerated(EnumType.STRING)
    private StatusSPI status;

    private LocalDate dataCriacao;

    private LocalDate dataLiquidacao;
}
