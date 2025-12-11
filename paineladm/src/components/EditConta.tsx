"use client";
import { useEffect, useState } from 'react';
import { getConta, setBaseUrl, updateConta, createChavePix } from '@/lib/api';

export default function EditConta({ server, contaId }: { server: string; contaId: string }) {
  const [conta, setConta] = useState<any>(null);
  const [status, setStatus] = useState('');
  const [chave, setChave] = useState({ tipo: 'EMAIL', valor: '', ativa: true });

  useEffect(() => {
    (async () => {
      if (!contaId) return;
      setBaseUrl(server);
      try {
        const c = await getConta(contaId);
        setConta(c);
      } catch (e) {
        setStatus('Erro ao carregar conta');
      }
    })();
  }, [server, contaId]);

  async function save() {
    if (!conta) return;
    setBaseUrl(server);
    setStatus('');
    try {
      await updateConta(conta.id, { numero: conta.numero, agencia: conta.agencia, saldo: conta.saldo, tipo: conta.tipo });
      setStatus('Conta atualizada');
    } catch (e) {
      setStatus('Erro ao atualizar conta');
    }
  }

  async function addKey() {
    if (!conta) return;
    setBaseUrl(server);
    setStatus('');
    try {
      await createChavePix({ ...chave, conta: { id: conta.id } });
      setStatus('Chave Pix criada');
    } catch (e) {
      setStatus('Erro ao criar chave');
    }
  }

  if (!conta) return <div>Selecione uma conta</div>;

  return (
    <div style={{ display: 'grid', gap: 12 }}>
      <h3>Editar conta</h3>
      <div style={{ display: 'grid', gap: 8, gridTemplateColumns: '1fr 1fr' }}>
        <input value={conta.numero ?? ''} onChange={e => setConta({ ...conta, numero: e.target.value })} placeholder="Número da conta" />
        <input value={conta.agencia ?? ''} onChange={e => setConta({ ...conta, agencia: e.target.value })} placeholder="Agência" />
        <input value={conta.saldo ?? ''} onChange={e => setConta({ ...conta, saldo: e.target.value })} placeholder="Saldo" />
        <select value={conta.tipo ?? 'CORRENTE'} onChange={e => setConta({ ...conta, tipo: e.target.value })}>
          <option value="CORRENTE">CORRENTE</option>
          <option value="POUPANCA">POUPANCA</option>
        </select>
      </div>
      <button onClick={save}>Salvar</button>

      <h4>Criar chave Pix</h4>
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
      <button onClick={addKey}>Criar chave</button>
      {status ? <div>{status}</div> : null}
    </div>
  );
}
