package bank.recebedorPSP.controller;

import bank.recebedorPSP.model.CobrancaPix;
import bank.recebedorPSP.service.CobrancaPixService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

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
}
