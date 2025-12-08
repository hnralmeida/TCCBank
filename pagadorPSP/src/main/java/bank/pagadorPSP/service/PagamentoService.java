package bank.pagadorPSP.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import bank.pagadorPSP.model.Conta;
import bank.pagadorPSP.model.Pagamento;
import bank.pagadorPSP.model.StatusPix;
import bank.pagadorPSP.repository.ContaRepository;
import bank.pagadorPSP.repository.PagamentoRepository;

@Service
public class PagamentoService extends _GenericService<Pagamento, PagamentoRepository> {

    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${central.baseUrl:http://banco-central:8095}")
    private String centralBaseUrl;

    @Value("${recebedor.baseUrl:http://recebedor-psp:8080}")
    private String recebedorBaseUrl;

    protected PagamentoService(PagamentoRepository pagamentoRepository, ContaRepository contaRepository) {
        super(pagamentoRepository);
        this.pagamentoRepository = pagamentoRepository;
        this.contaRepository = contaRepository;
    }

    public Pagamento processarPagamento(Map<String, Object> body) {
        String txid = body.get("txid") == null ? null : body.get("txid").toString();
        String contaIdStr = body.get("contaId") == null ? null : body.get("contaId").toString();
        if (txid == null || txid.isBlank() || contaIdStr == null || contaIdStr.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "txid e contaId são obrigatórios");
        }

        UUID contaId = null;
        try {
            contaId = UUID.fromString(contaIdStr);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contaId inválido");
        }

        Optional<Conta> contaOpt = contaRepository.findById(contaId);
        if (contaOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conta não encontrada");
        }
        Conta conta = contaOpt.get();

        ResponseEntity<String> busca = restTemplate.getForEntity(centralBaseUrl + "/transacaoliquidacao/detalhe/" + txid, String.class);
        if (!busca.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TransacaoLiquidacao inexistente no Banco Central");
        }

        try {
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(busca.getBody());
            Double valor = node.get("valor") != null && node.get("valor").isNumber() ? node.get("valor").asDouble() : null;
            String recebedorISPB = node.get("recebedorISPB") != null ? node.get("recebedorISPB").asText() : null;

            java.util.Map<String, Object> liquidarBody = new java.util.HashMap<>();
            liquidarBody.put("pagadorISPB", conta.getNumero());
            restTemplate.postForEntity(centralBaseUrl + "/transacaoliquidacao/" + txid + "/liquidar",
                    new org.springframework.http.HttpEntity<>(liquidarBody), String.class);

            Pagamento pagamento = new Pagamento();
            pagamento.setTxid(txid);
            pagamento.setValor(valor);
            pagamento.setDescricao(null);
            pagamento.setStatus(StatusPix.PROCESSADA);
            pagamento.setChaveDestino(recebedorISPB);
            pagamento.setConta(conta);
            return super.criar(pagamento);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao liquidar transação no Banco Central");
        }
    }

    public Map<String, Object> detalhe(String txid) {
        if (txid == null || txid.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "txid é obrigatório");
        }
        ResponseEntity<String> busca = restTemplate.getForEntity(centralBaseUrl + "/transacaoliquidacao/detalhe/" + txid, String.class);
        if (!busca.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TransacaoLiquidacao não encontrada");
        }
        try {
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(busca.getBody());
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("txid", node.get("txid") != null ? node.get("txid").asText() : null);
            result.put("valor", node.get("valor") != null && node.get("valor").isNumber() ? node.get("valor").asDouble() : null);
            result.put("status", node.get("status") != null ? node.get("status").asText() : null);
            result.put("recebedorISPB", node.get("recebedorISPB") != null ? node.get("recebedorISPB").asText() : null);
            return result;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao parsear resposta do Banco Central");
        }
    }

    public Pagamento liquidarPagamento(Map<String, Object> body) {
        String txid = body.get("txid") == null ? null : body.get("txid").toString();
        String contaIdStr = body.get("contaId") == null ? null : body.get("contaId").toString();
        if (txid == null || txid.isBlank() || contaIdStr == null || contaIdStr.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "txid e contaId são obrigatórios");
        }

        UUID contaId = null;
        try {
            contaId = UUID.fromString(contaIdStr);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contaId inválido");
        }

        Optional<Conta> contaOpt = contaRepository.findById(contaId);
        if (contaOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conta não encontrada");
        }
        Conta conta = contaOpt.get();

        ResponseEntity<String> busca = restTemplate.getForEntity(centralBaseUrl + "/transacaoliquidacao/detalhe/" + txid, String.class);
        if (!busca.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TransacaoLiquidacao inexistente no Banco Central");
        }

        try {
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(busca.getBody());
            Double valor = node.get("valor") != null && node.get("valor").isNumber() ? node.get("valor").asDouble() : null;
            String recebedorISPB = node.get("recebedorISPB") != null ? node.get("recebedorISPB").asText() : null;

            java.util.Map<String, Object> liquidarBody = new java.util.HashMap<>();
            liquidarBody.put("pagadorISPB", conta.getNumero());
            restTemplate.postForEntity(centralBaseUrl + "/transacaoliquidacao/" + txid + "/liquidar",
                    new org.springframework.http.HttpEntity<>(liquidarBody), String.class);

            java.util.Map<String, Object> atualizarStatusBody = new java.util.HashMap<>();
            atualizarStatusBody.put("status", StatusPix.PROCESSADA.name());
            try {
                restTemplate.postForEntity(recebedorBaseUrl + "/transacaopix/" + txid + "/status",
                        new org.springframework.http.HttpEntity<>(atualizarStatusBody), String.class);
            } catch (Exception ignored) {
            }

            Pagamento pagamento = new Pagamento();
            pagamento.setTxid(txid);
            pagamento.setValor(valor);
            pagamento.setDescricao(null);
            pagamento.setStatus(StatusPix.PROCESSADA);
            pagamento.setChaveDestino(recebedorISPB);
            pagamento.setConta(conta);
            return super.criar(pagamento);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao liquidar transação e sincronizar status");
        }
    }
}
