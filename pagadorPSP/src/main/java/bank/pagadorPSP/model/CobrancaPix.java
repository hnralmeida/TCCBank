package bank.pagadorPSP.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "CobrancaPix")
public class CobrancaPix {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 250)
    private String txid;

    private Double valor;

    @Column(length = 250)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusPix status;

    @ManyToOne
    private ChavePix chavePix;
}
