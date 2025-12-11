"use client";
import { useEffect, useState } from 'react';
import ChargeTab from '@/components/ChargeTab';
import Modal from '@/components/Modal';
import CreateAccountForm from '@/components/CreateAccountForm';
import EditConta from '@/components/EditConta';
import { listContas, setBaseUrl } from '@/lib/api';

export default function Page() {
  const [tab, setTab] = useState<'contas' | 'cobranca'>('contas');
  const [server, setServer] = useState('http://localhost:8080');
  const [contas, setContas] = useState<any[]>([]);
  const [selectedId, setSelectedId] = useState<string>('');
  const [openCreate, setOpenCreate] = useState(false);

  async function loadContas() {
    setBaseUrl(server);
    try {
      const list = await listContas();
      setContas(Array.isArray(list) ? list : []);
    } catch {}
  }

  useEffect(() => {
    loadContas();
  }, [server]);

  return (
    <div style={{ maxWidth: 1024, margin: '0 auto', padding: 16 }}>
      <h2>Painel Administrativo</h2>
      <div style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
        <button onClick={() => setTab('contas')} style={{ padding: '8px 12px', background: tab === 'contas' ? '#1e90ff' : '#eee' }}>Contas</button>
        <button onClick={() => setTab('cobranca')} style={{ padding: '8px 12px', background: tab === 'cobranca' ? '#1e90ff' : '#eee' }}>Cobrança Pix</button>
      </div>

      <div style={{ marginBottom: 16 }}>
        <label style={{ marginRight: 8 }}>Servidor</label>
        <input value={server} onChange={e => setServer(e.target.value)} placeholder="http://localhost:8080" />
      </div>

      {tab === 'contas' ? (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
              <h3>Lista de contas</h3>
              <button onClick={() => setOpenCreate(true)}>Nova Conta</button>
            </div>
            <div style={{ border: '1px solid #ddd', borderRadius: 8 }}>
              {contas.map(c => (
                <div key={c.id} style={{ padding: 8, borderBottom: '1px solid #eee', cursor: 'pointer' }} onClick={() => setSelectedId(c.id)}>
                  <div><strong>{c.numero}</strong> - Agência {c.agencia}</div>
                  <div>Saldo: {c.saldo} | Tipo: {c.tipo}</div>
                  <div>Cliente: {c.cliente ? (c.cliente.nome ?? c.cliente.registro ?? 'N/A') : 'N/A'}</div>
                </div>
              ))}
              {contas.length === 0 ? <div style={{ padding: 8 }}>Nenhuma conta</div> : null}
            </div>
          </div>
          <div>
            <EditConta server={server} contaId={selectedId} />
          </div>
        </div>
      ) : (
        <ChargeTab server={server} />
      )}

      <Modal open={openCreate} onClose={() => { setOpenCreate(false); loadContas(); }}>
        <CreateAccountForm server={server} onCreated={() => { setOpenCreate(false); loadContas(); }} />
      </Modal>
    </div>
  );
}
