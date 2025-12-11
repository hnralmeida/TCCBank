import axios from 'axios';

let baseUrl = 'http://localhost:8080';

export function setBaseUrl(url: string) {
  baseUrl = url || baseUrl;
}

export async function createCliente(payload: any) {
  const r = await axios.post(baseUrl + '/cliente', payload);
  return r.data;
}

export async function createConta(payload: any) {
  const r = await axios.post(baseUrl + '/conta', payload);
  return r.data;
}

export async function updateConta(id: string, payload: any) {
  const r = await axios.put(baseUrl + '/conta/' + id, payload);
  return r.data;
}

export async function createChavePix(payload: any) {
  const r = await axios.post(baseUrl + '/chavepix', payload);
  return r.data;
}

export async function listContas() {
  const r = await axios.get(baseUrl + '/conta');
  return r.data;
}

export async function getConta(id: string) {
  const r = await axios.get(baseUrl + '/conta/' + id);
  return r.data;
}

export async function createCobrancaPix(payload: any) {
  const r = await axios.post(baseUrl + '/cobrancapix', payload);
  return r.data;
}

export async function getCobrancaQrPng(txid: string) {
  const r = await axios.get(baseUrl + '/cobrancapix/' + encodeURIComponent(txid), { responseType: 'arraybuffer' });
  const bytes = new Uint8Array(r.data as ArrayBuffer);
  let binary = '';
  const chunk = 0x8000;
  for (let i = 0; i < bytes.length; i += chunk) {
    binary += String.fromCharCode.apply(null, Array.prototype.slice.call(bytes, i, i + chunk));
  }
  const b64 = typeof window !== 'undefined' ? window.btoa(binary) : Buffer.from(binary, 'binary').toString('base64');
  return 'data:image/png;base64,' + b64;
}

export async function listCobrancas() {
  const r = await axios.get(baseUrl + '/cobrancapix');
  return r.data;
}
