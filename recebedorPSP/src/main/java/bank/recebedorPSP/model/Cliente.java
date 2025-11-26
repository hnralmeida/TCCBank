package bank.recebedorPSP.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 250)
    private String nome;

    @Column(length = 250)
    private String registro;

    @Column(length = 250)
    private String email;

    @Column(length = 25)
    private String telefone;
}
