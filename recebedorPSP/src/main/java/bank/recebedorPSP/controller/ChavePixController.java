package bank.recebedorPSP.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.recebedorPSP.model.ChavePix;
import bank.recebedorPSP.service.ChavePixService;

@RestController
@RequestMapping("/chavepix")
public class ChavePixController extends _GenericController<ChavePix> {

    private final ChavePixService chavePixService;

    protected ChavePixController(ChavePixService chavePixService) {
        super(chavePixService);
        this.chavePixService = chavePixService;
    }
}
