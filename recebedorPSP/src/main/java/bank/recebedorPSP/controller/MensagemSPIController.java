package bank.recebedorPSP.controller;

import bank.recebedorPSP.model.MensagemSPI;
import bank.recebedorPSP.service.MensagemSPIService;
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
