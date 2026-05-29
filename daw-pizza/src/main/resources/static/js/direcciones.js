/* ============================================================
   direcciones.js — Módulo CRUD de Direcciones
   ============================================================ */

const Direcciones = (() => {
  let allDirecciones = [];

  async function render(container) {
    container.innerHTML = `
      <div class="page-section">
        <div class="page-header">
          <div>
            <h2><i class="bi bi-geo-alt-fill"></i> Direcciones</h2>
            <p>Gestiona las direcciones de entrega de los clientes</p>
          </div>
          <button class="btn-accent" id="btn-new-direccion">
            <i class="bi bi-plus-lg"></i> Nueva Dirección
          </button>
        </div>

        <div class="stats-row" id="dir-stats"></div>

        <div class="glass-card">
          <div class="card-body-custom" style="padding:0;">
            <div id="dir-table-wrapper">
              <div class="spinner-wrapper"><div class="spinner-custom"></div></div>
            </div>
          </div>
        </div>
      </div>`;

    document.getElementById('btn-new-direccion').onclick = () => openForm();
    await loadData();
  }

  async function loadData() {
    try {
      allDirecciones = await API.get('/direcciones');
      renderStats();
      renderTable();
    } catch (e) {
      document.getElementById('dir-table-wrapper').innerHTML =
        '<div class="empty-state"><i class="bi bi-wifi-off d-block"></i><p>Error al cargar las direcciones</p></div>';
    }
  }

  function renderStats() {
    const total = allDirecciones.length;
    const activas = allDirecciones.filter(d => d.activa).length;

    document.getElementById('dir-stats').innerHTML = `
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--accent-subtle);color:var(--accent)"><i class="bi bi-geo-alt-fill"></i></div>
        <div class="stat-value">${total}</div>
        <div class="stat-label">Total Direcciones</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--success-bg);color:var(--success)"><i class="bi bi-check-circle-fill"></i></div>
        <div class="stat-value">${activas}</div>
        <div class="stat-label">Activas</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--danger-bg);color:var(--danger)"><i class="bi bi-x-circle-fill"></i></div>
        <div class="stat-value">${total - activas}</div>
        <div class="stat-label">Inactivas</div>
      </div>`;
  }

  function renderTable() {
    const wrapper = document.getElementById('dir-table-wrapper');
    if (allDirecciones.length === 0) {
      wrapper.innerHTML = '<div class="empty-state"><i class="bi bi-geo d-block"></i><p>No hay direcciones registradas</p></div>';
      return;
    }

    let rows = allDirecciones.map(d => {
      const clienteNombre = d.cliente ? d.cliente.nombre : `ID ${d.idCliente}`;
      return `
      <tr>
        <td class="col-name">${esc(clienteNombre)}</td>
        <td>${esc(d.calle)}</td>
        <td>${esc(d.numero)}</td>
        <td>${esc(d.poblacion)}</td>
        <td>${d.activa
          ? '<span class="badge-custom badge-success"><i class="bi bi-check2"></i> Activa</span>'
          : '<span class="badge-custom badge-danger"><i class="bi bi-x-lg"></i> Inactiva</span>'}</td>
        <td>
          <div class="action-btns">
            <button class="btn-icon btn-icon-toggle" title="Marcar/Desmarcar activa" data-id="${d.id}">
              <i class="bi bi-toggle-${d.activa ? 'on' : 'off'}"></i>
            </button>
            <button class="btn-icon btn-icon-edit" title="Editar" data-id="${d.id}"><i class="bi bi-pencil-fill"></i></button>
            <button class="btn-icon btn-icon-delete" title="Eliminar" data-id="${d.id}"><i class="bi bi-trash3-fill"></i></button>
          </div>
        </td>
      </tr>`;
    }).join('');

    wrapper.innerHTML = `
      <table class="table-custom">
        <thead><tr><th>Cliente</th><th>Calle</th><th>Número</th><th>Población</th><th>Estado</th><th>Acciones</th></tr></thead>
        <tbody>${rows}</tbody>
      </table>`;

    wrapper.querySelectorAll('.btn-icon-toggle').forEach(btn =>
      btn.onclick = () => toggleActiva(btn.dataset.id));
    wrapper.querySelectorAll('.btn-icon-edit').forEach(btn =>
      btn.onclick = () => openForm(allDirecciones.find(d => d.id == btn.dataset.id)));
    wrapper.querySelectorAll('.btn-icon-delete').forEach(btn =>
      btn.onclick = () => confirmDelete(btn.dataset.id));
  }

  async function toggleActiva(id) {
    try {
      await API.put(`/direcciones/${id}/activa`);
      API.showToast('Estado de dirección actualizado', 'success');
      await loadData();
    } catch (e) { /* toast already shown */ }
  }

  async function openForm(direccion = null) {
    const isEdit = !!direccion;

    // Load clientes for selector
    let clientes = [];
    try {
      clientes = await Clientes.loadData();
    } catch (e) { clientes = []; }

    const clienteOpts = clientes.map(c =>
      `<option value="${c.id}" ${isEdit && direccion.idCliente === c.id ? 'selected' : ''}>${esc(c.nombre)}</option>`
    ).join('');

    document.getElementById('formModalTitle').innerHTML =
      `<i class="bi bi-${isEdit ? 'pencil' : 'geo-alt'}"></i> ${isEdit ? 'Editar' : 'Nueva'} Dirección`;

    document.getElementById('formModalBody').innerHTML = `
      <form id="dir-form" novalidate>
        <div class="mb-3">
          <label class="form-label" for="df-cliente">Cliente</label>
          <select class="form-select" id="df-cliente" required>
            <option value="">Selecciona un cliente</option>
            ${clienteOpts}
          </select>
        </div>
        <div class="mb-3">
          <label class="form-label" for="df-calle">Calle</label>
          <input type="text" class="form-control" id="df-calle" maxlength="150" required
            value="${isEdit ? esc(direccion.calle) : ''}" placeholder="Nombre de la calle" />
        </div>
        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label" for="df-numero">Número</label>
            <input type="text" class="form-control" id="df-numero" maxlength="30"
              value="${isEdit ? esc(direccion.numero) : ''}" placeholder="Ej: 12B" />
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label" for="df-poblacion">Población</label>
            <input type="text" class="form-control" id="df-poblacion" maxlength="100"
              value="${isEdit ? esc(direccion.poblacion) : ''}" placeholder="Ciudad / Localidad" />
          </div>
        </div>
      </form>`;

    const modal = new bootstrap.Modal(document.getElementById('formModal'));
    modal.show();

    document.getElementById('formModalSave').onclick = async () => {
      const form = document.getElementById('dir-form');
      if (!form.checkValidity()) { form.reportValidity(); return; }

      const body = {
        id: isEdit ? direccion.id : 0,
        idCliente: parseInt(val('df-cliente')),
        calle: val('df-calle'),
        numero: val('df-numero'),
        poblacion: val('df-poblacion')
        // NOT sending 'activa' — backend forbids it on update
      };

      try {
        if (isEdit) {
          await API.put(`/direcciones/${direccion.id}`, body);
          API.showToast('Dirección actualizada correctamente', 'success');
        } else {
          await API.post('/direcciones', body);
          API.showToast('Dirección creada correctamente', 'success');
        }
        modal.hide();
        await loadData();
      } catch (e) { /* toast already shown */ }
    };
  }

  function confirmDelete(id) {
    const dir = allDirecciones.find(d => d.id == id);
    document.getElementById('deleteModalMsg').textContent =
      `¿Eliminar la dirección "${dir ? dir.calle + ' ' + dir.numero : id}"?`;

    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();

    document.getElementById('deleteModalConfirm').onclick = async () => {
      try {
        await API.del(`/direcciones/${id}`);
        API.showToast('Dirección eliminada', 'success');
        modal.hide();
        await loadData();
      } catch (e) { /* toast already shown */ }
    };
  }

  // Helpers
  function esc(s) { const d = document.createElement('div'); d.textContent = s || ''; return d.innerHTML; }
  function val(id) { return document.getElementById(id).value.trim(); }

  return { render };
})();
