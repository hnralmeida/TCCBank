package bank.pagadorPSP.controller;

import bank.pagadorPSP.model.MensagemSPI;
import bank.pagadorPSP.service.MensagemSPIService;
import bank.pagadorPSP.service._GenericServiceTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mensagemspi")
public class MensagemSPIController extends _GenericController<MensagemSPI> {

    private final MensagemSPIService mensagemSPIService;

    protected MensagemSPIController(MensagemSPIService mensagemSPIService) {
        super(mensagemSPIService);
        this.mensagemSPIService = mensagemSPIService;
    }
}
