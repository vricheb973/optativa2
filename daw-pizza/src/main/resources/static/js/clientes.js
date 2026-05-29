/* ============================================================
   clientes.js — Módulo CRUD de Clientes
   ============================================================ */

const Clientes = (() => {
  let allClientes = [];

  async function render(container) {
    container.innerHTML = `
      <div class="page-section">
        <div class="page-header">
          <div>
            <h2><i class="bi bi-people-fill"></i> Clientes</h2>
            <p>Gestiona la base de datos de clientes</p>
          </div>
          <button class="btn-accent" id="btn-new-cliente">
            <i class="bi bi-person-plus-fill"></i> Nuevo Cliente
          </button>
        </div>

        <div class="stats-row" id="cliente-stats"></div>

        <div class="glass-card">
          <div class="card-body-custom" style="padding:0;">
            <div id="cliente-table-wrapper">
              <div class="spinner-wrapper"><div class="spinner-custom"></div></div>
            </div>
          </div>
        </div>
      </div>`;

    document.getElementById('btn-new-cliente').onclick = () => openForm();
    await loadData();
  }

  async function loadData() {
    try {
      allClientes = await API.get('/clientes');
      renderStats();
      renderTable();
    } catch (e) {
      document.getElementById('cliente-table-wrapper').innerHTML =
        '<div class="empty-state"><i class="bi bi-wifi-off d-block"></i><p>Error al cargar los clientes</p></div>';
    }
  }

  function renderStats() {
    document.getElementById('cliente-stats').innerHTML = `
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--accent-subtle);color:var(--accent)"><i class="bi bi-people-fill"></i></div>
        <div class="stat-value">${allClientes.length}</div>
        <div class="stat-label">Total Clientes</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--info-bg);color:var(--info)"><i class="bi bi-envelope-fill"></i></div>
        <div class="stat-value">${allClientes.filter(c => c.email).length}</div>
        <div class="stat-label">Con Email</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--success-bg);color:var(--success)"><i class="bi bi-telephone-fill"></i></div>
        <div class="stat-value">${allClientes.filter(c => c.telefono).length}</div>
        <div class="stat-label">Con Teléfono</div>
      </div>`;
  }

  function renderTable() {
    const wrapper = document.getElementById('cliente-table-wrapper');
    if (allClientes.length === 0) {
      wrapper.innerHTML = '<div class="empty-state"><i class="bi bi-person-x d-block"></i><p>No hay clientes registrados</p></div>';
      return;
    }

    let rows = allClientes.map(c => `
      <tr>
        <td class="col-name">${esc(c.nombre)}</td>
        <td>${esc(c.email || '—')}</td>
        <td>${esc(c.telefono || '—')}</td>
        <td>${esc(c.direccion || '—')}</td>
        <td>
          <div class="action-btns">
            <button class="btn-icon btn-icon-view" title="Ver detalle" data-id="${c.id}"><i class="bi bi-eye-fill"></i></button>
            <button class="btn-icon btn-icon-edit" title="Editar" data-id="${c.id}"><i class="bi bi-pencil-fill"></i></button>
            <button class="btn-icon btn-icon-delete" title="Eliminar" data-id="${c.id}"><i class="bi bi-trash3-fill"></i></button>
          </div>
        </td>
      </tr>`).join('');

    wrapper.innerHTML = `
      <table class="table-custom">
        <thead><tr><th>Nombre</th><th>Email</th><th>Teléfono</th><th>Dirección</th><th>Acciones</th></tr></thead>
        <tbody>${rows}</tbody>
      </table>`;

    wrapper.querySelectorAll('.btn-icon-view').forEach(btn =>
      btn.onclick = () => viewDetail(btn.dataset.id));
    wrapper.querySelectorAll('.btn-icon-edit').forEach(btn =>
      btn.onclick = () => openForm(allClientes.find(c => c.id == btn.dataset.id)));
    wrapper.querySelectorAll('.btn-icon-delete').forEach(btn =>
      btn.onclick = () => confirmDelete(btn.dataset.id));
  }

  async function viewDetail(id) {
    try {
      const dto = await API.get(`/clientes/${id}`);
      const dirHtml = dto.direccion
        ? `<p style="margin:0;color:var(--text-secondary);font-size:0.9rem;">
             <i class="bi bi-geo-alt-fill text-accent"></i>
             ${esc(dto.direccion.calle)} ${esc(dto.direccion.numero)}, ${esc(dto.direccion.poblacion)}
             ${dto.direccion.activa ? '<span class="badge-custom badge-success ms-2">Activa</span>' : ''}
           </p>`
        : '<p style="color:var(--text-muted);font-size:0.9rem;">Sin dirección activa</p>';

      document.getElementById('formModalTitle').innerHTML =
        `<i class="bi bi-person-circle"></i> ${esc(dto.nombre)}`;
      document.getElementById('formModalBody').innerHTML = `
        <div class="mb-3">
          <label class="form-label">Email</label>
          <p style="color:var(--text-primary);margin:0;">${esc(dto.email || '—')}</p>
        </div>
        <div class="mb-3">
          <label class="form-label">Teléfono</label>
          <p style="color:var(--text-primary);margin:0;">${esc(dto.telefono || '—')}</p>
        </div>
        <div>
          <label class="form-label">Dirección Activa</label>
          ${dirHtml}
        </div>`;

      // Hide save button for view mode
      document.getElementById('formModalSave').style.display = 'none';
      const modal = new bootstrap.Modal(document.getElementById('formModal'));
      modal.show();
      document.getElementById('formModal').addEventListener('hidden.bs.modal', () => {
        document.getElementById('formModalSave').style.display = '';
      }, { once: true });
    } catch (e) { /* toast already shown */ }
  }

  function openForm(cliente = null) {
    const isEdit = !!cliente;
    document.getElementById('formModalTitle').innerHTML =
      `<i class="bi bi-${isEdit ? 'pencil' : 'person-plus'}"></i> ${isEdit ? 'Editar' : 'Nuevo'} Cliente`;

    document.getElementById('formModalBody').innerHTML = `
      <form id="cliente-form" novalidate>
        <div class="mb-3">
          <label class="form-label" for="cf-nombre">Nombre</label>
          <input type="text" class="form-control" id="cf-nombre" maxlength="30" required
            value="${isEdit ? esc(cliente.nombre) : ''}" placeholder="Nombre completo" />
        </div>
        <div class="mb-3">
          <label class="form-label" for="cf-direccion">Dirección</label>
          <input type="text" class="form-control" id="cf-direccion" maxlength="100"
            value="${isEdit ? esc(cliente.direccion || '') : ''}" placeholder="Calle y número" />
        </div>
        <div class="mb-3">
          <label class="form-label" for="cf-email">Email</label>
          <input type="email" class="form-control" id="cf-email" maxlength="50"
            value="${isEdit ? esc(cliente.email || '') : ''}" placeholder="email@ejemplo.com" />
        </div>
        <div class="mb-3">
          <label class="form-label" for="cf-telefono">Teléfono</label>
          <input type="text" class="form-control" id="cf-telefono" maxlength="20"
            value="${isEdit ? esc(cliente.telefono || '') : ''}" placeholder="600 123 456" />
        </div>
      </form>`;

    const modal = new bootstrap.Modal(document.getElementById('formModal'));
    modal.show();

    document.getElementById('formModalSave').onclick = async () => {
      const form = document.getElementById('cliente-form');
      if (!form.checkValidity()) { form.reportValidity(); return; }

      const body = {
        id: isEdit ? cliente.id : 0,
        nombre: val('cf-nombre'),
        direccion: val('cf-direccion'),
        email: val('cf-email'),
        telefono: val('cf-telefono')
      };

      try {
        if (isEdit) {
          await API.put(`/clientes/${cliente.id}`, body);
          API.showToast('Cliente actualizado correctamente', 'success');
        } else {
          await API.post('/clientes', body);
          API.showToast('Cliente creado correctamente', 'success');
        }
        modal.hide();
        await loadData();
      } catch (e) { /* toast already shown */ }
    };
  }

  function confirmDelete(id) {
    const cliente = allClientes.find(c => c.id == id);
    document.getElementById('deleteModalMsg').textContent =
      `¿Eliminar al cliente "${cliente?.nombre || id}"?`;

    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();

    document.getElementById('deleteModalConfirm').onclick = async () => {
      try {
        await API.del(`/clientes/${id}`);
        API.showToast('Cliente eliminado', 'success');
        modal.hide();
        await loadData();
      } catch (e) { /* toast already shown */ }
    };
  }

  // Helpers
  function esc(s) { const d = document.createElement('div'); d.textContent = s || ''; return d.innerHTML; }
  function val(id) { return document.getElementById(id).value.trim(); }

  // Public: also expose loadData for selectors in other modules
  function getAll() { return allClientes; }

  return { render, getAll, loadData: async () => { allClientes = await API.get('/clientes'); return allClientes; } };
})();
