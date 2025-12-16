package bank.pagadorPSP.model;

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
@Table(name = "Conta", uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = {"agencia", "numero"})
})
@Getter
@Setter
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 250)
    private String numero;

    @Column(length = 250)
    private String agencia;

    private Double saldo;

    @Enumerated(EnumType.STRING)
    private ContaTipo tipo;

    @ManyToOne
    private Cliente cliente;
}
