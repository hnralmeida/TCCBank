package bank.recebedorPSP.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TransacaoPix")
public class TransacaoPix {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 250)
    private String txid;

    private Double valor;

    @Column(length = 250)
    private String chaveDestino;

    @Enumerated(EnumType.STRING)
    private StatusPix status;

    private LocalDate dataCriacao;

    private LocalDate dataConclusao;

    @ManyToOne
    private Conta contaPagadora;
}
