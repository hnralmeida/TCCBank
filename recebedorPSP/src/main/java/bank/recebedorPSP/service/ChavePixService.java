package bank.recebedorPSP.service;

import bank.recebedorPSP.model.ChavePix;
import bank.recebedorPSP.repository.ChavePixRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChavePixService extends _GenericService<ChavePix, ChavePixRepository> {

    private final ChavePixRepository chavePixRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${central.baseUrl:http://banco-central:8095}")
    private String centralBaseUrl;

    @Value("${recebedor.ispb:RECEBEDOR-ISPB}")
    private String recebedorIspb;

    protected ChavePixService(ChavePixRepository chavePixRepository) {
        super(chavePixRepository);
        this.chavePixRepository = chavePixRepository;
    }

    @Override
    public ChavePix criar(ChavePix entity) {
        ChavePix saved = super.criar(entity);

        try {
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
            restTemplate.postForObject(centralBaseUrl + "/api/chave-dict", request, String.class);
        } catch (Exception ignored) {
        }

        return saved;
    }
}
