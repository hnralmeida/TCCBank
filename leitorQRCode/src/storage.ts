import AsyncStorage from '@react-native-async-storage/async-storage';

const SERVER_URL_KEY = 'server_url';
const ACCOUNT_ID_KEY = 'account_id';
const ACCOUNT_NUMBER_KEY = 'account_number';
const ACCOUNT_NAME_KEY = 'account_name';

export async function setServerUrl(url: string) {
  await AsyncStorage.setItem(SERVER_URL_KEY, url);
}

export async function getServerUrl(): Promise<string | null> {
  return AsyncStorage.getItem(SERVER_URL_KEY);
}

export async function setAccountId(id: string) {
  await AsyncStorage.setItem(ACCOUNT_ID_KEY, id);
}

export async function getAccountId(): Promise<string | null> {
  return AsyncStorage.getItem(ACCOUNT_ID_KEY);
}

export async function setAccountNumber(num: string) {
  await AsyncStorage.setItem(ACCOUNT_NUMBER_KEY, num);
}

export async function getAccountNumber(): Promise<string | null> {
  return AsyncStorage.getItem(ACCOUNT_NUMBER_KEY);
}

export async function setAccountName(name: string) {
  await AsyncStorage.setItem(ACCOUNT_NAME_KEY, name);
}

export async function getAccountName(): Promise<string | null> {
  return AsyncStorage.getItem(ACCOUNT_NAME_KEY);
}
