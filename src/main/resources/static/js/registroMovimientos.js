// Cargar el header desde header.html
        document.addEventListener("DOMContentLoaded", function () {
    fetch("header.html")
        .then(response => response.text())
        .then(data => {
            document.getElementById("header").innerHTML = data;
        })
        .catch(error => console.error("Error cargando el header:", error));
});
function cerrarSesion() {
        localStorage.removeItem("token"); // Borra el token
        localStorage.removeItem("carrito"); // Borra el carrito
        sessionStorage.clear(); // Limpia la sesión
        window.location.href = "index.html"; // Redirige al login
    }
async function cargarHistorial() {
    try {
        const response = await fetch('/api/historial-accesorios');
        const historial = await response.json();
        const tbody = document.querySelector('#historialTable tbody');
        tbody.innerHTML = '';

        historial.forEach(item => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${item.idHistorialAccesorio}</td>
                <td>${item.accion}</td>
                <td>${item.nombreAccesorio}</td>
                <td>${new Date(item.fecha).toLocaleString()}</td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error("Error al cargar el historial:", error);
    }
}

function descargarPDF(url) {
    window.location.href = url;
}

// Cargar el historial cuando la página se cargue
document.addEventListener("DOMContentLoaded", cargarHistorial);

