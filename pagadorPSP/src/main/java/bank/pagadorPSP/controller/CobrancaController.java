package bank.pagadorPSP.controller;

import bank.pagadorPSP.model.Cobranca;
import bank.pagadorPSP.service.CobrancaService;
import bank.pagadorPSP.service._GenericServiceTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.RoundingMode;

@RestController
@RequestMapping("/cobranca")
public class CobrancaController extends _GenericController<Cobranca> {

    CobrancaService cobrancaService;

    protected CobrancaController(CobrancaService cobrancaService) {
        super(cobrancaService);
        this.cobrancaService = cobrancaService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cobranca> criar(@RequestBody Cobranca cobranca) {
        System.out.println("DEBUG - Recebi cobranca: " + cobranca);

        if (cobranca.getValor() != null) {
            // força o arredondamento para 2 casas
            Double valor = cobranca.getValor();
            valor = Math.round(valor * 100.0) / 100.0;
            cobranca.setValor(valor);
        }

        Cobranca salvo = cobrancaService.criar(cobranca);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
