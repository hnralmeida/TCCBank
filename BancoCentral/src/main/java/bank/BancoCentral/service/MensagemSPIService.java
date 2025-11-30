package bank.BancoCentral.service;

import bank.BancoCentral.model.MensagemSPI;
import bank.BancoCentral.repository.MensagemSPIRepository;
import org.springframework.stereotype.Service;

@Service
public class MensagemSPIService extends _GenericService<MensagemSPI, MensagemSPIRepository> {

    private final MensagemSPIRepository mensagemSPIRepository;

    protected MensagemSPIService(MensagemSPIRepository mensagemSPIRepository) {
        super(mensagemSPIRepository);
        this.mensagemSPIRepository = mensagemSPIRepository;
    }
}
