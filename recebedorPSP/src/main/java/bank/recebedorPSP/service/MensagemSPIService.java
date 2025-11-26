package bank.recebedorPSP.service;

import bank.recebedorPSP.model.MensagemSPI;
import bank.recebedorPSP.repository.MensagemSPIRepository;
import org.springframework.stereotype.Service;

@Service
public class MensagemSPIService extends _GenericService<MensagemSPI, MensagemSPIRepository> {
    protected MensagemSPIService() {
    }
}
