package bank.pagadorPSP.service;

import bank.pagadorPSP.model.Cliente;
import bank.pagadorPSP.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends _GenericService<Cliente, ClienteRepository> {

    private final ClienteRepository clienteRepository;

    protected ClienteService(ClienteRepository clienteRepository) {
        super(clienteRepository);
        this.clienteRepository = clienteRepository;
    }
}
