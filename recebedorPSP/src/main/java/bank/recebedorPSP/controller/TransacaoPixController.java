package bank.recebedorPSP.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.recebedorPSP.model.TransacaoPix;
import bank.recebedorPSP.service.TransacaoPixService;

@RestController
@RequestMapping("/transacaopix")
public class TransacaoPixController extends _GenericController<TransacaoPix> {

    private final TransacaoPixService transacaoPixService;

    protected TransacaoPixController(TransacaoPixService transacaoPixService) {
        super(transacaoPixService);
        this.transacaoPixService = transacaoPixService;
    }
}
