package bank.recebedorPSP.exception;

import java.util.UUID;

public class RegistroNotUpdated extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RegistroNotUpdated(UUID id) {
        super("Erro ao atualizar a entidade: " + id);
    }
}
