package bank.pagadorPSP.service;

import bank.pagadorPSP.model.Conta;
import bank.pagadorPSP.repository.ContaRepository;
import org.springframework.stereotype.Service;

@Service
public class ContaService extends _GenericService<Conta, ContaRepository> {

    private final ContaRepository contaRepository;

    protected ContaService(ContaRepository contaRepository) {
        super(contaRepository);
        this.contaRepository = contaRepository;
    }

    public java.util.Optional<Conta> buscarPorNumero(String numero) {
        java.util.List<Conta> list = contaRepository.findAllByNumero(numero);
        if (list == null || list.isEmpty()) return java.util.Optional.empty();
        return java.util.Optional.of(list.get(0));
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
