package bank.pagadorPSP.controller;

import java.util.Map;

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
public class PagarController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${central.baseUrl:https://nondepletory-connie-radiately.ngrok-free.dev}")
    private String centralBaseUrl;

    @PostMapping("/pagar")
    public ResponseEntity<Object> pagar(@RequestBody Map<String, Object> body) {
        String txid = body.get("txid") == null ? null : body.get("txid").toString();
        if (txid == null || txid.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "txid é obrigatório"));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("ngrok-skip-browser-warning", "true");
        headers.add(HttpHeaders.USER_AGENT, "TCCBank-PagadorPSP");
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(Map.of("txid", txid), headers);

        try {
            ResponseEntity<Object> resp = restTemplate.postForEntity(centralBaseUrl + "/processar-pagamento", req, Object.class);
            return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
        } catch (RestClientResponseException e) {
            String payload = e.getResponseBodyAsString();
            Object bodyOut = payload == null || payload.isBlank() ? Map.of("error", "Falha ao processar pagamento") : payload;
            return ResponseEntity.status(e.getStatusCode()).body(bodyOut);
        }
    }
}
