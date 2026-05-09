import { useState, useEffect } from 'react';

const empty = { productName: '', price: '', stockLeft: '', sell: '', high: '' };

export default function StockModal({ stock, onSave, onClose }) {
  const [form, setForm] = useState(empty);

  useEffect(() => {
    setForm(stock ? { ...stock } : empty);
  }, [stock]);

  const set = (k, v) => setForm(f => ({ ...f, [k]: v }));

  const submit = (e) => {
    e.preventDefault();
    onSave({ ...form, price: +form.price, stockLeft: +form.stockLeft, sell: +form.sell, high: +form.high });
  };

  return (
    <div className="overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <h2>{stock?.id ? 'Edit Stock' : 'Add Stock'}</h2>
        <form onSubmit={submit}>
          {[['productName', 'Product Name', 'text'], ['price', 'Price', 'number'], ['stockLeft', 'Stock Left', 'number'], ['sell', 'Sell', 'number'], ['high', 'High', 'number']].map(([key, label, type]) => (
            <div className="field" key={key}>
              <label>{label}</label>
              <input type={type} value={form[key]} onChange={e => set(key, e.target.value)} required step="any" />
            </div>
          ))}
          <div className="modal-actions">
            <button type="button" className="btn-outline" onClick={onClose}>Cancel</button>
            <button type="submit" className="btn">Save</button>
          </div>
        </form>
      </div>
    </div>
  );
}
