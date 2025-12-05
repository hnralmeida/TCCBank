package bank.pagadorPSP.service;

import bank.pagadorPSP.model.CobrancaPix;
import bank.pagadorPSP.repository.CobrancaPixRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
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
            ResponseEntity<String> resp = restTemplate.getForEntity(centralBaseUrl + "/chave-dict/" + entity.getChaveDestino(), String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chave Pix inexistente ou inativa no DICT");
        }

        return super.criar(entity);
    }
}
