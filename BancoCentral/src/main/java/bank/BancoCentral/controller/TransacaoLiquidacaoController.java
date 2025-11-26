package bank.BancoCentral.controller;

import bank.BancoCentral.model.TransacaoLiquidacao;
import bank.BancoCentral.service.TransacaoLiquidacaoService;

public class TransacaoLiquidacaoController extends _GenericController<TransacaoLiquidacao> {

    private final TransacaoLiquidacaoService transacaoLiquidacaoService;

    protected TransacaoLiquidacaoController(TransacaoLiquidacaoService transacaoLiquidacaoService) {
        super(transacaoLiquidacaoService);
        this.transacaoLiquidacaoService = transacaoLiquidacaoService;
    }
}
