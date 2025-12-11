export default function Modal({ open, onClose, children }: { open: boolean; onClose: () => void; children: React.ReactNode }) {
  if (!open) return null;
  return (
    <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div style={{ background: '#fff', padding: 16, borderRadius: 8, maxWidth: 520, width: '90%' }}>
        {children}
        <div style={{ marginTop: 16, textAlign: 'right' }}>
          <button onClick={onClose} style={{ padding: '8px 12px' }}>Fechar</button>
        </div>
      </div>
    </div>
  );
}
