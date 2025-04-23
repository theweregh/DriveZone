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
const GARANTIAS_URL = "/api/garantias";
const token = localStorage.getItem("token");

document.addEventListener("DOMContentLoaded", () => {
    if (!token) {
        alert("No estás autenticado.");
        return;
    }

    cargarGarantias();
});

function cargarGarantias() {
    fetch(GARANTIAS_URL, {
        headers: {
            "Authorization": token
        }
    })
    .then(res => res.json())
    .then(garantias => mostrarGarantias(garantias))
    .catch(err => console.error("❌ Error al cargar garantías:", err));
}
function mostrarGarantias(garantias) {
    const tabla = document.querySelector("#garantiasTable tbody");
    tabla.innerHTML = "";

    // ✅ Filtrar solo las que están en estado pendiente
    const garantiasPendientes = garantias.filter(g => g.estado === "pendiente");

    garantiasPendientes.forEach(g => {
        const idUsuario = g.factura?.ordenCompra?.usuario?.id || "N/A";

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${g.idGarantia}</td>
            <td>${g.factura.idFactura}</td>
            <td>${idUsuario}</td>
            <td>
                <select id="estado-${g.idGarantia}">
                    ${generarOpcionesEstado(g.estado)}
                </select>
            </td>
            <td>${g.fechaSolicitud}</td>
            <td>
                <button onclick="actualizarEstado(${g.idGarantia})">Actualizar</button>
            </td>
        `;

        tabla.appendChild(fila);
    });

    if (garantiasPendientes.length === 0) {
        tabla.innerHTML = `<tr><td colspan="6" style="text-align:center;">✅ No hay solicitudes pendientes.</td></tr>`;
    }
}

function generarOpcionesEstado(estadoActual) {
    const estados = ["pendiente","garantia", "devolucion", "rechazada"];
    return estados
        .map(est => `<option value="${est}" ${est === estadoActual ? "selected" : ""}>${est}</option>`)
        .join("");
}
function actualizarEstado(idGarantia) {
    // Primero buscamos los datos completos de la garantía para obtener la fecha de la factura
    fetch(`${GARANTIAS_URL}/${idGarantia}`, {
        method: "GET",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json"
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("Error al obtener datos de la garantía");
        return res.json();
    })
    .then(garantia => {
        const fechaFactura = new Date(garantia.factura.fecha);
        const hoy = new Date();

        const tresMesesEnMilis = 3 * 30 * 24 * 60 * 60 * 1000; // Aprox. 3 meses
        const diferencia = hoy - fechaFactura;

        if (diferencia > tresMesesEnMilis) {
            alert("⚠️ No se puede actualizar esta garantía porque la compra tiene más de 3 meses.");
            return; // ❌ No hace la petición
        }

        const nuevoEstado = document.querySelector(`#estado-${idGarantia}`).value;

        // Si no han pasado más de 3 meses, sí hacemos la actualización
        fetch(`${GARANTIAS_URL}/${idGarantia}`, {
            method: "PUT",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(nuevoEstado)
        })
        .then(res => {
            if (!res.ok) throw new Error("Error al actualizar estado");
            return res.json();
        })
        .then(() => {
            alert("✅ Estado actualizado correctamente.");
            cargarGarantias(); // refrescar
        })
        .catch(err => console.error("❌", err));
    })
    .catch(err => console.error("❌ Error al validar fecha de la garantía:", err));
}
