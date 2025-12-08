package bank.recebedorPSP.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
