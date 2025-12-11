import { useEffect, useState } from 'react';
import { View, Text, TextInput, TouchableOpacity } from 'react-native';
import { getAccountId, getServerUrl, setAccountId, setServerUrl, getAccountNumber, setAccountNumber, setAccountName, getAccountName } from '../storage';
import { Ionicons } from '@expo/vector-icons';
import axios from 'axios';
import { resolveServerUrl } from '../utils/network';

export default function Settings() {
  const [server, setServer] = useState('http://localhost:8090');
  const [accountId, setAccount] = useState('');
  const [accountNumber, setAccountNum] = useState('');
  const [foundInfo, setFoundInfo] = useState<string>('');
  const [accountName, setAccountNameState] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    (async () => {
      const s = await getServerUrl();
      const a = await getAccountId();
      const n = await getAccountNumber();
      const an = await getAccountName();
      if (s) setServer(s);
      if (a) setAccount(a);
      if (n) setAccountNum(n);
      if (an) setAccountNameState(an);
    })();
  }, []);

  async function save() {
    setSaving(true);
    try {
      await setServerUrl(server);
      await setAccountId(accountId);
      await setAccountNumber(accountNumber);
    } finally {
      setSaving(false);
    }
  }

  async function buscarConta() {
    setSaving(true);
    try {
      const url = `${resolveServerUrl(server)}/conta/numero`;
      const resp = await axios.get(url, { params: { q: accountNumber } });
      if (resp.status !== 200) {
        setFoundInfo('Conta não encontrada');
        return;
      }
      const data = resp.data;
      setAccount(data.id);
      await setAccountId(data.id);
      await setAccountNumber(accountNumber);
      const nome = data.cliente ? (data.cliente.nome ?? data.cliente.registro ?? '') : '';
      setAccountNameState(nome);
      await setAccountName(nome);
      setFoundInfo(`Cliente: ${nome || 'N/A'}`);
    } catch (e) {
      setFoundInfo('Erro ao buscar conta');
    } finally {
      setSaving(false);
    }
  }

  return (
    <View style={{ flex: 1, padding: 16 }}>
      <Text style={{ marginBottom: 8 }}>Endereço do servidor</Text>
      <TextInput value={server} onChangeText={setServer} placeholder="http://192.168.0.10:8090" autoCapitalize="none" style={{ borderWidth: 1, borderColor: '#ccc', borderRadius: 8, padding: 10, marginBottom: 16 }} />
      <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 8 }}>
        <Text style={{ marginRight: 8 }}>Número de conta</Text>
        <Ionicons name="sync-outline" size={18} color="#666" />
      </View>
      <TextInput value={accountNumber} onChangeText={setAccountNum} placeholder="123456" autoCapitalize="none" style={{ borderWidth: 1, borderColor: '#ccc', borderRadius: 8, padding: 10, marginBottom: 12 }} />
      {foundInfo ? <Text style={{ marginBottom: 12, color: '#333' }}>{foundInfo}</Text> : null}
      <TouchableOpacity onPress={buscarConta} disabled={saving} style={{ backgroundColor: '#888', paddingVertical: 10, borderRadius: 8, alignItems: 'center', marginBottom: 16 }}>
        <Text style={{ color: '#fff' }}>{saving ? 'Buscando...' : 'Buscar conta'}</Text>
      </TouchableOpacity>
      {accountName ? <Text style={{ marginBottom: 12, color: '#333' }}>Nome da conta: {accountName}</Text> : null}
      
      
      <TouchableOpacity onPress={save} disabled={saving} style={{ backgroundColor: '#1e90ff', paddingVertical: 12, borderRadius: 8, alignItems: 'center' }}>
        <Text style={{ color: '#fff' }}>{saving ? 'Salvando...' : 'Salvar'}</Text>
      </TouchableOpacity>
    </View>
  );
}
