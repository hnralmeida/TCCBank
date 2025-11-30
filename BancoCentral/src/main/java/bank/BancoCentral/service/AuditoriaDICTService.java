package bank.BancoCentral.service;

import bank.BancoCentral.model.AuditoriaDICT;
import bank.BancoCentral.repository.AuditoriaDICTRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaDICTService extends _GenericService<AuditoriaDICT, AuditoriaDICTRepository> {

    private final AuditoriaDICTRepository auditoriaDICTRepository;

    protected AuditoriaDICTService(AuditoriaDICTRepository auditoriaDICTRepository) {
        super(auditoriaDICTRepository);
        this.auditoriaDICTRepository = auditoriaDICTRepository;
    }
}
