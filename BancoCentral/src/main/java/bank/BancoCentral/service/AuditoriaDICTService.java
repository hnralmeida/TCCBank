package bank.BancoCentral.service;

import bank.BancoCentral.model.AuditoriaDICT;
import bank.BancoCentral.repository.AuditoriaDICTRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaDICTService extends _GenericService<AuditoriaDICT, AuditoriaDICTRepository> {
    protected AuditoriaDICTService() {
    }
}
