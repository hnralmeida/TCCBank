package bank.recebedorPSP.controller;

import bank.recebedorPSP.exception.GenericOperation;
import bank.recebedorPSP.service._GenericServiceTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping({"/api"})
public abstract class _GenericController<TipoEntidade> {
    private final _GenericServiceTypes<TipoEntidade> genericService;

    protected _GenericController(_GenericServiceTypes<TipoEntidade> genericService) {
        this.genericService = genericService;
    }

    @GetMapping
    @GenericOperation(
            description = "Listar todos os registros"
    )
    public ResponseEntity<List<TipoEntidade>> listar() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.genericService.listar());
    }

    @GetMapping({"/{id}"})
    @GenericOperation(
            description = "Buscar um registro por ID"
    )
    public ResponseEntity<TipoEntidade> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.genericService.buscarPorId(id));
    }

    @PostMapping
    @GenericOperation(
            description = "Criar um registro"
    )
    public ResponseEntity<TipoEntidade> criar(@RequestBody @Validated TipoEntidade entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.genericService.criar(entity));
    }

    @PutMapping({"/{id}"})
    @GenericOperation(
            description = "Atualizar um registro"
    )
    public ResponseEntity<TipoEntidade> atualizar(@PathVariable UUID id, @RequestBody TipoEntidade entity) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.genericService.atualizar(id, entity));
    }

    @DeleteMapping({"/{id}"})
    @GenericOperation(
            description = "Excluir um registro"
    )
    public ResponseEntity<String> excluir(@PathVariable UUID id) {
        this.genericService.excluir(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Excluído");
    }
}
