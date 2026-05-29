/* ============================================================
   pizzas.js — Módulo CRUD de Pizzas
   ============================================================ */

const Pizzas = (() => {
  let allPizzas = [];

  async function render(container) {
    container.innerHTML = `
      <div class="page-section">
        <div class="page-header">
          <div>
            <h2><i class="bi bi-circle-fill" style="font-size:1rem;color:var(--accent)"></i> Pizzas</h2>
            <p>Gestiona el catálogo de pizzas de tu pizzería</p>
          </div>
          <button class="btn-accent" id="btn-new-pizza">
            <i class="bi bi-plus-lg"></i> Nueva Pizza
          </button>
        </div>

        <div class="stats-row" id="pizza-stats"></div>

        <div class="glass-card">
          <div class="card-body-custom" style="padding:0;">
            <div id="pizza-table-wrapper">
              <div class="spinner-wrapper"><div class="spinner-custom"></div></div>
            </div>
          </div>
        </div>
      </div>`;

    document.getElementById('btn-new-pizza').onclick = () => openForm();
    await loadData();
  }

  async function loadData() {
    try {
      allPizzas = await API.get('/pizzas');
      renderStats();
      renderTable();
    } catch (e) {
      document.getElementById('pizza-table-wrapper').innerHTML =
        '<div class="empty-state"><i class="bi bi-wifi-off d-block"></i><p>Error al cargar las pizzas</p></div>';
    }
  }

  function renderStats() {
    const total = allPizzas.length;
    const disponibles = allPizzas.filter(p => p.disponible).length;
    const veganas = allPizzas.filter(p => p.vegana).length;
    const vegetarianas = allPizzas.filter(p => p.vegetariana).length;

    document.getElementById('pizza-stats').innerHTML = `
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--accent-subtle);color:var(--accent)"><i class="bi bi-grid-3x3-gap-fill"></i></div>
        <div class="stat-value">${total}</div>
        <div class="stat-label">Total Pizzas</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--success-bg);color:var(--success)"><i class="bi bi-check-circle-fill"></i></div>
        <div class="stat-value">${disponibles}</div>
        <div class="stat-label">Disponibles</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--purple-bg);color:var(--purple)"><i class="bi bi-leaf"></i></div>
        <div class="stat-value">${veganas}</div>
        <div class="stat-label">Veganas</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background:var(--info-bg);color:var(--info)"><i class="bi bi-flower1"></i></div>
        <div class="stat-value">${vegetarianas}</div>
        <div class="stat-label">Vegetarianas</div>
      </div>`;
  }

  function renderTable() {
    const wrapper = document.getElementById('pizza-table-wrapper');
    if (allPizzas.length === 0) {
      wrapper.innerHTML = '<div class="empty-state"><i class="bi bi-inbox d-block"></i><p>No hay pizzas registradas</p></div>';
      return;
    }

    let rows = allPizzas.map(p => `
      <tr>
        <td class="col-name">${esc(p.nombre)}</td>
        <td>${esc(p.descripcion)}</td>
        <td class="col-price">${p.precio.toFixed(2)} €</td>
        <td>${badge(p.disponible, 'Sí', 'No', 'success', 'danger')}</td>
        <td>${badge(p.vegana, 'Sí', 'No', 'purple', 'danger')}</td>
        <td>${badge(p.vegetariana, 'Sí', 'No', 'info', 'danger')}</td>
        <td>
          <div class="action-btns">
            <button class="btn-icon btn-icon-edit" title="Editar" data-id="${p.id}"><i class="bi bi-pencil-fill"></i></button>
            <button class="btn-icon btn-icon-delete" title="Eliminar" data-id="${p.id}"><i class="bi bi-trash3-fill"></i></button>
          </div>
        </td>
      </tr>`).join('');

    wrapper.innerHTML = `
      <table class="table-custom">
        <thead>
          <tr>
            <th>Nombre</th><th>Descripción</th><th>Precio</th>
            <th>Disponible</th><th>Vegana</th><th>Vegetariana</th><th>Acciones</th>
          </tr>
        </thead>
        <tbody>${rows}</tbody>
      </table>`;

    wrapper.querySelectorAll('.btn-icon-edit').forEach(btn =>
      btn.onclick = () => openForm(allPizzas.find(p => p.id == btn.dataset.id)));
    wrapper.querySelectorAll('.btn-icon-delete').forEach(btn =>
      btn.onclick = () => confirmDelete(btn.dataset.id));
  }

  function openForm(pizza = null) {
    const isEdit = !!pizza;
    document.getElementById('formModalTitle').innerHTML =
      `<i class="bi bi-${isEdit ? 'pencil' : 'plus-circle'}"></i> ${isEdit ? 'Editar' : 'Nueva'} Pizza`;

    document.getElementById('formModalBody').innerHTML = `
      <form id="pizza-form" novalidate>
        <div class="mb-3">
          <label class="form-label" for="pf-nombre">Nombre</label>
          <input type="text" class="form-control" id="pf-nombre" maxlength="30" required
            value="${isEdit ? esc(pizza.nombre) : ''}" placeholder="Ej: Margarita" />
        </div>
        <div class="mb-3">
          <label class="form-label" for="pf-desc">Descripción</label>
          <textarea class="form-control" id="pf-desc" maxlength="150" rows="2" required
            placeholder="Ingredientes principales">${isEdit ? esc(pizza.descripcion) : ''}</textarea>
        </div>
        <div class="mb-3">
          <label class="form-label" for="pf-precio">Precio (€)</label>
          <input type="number" class="form-control" id="pf-precio" min="0" step="0.01" required
            value="${isEdit ? pizza.precio : ''}" placeholder="0.00" />
        </div>
        <div class="d-flex gap-4">
          <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" id="pf-disponible" ${isEdit && pizza.disponible ? 'checked' : (!isEdit ? 'checked' : '')}>
            <label class="form-check-label" for="pf-disponible">Disponible</label>
          </div>
          <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" id="pf-vegana" ${isEdit && pizza.vegana ? 'checked' : ''}>
            <label class="form-check-label" for="pf-vegana">Vegana</label>
          </div>
          <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" id="pf-vegetariana" ${isEdit && pizza.vegetariana ? 'checked' : ''}>
            <label class="form-check-label" for="pf-vegetariana">Vegetariana</label>
          </div>
        </div>
      </form>`;

    const modal = new bootstrap.Modal(document.getElementById('formModal'));
    modal.show();

    document.getElementById('formModalSave').onclick = async () => {
      const form = document.getElementById('pizza-form');
      if (!form.checkValidity()) { form.reportValidity(); return; }

      const body = {
        id: isEdit ? pizza.id : 0,
        nombre: val('pf-nombre'),
        descripcion: val('pf-desc'),
        precio: parseFloat(val('pf-precio')),
        disponible: document.getElementById('pf-disponible').checked,
        vegana: document.getElementById('pf-vegana').checked,
        vegetariana: document.getElementById('pf-vegetariana').checked
      };

      try {
        if (isEdit) {
          await API.put(`/pizzas/${pizza.id}`, body);
          API.showToast('Pizza actualizada correctamente', 'success');
        } else {
          await API.post('/pizzas', body);
          API.showToast('Pizza creada correctamente', 'success');
        }
        modal.hide();
        await loadData();
      } catch (e) { /* toast already shown by API */ }
    };
  }

  function confirmDelete(id) {
    const pizza = allPizzas.find(p => p.id == id);
    document.getElementById('deleteModalMsg').textContent =
      `¿Eliminar la pizza "${pizza?.nombre || id}"?`;

    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();

    document.getElementById('deleteModalConfirm').onclick = async () => {
      try {
        await API.del(`/pizzas/${id}`);
        API.showToast('Pizza eliminada', 'success');
        modal.hide();
        await loadData();
      } catch (e) { /* toast already shown */ }
    };
  }

  // Helpers
  function esc(s) { const d = document.createElement('div'); d.textContent = s || ''; return d.innerHTML; }
  function val(id) { return document.getElementById(id).value.trim(); }
  function badge(cond, yes, no, colorYes, colorNo) {
    return cond
      ? `<span class="badge-custom badge-${colorYes}"><i class="bi bi-check2"></i> ${yes}</span>`
      : `<span class="badge-custom badge-${colorNo}"><i class="bi bi-x-lg"></i> ${no}</span>`;
  }

  return { render };
})();
