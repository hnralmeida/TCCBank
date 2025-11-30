package bank.recebedorPSP.service;

import bank.recebedorPSP.model.Cliente;
import bank.recebedorPSP.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends _GenericService<Cliente, ClienteRepository> {

    private final ClienteRepository clienteRepository;

    protected ClienteService(ClienteRepository clienteRepository) {
        super(clienteRepository);
        this.clienteRepository = clienteRepository;
    }
}
