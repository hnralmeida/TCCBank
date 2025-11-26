package bank.pagadorPSP.controller;

import bank.pagadorPSP.model.CobrancaPix;
import bank.pagadorPSP.service.CobrancaPixService;
import bank.pagadorPSP.service._GenericServiceTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cobrancapix")
public class CobrancaPixController extends _GenericController<CobrancaPix> {

    private final CobrancaPixService cobrancaPixService;

    protected CobrancaPixController(CobrancaPixService cobrancaPixService) {
        super(cobrancaPixService);
        this.cobrancaPixService = cobrancaPixService;
    }
}
