package bank.pagadorPSP.service;

import bank.pagadorPSP.model.AuditoriaLog;
import bank.pagadorPSP.repository.AuditoriaLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaLogService extends _GenericService<AuditoriaLog, AuditoriaLogRepository> {

    private final AuditoriaLogRepository auditoriaLogRepository;

    protected AuditoriaLogService(AuditoriaLogRepository auditoriaLogRepository) {
        super(auditoriaLogRepository);
        this.auditoriaLogRepository = auditoriaLogRepository;
    }
}
