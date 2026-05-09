const BASE = '/api';

export const getStocks = () => fetch(`${BASE}/stocks`).then(r => r.json());
export const searchStocks = (name) => fetch(`${BASE}/stocks/search?name=${encodeURIComponent(name)}`).then(r => r.json());
export const createStock = (stock) => fetch(`${BASE}/stocks`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(stock) }).then(r => r.json());
export const updateStock = (id, stock) => fetch(`${BASE}/stocks/${id}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(stock) }).then(r => r.json());
export const deleteStock = (id) => fetch(`${BASE}/stocks/${id}`, { method: 'DELETE' });
export const exportExcel = () => window.open(`${BASE}/excel/export`);
export const importExcel = (file) => {
  const form = new FormData();
  form.append('file', file);
  return fetch(`${BASE}/excel/import`, { method: 'POST', body: form }).then(r => r.text());
};
