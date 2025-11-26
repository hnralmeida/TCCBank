package bank.recebedorPSP.service;

import bank.recebedorPSP.model.TransacaoPix;
import bank.recebedorPSP.repository.TransacaoPixRepository;
import org.springframework.stereotype.Service;

@Service
public class TransacaoPixService extends _GenericService<TransacaoPix, TransacaoPixRepository> {
    protected TransacaoPixService() {
    }
}
