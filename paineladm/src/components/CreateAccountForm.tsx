"use client";
import { useState } from 'react';
import { createCliente, createConta, setBaseUrl } from '@/lib/api';

export default function CreateAccountForm({ server, onCreated }: { server: string; onCreated: (conta: any) => void }) {
  const [cliente, setCliente] = useState({ nome: '', registro: '', email: '', telefone: '' });
  const [conta, setConta] = useState({ numero: '', agencia: '', saldo: '', tipo: 'CORRENTE' });
  const [status, setStatus] = useState('');

  async function handleCreate() {
    setBaseUrl(server);
    setStatus('');
    try {
      const c = await createCliente(cliente);
      const createdConta = await createConta({ ...conta, cliente: { id: c.id } });
      setStatus('Conta criada');
      onCreated(createdConta);
    } catch (e) {
      setStatus('Erro ao criar');
    }
  }

  return (
    <div style={{ display: 'grid', gap: 12 }}>
      <h3>Criar conta</h3>
      <div style={{ display: 'grid', gap: 8, gridTemplateColumns: '1fr 1fr' }}>
        <input value={cliente.nome} onChange={e => setCliente({ ...cliente, nome: e.target.value })} placeholder="Nome" />
        <input value={cliente.registro} onChange={e => setCliente({ ...cliente, registro: e.target.value })} placeholder="Registro" />
        <input value={cliente.email} onChange={e => setCliente({ ...cliente, email: e.target.value })} placeholder="Email" />
        <input value={cliente.telefone} onChange={e => setCliente({ ...cliente, telefone: e.target.value })} placeholder="Telefone" />
        <input value={conta.numero} onChange={e => setConta({ ...conta, numero: e.target.value })} placeholder="Número da conta" />
        <input value={conta.agencia} onChange={e => setConta({ ...conta, agencia: e.target.value })} placeholder="Agência" />
        <input value={conta.saldo} onChange={e => setConta({ ...conta, saldo: e.target.value })} placeholder="Saldo" />
        <select value={conta.tipo} onChange={e => setConta({ ...conta, tipo: e.target.value })}>
          <option value="CORRENTE">CORRENTE</option>
          <option value="POUPANCA">POUPANCA</option>
        </select>
      </div>
      <button onClick={handleCreate} style={{ marginTop: 8 }}>Criar</button>
      {status ? <div>{status}</div> : null}
    </div>
  );
}
