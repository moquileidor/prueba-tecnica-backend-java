// Funciones comunes para la aplicación

// Obtener token JWT del localStorage
function getToken() {
    return localStorage.getItem('token');
}

// Guardar token JWT en localStorage
function saveToken(token) {
    localStorage.setItem('token', token);
}

// Eliminar token JWT del localStorage
function removeToken() {
    localStorage.removeItem('token');
}

// Verificar si el usuario está autenticado
function isAuthenticated() {
    return !!getToken();
}

// Hacer petición con autenticación
async function fetchWithAuth(url, options = {}) {
    const token = getToken();
    
    if (!options.headers) {
        options.headers = {};
    }
    
    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }
    
    options.headers['Content-Type'] = 'application/json';
    
    return fetch(url, options);
}

// Cerrar sesión
function logout() {
    removeToken();
    window.location.href = '/login.html';
}

// Formatear fecha
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

console.log('App.js cargado correctamente');
