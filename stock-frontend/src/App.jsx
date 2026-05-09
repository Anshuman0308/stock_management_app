import { useState, useEffect, useRef, useCallback } from 'react';
import { getStocks, searchStocks, createStock, updateStock, deleteStock, exportExcel, importExcel } from './api';
import StockModal from './StockModal';
import './App.css';

export default function App() {
  const [stocks, setStocks] = useState([]);
  const [query, setQuery] = useState('');
  const [modal, setModal] = useState(null); // null | {} | stock
  const [msg, setMsg] = useState('');
  const fileRef = useRef();

  const load = useCallback(async (q = query) => {
    const data = q ? await searchStocks(q) : await getStocks();
    setStocks(data);
  }, [query]);

  useEffect(() => { load(); }, [load]);

  const flash = useCallback((m) => { setMsg(m); setTimeout(() => setMsg(''), 2500); }, []);

  const handleSave = useCallback(async (form) => {
    if (form.id) await updateStock(form.id, form);
    else await createStock(form);
    setModal(null);
    load();
    flash(form.id ? 'Updated.' : 'Added.');
  }, [load, flash]);

  const handleDelete = useCallback(async (id) => {
    if (!confirm('Delete this stock?')) return;
    await deleteStock(id);
    load();
    flash('Deleted.');
  }, [load, flash]);

  const handleImport = useCallback(async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    const res = await importExcel(file);
    load();
    flash(res);
    fileRef.current.value = '';
  }, [load, flash]);

  return (
    <div className="app">
      <header>
        <h1>Stock Management</h1>
        <div className="header-actions">
          <input
            className="search"
            placeholder="Search product..."
            value={query}
            onChange={e => { setQuery(e.target.value); load(e.target.value); }}
          />
          <button className="btn" onClick={() => setModal({})}>+ Add</button>
          <button className="btn-outline" onClick={exportExcel}>Export</button>
          <button className="btn-outline" onClick={() => fileRef.current.click()}>Import</button>
          <input ref={fileRef} type="file" accept=".xlsx" hidden onChange={handleImport} />
        </div>
      </header>

      {msg && <div className="toast">{msg}</div>}

      <table>
        <thead>
          <tr>{['ID', 'Product', 'Price', 'Stock Left', 'Sell', 'High', ''].map(h => <th key={h}>{h}</th>)}</tr>
        </thead>
        <tbody>
          {stocks.length === 0
            ? <tr><td colSpan={7} className="empty">No stocks found.</td></tr>
            : stocks.map(s => (
              <tr key={s.id}>
                <td>{s.id}</td>
                <td>{s.productName}</td>
                <td>{s.price.toFixed(2)}</td>
                <td>{s.stockLeft}</td>
                <td>{s.sell}</td>
                <td>{s.high.toFixed(2)}</td>
                <td className="actions">
                  <button className="btn-sm" onClick={() => setModal(s)}>Edit</button>
                  <button className="btn-sm danger" onClick={() => handleDelete(s.id)}>Del</button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>

      {modal !== null && <StockModal stock={modal} onSave={handleSave} onClose={() => setModal(null)} />}
    </div>
  );
}
