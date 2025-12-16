package bank.recebedorPSP.controller;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import bank.recebedorPSP.model.CobrancaPix;
import bank.recebedorPSP.service.CobrancaPixService;

@RestController
@RequestMapping("/cobrancapix")
public class CobrancaPixController extends _GenericController<CobrancaPix> {

    private final CobrancaPixService cobrancaPixService;

    protected CobrancaPixController(CobrancaPixService cobrancaPixService) {
        super(cobrancaPixService);
        this.cobrancaPixService = cobrancaPixService;
    }

    @GetMapping(value = "/{txid:.+}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> qrcode(@PathVariable String txid) throws Exception {
        Optional<CobrancaPix> opt = cobrancaPixService.buscarPorTxid(txid);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CobrancaPix c = opt.get();
        String payload = "TXID=" + (c.getTxid() == null ? "" : c.getTxid())
                + "|VALOR=" + (c.getValor() == null ? "" : c.getValor())
                + "|CHAVE=" + (c.getChaveDestino() == null ? "" : c.getChaveDestino());
        BitMatrix matrix = new MultiFormatWriter().encode(payload, BarcodeFormat.QR_CODE, 256, 256);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        return ResponseEntity.status(HttpStatus.OK).body(baos.toByteArray());
    }

    @PostMapping("/{txid}/status")
    public ResponseEntity<CobrancaPix> atualizarStatus(@PathVariable String txid,
                                                       @RequestBody java.util.Map<String, Object> body) {
        Object s = body.get("status");
        if (s == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String statusStr = s.toString();
        try {
            CobrancaPix updated = cobrancaPixService.atualizarStatusPorTxid(txid, bank.recebedorPSP.model.StatusPix.valueOf(statusStr));
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/sincronizar/{txid}")
    public ResponseEntity<CobrancaPix> sincronizar(@PathVariable String txid) {
        try {
            CobrancaPix updated = cobrancaPixService.sincronizarStatusComCentral(txid);
            return ResponseEntity.ok(updated);
        } catch (org.springframework.web.server.ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
}
