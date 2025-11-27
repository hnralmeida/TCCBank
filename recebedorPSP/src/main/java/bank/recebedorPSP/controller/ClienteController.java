package bank.recebedorPSP.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.recebedorPSP.model.Cliente;
import bank.recebedorPSP.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController extends _GenericController<Cliente> {

    private final ClienteService clienteService;

    protected ClienteController(ClienteService clienteService) {
        super(clienteService);
        this.clienteService = clienteService;
    }
}
