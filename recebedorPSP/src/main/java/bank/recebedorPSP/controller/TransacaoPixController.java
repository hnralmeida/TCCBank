package bank.recebedorPSP.controller;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import bank.recebedorPSP.model.TransacaoPix;
import bank.recebedorPSP.service.TransacaoPixService;

@RestController
@RequestMapping("/transacaopix")
public class TransacaoPixController extends _GenericController<TransacaoPix> {

    private final TransacaoPixService transacaoPixService;

    protected TransacaoPixController(TransacaoPixService transacaoPixService) {
        super(transacaoPixService);
        this.transacaoPixService = transacaoPixService;
    }

    @GetMapping(value = "/{txid:.+}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> qrcode(@PathVariable String txid) throws Exception {
        Optional<TransacaoPix> opt = transacaoPixService.buscarPorTxid(txid);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        TransacaoPix t = opt.get();
        String payload = "TXID=" + (t.getTxid() == null ? "" : t.getTxid())
                + "|VALOR=" + (t.getValor() == null ? "" : t.getValor())
                + "|CHAVE=" + (t.getChaveDestino() == null ? "" : t.getChaveDestino());
        BitMatrix matrix = new MultiFormatWriter().encode(payload, BarcodeFormat.QR_CODE, 256, 256);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        return ResponseEntity.status(HttpStatus.OK).body(baos.toByteArray());
    }

    @PostMapping("/{txid}/status")
    public ResponseEntity<TransacaoPix> atualizarStatus(@PathVariable String txid,
                                                        @RequestBody java.util.Map<String, Object> body) {
        Object s = body.get("status");
        if (s == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String statusStr = s.toString();
        try {
            TransacaoPix updated = transacaoPixService.atualizarStatusPorTxid(txid, bank.recebedorPSP.model.StatusPix.valueOf(statusStr));
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
