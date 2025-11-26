package bank.pagadorPSP.controller;

import bank.pagadorPSP.model.Conta;
import bank.pagadorPSP.service.ContaService;
import bank.pagadorPSP.service._GenericServiceTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conta")
public class ContaController extends _GenericController<Conta> {

    private ContaService contaService;

    protected ContaController(ContaService contaService) {
        super(contaService);
        this.contaService = contaService;
    }
}
