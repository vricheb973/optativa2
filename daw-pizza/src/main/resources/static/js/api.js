/* ============================================================
   api.js — Módulo centralizado de llamadas a la API
   ============================================================ */

const API = (() => {
  const BASE = '';

  // ---- Toast system ----
  function showToast(message, type = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toast-container';
      container.className = 'toast-container-custom';
      document.body.appendChild(container);
    }

    const icons = {
      success: 'bi-check-circle-fill',
      error: 'bi-exclamation-triangle-fill',
      info: 'bi-info-circle-fill'
    };

    const toast = document.createElement('div');
    toast.className = `toast-custom toast-${type}`;
    toast.innerHTML = `<i class="bi ${icons[type] || icons.info}"></i><span>${message}</span>`;
    toast.onclick = () => dismissToast(toast);
    container.appendChild(toast);

    setTimeout(() => dismissToast(toast), 4000);
  }

  function dismissToast(el) {
    if (!el || !el.parentNode) return;
    el.style.animation = 'toastSlideOut 0.3s forwards';
    setTimeout(() => el.remove(), 300);
  }

  // ---- HTTP helpers ----
  async function request(method, url, body = null) {
    const opts = {
      method,
      headers: { 'Content-Type': 'application/json' }
    };
    if (body) opts.body = JSON.stringify(body);

    let res;
    try {
      res = await fetch(BASE + url, opts);
    } catch (err) {
      showToast('Error de conexión con el servidor', 'error');
      throw err;
    }

    // DELETE / empty 200
    if (res.status === 200 && res.headers.get('content-length') === '0') return null;
    if (res.status === 204) return null;

    if (!res.ok) {
      let msg;
      try {
        msg = await res.text();
      } catch {
        msg = 'Error desconocido';
      }
      showToast(msg || `Error ${res.status}`, 'error');
      throw new Error(msg);
    }

    // Some OK responses may have no body
    const text = await res.text();
    if (!text) return null;
    return JSON.parse(text);
  }

  // ---- CRUD shortcuts ----
  const get    = (url) => request('GET', url);
  const post   = (url, body) => request('POST', url, body);
  const put    = (url, body) => request('PUT', url, body);
  const del    = (url) => request('DELETE', url);

  return { get, post, put, del, showToast };
})();
