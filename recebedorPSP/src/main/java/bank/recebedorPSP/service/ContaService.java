package bank.recebedorPSP.service;

import bank.recebedorPSP.model.Conta;
import bank.recebedorPSP.repository.ContaRepository;
import org.springframework.stereotype.Service;

@Service
public class ContaService extends _GenericService<Conta, ContaRepository> {

    private final ContaRepository contaRepository;

    protected ContaService(ContaRepository contaRepository) {
        super(contaRepository);
        this.contaRepository = contaRepository;
    }

    @Override
    public Conta criar(Conta entity) {
        String agencia = entity.getAgencia();
        String numero = entity.getNumero();
        if (agencia != null && numero != null && !agencia.isBlank() && !numero.isBlank()) {
            java.util.Optional<Conta> existing = contaRepository.findByAgenciaAndNumero(agencia, numero);
            if (existing.isPresent()) {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Agência+Número já existente");
            }
        }
        return super.criar(entity);
    }
}
