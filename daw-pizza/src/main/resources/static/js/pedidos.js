/* ============================================================
   pedidos.js — Módulo CRUD de Pedidos + PizzaPedido
   ============================================================ */

const Pedidos = (() => {
  let allPedidos = [];
  let expandedId = null;

  async function render(container) {
    container.innerHTML = `
      <div class="page-section">
        <div class="page-header">
          <div>
            <h2><i class="bi bi-receipt"></i> Pedidos</h2>
            <p>Gestiona los pedidos y sus líneas de pizza</p>
          </div>
          <button class="btn-accent" id="btn-new-pedido">
            <i class="bi bi-plus-lg"></i> Nuevo Pedido
          </button>
        </div>

        <div class="stats-row" id="pedido-stats"></div>

        <div class="glass-card">
          <div class="card-body-custom" style="padding:0;">
            <div id="pedido-table-wrapper">
              <div class="spinner-wrapper"><div class="spinner-custom"></div></div>
            </div>
          </div>
        </div>
      </div>`;

    document.getElementById('btn-new-pedido').onclick = () => openPedidoForm();
    await loadData();
  }

  async function loadData() {
    try {
      allPedidos = await API.get('/pedidos');
      renderStats();
      renderTable();
    } catch (e) {
      document.getElementById('pedido-table-wrapper').innerHTML =
        '<div class="empty-state"><i class="bi bi-wifi-off d-block"></i><p>Error al cargar los pedidos</p></div>';
    }
  }

  function renderStats() {
    const total = allPedidos.length;
    const sumTotal = allPedidos.reduce((s, p) => s + (p.total || 0), 0);
    const metodoCounts = { RECOGER: 0, DOMICILIO: 0, LOCAL: 0 };
    allPedidos.forEach(p => { if (metodoCounts[p.metodo] !== undefined) metodoCounts[p.metodo]++; });

    document.getElementById('pedido-stats').innerHTML = `
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--accent-subtle);color:var(--accent)"><i class="bi bi-receipt"></i></div>
        <div class="stat-value">${total}</div>
        <div class="stat-label">Total Pedidos</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--success-bg);color:var(--success)"><i class="bi bi-currency-euro"></i></div>
        <div class="stat-value">${sumTotal.toFixed(2)} €</div>
        <div class="stat-label">Facturación</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--info-bg);color:var(--info)"><i class="bi bi-truck"></i></div>
        <div class="stat-value">${metodoCounts.DOMICILIO}</div>
        <div class="stat-label">A Domicilio</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--warning-bg);color:var(--warning)"><i class="bi bi-shop"></i></div>
        <div class="stat-value">${metodoCounts.LOCAL}</div>
        <div class="stat-label">En Local</div>
      </div>`;
  }

  function metodoColor(m) {
    switch (m) {
      case 'DOMICILIO': return 'success';
      case 'RECOGER': return 'info';
      case 'LOCAL': return 'warning';
      default: return 'accent';
    }
  }

  function metodoIcon(m) {
    switch (m) {
      case 'DOMICILIO': return 'bi-truck';
      case 'RECOGER': return 'bi-bag-check-fill';
      case 'LOCAL': return 'bi-shop';
      default: return 'bi-question-circle';
    }
  }

  function formatDate(dt) {
    if (!dt) return '—';
    const d = new Date(dt);
    return d.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' })
      + ' ' + d.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
  }

  function renderTable() {
    const wrapper = document.getElementById('pedido-table-wrapper');
    if (allPedidos.length === 0) {
      wrapper.innerHTML = '<div class="empty-state"><i class="bi bi-inbox d-block"></i><p>No hay pedidos registrados</p></div>';
      return;
    }

    let rows = allPedidos.map(p => {
      const expanded = expandedId === p.id;
      return `
      <tr class="${expanded ? 'table-active' : ''}" style="cursor:pointer;" data-pedido-id="${p.id}">
        <td class="col-name">#${p.id}</td>
        <td>${esc(p.cliente || '—')}</td>
        <td>${esc(p.telefono || '—')}</td>
        <td><span class="badge-custom badge-${metodoColor(p.metodo)}"><i class="bi ${metodoIcon(p.metodo)}"></i> ${p.metodo}</span></td>
        <td class="col-price">${(p.total || 0).toFixed(2)} €</td>
        <td>${formatDate(p.fecha)}</td>
        <td><span class="badge-custom badge-accent">${p.numeroPizzas}</span></td>
        <td>
          <div class="action-btns">
            <button class="btn-icon btn-icon-view" title="Ver detalle" data-id="${p.id}"><i class="bi bi-chevron-${expanded ? 'up' : 'down'}"></i></button>
            <button class="btn-icon btn-icon-edit" title="Editar" data-id="${p.id}"><i class="bi bi-pencil-fill"></i></button>
            <button class="btn-icon btn-icon-delete" title="Eliminar" data-id="${p.id}"><i class="bi bi-trash3-fill"></i></button>
          </div>
        </td>
      </tr>
      ${expanded ? `<tr class="pedido-detail-row"><td colspan="8"><div class="pedido-detail-content" id="pedido-detail-${p.id}"><div class="spinner-wrapper"><div class="spinner-custom"></div></div></div></td></tr>` : ''}`;
    }).join('');

    wrapper.innerHTML = `
      <table class="table-custom">
        <thead><tr><th>ID</th><th>Cliente</th><th>Teléfono</th><th>Método</th><th>Total</th><th>Fecha</th><th>Pizzas</th><th>Acciones</th></tr></thead>
        <tbody>${rows}</tbody>
      </table>`;

    // Toggle detail
    wrapper.querySelectorAll('.btn-icon-view').forEach(btn => {
      btn.onclick = (e) => {
        e.stopPropagation();
        const id = parseInt(btn.dataset.id);
        expandedId = expandedId === id ? null : id;
        renderTable();
        if (expandedId === id) loadPizzaPedidos(id);
      };
    });

    // Row click to toggle too
    wrapper.querySelectorAll('tr[data-pedido-id]').forEach(row => {
      row.onclick = (e) => {
        if (e.target.closest('.action-btns')) return;
        const id = parseInt(row.dataset.pedidoId);
        expandedId = expandedId === id ? null : id;
        renderTable();
        if (expandedId === id) loadPizzaPedidos(id);
      };
    });

    wrapper.querySelectorAll('.btn-icon-edit').forEach(btn => {
      btn.onclick = (e) => {
        e.stopPropagation();
        openPedidoForm(allPedidos.find(p => p.id == btn.dataset.id));
      };
    });
    wrapper.querySelectorAll('.btn-icon-delete').forEach(btn => {
      btn.onclick = (e) => {
        e.stopPropagation();
        confirmDeletePedido(btn.dataset.id);
      };
    });

    // If there's an expanded row, load its data
    if (expandedId) loadPizzaPedidos(expandedId);
  }

  // ---- PizzaPedido sub-resource ----
  async function loadPizzaPedidos(idPedido) {
    const el = document.getElementById(`pedido-detail-${idPedido}`);
    if (!el) return;

    try {
      const pedido = allPedidos.find(p => p.id === idPedido);
      const pizzas = pedido?.pizzas || await API.get(`/pedidos/${idPedido}/pizzas`);
      renderPizzaPedidos(idPedido, pizzas, el, pedido);
    } catch (e) {
      el.innerHTML = '<p style="color:var(--danger);padding:12px;">Error al cargar las pizzas del pedido</p>';
    }
  }

  function renderPizzaPedidos(idPedido, pizzas, el, pedido) {
    const notasHtml = pedido && pedido.notas
      ? `<p style="color:var(--text-secondary);font-size:0.85rem;margin-bottom:16px;"><i class="bi bi-chat-left-text text-accent me-1"></i> <strong>Notas:</strong> ${esc(pedido.notas)}</p>`
      : '';

    let rows = '';
    if (pizzas.length === 0) {
      rows = '<tr><td colspan="5" class="text-center" style="color:var(--text-muted);padding:24px;">Sin pizzas en este pedido</td></tr>';
    } else {
      rows = pizzas.map(pp => `
        <tr>
          <td class="col-name">${esc(pp.pizza)}</td>
          <td>${pp.cantidad}</td>
          <td class="col-price">${(pp.precio || 0).toFixed(2)} €</td>
          <td>
            <div class="action-btns">
              <button class="btn-icon btn-icon-edit btn-edit-pp" data-pedido="${idPedido}" data-id="${pp.id}" title="Editar"><i class="bi bi-pencil-fill"></i></button>
              <button class="btn-icon btn-icon-delete btn-del-pp" data-pedido="${idPedido}" data-id="${pp.id}" title="Eliminar"><i class="bi bi-trash3-fill"></i></button>
            </div>
          </td>
        </tr>`).join('');
    }

    el.innerHTML = `
      ${notasHtml}
      <div class="detail-section">
        <h5><i class="bi bi-list-ul"></i> Líneas del Pedido
          <button class="btn-accent btn-sm ms-auto" id="btn-add-pp-${idPedido}" style="font-size:0.75rem;padding:6px 14px;">
            <i class="bi bi-plus-lg"></i> Añadir Pizza
          </button>
        </h5>
        <table class="table-custom">
          <thead><tr><th>Pizza</th><th>Cantidad</th><th>Precio</th><th>Acciones</th></tr></thead>
          <tbody>${rows}</tbody>
        </table>
      </div>`;

    // Bind add
    document.getElementById(`btn-add-pp-${idPedido}`).onclick = () => openPPForm(idPedido);

    // Bind edit/delete
    el.querySelectorAll('.btn-edit-pp').forEach(btn => {
      btn.onclick = () => {
        const pp = pizzas.find(x => x.id == btn.dataset.id);
        openPPForm(parseInt(btn.dataset.pedido), pp);
      };
    });
    el.querySelectorAll('.btn-del-pp').forEach(btn => {
      btn.onclick = () => confirmDeletePP(parseInt(btn.dataset.pedido), parseInt(btn.dataset.id), pizzas);
    });
  }

  // ---- Pedido Form ----
  async function openPedidoForm(pedido = null) {
    const isEdit = !!pedido;

    let clientes = [];
    try { clientes = await Clientes.loadData(); } catch (e) { /* empty */ }

    // For edit, we need to find the idCliente from the pedido's cliente name
    let selectedClienteId = '';
    if (isEdit && pedido.cliente) {
      const c = clientes.find(cl => cl.nombre === pedido.cliente);
      if (c) selectedClienteId = c.id;
    }

    const clienteOpts = clientes.map(c =>
      `<option value="${c.id}" ${selectedClienteId == c.id ? 'selected' : ''}>${esc(c.nombre)}</option>`
    ).join('');

    document.getElementById('formModalTitle').innerHTML =
      `<i class="bi bi-${isEdit ? 'pencil' : 'receipt'}"></i> ${isEdit ? 'Editar' : 'Nuevo'} Pedido`;

    document.getElementById('formModalBody').innerHTML = `
      <form id="pedido-form" novalidate>
        <div class="mb-3">
          <label class="form-label" for="pedf-cliente">Cliente</label>
          <select class="form-select" id="pedf-cliente" required>
            <option value="">Selecciona un cliente</option>
            ${clienteOpts}
          </select>
        </div>
        <div class="mb-3">
          <label class="form-label" for="pedf-metodo">Método de Entrega</label>
          <select class="form-select" id="pedf-metodo" required>
            <option value="">Selecciona método</option>
            <option value="RECOGER" ${isEdit && pedido.metodo === 'RECOGER' ? 'selected' : ''}>🛍️ Recoger</option>
            <option value="DOMICILIO" ${isEdit && pedido.metodo === 'DOMICILIO' ? 'selected' : ''}>🚚 A Domicilio</option>
            <option value="LOCAL" ${isEdit && pedido.metodo === 'LOCAL' ? 'selected' : ''}>🏪 En Local</option>
          </select>
        </div>
        <div class="mb-3">
          <label class="form-label" for="pedf-notas">Notas</label>
          <textarea class="form-control" id="pedf-notas" rows="2" maxlength="200"
            placeholder="Instrucciones especiales (opcional)">${isEdit ? esc(pedido.notas || '') : ''}</textarea>
        </div>
      </form>`;

    const modal = new bootstrap.Modal(document.getElementById('formModal'));
    modal.show();

    document.getElementById('formModalSave').onclick = async () => {
      const form = document.getElementById('pedido-form');
      if (!form.checkValidity()) { form.reportValidity(); return; }

      const body = {
        id: isEdit ? pedido.id : 0,
        idCliente: parseInt(val('pedf-cliente')),
        metodo: val('pedf-metodo'),
        notas: val('pedf-notas')
      };

      try {
        if (isEdit) {
          await API.put(`/pedidos/${pedido.id}`, body);
          API.showToast('Pedido actualizado correctamente', 'success');
        } else {
          await API.post('/pedidos', body);
          API.showToast('Pedido creado correctamente', 'success');
        }
        modal.hide();
        await loadData();
      } catch (e) { /* toast already shown */ }
    };
  }

  // ---- PizzaPedido Form ----
  async function openPPForm(idPedido, pp = null) {
    const isEdit = !!pp;

    let pizzas = [];
    try { pizzas = await API.get('/pizzas'); } catch (e) { /* empty */ }
    const disponibles = pizzas.filter(p => p.disponible);

    const pizzaOpts = disponibles.map(p =>
      `<option value="${p.id}" ${isEdit && pp.idPizza === p.id ? 'selected' : ''}>${esc(p.nombre)} — ${p.precio.toFixed(2)} €</option>`
    ).join('');

    document.getElementById('formModalTitle').innerHTML =
      `<i class="bi bi-${isEdit ? 'pencil' : 'plus-circle'}"></i> ${isEdit ? 'Editar' : 'Añadir'} Pizza al Pedido #${idPedido}`;

    document.getElementById('formModalBody').innerHTML = `
      <form id="pp-form" novalidate>
        <div class="mb-3">
          <label class="form-label" for="ppf-pizza">Pizza</label>
          <select class="form-select" id="ppf-pizza" required>
            <option value="">Selecciona una pizza</option>
            ${pizzaOpts}
          </select>
        </div>
        <div class="mb-3">
          <label class="form-label" for="ppf-cantidad">Cantidad</label>
          <input type="number" class="form-control" id="ppf-cantidad" min="0.5" step="0.5" required
            value="${isEdit ? pp.cantidad : '1'}" placeholder="1" />
        </div>
        <p style="font-size:0.8rem;color:var(--text-muted);margin:0;">
          <i class="bi bi-info-circle"></i> El precio se calcula automáticamente según el precio de la pizza y la cantidad.
        </p>
      </form>`;

    const modal = new bootstrap.Modal(document.getElementById('formModal'));
    modal.show();

    document.getElementById('formModalSave').onclick = async () => {
      const form = document.getElementById('pp-form');
      if (!form.checkValidity()) { form.reportValidity(); return; }

      const body = {
        id: isEdit ? pp.id : 0,
        idPizza: parseInt(val('ppf-pizza')),
        idPedido: idPedido,
        cantidad: parseFloat(val('ppf-cantidad'))
      };

      try {
        if (isEdit) {
          await API.put(`/pedidos/${idPedido}/pizzas/${pp.id}`, body);
          API.showToast('Línea actualizada correctamente', 'success');
        } else {
          await API.post(`/pedidos/${idPedido}/pizzas`, body);
          API.showToast('Pizza añadida al pedido', 'success');
        }
        modal.hide();
        // Reload full pedido data to refresh totals
        expandedId = idPedido;
        await loadData();
      } catch (e) { /* toast already shown */ }
    };
  }

  // ---- Deletes ----
  function confirmDeletePedido(id) {
    const pedido = allPedidos.find(p => p.id == id);
    document.getElementById('deleteModalMsg').textContent =
      `¿Eliminar el pedido #${id} de ${pedido?.cliente || '—'}?`;

    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();

    document.getElementById('deleteModalConfirm').onclick = async () => {
      try {
        await API.del(`/pedidos/${id}`);
        API.showToast('Pedido eliminado', 'success');
        modal.hide();
        if (expandedId == id) expandedId = null;
        await loadData();
      } catch (e) { /* toast already shown */ }
    };
  }

  function confirmDeletePP(idPedido, idPP, pizzas) {
    const pp = pizzas.find(x => x.id === idPP);
    document.getElementById('deleteModalMsg').textContent =
      `¿Eliminar "${pp?.pizza || 'esta pizza'}" del pedido #${idPedido}?`;

    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();

    document.getElementById('deleteModalConfirm').onclick = async () => {
      try {
        await API.del(`/pedidos/${idPedido}/pizzas/${idPP}`);
        API.showToast('Pizza eliminada del pedido', 'success');
        modal.hide();
        expandedId = idPedido;
        await loadData();
      } catch (e) { /* toast already shown */ }
    };
  }

  // Helpers
  function esc(s) { const d = document.createElement('div'); d.textContent = s || ''; return d.innerHTML; }
  function val(id) { return document.getElementById(id).value.trim(); }

  return { render };
})();
