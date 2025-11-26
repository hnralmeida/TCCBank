package bank.pagadorPSP.service;

import bank.pagadorPSP.model.CobrancaPix;
import bank.pagadorPSP.repository.CobrancaPixRepository;
import org.springframework.stereotype.Service;

@Service
public class CobrancaPixService extends _GenericService<CobrancaPix, CobrancaPixRepository> {

    private final CobrancaPixRepository cobrancaPixRepository;

    protected CobrancaPixService(CobrancaPixRepository cobrancaPixRepository) {
        super(cobrancaPixRepository);
        this.cobrancaPixRepository = cobrancaPixRepository;
    }
}
