package bank.pagadorPSP.service;

import bank.pagadorPSP.model.MensagemSPI;
import bank.pagadorPSP.repository.MensagemSPIRepository;
import org.springframework.stereotype.Service;

@Service
public class MensagemSPIService extends _GenericService<MensagemSPI, MensagemSPIRepository> {

    private final MensagemSPIRepository mensagemSPIRepository;

    protected MensagemSPIService(MensagemSPIRepository mensagemSPIRepository) {
        super(mensagemSPIRepository);
        this.mensagemSPIRepository = mensagemSPIRepository;
    }
}
