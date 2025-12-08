package bank.pagadorPSP.controller;

import bank.pagadorPSP.model.Pagamento;
import bank.pagadorPSP.service.PagamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController extends _GenericController<Pagamento> {

    private final PagamentoService pagamentoService;

    protected PagamentoController(PagamentoService pagamentoService) {
        super(pagamentoService);
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/processar")
    public ResponseEntity<Pagamento> processar(@RequestBody Map<String, Object> body) {
        Pagamento p = pagamentoService.processarPagamento(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @GetMapping("/detalhe/{txid}")
    public ResponseEntity<Map<String, Object>> detalhe(@PathVariable String txid) {
        Map<String, Object> result = pagamentoService.detalhe(txid);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/liquidar")
    public ResponseEntity<Pagamento> liquidar(@RequestBody Map<String, Object> body) {
        Pagamento p = pagamentoService.liquidarPagamento(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }
}
