import { useCallback, useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import CameraModal from '../components/CameraModal';
import { getAccountId, getServerUrl } from '../storage';
import { useNavigation } from '@react-navigation/native';
import axios from 'axios';
import { resolveServerUrl } from '../utils/network';

export default function Home() {
  const navigation = useNavigation();
  const [cameraOpen, setCameraOpen] = useState(false);
  const [sending, setSending] = useState(false);

  const handleScanned = useCallback(async (data: string) => {
    setSending(true);
    try {
      const server = resolveServerUrl(await getServerUrl());
      const contaId = await getAccountId();
      if (!server || !contaId) {
        setSending(false);
        setCameraOpen(false);
        return;
      }
      const txid = parseTxid(data);
      const body = { txid, contaId };
      await axios.post(`${server}/pagamento/processar`, body, { timeout: 10000 });
    } catch (e) {
    } finally {
      setSending(false);
      setCameraOpen(false);
    }
  }, []);

  function parseTxid(raw: string): string {
    if (!raw) return '';
    if (raw.includes('TXID=')) {
      const start = raw.indexOf('TXID=') + 5;
      const rest = raw.substring(start);
      const endIdx = rest.indexOf('|');
      return endIdx >= 0 ? rest.substring(0, endIdx) : rest;
    }
    return raw.trim();
  }

  return (
    <View style={{ flex: 1, backgroundColor: '#fff' }}>
      <View style={{ height: 56, alignItems: 'center', flexDirection: 'row', paddingHorizontal: 16 }}>
        <TouchableOpacity onPress={() => navigation.navigate('Settings' as never)}>
          <Ionicons name="settings-outline" size={24} color="#333" />
        </TouchableOpacity>
      </View>
      <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
        <Text>Leitor de QRCode</Text>
      </View>
      <View style={{ alignItems: 'center', justifyContent: 'center', paddingBottom: 24 }}>
        <TouchableOpacity onPress={() => setCameraOpen(true)} style={{ backgroundColor: '#1e90ff', borderRadius: 48, width: 72, height: 72, alignItems: 'center', justifyContent: 'center' }} disabled={sending}>
          <Ionicons name="camera" size={28} color="#fff" />
        </TouchableOpacity>
      </View>
      <CameraModal visible={cameraOpen} onClose={() => setCameraOpen(false)} onScanned={handleScanned} />
    </View>
  );
}
