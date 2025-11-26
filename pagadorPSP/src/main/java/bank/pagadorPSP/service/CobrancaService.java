package bank.pagadorPSP.service;

import bank.pagadorPSP.model.Cobranca;
import bank.pagadorPSP.repository.CobrancaRepository;
import org.springframework.stereotype.Service;

@Service
public class CobrancaService extends _GenericService<Cobranca, CobrancaRepository> {

    private final CobrancaRepository cobrancaRepository;

    protected CobrancaService(CobrancaRepository cobrancaRepository) {
        super(cobrancaRepository);
        this.cobrancaRepository = cobrancaRepository;
    }
}
