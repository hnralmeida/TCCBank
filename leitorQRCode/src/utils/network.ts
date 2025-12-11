import { Platform } from 'react-native';

export function resolveServerUrl(url: string | null): string {
  const base = (url && url.trim()) ? url.trim() : 'http://localhost:8090';
  try {
    const u = new URL(base);
    const host = u.hostname.toLowerCase();
    if (host === 'localhost') {
      if (Platform.OS === 'android') {
        u.hostname = '10.0.2.2';
      } else if (Platform.OS === 'ios') {
        u.hostname = '127.0.0.1';
      }
    }
    return u.toString().replace(/\/$/, '');
  } catch {
    return base;
  }
}
