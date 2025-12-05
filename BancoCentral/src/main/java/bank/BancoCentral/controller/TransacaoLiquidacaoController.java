package bank.BancoCentral.controller;

import bank.BancoCentral.model.TransacaoLiquidacao;
import bank.BancoCentral.service.TransacaoLiquidacaoService;
import bank.BancoCentral.model.ParticipanteSPI;
import bank.BancoCentral.model.StatusSPI;
import bank.BancoCentral.repository.ParticipanteSPIRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/transacaoliquidacao")
public class TransacaoLiquidacaoController extends _GenericController<TransacaoLiquidacao> {

    private final TransacaoLiquidacaoService transacaoLiquidacaoService;
    private final ParticipanteSPIRepository participanteSPIRepository;

    protected TransacaoLiquidacaoController(TransacaoLiquidacaoService transacaoLiquidacaoService,
                                            ParticipanteSPIRepository participanteSPIRepository) {
        super(transacaoLiquidacaoService);
        this.transacaoLiquidacaoService = transacaoLiquidacaoService;
        this.participanteSPIRepository = participanteSPIRepository;
    }

    @PostMapping("/min")
    public ResponseEntity<TransacaoLiquidacao> criarMinimo(@org.springframework.web.bind.annotation.RequestBody Map<String, Object> body) {
        String txid = body.get("txid") == null ? null : body.get("txid").toString();
        String recebedorISPB = body.get("recebedorISPB") == null ? null : body.get("recebedorISPB").toString();

        if (txid == null || txid.isBlank()) {
            String raw = java.util.UUID.randomUUID().toString().replace("-", "");
            txid = raw.substring(0, Math.min(32, raw.length()));
        }

        ParticipanteSPI participante = null;
        if (recebedorISPB != null && !recebedorISPB.isBlank()) {
            participante = participanteSPIRepository.findByCodigoISPB(recebedorISPB)
                    .orElseGet(() -> {
                        ParticipanteSPI p = new ParticipanteSPI();
                        p.setCodigoISPB(recebedorISPB);
                        p.setNome(recebedorISPB);
                        p.setAtivo(true);
                        return participanteSPIRepository.save(p);
                    });
        }

        TransacaoLiquidacao t = new TransacaoLiquidacao();
        t.setTxid(txid);
        t.setRecebedorISPB(participante);
        t.setStatus(StatusSPI.PENDENTE);
        t.setDataCriacao(LocalDate.now());

        TransacaoLiquidacao saved = transacaoLiquidacaoService.criar(t);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
