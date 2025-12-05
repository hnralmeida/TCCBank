package bank.BancoCentral.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.BancoCentral.model.ChaveDICT;
import bank.BancoCentral.repository.ChaveDICTRepository;
import bank.BancoCentral.service.ChaveDICTService;

@RestController
@RequestMapping("/chave-dict")
public class ChaveDICTController extends _GenericController<ChaveDICT> {

    private final ChaveDICTService chaveDICTService;
    private final ChaveDICTRepository chaveDICTRepository;

    protected ChaveDICTController(ChaveDICTService chaveDICTService, ChaveDICTRepository chaveDICTRepository) {
        super(chaveDICTService);
        this.chaveDICTService = chaveDICTService;
        this.chaveDICTRepository = chaveDICTRepository;
    }

    @GetMapping("/{valor}")
    public ResponseEntity<ChaveDICT> buscarPorValor(@PathVariable String valor) {
        java.util.Optional<ChaveDICT> opt = chaveDICTRepository.findByValor(valor);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(opt.get());
    }

    @GetMapping("/valor")
    public ResponseEntity<ChaveDICT> buscarPorValorQuery(@org.springframework.web.bind.annotation.RequestParam("q") String valor) {
        java.util.Optional<ChaveDICT> opt = chaveDICTRepository.findByValor(valor);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(opt.get());
    }
}
