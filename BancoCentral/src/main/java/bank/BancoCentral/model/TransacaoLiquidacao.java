package bank.BancoCentral.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TransacaoLiquidacao")
@Getter
@Setter
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
