package bank.recebedorPSP.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import bank.recebedorPSP.model.Conta;
import bank.recebedorPSP.model.StatusPix;
import bank.recebedorPSP.model.TransacaoPix;
import bank.recebedorPSP.repository.ContaRepository;
import bank.recebedorPSP.repository.TransacaoPixRepository;

@Service
public class TransacaoPixService extends _GenericService<TransacaoPix, TransacaoPixRepository> {

    private final TransacaoPixRepository transacaoPixRepository;
    private final ContaRepository contaRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${central.baseUrl:http://banco-central:8095}")
    private String centralBaseUrl;

    @Value("${recebedor.ispb:RECEBEDOR-ISPB}")
    private String recebedorIspb;

    protected TransacaoPixService(TransacaoPixRepository transacaoPixRepository, ContaRepository contaRepository) {
        super(transacaoPixRepository);
        this.transacaoPixRepository = transacaoPixRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    public TransacaoPix criar(TransacaoPix entity) {
        if (entity.getContaPagadora() != null && entity.getContaPagadora().getId() != null) {
            Conta conta = contaRepository.findById(entity.getContaPagadora().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta pagadora não encontrada"));
            entity.setContaPagadora(conta);
        }

        if (entity.getTxid() == null || entity.getTxid().isBlank()) {
            entity.setTxid(generateTxid());
        }

        entity.setStatus(StatusPix.PENDENTE);
        entity.setDataCriacao(LocalDate.now());

        TransacaoPix saved = super.criar(entity);

        Map<String, Object> payload = new HashMap<>();
        payload.put("txid", saved.getTxid());
        payload.put("valor", saved.getValor());
        payload.put("status", "PENDENTE");
        payload.put("dataCriacao", LocalDate.now().toString());
        payload.put("recebedorISPB", recebedorIspb);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        try {
            restTemplate.postForEntity(centralBaseUrl + "/transacaoliquidacao", request, String.class);
        } catch (Exception ignored) {
        }

        return saved;
    }

    private String generateTxid() {
        String raw = java.util.UUID.randomUUID().toString().replace("-", "");
        return raw.substring(0, Math.min(32, raw.length()));
    }

    public Optional<TransacaoPix> buscarPorTxid(String txid) {
        return transacaoPixRepository.findByTxid(txid);
    }
}
