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
}
