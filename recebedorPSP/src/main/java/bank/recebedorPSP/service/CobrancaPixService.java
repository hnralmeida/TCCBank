package bank.recebedorPSP.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import bank.recebedorPSP.model.CobrancaPix;
import bank.recebedorPSP.repository.CobrancaPixRepository;

@Service
public class CobrancaPixService extends _GenericService<CobrancaPix, CobrancaPixRepository> {

    private final CobrancaPixRepository cobrancaPixRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${central.baseUrl:http://banco-central:8095}")
    private String centralBaseUrl;

    protected CobrancaPixService(CobrancaPixRepository cobrancaPixRepository) {
        super(cobrancaPixRepository);
        this.cobrancaPixRepository = cobrancaPixRepository;
    }

    @Override
    public CobrancaPix criar(CobrancaPix entity) {
        if (entity.getChaveDestino() == null || entity.getChaveDestino().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chave destino obrigatória");
        }

        try {
            java.net.URI uri = UriComponentsBuilder.fromHttpUrl(centralBaseUrl)
                    .path("/chave-dict/valor")
                    .queryParam("q", entity.getChaveDestino())
                    .build(true)
                    .toUri();
            org.springframework.http.ResponseEntity<String> resp = restTemplate.getForEntity(uri, String.class);
            if (!resp.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chave Pix inexistente ou inativa no DICT");
            }
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(resp.getBody());
            com.fasterxml.jackson.databind.JsonNode ativaNode = node.get("ativa");
            if (ativaNode == null || !ativaNode.asBoolean()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chave Pix inexistente ou inativa no DICT");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chave Pix inexistente ou inativa no DICT");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chave Pix inexistente ou inativa no DICT");
        }

        if (entity.getTxid() == null || entity.getTxid().isBlank()) {
            entity.setTxid(generateTxid());
        }

        CobrancaPix saved = super.criar(entity);

        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("txid", saved.getTxid());
        payload.put("valor", saved.getValor());
        payload.put("recebedorISPB", saved.getChaveDestino());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<java.util.Map<String, Object>> request = new HttpEntity<>(payload, headers);
        try {
            restTemplate.postForEntity(centralBaseUrl + "/transacaoliquidacao/min", request, String.class);
        } catch (Exception ignored) {
        }

        return saved;
    }

    private String generateTxid() {
        String raw = java.util.UUID.randomUUID().toString().replace("-", "");
        return raw.substring(0, Math.min(32, raw.length()));
    }

    public java.util.Optional<CobrancaPix> buscarPorTxid(String txid) {
        return cobrancaPixRepository.findByTxid(txid);
    }
}
