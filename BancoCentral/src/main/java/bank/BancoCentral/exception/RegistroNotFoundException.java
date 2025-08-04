package bank.BancoCentral.exception;

import java.util.UUID;

public class RegistroNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegistroNotFoundException(UUID id) {
        super("Registro não encontrado com o id: " + id);
    }
}