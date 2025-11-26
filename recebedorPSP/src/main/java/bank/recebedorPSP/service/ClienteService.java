package bank.recebedorPSP.service;

import bank.recebedorPSP.model.Cliente;
import bank.recebedorPSP.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends _GenericService<Cliente, ClienteRepository> {
    protected ClienteService() {
    }
}
