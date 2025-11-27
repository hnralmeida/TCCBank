package bank.recebedorPSP.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.recebedorPSP.model.Conta;
import bank.recebedorPSP.service.ContaService;

@RestController
@RequestMapping("/conta")
public class ContaController extends _GenericController<Conta> {

    private final ContaService contaService;

    protected ContaController(ContaService contaService) {
        super(contaService);
        this.contaService = contaService;
    }
}
