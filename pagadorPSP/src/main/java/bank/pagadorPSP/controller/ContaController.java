package bank.pagadorPSP.controller;

import bank.pagadorPSP.model.Conta;
import bank.pagadorPSP.service.ContaService;
import bank.pagadorPSP.service._GenericServiceTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conta")
public class ContaController extends _GenericController<Conta> {

    private ContaService contaService;

    protected ContaController(ContaService contaService) {
        super(contaService);
        this.contaService = contaService;
    }

    @GetMapping("/numero")
    public ResponseEntity<Conta> buscarPorNumero(@RequestParam("q") String numero) {
        java.util.Optional<Conta> opt = contaService.buscarPorNumero(numero);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(opt.get());
    }
}
