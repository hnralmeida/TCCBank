import { useEffect, useState } from 'react';
import { Modal, View, Text, TouchableOpacity } from 'react-native';
import { CameraView, useCameraPermissions } from 'expo-camera';

type Props = {
  visible: boolean;
  onClose: () => void;
  onScanned: (data: string) => void;
};

export default function CameraModal({ visible, onClose, onScanned }: Props) {
  const [permission, requestPermission] = useCameraPermissions();
  const [scanned, setScanned] = useState(false);

  useEffect(() => {
    if (!permission) {
      requestPermission();
    }
  }, [permission]);

  function handleBarCodeScanned({ data }: { data: string }) {
    if (scanned) return;
    setScanned(true);
    onScanned(data);
  }

  return (
    <Modal visible={visible} animationType="slide" onRequestClose={onClose}>
      {permission && !permission.granted ? (
        <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
          <Text>Sem permissão para usar a câmera</Text>
          <TouchableOpacity onPress={requestPermission} style={{ marginTop: 16 }}>
            <Text>Permitir</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <View style={{ flex: 1 }}>
          <CameraView
            onBarcodeScanned={handleBarCodeScanned as any}
            barcodeScannerSettings={{
              barcodeTypes: ['qr']
            }}
            style={{ flex: 1 }}
          />
          <View style={{ position: 'absolute', bottom: 24, alignSelf: 'center' }}>
            <TouchableOpacity onPress={onClose} style={{ backgroundColor: '#000', paddingHorizontal: 20, paddingVertical: 12, borderRadius: 8 }}>
              <Text style={{ color: '#fff' }}>Fechar</Text>
            </TouchableOpacity>
          </View>
        </View>
      )}
    </Modal>
  );
}
