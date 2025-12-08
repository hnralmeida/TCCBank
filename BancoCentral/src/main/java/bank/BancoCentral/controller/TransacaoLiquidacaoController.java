package bank.BancoCentral.controller;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.BancoCentral.model.ParticipanteSPI;
import bank.BancoCentral.model.StatusSPI;
import bank.BancoCentral.model.TransacaoLiquidacao;
import bank.BancoCentral.repository.ParticipanteSPIRepository;
import bank.BancoCentral.service.TransacaoLiquidacaoService;

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
        Double valor = null;
        Object valorObj = body.get("valor");
        if (valorObj instanceof Number) {
            valor = ((Number) valorObj).doubleValue();
        } else if (valorObj != null) {
            try {
                valor = Double.valueOf(valorObj.toString());
            } catch (Exception ignored) {
            }
        }

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
        t.setValor(valor);
        t.setRecebedorISPB(participante);
        t.setStatus(StatusSPI.PENDENTE);
        t.setDataCriacao(LocalDate.now());

        TransacaoLiquidacao saved = transacaoLiquidacaoService.criar(t);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{txid}")
    public ResponseEntity<TransacaoLiquidacao> buscarPorTxid(@PathVariable String txid) {
        Optional<TransacaoLiquidacao> opt = transacaoLiquidacaoService.buscarPorTxid(txid);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(opt.get());
    }

    @GetMapping("/existe/{txid}")
    public ResponseEntity<Map<String, Object>> existe(@PathVariable String txid) {
        Optional<TransacaoLiquidacao> opt = transacaoLiquidacaoService.buscarPorTxid(txid);
        return ResponseEntity.status(opt.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(java.util.Map.of("exists", opt.isPresent()));
    }

    @GetMapping("/detalhe/{txid}")
    public ResponseEntity<Map<String, Object>> detalhe(@PathVariable String txid) {
        Optional<TransacaoLiquidacao> opt = transacaoLiquidacaoService.buscarPorTxid(txid);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        TransacaoLiquidacao t = opt.get();
        String recebedor = t.getRecebedorISPB() == null ? null : t.getRecebedorISPB().getCodigoISPB();
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("txid", t.getTxid());
        result.put("valor", t.getValor());
        result.put("status", t.getStatus());
        result.put("recebedorISPB", recebedor);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{txid}/liquidar")
    public ResponseEntity<TransacaoLiquidacao> liquidar(@PathVariable String txid,
                                                        @org.springframework.web.bind.annotation.RequestBody Map<String, Object> body) {
        Optional<TransacaoLiquidacao> opt = transacaoLiquidacaoService.buscarPorTxid(txid);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String pagadorISPB = body.get("pagadorISPB") == null ? null : body.get("pagadorISPB").toString();
        ParticipanteSPI participantePagador = null;
        if (pagadorISPB != null && !pagadorISPB.isBlank()) {
            participantePagador = participanteSPIRepository.findByCodigoISPB(pagadorISPB)
                    .orElseGet(() -> {
                        ParticipanteSPI p = new ParticipanteSPI();
                        p.setCodigoISPB(pagadorISPB);
                        p.setNome(pagadorISPB);
                        p.setAtivo(true);
                        return participanteSPIRepository.save(p);
                    });
        }

        TransacaoLiquidacao t = opt.get();
        t.setPagadorISPB(participantePagador);
        t.setStatus(StatusSPI.PROCESSADA);
        t.setDataLiquidacao(LocalDate.now());
        TransacaoLiquidacao saved = transacaoLiquidacaoService.atualizar(t.getId(), t);
        return ResponseEntity.ok(saved);
    }
}
