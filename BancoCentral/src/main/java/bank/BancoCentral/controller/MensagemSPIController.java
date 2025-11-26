package bank.BancoCentral.controller;

import bank.BancoCentral.model.MensagemSPI;
import bank.BancoCentral.service.MensagemSPIService;

public class MensagemSPIController extends _GenericController<MensagemSPI> {

    private final MensagemSPIService mensagemSPIService;

    protected MensagemSPIController(MensagemSPIService mensagemSPIService) {
        super(mensagemSPIService);
        this.mensagemSPIService = mensagemSPIService;
    }
}
