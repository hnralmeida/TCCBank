package bank.pagadorPSP.controller;

import bank.pagadorPSP.model.ChavePix;
import bank.pagadorPSP.service.ChavePixService;
import bank.pagadorPSP.service._GenericServiceTypes;
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
