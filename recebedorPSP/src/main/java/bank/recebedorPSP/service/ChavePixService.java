package bank.recebedorPSP.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import bank.recebedorPSP.model.ChavePix;
import bank.recebedorPSP.model.Conta;
import bank.recebedorPSP.repository.ChavePixRepository;
import bank.recebedorPSP.repository.ContaRepository;

@Service
public class ChavePixService extends _GenericService<ChavePix, ChavePixRepository> {

    private final ChavePixRepository chavePixRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ContaRepository contaRepository;

    @Value("${central.baseUrl:http://banco-central:8095}")
    private String centralBaseUrl;

    @Value("${recebedor.ispb:RECEBEDOR-ISPB}")
    private String recebedorIspb;

    protected ChavePixService(ChavePixRepository chavePixRepository, ContaRepository contaRepository) {
        super(chavePixRepository);
        this.chavePixRepository = chavePixRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    public ChavePix criar(ChavePix entity) {
        if (entity.getConta() == null || entity.getConta().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conta obrigatória para ChavePix");
        }

        Conta conta = contaRepository.findById(entity.getConta().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        entity.setConta(conta);

        ChavePix saved = super.criar(entity);

        Map<String, Object> payload = new HashMap<>();
        payload.put("tipo", saved.getTipo().name());
        payload.put("valor", saved.getValor());
        payload.put("contaBanco", saved.getConta() != null ? saved.getConta().getNumero() : null);
        payload.put("agencia", saved.getConta() != null ? saved.getConta().getAgencia() : null);
        payload.put("ispb", recebedorIspb);
        payload.put("ativa", saved.getAtiva());
        payload.put("dataCriacao", LocalDateTime.now().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        try {
            restTemplate.postForEntity(centralBaseUrl + "/chave-dict", request, String.class);
        } catch (Exception e) {
            // não interrompe criação local caso integração falhe
        }

        return saved;
    }
}
