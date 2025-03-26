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
$(document).ready(function () {
    cargarOrdenes();
});

async function cargarOrdenes() {
    try {
        const response = await fetch('/api/ordenes', {
            method: 'GET',
            headers: getHeaders()
        });
        const ordenes = await response.json();

        if (!Array.isArray(ordenes)) throw new Error("Formato de datos inválido");

        mostrarOrdenes(ordenes);
    } catch (error) {
        console.error("❌ Error al obtener órdenes:", error);
        alert("No se pudieron cargar las órdenes.");
    }
}

function mostrarOrdenes(ordenes) {
    const tabla = document.getElementById("tabla-ordenes");
    tabla.innerHTML = "";

    ordenes.forEach(orden => {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${orden.idOrdenCompra}</td>
            <td>${orden.vendedor}</td>
            <td>${new Date(orden.fecha).toLocaleDateString()}</td>
            <td>$${orden.precioVenta.toFixed(2)}</td>
            <td>$${orden.subtotal.toFixed(2)}</td>
            <td>$${orden.descuento.toFixed(2)}</td>
            <td>$${orden.impuesto.toFixed(2)}</td>
            <td>$${orden.total.toFixed(2)}</td>
            <td>${orden.usuario ? orden.usuario.id : "-"}</td>
            <td>${orden.cliente ? orden.cliente.idCliente : "-"}</td>
        `;
        tabla.appendChild(fila);
    });
}

function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}

