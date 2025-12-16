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

    @Override
    public Cliente criar(Cliente entity) {
        String reg = entity.getRegistro();
        if (reg != null && !reg.isBlank()) {
            java.util.Optional<Cliente> existing = clienteRepository.findByRegistro(reg);
            if (existing.isPresent()) {
                throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Registro já cadastrado");
            }
        }
        return super.criar(entity);
    }
}
