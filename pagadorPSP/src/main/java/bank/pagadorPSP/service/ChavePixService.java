package bank.pagadorPSP.service;

import bank.pagadorPSP.model.ChavePix;
import bank.pagadorPSP.repository.ChavePixRepository;
import org.springframework.stereotype.Service;

@Service
public class ChavePixService extends _GenericService<ChavePix, ChavePixRepository> {

    private final ChavePixRepository chavePixRepository;

    protected ChavePixService(ChavePixRepository chavePixRepository) {
        super(chavePixRepository);
        this.chavePixRepository = chavePixRepository;
    }
}
