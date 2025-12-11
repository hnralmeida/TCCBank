"use client";
import { useEffect, useState } from 'react';
import { createCobrancaPix, getCobrancaQrPng, setBaseUrl, listCobrancas } from '@/lib/api';
import Modal from './Modal';

export default function ChargeTab({ server }: { server: string }) {
  const [valor, setValor] = useState('');
  const [chave, setChave] = useState('');
  const [open, setOpen] = useState(false);
  const [qr, setQr] = useState('');
  const [txid, setTxid] = useState('');
  const [status, setStatus] = useState('');
  const [cobrancas, setCobrancas] = useState<any[]>([]);
  const [page, setPage] = useState(1);
  const pageSize = 10;

  async function gerar() {
    setBaseUrl(server);
    setStatus('');
    try {
      const c = await createCobrancaPix({ valor: valor ? parseFloat(valor) : null, chaveDestino: chave });
      setTxid(c.txid);
      const img = await getCobrancaQrPng(c.txid);
      setQr(img);
      setOpen(true);
      await carregarCobrancas();
    } catch (e) {
      setStatus('Erro ao gerar cobrança');
    }
  }

  async function carregarCobrancas() {
    setBaseUrl(server);
    try {
      const list = await listCobrancas();
      setCobrancas(Array.isArray(list) ? list : []);
      setPage(1);
    } catch {}
  }

  useEffect(() => {
    carregarCobrancas();
  }, [server]);

  return (
    <div style={{ display: 'grid', gap: 16 }}>
      <div style={{ display: 'grid', gap: 8, gridTemplateColumns: '1fr 1fr' }}>
        <input value={valor} onChange={e => setValor(e.target.value)} placeholder="Valor" />
        <input value={chave} onChange={e => setChave(e.target.value)} placeholder="Chave Pix" />
      </div>
      <button onClick={gerar}>Gerar cobrança</button>
      {status ? <div>{status}</div> : null}

      <div style={{ borderTop: '1px solid #eee', paddingTop: 12 }}>
        <h4>Cobranças</h4>
        <div style={{ border: '1px solid #ddd', borderRadius: 8 }}>
          {(cobrancas.slice((page - 1) * pageSize, page * pageSize)).map(c => (
            <div key={c.id} style={{ padding: 8, borderBottom: '1px solid #eee' }}>
              <div><strong>TXID:</strong> {c.txid}</div>
              {c.valor ? <div><strong>Valor:</strong> {c.valor}</div> : null}
              <div><strong>Chave:</strong> {c.chaveDestino}</div>
              <div><strong>Status:</strong> {c.status}</div>
            </div>
          ))}
          {cobrancas.length === 0 ? <div style={{ padding: 8 }}>Nenhuma cobrança</div> : null}
        </div>
        <div style={{ display: 'flex', gap: 8, marginTop: 8 }}>
          <button disabled={page <= 1} onClick={() => setPage(p => Math.max(1, p - 1))}>Anterior</button>
          <span>Página {page} de {Math.max(1, Math.ceil(cobrancas.length / pageSize))}</span>
          <button disabled={page >= Math.ceil(cobrancas.length / pageSize)} onClick={() => setPage(p => Math.min(Math.ceil(cobrancas.length / pageSize), p + 1))}>Próxima</button>
        </div>
      </div>
      <Modal open={open} onClose={() => setOpen(false)}>
        <div style={{ display: 'grid', gap: 12 }}>
          <div>TXID: {txid}</div>
          {valor ? <div>Valor: {valor}</div> : null}
          {qr ? <img src={qr} alt="QR Code" style={{ width: 256, height: 256, alignSelf: 'center' }} /> : null}
        </div>
      </Modal>
    </div>
  );
}
