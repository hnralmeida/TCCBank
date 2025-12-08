package bank.pagadorPSP.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "Pagamento")
@Getter
@Setter
public class Pagamento {
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

    @Column(length = 250)
    private String chaveDestino;

    @ManyToOne
    private Conta conta;
}
