package bank.BancoCentral.service;

import bank.BancoCentral.model.ParticipanteSPI;
import bank.BancoCentral.repository.ParticipanteSPIRepository;
import org.springframework.stereotype.Service;

@Service
public class ParticipanteSPIService extends _GenericService<ParticipanteSPI, ParticipanteSPIRepository> {
    protected ParticipanteSPIService() {
    }
}
