package bank.BancoCentral.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.BancoCentral.model.ChaveDICT;
import bank.BancoCentral.service.ChaveDICTService;

@RestController
@RequestMapping("/chave-dict")
public class ChaveDICTController extends _GenericController<ChaveDICT> {

    private final ChaveDICTService chaveDICTService;

    protected ChaveDICTController(ChaveDICTService chaveDICTService) {
        super(chaveDICTService);
        this.chaveDICTService = chaveDICTService;
    }
}
