package bank.recebedorPSP.controller;

import bank.recebedorPSP.model.ChavePix;
import bank.recebedorPSP.service.ChavePixService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chavepix")
public class ChavePixController extends _GenericController<ChavePix> {

    private final ChavePixService chavePixService;

    protected ChavePixController(ChavePixService chavePixService) {
        super(chavePixService);
        this.chavePixService = chavePixService;
    }
}
