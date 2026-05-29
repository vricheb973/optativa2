/* ============================================================
   app.js — Controlador principal: navegación y routing
   ============================================================ */

document.addEventListener('DOMContentLoaded', () => {
  App.init();
});

const App = (() => {
  let mainContent = null;
  let navItems = [];
  let sidebar = null;
  let overlay = null;

  function init() {
    mainContent = document.getElementById('main-content');
    navItems = document.querySelectorAll('.sidebar-nav .nav-item');
    sidebar = document.getElementById('sidebar');
    overlay = document.getElementById('sidebar-overlay');

    // Navigation item clicks
    navItems.forEach(item => {
      item.addEventListener('click', () => {
        const page = item.dataset.page;
        navigateTo(page);
        closeMobileSidebar();
      });
    });

    // Mobile sidebar toggle
    const toggleBtn = document.getElementById('sidebar-toggle');
    if (toggleBtn) {
      toggleBtn.addEventListener('click', toggleMobileSidebar);
    }
    if (overlay) {
      overlay.addEventListener('click', closeMobileSidebar);
    }

    // Load initial page (Dashboard)
    navigateTo('dashboard');
  }

  function toggleMobileSidebar() {
    sidebar.classList.toggle('show');
    overlay.classList.toggle('show');
  }

  function closeMobileSidebar() {
    sidebar.classList.remove('show');
    overlay.classList.remove('show');
  }

  async function navigateTo(pageId) {
    // Update active class in sidebar navigation
    navItems.forEach(item => {
      if (item.dataset.page === pageId) {
        item.classList.add('active');
      } else {
        item.classList.remove('active');
      }
    });

    // Clear main content
    mainContent.innerHTML = '';

    // Route rendering based on page ID
    switch (pageId) {
      case 'dashboard':
        await renderDashboard(mainContent);
        break;
      case 'pizzas':
        await Pizzas.render(mainContent);
        break;
      case 'clientes':
        await Clientes.render(mainContent);
        break;
      case 'direcciones':
        await Direcciones.render(mainContent);
        break;
      case 'pedidos':
        await Pedidos.render(mainContent);
        break;
      default:
        mainContent.innerHTML = `<div class="alert alert-danger m-4">Página no encontrada: "${pageId}"</div>`;
    }
  }

  // ---- Dashboard Render ----
  async function renderDashboard(container) {
    container.innerHTML = `
      <div class="page-section">
        <div class="welcome-section">
          <span class="welcome-icon">🍕</span>
          <h2>¡Bienvenido a DAW Pizza!</h2>
          <p>Gestiona de manera integral tu catálogo de pizzas, clientes, direcciones y pedidos desde este panel premium.</p>
        </div>

        <div class="stats-row" id="dashboard-stats">
          <div class="stat-card">
            <div class="spinner-wrapper" style="padding: 10px;"><div class="spinner-custom" style="width:24px;height:24px;"></div></div>
          </div>
          <div class="stat-card">
            <div class="spinner-wrapper" style="padding: 10px;"><div class="spinner-custom" style="width:24px;height:24px;"></div></div>
          </div>
          <div class="stat-card">
            <div class="spinner-wrapper" style="padding: 10px;"><div class="spinner-custom" style="width:24px;height:24px;"></div></div>
          </div>
          <div class="stat-card">
            <div class="spinner-wrapper" style="padding: 10px;"><div class="spinner-custom" style="width:24px;height:24px;"></div></div>
          </div>
        </div>

        <div class="row mt-4">
          <div class="col-md-6 mb-4">
            <div class="glass-card h-100">
              <div class="card-header-custom">
                <h5 class="m-0"><i class="bi bi-lightning-charge-fill text-accent"></i> Accesos Rápidos</h5>
              </div>
              <div class="card-body-custom d-flex flex-column gap-3">
                <button class="btn-accent w-100 text-start d-flex align-items-center justify-content-between" id="dash-new-pedido">
                  <span><i class="bi bi-plus-circle-fill me-2"></i> Crear Nuevo Pedido</span>
                  <i class="bi bi-chevron-right"></i>
                </button>
                <button class="btn-ghost w-100 text-start d-flex align-items-center justify-content-between" id="dash-new-pizza">
                  <span><i class="bi bi-circle-fill me-2" style="font-size:0.6rem;color:var(--accent)"></i> Añadir Nueva Pizza</span>
                  <i class="bi bi-chevron-right"></i>
                </button>
                <button class="btn-ghost w-100 text-start d-flex align-items-center justify-content-between" id="dash-new-cliente">
                  <span><i class="bi bi-person-plus-fill me-2"></i> Registrar Cliente</span>
                  <i class="bi bi-chevron-right"></i>
                </button>
                <button class="btn-ghost w-100 text-start d-flex align-items-center justify-content-between" id="dash-new-direccion">
                  <span><i class="bi bi-geo-alt-fill me-2"></i> Añadir Dirección</span>
                  <i class="bi bi-chevron-right"></i>
                </button>
              </div>
            </div>
          </div>

          <div class="col-md-6 mb-4">
            <div class="glass-card h-100">
              <div class="card-header-custom">
                <h5 class="m-0"><i class="bi bi-info-circle-fill text-accent"></i> Resumen del Sistema</h5>
              </div>
              <div class="card-body-custom">
                <p class="text-secondary" style="font-size:0.9rem;">
                  Este sistema está conectado a un backend robusto desarrollado con <strong>Spring Boot 3</strong>.
                  La arquitectura expone una API REST moderna con control de excepciones y validaciones avanzadas en base de datos.
                </p>
                <hr style="border-color:var(--border-color);">
                <div class="d-flex flex-column gap-2" style="font-size:0.85rem;color:var(--text-secondary);">
                  <div class="d-flex justify-content-between">
                    <span>Estado del Servidor</span>
                    <span class="badge-custom badge-success"><i class="bi bi-check2"></i> En Línea</span>
                  </div>
                  <div class="d-flex justify-content-between">
                    <span>Tecnología Front</span>
                    <span>HTML5, Vanilla JS, Bootstrap 5.3</span>
                  </div>
                  <div class="d-flex justify-content-between">
                    <span>Estilos</span>
                    <span>Tema Oscuro Premium (Glassmorphic)</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>`;

    // Fetch stats in background
    loadDashboardStats();

    // Bind Quick Actions
    document.getElementById('dash-new-pedido').onclick = () => {
      navigateTo('pedidos').then(() => {
        const btn = document.getElementById('btn-new-pedido');
        if (btn) btn.click();
      });
    };
    document.getElementById('dash-new-pizza').onclick = () => {
      navigateTo('pizzas').then(() => {
        const btn = document.getElementById('btn-new-pizza');
        if (btn) btn.click();
      });
    };
    document.getElementById('dash-new-cliente').onclick = () => {
      navigateTo('clientes').then(() => {
        const btn = document.getElementById('btn-new-cliente');
        if (btn) btn.click();
      });
    };
    document.getElementById('dash-new-direccion').onclick = () => {
      navigateTo('direcciones').then(() => {
        const btn = document.getElementById('btn-new-direccion');
        if (btn) btn.click();
      });
    };
  }

  async function loadDashboardStats() {
    const statsContainer = document.getElementById('dashboard-stats');
    if (!statsContainer) return;

    try {
      // Load all lists in parallel
      const [pizzas, clientes, direcciones, pedidos] = await Promise.all([
        API.get('/pizzas').catch(() => []),
        API.get('/clientes').catch(() => []),
        API.get('/direcciones').catch(() => []),
        API.get('/pedidos').catch(() => [])
      ]);

      const totalFacturacion = pedidos.reduce((sum, p) => sum + (p.total || 0), 0);

      statsContainer.innerHTML = `
        <div class="stat-card" style="cursor:pointer;" onclick="App.navigateToPage('pizzas')">
          <div class="stat-icon" style="background:var(--accent-subtle);color:var(--accent)">
            <i class="bi bi-circle-fill" style="font-size:0.8rem;"></i>
          </div>
          <div class="stat-value">${pizzas.length}</div>
          <div class="stat-label">Pizzas en Carta</div>
        </div>
        <div class="stat-card" style="cursor:pointer;" onclick="App.navigateToPage('clientes')">
          <div class="stat-icon" style="background:var(--info-bg);color:var(--info)">
            <i class="bi bi-people-fill"></i>
          </div>
          <div class="stat-value">${clientes.length}</div>
          <div class="stat-label">Clientes Activos</div>
        </div>
        <div class="stat-card" style="cursor:pointer;" onclick="App.navigateToPage('pedidos')">
          <div class="stat-icon" style="background:var(--success-bg);color:var(--success)">
            <i class="bi bi-currency-euro"></i>
          </div>
          <div class="stat-value">${totalFacturacion.toFixed(2)} €</div>
          <div class="stat-label">Ventas Totales</div>
        </div>
        <div class="stat-card" style="cursor:pointer;" onclick="App.navigateToPage('pedidos')">
          <div class="stat-icon" style="background:var(--purple-bg);color:var(--purple)">
            <i class="bi bi-receipt"></i>
          </div>
          <div class="stat-value">${pedidos.length}</div>
          <div class="stat-label">Pedidos Registrados</div>
        </div>`;
    } catch (e) {
      statsContainer.innerHTML = `
        <div class="alert alert-danger w-100 text-center m-0" style="background:var(--danger-bg);color:var(--danger);border-color:var(--border-color);">
          <i class="bi bi-exclamation-triangle-fill me-2"></i> Error al cargar estadísticas del sistema.
        </div>`;
    }
  }

  return {
    init,
    navigateToPage: navigateTo
  };
})();
