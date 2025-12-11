"use client";
import { useState } from 'react';
import { createCliente, createConta, createChavePix, setBaseUrl, updateConta } from '@/lib/api';

export default function AccountForm() {
  const [server, setServer] = useState('http://localhost:8080');
  const [cliente, setCliente] = useState({ nome: '', registro: '', email: '', telefone: '' });
  const [conta, setConta] = useState({ numero: '', agencia: '', saldo: '', tipo: 'CORRENTE' });
  const [contaId, setContaId] = useState('');
  const [chave, setChave] = useState({ tipo: 'EMAIL', valor: '', ativa: true });
  const [status, setStatus] = useState('');

  async function handleCreate() {
    setBaseUrl(server);
    setStatus('');
    try {
      const c = await createCliente(cliente);
      const createdConta = await createConta({ ...conta, cliente: { id: c.id } });
      setContaId(createdConta.id);
      setStatus('Conta criada');
    } catch (e) {
      setStatus('Erro ao criar');
    }
  }

  async function handleUpdate() {
    setBaseUrl(server);
    setStatus('');
    try {
      await updateConta(contaId, conta);
      setStatus('Conta atualizada');
    } catch (e) {
      setStatus('Erro ao atualizar');
    }
  }

  async function handleCreateKey() {
    setBaseUrl(server);
    setStatus('');
    try {
      await createChavePix({ ...chave, conta: { id: contaId } });
      setStatus('Chave Pix criada');
    } catch (e) {
      setStatus('Erro ao criar chave');
    }
  }

  return (
    <div style={{ display: 'grid', gap: 16 }}>
      <div>
        <label>Servidor</label>
        <input value={server} onChange={e => setServer(e.target.value)} placeholder="http://localhost:8080" />
      </div>
      <div style={{ border: '1px solid #ddd', padding: 12, borderRadius: 8 }}>
        <h3>Criar cliente e conta</h3>
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
        {contaId ? <div>ID da conta: {contaId}</div> : null}
      </div>

      <div style={{ border: '1px solid #ddd', padding: 12, borderRadius: 8 }}>
        <h3>Editar conta</h3>
        <input value={contaId} onChange={e => setContaId(e.target.value)} placeholder="ID da conta" />
        <div style={{ display: 'grid', gap: 8, gridTemplateColumns: '1fr 1fr' }}>
          <input value={conta.numero} onChange={e => setConta({ ...conta, numero: e.target.value })} placeholder="Número da conta" />
          <input value={conta.agencia} onChange={e => setConta({ ...conta, agencia: e.target.value })} placeholder="Agência" />
          <input value={conta.saldo} onChange={e => setConta({ ...conta, saldo: e.target.value })} placeholder="Saldo" />
          <select value={conta.tipo} onChange={e => setConta({ ...conta, tipo: e.target.value })}>
            <option value="CORRENTE">CORRENTE</option>
            <option value="POUPANCA">POUPANCA</option>
          </select>
        </div>
        <button onClick={handleUpdate} style={{ marginTop: 8 }}>Salvar</button>
      </div>

      <div style={{ border: '1px solid #ddd', padding: 12, borderRadius: 8 }}>
        <h3>Criar chave Pix</h3>
        <div style={{ display: 'grid', gap: 8, gridTemplateColumns: '1fr 1fr' }}>
          <select value={chave.tipo} onChange={e => setChave({ ...chave, tipo: e.target.value })}>
            <option value="EMAIL">EMAIL</option>
            <option value="CPF">CPF</option>
            <option value="CNPJ">CNPJ</option>
            <option value="ALEATORIA">ALEATORIA</option>
            <option value="CELULAR">CELULAR</option>
          </select>
          <input value={chave.valor} onChange={e => setChave({ ...chave, valor: e.target.value })} placeholder="Valor da chave" />
          <label><input type="checkbox" checked={chave.ativa} onChange={e => setChave({ ...chave, ativa: e.target.checked })} /> Ativa</label>
        </div>
        <button onClick={handleCreateKey} style={{ marginTop: 8 }}>Criar chave</button>
      </div>

      {status ? <div>{status}</div> : null}
    </div>
  );
}
