package bank.pagadorPSP.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "ChavePix")
public class ChavePix {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ChavePixTipo tipo;

    @Column(length = 250)
    private String valor;

    private Boolean ativa;

    @ManyToOne
    private Conta conta;
}
