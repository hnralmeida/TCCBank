package bank.pagadorPSP.controller;

import bank.pagadorPSP.model.Cliente;
import bank.pagadorPSP.service.ClienteService;
import bank.pagadorPSP.service._GenericServiceTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController extends _GenericController<Cliente> {

    private final ClienteService clienteService;

    protected ClienteController(ClienteService clienteService) {
        super(clienteService);
        this.clienteService = clienteService;
    }
}
