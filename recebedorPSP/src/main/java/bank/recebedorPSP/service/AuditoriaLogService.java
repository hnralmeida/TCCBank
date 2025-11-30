package bank.recebedorPSP.service;

import bank.recebedorPSP.model.AuditoriaLog;
import bank.recebedorPSP.repository.AuditoriaLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaLogService extends _GenericService<AuditoriaLog, AuditoriaLogRepository> {

    private final AuditoriaLogRepository auditoriaLogRepository;

    protected AuditoriaLogService(AuditoriaLogRepository auditoriaLogRepository) {
        super(auditoriaLogRepository);
        this.auditoriaLogRepository = auditoriaLogRepository;
    }
}
