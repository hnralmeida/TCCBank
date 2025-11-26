package bank.recebedorPSP.controller;

import bank.recebedorPSP.model.AuditoriaLog;
import bank.recebedorPSP.service.AuditoriaLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaLogController extends _GenericController<AuditoriaLog> {

    private final AuditoriaLogService auditoriaLogService;

    protected AuditoriaLogController(AuditoriaLogService auditoriaLogService) {
        super(auditoriaLogService);
        this.auditoriaLogService = auditoriaLogService;
    }
}
