package bank.BancoCentral.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ChaveDICT")
public class ChaveDICT {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ChaveTipo tipo;

    @Column(length = 250)
    private String valor;

    @Column(length = 250)
    private String contaBanco;

    @Column(length = 250)
    private String agencia;

    @Column(length = 250)
    private String ispb;

    private Boolean ativa;

    private LocalDateTime dataCriacao;
}
