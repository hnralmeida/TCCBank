package bank.BancoCentral.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProcessarPagamentoController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ConcurrentHashMap<String, String> pagamentos = new ConcurrentHashMap<>();

    @Value("${recebedor.baseUrl:http://localhost:8080}")
    private String recebedorBaseUrl;

    @PostMapping("/processar-pagamento")
    public ResponseEntity<Map<String, Object>> processar(@RequestBody Map<String, Object> body) {
        String txid = body.get("txid") == null ? null : body.get("txid").toString();
        if (txid == null || txid.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "txid é obrigatório"));
        }

        if (!cobrancaExisteNoRecebedor(txid)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "txid inválido"));
        }

        String anterior = pagamentos.put(txid, "PAGO");
        if (anterior == null) {
            enviarWebhookParaRecebedor(txid);
        }

        return ResponseEntity.ok(Map.of("txid", txid, "status", "PAGO"));
    }

    private boolean cobrancaExisteNoRecebedor(String txid) {
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(recebedorBaseUrl + "/cobrancas/" + txid, String.class);
            return resp.getStatusCode().is2xxSuccessful();
        } catch (RestClientResponseException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void enviarWebhookParaRecebedor(String txid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(Map.of("txid", txid, "status", "PAGO"), headers);
        try {
            restTemplate.postForEntity(recebedorBaseUrl + "/webhook/pagamento", req, String.class);
        } catch (Exception ignored) {
        }
    }
}
