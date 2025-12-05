package bank.BancoCentral.model;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PARTICIPANTESPI")
@Getter
@Setter
public class ParticipanteSPI {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 250)
    private String nome;

    @Column(length = 25)
    private String codigoISPB;

    @Enumerated(EnumType.STRING)
    private ParticipanteTipo tipo;

    private Double saldoContaRes;

    private Boolean ativo;
}
