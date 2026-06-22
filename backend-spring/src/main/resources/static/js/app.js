// App State
let state = {
    token: sessionStorage.getItem('token') || null,
    user: JSON.parse(sessionStorage.getItem('user')) || null,
    menu: [],
    cart: [],
    orders: [],
    charts: {
        dishes: null,
        hours: null
    }
};

// API Base URL
const API_BASE = window.location.origin;

// DOM Elements
const elements = {
    // Screens
    screenAuth: document.getElementById('screen-auth'),
    screenStudent: document.getElementById('screen-student'),
    screenChef: document.getElementById('screen-chef'),
    
    // Auth
    tabLoginTrigger: document.getElementById('tab-login-trigger'),
    tabRegisterTrigger: document.getElementById('tab-register-trigger'),
    formLoginContainer: document.getElementById('form-login-container'),
    formRegisterContainer: document.getElementById('form-register-container'),
    formLogin: document.getElementById('form-login'),
    formRegister: document.getElementById('form-register'),
    userNav: document.getElementById('user-nav'),
    userDisplayName: document.getElementById('user-display-name'),
    userDisplayRole: document.getElementById('user-display-role'),
    btnLogout: document.getElementById('btn-logout'),
    
    // Student Dashboard
    menuItemsContainer: document.getElementById('menu-items-container'),
    cartItemsContainer: document.getElementById('cart-items-container'),
    cartSummaryBox: document.getElementById('cart-summary-box'),
    cartSubtotal: document.getElementById('cart-subtotal'),
    cartTotal: document.getElementById('cart-total'),
    btnCheckout: document.getElementById('btn-checkout'),
    studentOrdersHistory: document.getElementById('student-orders-history'),
    
    // Chef Dashboard Tabs
    btnChefOrders: document.getElementById('btn-chef-orders'),
    btnChefProducts: document.getElementById('btn-chef-products'),
    btnChefAnalytics: document.getElementById('btn-chef-analytics'),
    
    // Chef Panels
    chefOrdersPanel: document.getElementById('chef-orders-panel'),
    chefProductsPanel: document.getElementById('chef-products-panel'),
    chefAnalyticsPanel: document.getElementById('chef-analytics-panel'),
    
    // Chef Comandas
    btnRefreshOrders: document.getElementById('btn-refresh-orders'),
    countPendiente: document.getElementById('count-pendiente'),
    countPreparando: document.getElementById('count-preparando'),
    countListo: document.getElementById('count-listo'),
    listPendiente: document.getElementById('list-pendiente'),
    listPreparando: document.getElementById('list-preparando'),
    listListo: document.getElementById('list-listo'),
    
    // Chef Products
    chefProductsTableBody: document.getElementById('chef-products-table-body'),
    formAddProduct: document.getElementById('form-add-product'),
    
    // Chef Analytics
    btnRefreshAnalytics: document.getElementById('btn-refresh-analytics'),
    kpiEarnings: document.getElementById('kpi-earnings'),
    kpiOrdersCount: document.getElementById('kpi-orders-count'),
    kpiStarDish: document.getElementById('kpi-star-dish'),
    kpiPeakHour: document.getElementById('kpi-peak-hour'),
    
    // Toast Notification
    toastNotification: document.getElementById('toast-notification'),
    toastIcon: document.getElementById('toast-icon'),
    toastMessage: document.getElementById('toast-message'),

    // Simulated Payment Modal
    paymentModal: document.getElementById('payment-modal'),
    btnClosePayment: document.getElementById('btn-close-payment'),
    paymentModalTotal: document.getElementById('payment-modal-total'),
    payTabCard: document.getElementById('pay-tab-card'),
    payTabQr: document.getElementById('pay-tab-qr'),
    payTabCash: document.getElementById('pay-tab-cash'),
    payFormCard: document.getElementById('pay-form-card'),
    payFormQr: document.getElementById('pay-form-qr'),
    payFormCash: document.getElementById('pay-form-cash'),
    btnCancelPay: document.getElementById('btn-cancel-pay'),
    btnSubmitPay: document.getElementById('btn-submit-pay'),
    modalStatusOverlay: document.getElementById('modal-status-overlay'),
    statusContentLoading: document.getElementById('status-content-loading'),
    statusContentSuccess: document.getElementById('status-content-success')
};

// Init Application
document.addEventListener('DOMContentLoaded', () => {
    setupEventListeners();
    checkAuthSession();
});

// Toast System
function showToast(message, type = 'success') {
    elements.toastMessage.textContent = message;
    
    // Set icon & border color based on type
    elements.toastNotification.className = 'toast'; // reset classes
    elements.toastIcon.className = 'fa-solid';
    
    if (type === 'success') {
        elements.toastNotification.style.borderColor = 'var(--success)';
        elements.toastIcon.classList.add('fa-circle-check');
    } else if (type === 'error') {
        elements.toastNotification.style.borderColor = 'var(--danger)';
        elements.toastIcon.classList.add('fa-circle-xmark');
    } else {
        elements.toastNotification.style.borderColor = 'var(--info)';
        elements.toastIcon.classList.add('fa-circle-info');
    }
    
    elements.toastNotification.classList.remove('hidden');
    
    setTimeout(() => {
        elements.toastNotification.classList.add('hidden');
    }, 4000);
}

// Event Listeners Configuration
function setupEventListeners() {
    // Auth Tab toggles
    elements.tabLoginTrigger.addEventListener('click', () => {
        elements.tabLoginTrigger.classList.add('active');
        elements.tabRegisterTrigger.classList.remove('active');
        elements.formLoginContainer.classList.add('active');
        elements.formRegisterContainer.classList.remove('active');
    });

    elements.tabRegisterTrigger.addEventListener('click', () => {
        elements.tabRegisterTrigger.classList.add('active');
        elements.tabLoginTrigger.classList.remove('active');
        elements.formRegisterContainer.classList.add('active');
        elements.formLoginContainer.classList.remove('active');
    });

    // Form Submissions
    elements.formLogin.addEventListener('submit', handleLogin);
    elements.formRegister.addEventListener('submit', handleRegister);
    elements.btnLogout.addEventListener('click', logout);
    
    // Student Actions
    elements.btnCheckout.addEventListener('click', openPaymentModal);
    
    // Simulated Payment Modal Tab Switching
    elements.payTabCard.addEventListener('click', () => switchPaymentTab('card'));
    elements.payTabQr.addEventListener('click', () => switchPaymentTab('qr'));
    elements.payTabCash.addEventListener('click', () => switchPaymentTab('cash'));
    
    // Simulated Payment Modal Close/Cancel
    elements.btnClosePayment.addEventListener('click', () => elements.paymentModal.classList.add('hidden'));
    elements.btnCancelPay.addEventListener('click', () => elements.paymentModal.classList.add('hidden'));
    
    // Simulated Payment Submit
    elements.btnSubmitPay.addEventListener('click', handlePaymentSubmit);
    
    // Chef Tab Switching
    elements.btnChefOrders.addEventListener('click', () => switchChefPanel('orders'));
    elements.btnChefProducts.addEventListener('click', () => switchChefPanel('products'));
    elements.btnChefAnalytics.addEventListener('click', () => switchChefPanel('analytics'));
    
    // Chef Actions
    elements.btnRefreshOrders.addEventListener('click', loadChefOrders);
    elements.formAddProduct.addEventListener('submit', handleAddProduct);
    elements.btnRefreshAnalytics.addEventListener('click', loadChefAnalytics);
}

// Check if user is logged in
function checkAuthSession() {
    if (state.token && state.user) {
        showAuthenticatedUser();
    } else {
        showScreen('auth');
    }
}

// Switch Screens
function showScreen(screenName) {
    // Hide all screens
    elements.screenAuth.classList.remove('active');
    elements.screenStudent.classList.remove('active');
    elements.screenChef.classList.remove('active');
    
    if (screenName === 'auth') {
        elements.screenAuth.classList.add('active');
        elements.userNav.classList.add('hidden');
    } else if (screenName === 'student') {
        elements.screenStudent.classList.add('active');
        elements.userNav.classList.remove('hidden');
        loadStudentMenu();
        loadStudentOrders();
    } else if (screenName === 'chef') {
        elements.screenChef.classList.add('active');
        elements.userNav.classList.remove('hidden');
        switchChefPanel('orders'); // default tab
    }
}

// Show user name in header
function showAuthenticatedUser() {
    elements.userDisplayName.innerHTML = `<i class="fa-solid fa-user"></i> ${state.user.nombre}`;
    elements.userDisplayRole.textContent = state.user.rol === 'COCINERO' ? 'Cocinero' : 'Estudiante';
    
    if (state.user.rol === 'COCINERO') {
        showScreen('chef');
    } else {
        showScreen('student');
    }
}

// Fetch helper with auth header
async function apiRequest(endpoint, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };
    
    if (state.token) {
        headers['Authorization'] = `Bearer ${state.token}`;
    }
    
    const config = {
        ...options,
        headers
    };
    
    const response = await fetch(`${API_BASE}${endpoint}`, config);
    
    if (response.status === 401 || response.status === 403) {
        // Token expirado o invalido, cerrar sesion
        logout();
        throw new Error('Sesión expirada. Por favor inicie sesión nuevamente.');
    }
    
    return response;
}

// Auth Handlers
async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    
    try {
        const response = await apiRequest('/api/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Error en las credenciales.');
        }
        
        const data = await response.json();
        
        state.token = data.token;
        state.user = {
            id: data.id,
            nombre: data.nombre,
            email: data.email,
            rol: data.rol
        };
        
        sessionStorage.setItem('token', state.token);
        sessionStorage.setItem('user', JSON.stringify(state.user));
        
        showToast(`¡Bienvenido de nuevo, ${state.user.nombre}!`);
        showAuthenticatedUser();
        
        elements.formLogin.reset();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const nombre = document.getElementById('reg-nombre').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    const rol = document.querySelector('input[name="reg-rol"]:checked').value;
    
    try {
        const response = await apiRequest('/api/auth/register', {
            method: 'POST',
            body: JSON.stringify({ nombre, email, password, rol })
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Error al registrar usuario.');
        }
        
        showToast('Registro exitoso. Ahora puedes iniciar sesión.');
        
        // Switch tab to login
        elements.tabLoginTrigger.click();
        document.getElementById('login-email').value = email;
        elements.formRegister.reset();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

function logout() {
    state.token = null;
    state.user = null;
    state.cart = [];
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('user');
    showToast('Sesión cerrada correctamente.', 'info');
    showScreen('auth');
}

/* ==========================================================================
   STUDENT CONTROLLERS
   ========================================================================== */

// Helper to match icons to food names
function getFoodIcon(name) {
    name = name.toLowerCase();
    if (name.includes('hamburguesa') || name.includes('burger')) return '🍔';
    if (name.includes('papa') || name.includes('frita')) return '🍟';
    if (name.includes('cafe') || name.includes('expreso') || name.includes('bebida')) return '☕';
    if (name.includes('gaseosa') || name.includes('refresco') || name.includes('cola')) return '🥤';
    if (name.includes('sandwich') || name.includes('emparedado') || name.includes('pan')) return '🥪';
    if (name.includes('empanada')) return '🥟';
    if (name.includes('pizza')) return '🍕';
    if (name.includes('donas') || name.includes('pastel')) return '🍩';
    return '🍽️';
}

// Load products for the student
async function loadStudentMenu() {
    try {
        const response = await apiRequest('/api/productos');
        if (!response.ok) throw new Error('No se pudo cargar el menú.');
        
        state.menu = await response.json();
        renderStudentMenu();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Render menu items
function renderStudentMenu() {
    if (!state.menu.length) {
        elements.menuItemsContainer.innerHTML = '<div class="loader-placeholder">No hay productos disponibles hoy.</div>';
        return;
    }
    
    elements.menuItemsContainer.innerHTML = state.menu.map(p => {
        const dispoClass = p.disponibilidad ? '' : 'dispo-false';
        const isDispo = p.disponibilidad;
        const imgContent = p.imagenUrl ? `<img src="${p.imagenUrl}" alt="${p.nombre}">` : `<span style="font-size: 48px;">${getFoodIcon(p.nombre)}</span>`;
        return `
            <div class="menu-card ${dispoClass}">
                <div class="menu-card-image">
                    ${imgContent}
                    ${!isDispo ? '<span class="food-tag bg-danger">Agotado</span>' : ''}
                </div>
                <div class="menu-card-content">
                    <h4>${p.nombre}</h4>
                    <div class="menu-card-price">$${p.precio.toFixed(2)}</div>
                    
                    ${isDispo ? `
                        <div class="menu-card-actions">
                            <input type="number" class="qty-input" id="qty-${p.id}" value="1" min="1" max="10">
                            <button class="btn btn-primary btn-sm btn-block" onclick="addToCart(${p.id})">
                                <i class="fa-solid fa-cart-plus"></i> Pedir
                            </button>
                        </div>
                    ` : `
                        <button class="btn btn-outline btn-sm btn-block" disabled>No disponible</button>
                    `}
                </div>
            </div>
        `;
    }).join('');
}

// Cart Management
window.addToCart = function(productId) {
    const qtyInput = document.getElementById(`qty-${productId}`);
    const cantidad = parseInt(qtyInput.value) || 1;
    const producto = state.menu.find(p => p.id === productId);
    
    if (!producto) return;
    
    const existing = state.cart.find(item => item.producto.id === productId);
    if (existing) {
        existing.cantidad += cantidad;
    } else {
        state.cart.push({ producto, cantidad });
    }
    
    showToast(`Agregado: ${cantidad}x ${producto.nombre} al carrito.`);
    renderCart();
    qtyInput.value = 1; // reset input
}

function renderCart() {
    if (!state.cart.length) {
        elements.cartItemsContainer.innerHTML = `
            <div class="empty-cart-message">
                <i class="fa-solid fa-pizza-slice"></i>
                <p>Tu carrito está vacío. ¡Elige algo rico de la lista!</p>
            </div>
        `;
        elements.cartSummaryBox.classList.add('hidden');
        return;
    }
    
    elements.cartItemsContainer.innerHTML = state.cart.map((item, idx) => `
        <div class="cart-item">
            <div class="cart-item-info">
                <h5>${item.producto.nombre}</h5>
                <span class="text-muted">${item.cantidad}x $${item.producto.precio.toFixed(2)}</span>
            </div>
            <div class="cart-item-actions">
                <strong>$${(item.producto.precio * item.cantidad).toFixed(2)}</strong>
                <button class="btn-remove-cart" onclick="removeFromCart(${idx})">
                    <i class="fa-solid fa-trash-can"></i>
                </button>
            </div>
        </div>
    `).join('');
    
    // Calculate total
    const total = state.cart.reduce((sum, item) => sum + (item.producto.precio * item.cantidad), 0);
    elements.cartSubtotal.textContent = `$${total.toFixed(2)}`;
    elements.cartTotal.textContent = `$${total.toFixed(2)}`;
    elements.cartSummaryBox.classList.remove('hidden');
}

window.removeFromCart = function(index) {
    const item = state.cart[index];
    state.cart.splice(index, 1);
    showToast(`Removido: ${item.producto.nombre} del carrito.`, 'info');
    renderCart();
}

// Open Simulated Payment Modal
function openPaymentModal() {
    if (!state.cart.length) return;
    const total = state.cart.reduce((sum, item) => sum + (item.producto.precio * item.cantidad), 0);
    elements.paymentModalTotal.textContent = `$${total.toFixed(2)}`;
    
    // Reset modal status
    elements.modalStatusOverlay.classList.add('hidden');
    elements.statusContentLoading.classList.remove('hidden');
    elements.statusContentSuccess.classList.add('hidden');
    
    // Switch to default payment form (card)
    switchPaymentTab('card');
    
    elements.paymentModal.classList.remove('hidden');
}

// Switch payment tabs in simulated checkout
function switchPaymentTab(tabName) {
    elements.payTabCard.classList.remove('active');
    elements.payTabQr.classList.remove('active');
    elements.payTabCash.classList.remove('active');
    
    elements.payFormCard.classList.remove('active');
    elements.payFormQr.classList.remove('active');
    elements.payFormCash.classList.remove('active');
    
    if (tabName === 'card') {
        elements.payTabCard.classList.add('active');
        elements.payFormCard.classList.add('active');
    } else if (tabName === 'qr') {
        elements.payTabQr.classList.add('active');
        elements.payFormQr.classList.add('active');
    } else if (tabName === 'cash') {
        elements.payTabCash.classList.add('active');
        elements.payFormCash.classList.add('active');
    }
}

// Handle simulated bank authorization response
async function handlePaymentSubmit() {
    elements.btnSubmitPay.disabled = true;
    
    // Show inner status loader
    elements.modalStatusOverlay.classList.remove('hidden');
    elements.statusContentLoading.classList.remove('hidden');
    elements.statusContentSuccess.classList.add('hidden');
    
    try {
        // Wait 2 seconds simulating card/QR payment check
        await new Promise(resolve => setTimeout(resolve, 2000));
        
        // Show success icon for 1.2 seconds
        elements.statusContentLoading.classList.add('hidden');
        elements.statusContentSuccess.classList.remove('hidden');
        
        await new Promise(resolve => setTimeout(resolve, 1200));
        
        // Hide the payment modal overlay
        elements.paymentModal.classList.add('hidden');
        
        // Complete the actual order submission
        await processCheckout();
    } catch (e) {
        showToast('Error en la simulación de pago', 'error');
        elements.modalStatusOverlay.classList.add('hidden');
    } finally {
        elements.btnSubmitPay.disabled = false;
    }
}

// Checkout
async function processCheckout() {
    if (!state.cart.length) return;
    
    // Mapear el carrito a lo que espera la API: { items: [ { productoId, cantidad } ] }
    const items = state.cart.map(item => ({
        productoId: item.producto.id,
        cantidad: item.cantidad
    }));
    
    try {
        elements.btnCheckout.disabled = true;
        elements.btnCheckout.textContent = 'Enviando...';
        
        const response = await apiRequest('/api/pedidos', {
            method: 'POST',
            body: JSON.stringify({ items })
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Error al procesar el pedido.');
        }
        
        showToast('¡Tu pedido ha sido enviado a la cocina con éxito!');
        state.cart = [];
        renderCart();
        loadStudentOrders();
    } catch (err) {
        showToast(err.message, 'error');
    } finally {
        elements.btnCheckout.disabled = false;
        elements.btnCheckout.innerHTML = '<i class="fa-solid fa-receipt"></i> Confirmar Pedido';
    }
}

// Load student history and status
async function loadStudentOrders() {
    try {
        const response = await apiRequest('/api/pedidos/usuario/me');
        if (!response.ok) throw new Error('Error al cargar historial.');
        
        state.orders = await response.json();
        renderStudentOrders();
    } catch (err) {
        // Evitamos alertar al usuario si falla por primera vez o reconecta
    }
}

function getStatusBadgeClass(status) {
    if (status === 'PENDIENTE') return 'badge-danger';
    if (status === 'PREPARANDO') return 'badge-warning';
    return 'badge-success';
}

function getStatusLabel(status) {
    if (status === 'PENDIENTE') return 'En cola';
    if (status === 'PREPARANDO') return 'Preparándose';
    return '¡Listo para recoger!';
}

function formatShortDate(dateStr) {
    try {
        const date = new Date(dateStr);
        return date.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' }) + ' - ' + date.toLocaleDateString('es-ES', { day: '2-digit', month: 'short' });
    } catch (e) {
        return dateStr;
    }
}

function renderStudentOrders() {
    if (!state.orders.length) {
        elements.studentOrdersHistory.innerHTML = '<div class="empty-history">No has realizado pedidos aún.</div>';
        return;
    }
    
    elements.studentOrdersHistory.innerHTML = state.orders.map(order => {
        const statusClass = getStatusBadgeClass(order.estado);
        const statusLabel = getStatusLabel(order.estado);
        const dateFormatted = formatShortDate(order.fechaCreacion);
        
        const detailsList = order.detalles.map(d => `${d.cantidad}x ${d.producto.nombre}`).join(', ');
        
        return `
            <div class="history-item">
                <div class="history-item-header">
                    <span class="history-item-id">Orden #${order.id}</span>
                    <span class="badge ${statusClass}">${statusLabel}</span>
                </div>
                <div class="history-item-details">${detailsList}</div>
                <div class="history-item-total">
                    <span class="history-item-date">${dateFormatted}</span>
                    <span>$${order.total.toFixed(2)}</span>
                </div>
            </div>
        `;
    }).join('');
}


/* ==========================================================================
   CHEF CONTROLLERS
   ========================================================================== */

function switchChefPanel(panelName) {
    elements.btnChefOrders.classList.remove('active');
    elements.btnChefProducts.classList.remove('active');
    elements.btnChefAnalytics.classList.remove('active');
    
    elements.chefOrdersPanel.classList.remove('active');
    elements.chefProductsPanel.classList.remove('active');
    elements.chefAnalyticsPanel.classList.remove('active');
    
    if (panelName === 'orders') {
        elements.btnChefOrders.classList.add('active');
        elements.chefOrdersPanel.classList.add('active');
        loadChefOrders();
    } else if (panelName === 'products') {
        elements.btnChefProducts.classList.add('active');
        elements.chefProductsPanel.classList.add('active');
        loadChefProducts();
    } else if (panelName === 'analytics') {
        elements.btnChefAnalytics.classList.add('active');
        elements.chefAnalyticsPanel.classList.add('active');
        loadChefAnalytics();
    }
}

// Chef - Load Active orders
async function loadChefOrders() {
    try {
        const response = await apiRequest('/api/pedidos');
        if (!response.ok) throw new Error('No se pudieron obtener los pedidos.');
        
        const orders = await response.json();
        
        // Group by status
        const pendientes = orders.filter(o => o.estado === 'PENDIENTE');
        const preparando = orders.filter(o => o.estado === 'PREPARANDO');
        const listos = orders.filter(o => o.estado === 'LISTO');
        
        elements.countPendiente.textContent = pendientes.length;
        elements.countPreparando.textContent = preparando.length;
        elements.countListo.textContent = listos.length;
        
        renderChefColumn(elements.listPendiente, pendientes, 'PREPARANDO', 'Empezar a Cocinar', 'btn-secondary');
        renderChefColumn(elements.listPreparando, preparando, 'LISTO', 'Marcar Listo', 'btn-primary');
        renderChefColumn(elements.listListo, listos, null, null, null);
        
    } catch (err) {
        showToast(err.message, 'error');
    }
}

function renderChefColumn(container, list, nextStatus, nextBtnLabel, btnClass) {
    if (!list.length) {
        container.innerHTML = '<div class="empty-history" style="padding:40px 0;">Vacío</div>';
        return;
    }
    
    container.innerHTML = list.map(order => {
        const dateFormatted = formatShortDate(order.fechaCreacion);
        const itemRows = order.detalles.map(d => `<li><strong>${d.cantidad}x</strong> ${d.producto.nombre}</li>`).join('');
        
        return `
            <div class="chef-order-card">
                <div class="chef-order-header">
                    <strong>#${order.id} - ${order.usuario.nombre}</strong>
                    <span class="text-muted">${dateFormatted}</span>
                </div>
                <div class="chef-order-items">
                    <ul>${itemRows}</ul>
                </div>
                <div class="chef-order-footer">
                    <span class="chef-order-total">$${order.total.toFixed(2)}</span>
                    ${nextStatus ? `
                        <button class="btn ${btnClass} btn-sm" onclick="updateOrderStatus(${order.id}, '${nextStatus}')">
                            ${nextBtnLabel} <i class="fa-solid fa-chevron-right"></i>
                        </button>
                    ` : `
                        <span class="badge badge-success" style="font-size:10px;"><i class="fa-solid fa-circle-check"></i> Listo p/ retirar</span>
                    `}
                </div>
            </div>
        `;
    }).join('');
}

// Update Order status in kitchen
window.updateOrderStatus = async function(orderId, nuevoEstado) {
    try {
        const response = await apiRequest(`/api/pedidos/${orderId}/estado?estado=${nuevoEstado}`, {
            method: 'PATCH'
        });
        
        if (!response.ok) throw new Error('No se pudo actualizar el estado.');
        
        showToast(`Pedido #${orderId} actualizado a ${nuevoEstado}.`);
        loadChefOrders();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Chef - Load menu for edit
async function loadChefProducts() {
    try {
        const response = await apiRequest('/api/productos');
        if (!response.ok) throw new Error('No se pudieron cargar los productos.');
        
        const products = await response.json();
        renderChefProductsTable(products);
    } catch (err) {
        showToast(err.message, 'error');
    }
}

function renderChefProductsTable(products) {
    if (!products.length) {
        elements.chefProductsTableBody.innerHTML = '<tr><td colspan="4" class="text-muted text-center" style="padding:30px;">No hay productos registrados.</td></tr>';
        return;
    }
    
    elements.chefProductsTableBody.innerHTML = products.map(p => `
        <tr>
            <td>
                <span style="font-size: 20px; margin-right: 8px;">${getFoodIcon(p.nombre)}</span>
                <strong>${p.nombre}</strong>
            </td>
            <td>$${p.precio.toFixed(2)}</td>
            <td>
                <label class="switch">
                    <input type="checkbox" ${p.disponibilidad ? 'checked' : ''} onchange="toggleProductAvailability(${p.id}, this.checked)">
                    <span class="slider"></span>
                </label>
            </td>
            <td>
                <button class="btn btn-outline btn-sm" style="color:var(--danger); border-color:rgba(255,118,117,0.2);" onclick="deleteProduct(${p.id})">
                    <i class="fa-solid fa-trash-can"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

window.toggleProductAvailability = async function(productId, checked) {
    try {
        // En hexagonal, cargamos y guardamos el producto
        const getResp = await apiRequest(`/api/productos/${productId}`);
        if (!getResp.ok) throw new Error('No se encontró el producto.');
        const prod = await getResp.json();
        
        prod.disponibilidad = checked;
        
        const putResp = await apiRequest(`/api/productos/${productId}`, {
            method: 'PUT',
            body: JSON.stringify(prod)
        });
        
        if (!putResp.ok) throw new Error('No se pudo guardar la disponibilidad.');
        
        showToast(`Disponibilidad de "${prod.nombre}" actualizada.`);
    } catch (err) {
        showToast(err.message, 'error');
        loadChefProducts(); // revert switch on UI
    }
}

// Add new product
async function handleAddProduct(e) {
    e.preventDefault();
    const nombre = document.getElementById('prod-nombre').value;
    const precio = parseFloat(document.getElementById('prod-precio').value);
    const disponibilidad = document.getElementById('prod-dispo').checked;
    const imagenUrl = document.getElementById('prod-imagen').value || null;
    
    try {
        const response = await apiRequest('/api/productos', {
            method: 'POST',
            body: JSON.stringify({ nombre, precio, disponibilidad, imagenUrl })
        });
        
        if (!response.ok) throw new Error('Error al registrar plato.');
        
        showToast(`Plato "${nombre}" agregado al menú.`);
        elements.formAddProduct.reset();
        loadChefProducts();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Delete product
window.deleteProduct = async function(productId) {
    if (!confirm('¿Estás seguro de eliminar este producto? Esto no afectará los pedidos ya existentes.')) return;
    
    try {
        const response = await apiRequest(`/api/productos/${productId}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Error al eliminar producto.');
        
        showToast('Producto eliminado.');
        loadChefProducts();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Chef - Load Analytics from Python relay
async function loadChefAnalytics() {
    try {
        const response = await apiRequest('/api/pedidos/analytics');
        if (!response.ok) throw new Error('Error al obtener datos analíticos.');
        
        const report = await response.json();
        
        if (report.error) {
            // Mostrar error amigable de microservicio no conectado
            elements.kpiEarnings.textContent = "$0.00";
            elements.kpiOrdersCount.textContent = "0";
            elements.kpiStarDish.textContent = "Sin conexión";
            elements.kpiPeakHour.textContent = "Sin conexión";
            
            showToast(report.error, 'warning');
            
            // Render blank/fallback charts
            renderCharts([], {});
            return;
        }
        
        // Rellenar KPIs
        elements.kpiEarnings.textContent = `$${report.total_ventas.toFixed(2)}`;
        elements.kpiOrdersCount.textContent = report.total_pedidos;
        
        const topDishes = report.platos_mas_vendidos || [];
        elements.kpiStarDish.textContent = topDishes.length > 0 ? topDishes[0].producto : 'Ninguno';
        
        // Determinar hora mas concurrida
        const hoursObj = report.horas_pico || {};
        let peakHour = '-';
        let maxCount = 0;
        for (const [hour, count] of Object.entries(hoursObj)) {
            if (count > maxCount) {
                maxCount = count;
                peakHour = hour;
            }
        }
        elements.kpiPeakHour.textContent = maxCount > 0 ? `${peakHour} (${maxCount} ped.)` : '-';
        
        // Renderizar graficos Chart.js
        renderCharts(topDishes, hoursObj);
        
    } catch (err) {
        showToast(err.message, 'error');
    }
}

// Render Chart.js visual graphics
function renderCharts(dishesData, hoursData) {
    const dishesCanvas = document.getElementById('chart-dishes');
    const hoursCanvas = document.getElementById('chart-hours');
    
    // Destruir graficos previos si existen
    if (state.charts.dishes) state.charts.dishes.destroy();
    if (state.charts.hours) state.charts.hours.destroy();
    
    // Chart 1: Platos mas vendidos
    const labelsDishes = dishesData.map(d => d.producto);
    const countsDishes = dishesData.map(d => d.cantidad);
    
    state.charts.dishes = new Chart(dishesCanvas, {
        type: 'bar',
        data: {
            labels: labelsDishes.length > 0 ? labelsDishes : ['Sin datos'],
            datasets: [{
                label: 'Unidades Vendidas',
                data: countsDishes.length > 0 ? countsDishes : [0],
                backgroundColor: 'rgba(255, 112, 67, 0.65)',
                borderColor: 'rgba(255, 112, 67, 1)',
                borderWidth: 1.5,
                borderRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { color: 'rgba(255, 255, 255, 0.05)' },
                    ticks: { color: 'var(--text-muted)', stepSize: 1 }
                },
                x: {
                    grid: { display: false },
                    ticks: { color: 'var(--text-muted)' }
                }
            }
        }
    });
    
    // Chart 2: Horas Pico Line Chart
    const hoursKeys = Object.keys(hoursData);
    const hoursValues = Object.values(hoursData);
    
    state.charts.hours = new Chart(hoursCanvas, {
        type: 'line',
        data: {
            labels: hoursKeys.length > 0 ? hoursKeys : ['Sin datos'],
            datasets: [{
                label: 'Cantidad de Pedidos',
                data: hoursValues.length > 0 ? hoursValues : [0],
                borderColor: 'rgba(108, 92, 231, 1)',
                backgroundColor: 'rgba(108, 92, 231, 0.15)',
                borderWidth: 3,
                tension: 0.4,
                fill: true,
                pointBackgroundColor: 'rgba(108, 92, 231, 1)',
                pointRadius: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { color: 'rgba(255, 255, 255, 0.05)' },
                    ticks: { color: 'var(--text-muted)', stepSize: 1 }
                },
                x: {
                    grid: { display: false },
                    ticks: { color: 'var(--text-muted)' }
                }
            }
        }
    });
}

// Auto Refresh loops (Student / Cook monitors status changes every 8s)
setInterval(() => {
    if (state.token && state.user) {
        if (state.user.rol === 'ESTUDIANTE') {
            loadStudentOrders();
        } else if (state.user.rol === 'COCINERO' && elements.chefOrdersPanel.classList.contains('active')) {
            loadChefOrders();
        }
    }
}, 8000);
