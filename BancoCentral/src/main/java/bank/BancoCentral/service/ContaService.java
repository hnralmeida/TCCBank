package bank.BancoCentral.service;

import bank.BancoCentral.model.Conta;
import bank.BancoCentral.repository.ContaRepository;
import org.springframework.stereotype.Service;

@Service
public class ContaService extends _GenericService<Conta, ContaRepository> {
    protected ContaService() {
    }
}
