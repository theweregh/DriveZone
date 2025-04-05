/*$(document).ready(function() {
    $("#header").load("headerCliente.html", function() {
        console.log("✅ Header cargado correctamente.");
    });
});
const API_URL = "/api/facturas/mis-facturas";
const DEVOLUCION_URL = "/api/devoluciones";
const token = localStorage.getItem("token"); // Obtener el JWT del usuario

document.addEventListener("DOMContentLoaded", () => {
    if (!token) {
        alert("No estás autenticado.");
        return;
    }

    fetchFacturas();
});

function fetchFacturas() {
    fetch(API_URL, {
        method: "GET",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json"
        }
    })
    .then(response => response.json())
    .then(facturas => mostrarFacturas(facturas))
    .catch(error => console.error("Error al obtener facturas:", error));
}

function mostrarFacturas(facturas) {
    const tabla = document.getElementById("facturasTable");
    tabla.innerHTML = "";

    facturas.forEach(factura => {
        const fila = document.createElement("tr");

        fila.innerHTML = `
            <td>${factura.idFactura}</td>
            <td>${factura.fecha}</td>
            <td>${factura.total}</td>
            <td>
                <button onclick="solicitarDevolucion(${factura.id})">Solicitar Garantía</button>
            </td>
        `;

        tabla.appendChild(fila);
    });
}

function solicitarDevolucion(idFactura) {
    fetch(DEVOLUCION_URL, {
        method: "POST",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ idFactura })
    })
    .then(response => response.json())
    .then(data => alert(data.mensaje))
    .catch(error => console.error("Error al solicitar devolución:", error));
}*/
$(document).ready(function () {
    $("#header").load("headerCliente.html", function () {
        console.log("✅ Header cargado correctamente.");
    });
});

const API_URL = "/api/facturas/mis-facturas"; // Obtener facturas del cliente
const GARANTIA_URL = "/api/garantias"; // Endpoint correcto para garantía
const token = localStorage.getItem("token"); // Obtener el JWT del usuario

document.addEventListener("DOMContentLoaded", () => {
    if (!token) {
        alert("No estás autenticado.");
        return;
    }

    fetchFacturas();
});

function fetchFacturas() {
    fetch(API_URL, {
        method: "GET",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json"
        }
    })
    .then(response => response.json())
    .then(facturas => mostrarFacturas(facturas))
    .catch(error => console.error("❌ Error al obtener facturas:", error));
}

function mostrarFacturas(facturas) {
    const tabla = document.getElementById("facturasTable");
    tabla.innerHTML = "";

    facturas.forEach(factura => {
        const fila = document.createElement("tr");

        fila.innerHTML = `
            <td>${factura.idFactura}</td>
            <td>${factura.fecha}</td>
            <td>${factura.total}</td>
            <td>
                <button onclick="solicitarGarantia(${factura.idFactura})">
                    Solicitar Garantía
                </button>
            </td>
        `;

        tabla.appendChild(fila);
    });
}
/*
function solicitarGarantia(idFactura) {
    fetch(GARANTIA_URL, {
        method: "POST",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            idFactura,
            estado: "pendiente",
            motivo
        })
    })
    .then(response => {
        if (!response.ok) throw new Error("Error al registrar la garantía");
        return response.json();
    })
    .then(data => alert("✅ Solicitud de garantía enviada correctamente."))
    .catch(error => console.error("❌ Error al solicitar garantía:", error));
}*/
function solicitarGarantia(idFactura) {
    fetch(GARANTIA_URL, {
        method: "POST",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            factura: {
                idFactura: idFactura
            },
            estado: "pendiente"
        })
    })
    .then(response => {
        if (!response.ok) throw new Error("Error al registrar la garantía");
        return response.json();
    })
    .then(data => alert("✅ Solicitud de garantía enviada correctamente."))
    .catch(error => console.error("❌ Error al solicitar garantía:", error));
}



