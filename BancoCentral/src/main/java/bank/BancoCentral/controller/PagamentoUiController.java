package bank.BancoCentral.controller;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@RestController
public class PagamentoUiController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${public.baseUrl:https://nondepletory-connie-radiately.ngrok-free.dev}")
    private String publicBaseUrl;

    @Value("${recebedor.baseUrl:http://localhost:8080}")
    private String recebedorBaseUrl;

    @GetMapping(value = "/qrcode/pay/{txid}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> qrcodePay(@PathVariable String txid) throws Exception {
        if (txid == null || txid.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String url = normalizarBaseUrl(publicBaseUrl) + "/pay/" + txid;
        BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        return ResponseEntity.ok(baos.toByteArray());
    }

    @GetMapping(value = "/pay/{txid}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> pay(@PathVariable String txid) {
        if (txid == null || txid.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_HTML).body("""
                    <!doctype html>
                    <html lang="pt-BR">
                    <head><meta charset="utf-8"><title>Pagamento</title></head>
                    <body>TXID inválido</body>
                    </html>
                    """);
        }

        String html = """
                <!doctype html>
                <html lang="pt-BR">
                <head>
                  <meta charset="utf-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>Pagamento</title>
                  <style>
                    body { font-family: Arial, sans-serif; max-width: 720px; margin: 32px auto; padding: 0 16px; }
                    .card { border: 1px solid #ddd; border-radius: 10px; padding: 16px; }
                    .row { margin: 10px 0; }
                    .label { color: #666; font-size: 12px; text-transform: uppercase; letter-spacing: .03em; }
                    .value { font-size: 16px; word-break: break-all; }
                    button { padding: 10px 14px; border: 0; border-radius: 8px; background: #0b5; color: white; font-weight: 700; cursor: pointer; }
                    button[disabled] { background: #999; cursor: not-allowed; }
                    .status { display: inline-block; padding: 4px 10px; border-radius: 999px; background: #eee; font-weight: 700; }
                    .ok { background: #d9f7df; }
                    .msg { margin-top: 12px; }
                    code { background: #f5f5f5; padding: 2px 6px; border-radius: 6px; }
                  </style>
                </head>
                <body>
                  <h1>Pagamento</h1>
                  <div class="card">
                    <div class="row">
                      <div class="label">TXID</div>
                      <div class="value"><code id="txid"></code></div>
                    </div>
                    <div class="row">
                      <div class="label">Recebedor</div>
                      <div class="value" id="nomeRecebedor">—</div>
                    </div>
                    <div class="row">
                      <div class="label">Banco de destino</div>
                      <div class="value" id="bancoDestino">—</div>
                    </div>
                    <div class="row">
                      <div class="label">Valor</div>
                      <div class="value" id="valor">—</div>
                    </div>
                    <div class="row">
                      <div class="label">Status</div>
                      <div class="value"><span class="status" id="status">Carregando…</span></div>
                    </div>
                    <div class="row">
                      <button id="btnPagar" type="button">Pagar</button>
                    </div>
                    <div class="msg" id="msg"></div>
                  </div>
                  <script>
                    const txid = %s;
                    const $txid = document.getElementById('txid');
                    const $nomeRecebedor = document.getElementById('nomeRecebedor');
                    const $bancoDestino = document.getElementById('bancoDestino');
                    const $valor = document.getElementById('valor');
                    const $status = document.getElementById('status');
                    const $msg = document.getElementById('msg');
                    const $btn = document.getElementById('btnPagar');
                    let pollTimer = null;

                    function setStatusText(status) {
                      $status.textContent = status || '—';
                      $status.classList.toggle('ok', String(status).toUpperCase() === 'PAGO');
                    }

                    async function carregarCobranca() {
                      const resp = await fetch(`/ui/cobrancas/${encodeURIComponent(txid)}`, { headers: { 'Accept': 'application/json' }});
                      if (!resp.ok) throw new Error('Falha ao buscar cobrança');
                      return await resp.json();
                    }

                    async function atualizarStatus() {
                      try {
                        const c = await carregarCobranca();
                        $nomeRecebedor.textContent = c.nomeRecebedor ? String(c.nomeRecebedor) : '—';
                        $bancoDestino.textContent = c.bancoDestino ? String(c.bancoDestino) : '—';
                        $valor.textContent = (c.valor !== undefined && c.valor !== null && c.valor !== '') ? String(c.valor) : '—';
                        setStatusText(c.status);
                        if (String(c.status).toUpperCase() === 'PAGO') {
                          $msg.textContent = 'Pagamento confirmado';
                          $btn.disabled = true;
                          if (pollTimer) { clearInterval(pollTimer); pollTimer = null; }
                        }
                      } catch (e) {
                        $msg.textContent = 'Erro ao consultar status';
                      }
                    }

                    function iniciarPolling() {
                      if (pollTimer) return;
                      pollTimer = setInterval(atualizarStatus, 2000);
                    }

                    async function pagar() {
                      $btn.disabled = true;
                      $msg.textContent = 'Processando pagamento…';
                      try {
                        const resp = await fetch(`/processar-pagamento`, {
                          method: 'POST',
                          headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
                          body: JSON.stringify({ txid })
                        });
                        if (!resp.ok) {
                          const txt = await resp.text();
                          throw new Error(txt || 'Falha no pagamento');
                        }
                        $msg.textContent = 'Pagamento enviado. Aguardando confirmação…';
                        iniciarPolling();
                        await atualizarStatus();
                      } catch (e) {
                        $msg.textContent = 'Erro ao pagar: ' + (e && e.message ? e.message : 'desconhecido');
                        $btn.disabled = false;
                      }
                    }

                    $txid.textContent = txid;
                    $btn.addEventListener('click', pagar);
                    atualizarStatus();
                    iniciarPolling();
                  </script>
                </body>
                </html>
                """.formatted("\"" + txid.replace("\"", "") + "\"");

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }

    @GetMapping(value = "/ui/cobrancas/{txid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uiBuscarCobranca(@PathVariable String txid) {
        if (txid == null || txid.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(normalizarBaseUrl(recebedorBaseUrl) + "/cobrancas/" + txid, String.class);
            return ResponseEntity.status(resp.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(resp.getBody());
        } catch (RestClientResponseException e) {
            String body = e.getResponseBodyAsString();
            return ResponseEntity.status(e.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"Falha ao consultar recebedor\"}");
        }
    }

    @PostMapping(value = "/ui/webhook/pagamento", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> uiWebhookPagamento(@RequestBody Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("ngrok-skip-browser-warning", "true");
        try {
            restTemplate.postForEntity(normalizarBaseUrl(recebedorBaseUrl) + "/webhook/pagamento",
                    new org.springframework.http.HttpEntity<>(body, headers), String.class);
        } catch (Exception ignored) {
        }
        return ResponseEntity.noContent().build();
    }

    private String normalizarBaseUrl(String baseUrl) {
        if (baseUrl == null) {
            return "";
        }
        if (baseUrl.endsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl;
    }
}
