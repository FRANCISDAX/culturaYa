// Toggle sidebar en móvil y tablet
function toggleMobileSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const overlay = document.querySelector('.sidebar-overlay');
    
    if (window.innerWidth <= 768) {
        // Comportamiento para móvil
        sidebar.classList.toggle('mobile-open');
        
        if (sidebar.classList.contains('mobile-open')) {
            createOverlay();
        } else {
            removeOverlay();
        }
    } else if (window.innerWidth <= 992) {
        // Comportamiento para tablet
        sidebar.classList.toggle('collapsed');
    }
}

// Manejar clic en enlaces del sidebar (cierra sidebar en móvil)
function handleNavClick() {
    if (window.innerWidth <= 768) {
        toggleMobileSidebar();
    }
}

// Crear overlay para móvil
function createOverlay() {
    // Remover overlay existente si hay uno
    removeOverlay();
    
    const overlay = document.createElement('div');
    overlay.className = 'sidebar-overlay';
    overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.5);
        z-index: 999;
        backdrop-filter: blur(2px);
    `;
    overlay.onclick = toggleMobileSidebar;
    document.body.appendChild(overlay);
    
    // Prevenir scroll del body cuando el sidebar está abierto
    document.body.style.overflow = 'hidden';
}

function removeOverlay() {
    const overlay = document.querySelector('.sidebar-overlay');
    if (overlay) {
        overlay.remove();
    }
    
    // Restaurar scroll del body
    document.body.style.overflow = '';
}

// Cerrar sidebar al hacer resize si estamos en desktop
window.addEventListener('resize', function() {
    const sidebar = document.querySelector('.sidebar');
    
    if (window.innerWidth > 768) {
        sidebar.classList.remove('mobile-open');
        removeOverlay();
    }
    
    if (window.innerWidth > 992) {
        sidebar.classList.remove('collapsed');
    }
});

// Cerrar sidebar con tecla Escape
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        const sidebar = document.querySelector('.sidebar');
        if (sidebar.classList.contains('mobile-open')) {
            toggleMobileSidebar();
        }
    }
});

// Inicializar al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    // Asegurar que el sidebar esté cerrado en móvil al cargar
    if (window.innerWidth <= 768) {
        const sidebar = document.querySelector('.sidebar');
        sidebar.classList.remove('mobile-open');
        removeOverlay();
    }
});