package bank.BancoCentral.service;

import bank.BancoCentral.model.TransacaoLiquidacao;
import bank.BancoCentral.repository.TransacaoLiquidacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class TransacaoLiquidacaoService extends _GenericService<TransacaoLiquidacao, TransacaoLiquidacaoRepository> {

    private final TransacaoLiquidacaoRepository transacaoLiquidacaoRepository;

    protected TransacaoLiquidacaoService(TransacaoLiquidacaoRepository transacaoLiquidacaoRepository) {
        super(transacaoLiquidacaoRepository);
        this.transacaoLiquidacaoRepository = transacaoLiquidacaoRepository;
    }

    public java.util.Optional<TransacaoLiquidacao> buscarPorTxid(String txid) {
        return transacaoLiquidacaoRepository.findByTxid(txid);
    }
}
