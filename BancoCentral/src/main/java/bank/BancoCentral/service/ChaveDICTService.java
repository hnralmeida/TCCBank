package bank.BancoCentral.service;

import bank.BancoCentral.model.ChaveDICT;
import bank.BancoCentral.repository.ChaveDICTRepository;
import org.springframework.stereotype.Service;

@Service
public class ChaveDICTService extends _GenericService<ChaveDICT, ChaveDICTRepository> {
    protected ChaveDICTService() {
    }
}
