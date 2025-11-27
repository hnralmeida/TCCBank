package bank.recebedorPSP.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.recebedorPSP.model.AuditoriaLog;
import bank.recebedorPSP.service.AuditoriaLogService;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaLogController extends _GenericController<AuditoriaLog> {

    private final AuditoriaLogService auditoriaLogService;

    protected AuditoriaLogController(AuditoriaLogService auditoriaLogService) {
        super(auditoriaLogService);
        this.auditoriaLogService = auditoriaLogService;
    }
}
