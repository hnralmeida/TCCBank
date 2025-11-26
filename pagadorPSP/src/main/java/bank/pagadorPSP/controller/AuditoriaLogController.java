package bank.pagadorPSP.controller;

import bank.pagadorPSP.model.AuditoriaLog;
import bank.pagadorPSP.service.AuditoriaLogService;
import bank.pagadorPSP.service._GenericServiceTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auditorialog")
public class AuditoriaLogController extends _GenericController<AuditoriaLog> {

    private final AuditoriaLogService auditoriaLogService;

    protected AuditoriaLogController(AuditoriaLogService auditoriaLogService) {
        super(auditoriaLogService);
        this.auditoriaLogService = auditoriaLogService;
    }
}
