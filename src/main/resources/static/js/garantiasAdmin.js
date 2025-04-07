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
    const nuevoEstado = document.querySelector(`#estado-${idGarantia}`).value;

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
}
