package bank.recebedorPSP.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CobrancasController {

    private final ConcurrentHashMap<String, Cobranca> cobrancas = new ConcurrentHashMap<>();

    @PostMapping("/cobrancas")
    public ResponseEntity<Cobranca> criar(@RequestBody(required = false) Map<String, Object> body) {
        String txid = gerarTxidUnico();
        String payload = gerarPayload(txid, body);
        String payLink = "/pay/" + txid;

        String nomeRecebedor = body == null || body.get("nomeRecebedor") == null ? "" : body.get("nomeRecebedor").toString();
        String bancoDestino = body == null || body.get("bancoDestino") == null ? "" : body.get("bancoDestino").toString();

        if (nomeRecebedor != null) {
            nomeRecebedor = nomeRecebedor.trim();
        }
        if (bancoDestino != null) {
            bancoDestino = bancoDestino.trim();
        }

        if (nomeRecebedor == null || nomeRecebedor.isBlank()) {
            nomeRecebedor = "Recebedor";
        }
        if (bancoDestino == null || bancoDestino.isBlank()) {
            bancoDestino = "TCCBank";
        }
        Double valor = null;
        Object valorObj = body == null ? null : body.get("valor");
        if (valorObj instanceof Number) {
            valor = ((Number) valorObj).doubleValue();
        } else if (valorObj != null) {
            try {
                valor = Double.valueOf(valorObj.toString());
            } catch (Exception ignored) {
            }
        }

        Cobranca cobranca = new Cobranca(txid, "PENDENTE", payload, payLink, nomeRecebedor, valor, bancoDestino);
        cobrancas.put(txid, cobranca);
        return ResponseEntity.status(HttpStatus.CREATED).body(cobranca);
    }

    @GetMapping("/cobrancas/{txid}")
    public ResponseEntity<Cobranca> buscar(@PathVariable String txid) {
        Cobranca cobranca = cobrancas.get(txid);
        if (cobranca == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(cobranca);
    }

    @GetMapping(value = "/pay/{txid}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> pay(@PathVariable String txid) {
        if (txid == null || txid.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body("""
                            <!doctype html>
                            <html lang="pt-BR">
                            <head><meta charset="utf-8"><title>Pagamento</title></head>
                            <body>TXID inválido</body>
                            </html>
                            """);
        }

        Cobranca cobranca = cobrancas.get(txid);
        if (cobranca == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body("""
                            <!doctype html>
                            <html lang="pt-BR">
                            <head><meta charset="utf-8"><title>Pagamento</title></head>
                            <body>Cobrança não encontrada</body>
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
                    const recebedorBaseUrl = window.location.origin;
                    const pagadorUrl = 'http://localhost:8090/pagar';
                    const $txid = document.getElementById('txid');
                    const $status = document.getElementById('status');
                    const $msg = document.getElementById('msg');
                    const $btn = document.getElementById('btnPagar');
                    let pollTimer = null;

                    function setStatusText(status) {
                      $status.textContent = status || '—';
                      $status.classList.toggle('ok', String(status).toUpperCase() === 'PAGO');
                    }

                    async function carregarCobranca() {
                      const resp = await fetch(`${recebedorBaseUrl}/cobrancas/${encodeURIComponent(txid)}`, { headers: { 'Accept': 'application/json' }});
                      if (!resp.ok) throw new Error('Falha ao buscar cobrança');
                      return await resp.json();
                    }

                    async function atualizarStatus() {
                      try {
                        const c = await carregarCobranca();
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
                        const resp = await fetch(pagadorUrl, {
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

    @PostMapping("/webhook/pagamento")
    public ResponseEntity<Void> webhookPagamento(@RequestBody Map<String, Object> body) {
        String txid = body.get("txid") == null ? null : body.get("txid").toString();
        String status = body.get("status") == null ? null : body.get("status").toString();

        if (txid == null || txid.isBlank() || status == null || status.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Cobranca cobranca = cobrancas.get(txid);
        if (cobranca == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!"PAGO".equalsIgnoreCase(status)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        cobrancas.computeIfPresent(txid, (k, v) -> new Cobranca(v.txid(), "PAGO", v.payload(), v.payLink(), v.nomeRecebedor(), v.valor(), v.bancoDestino()));
        return ResponseEntity.noContent().build();
    }

    private String gerarTxidUnico() {
        while (true) {
            String raw = java.util.UUID.randomUUID().toString().replace("-", "");
            String txid = raw.substring(0, Math.min(32, raw.length()));
            if (!cobrancas.containsKey(txid)) {
                return txid;
            }
        }
    }

    private String gerarPayload(String txid, Map<String, Object> body) {
        Object valor = body == null ? null : body.get("valor");
        Object descricao = body == null ? null : body.get("descricao");
        Object nomeRecebedor = body == null ? null : body.get("nomeRecebedor");
        Object bancoDestino = body == null ? null : body.get("bancoDestino");
        String v = valor == null ? "" : valor.toString();
        String d = descricao == null ? "" : descricao.toString();
        String n = nomeRecebedor == null ? "" : nomeRecebedor.toString();
        String b = bancoDestino == null ? "" : bancoDestino.toString();
        return "PIX|TXID=" + txid + "|VALOR=" + v + "|DESC=" + d + "|RECEBEDOR=" + n + "|BANCO=" + b;
    }

    public record Cobranca(String txid, String status, String payload, String payLink, String nomeRecebedor, Double valor,
                           String bancoDestino) {
    }
}
