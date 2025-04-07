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
}/*
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
}*//*
function solicitarGarantia(idFactura) {
    // Buscar la factura seleccionada en la tabla ya cargada
    fetch(API_URL, {
        method: "GET",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json"
        }
    })
    .then(response => response.json())
    .then(facturas => {
        const factura = facturas.find(f => f.idFactura === idFactura);

        if (!factura) {
            alert("❌ No se encontró la factura.");
            return;
        }

        const fechaFactura = new Date(factura.fecha);
        const fechaActual = new Date();

        // Calcular la diferencia en meses
        const mesesDiferencia = (fechaActual.getFullYear() - fechaFactura.getFullYear()) * 12 +
                                (fechaActual.getMonth() - fechaFactura.getMonth());

        if (mesesDiferencia > 3 || (mesesDiferencia === 3 && fechaActual.getDate() > fechaFactura.getDate())) {
            alert("❌ No se puede solicitar garantía. Han pasado más de 3 meses desde la compra.");
            return;
        }

        // Si pasa la validación, se hace la solicitud
        fetch(GARANTIA_URL, {
            method: "POST",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                factura: { idFactura: idFactura },
                estado: "pendiente"
            })
        })
        .then(response => {
            if (!response.ok) throw new Error("Error al registrar la garantía");
            return response.json();
        })
        .then(data => alert("✅ Solicitud de garantía enviada correctamente."))
        .catch(error => console.error("❌ Error al solicitar garantía:", error));
    })
    .catch(error => console.error("❌ Error al validar fecha:", error));
}*/
function solicitarGarantia(idFactura) {
    // Primero consultamos las garantías existentes
    fetch(GARANTIA_URL, {
        method: "GET",
        headers: {
            "Authorization": token
        }
    })
    .then(res => res.json())
    .then(garantias => {
        const existe = garantias.find(g =>
            g.factura?.idFactura === idFactura &&
            ["pendiente", "garantia", "devolucion", "rechazada"].includes(g.estado)
        );

        if (existe) {
            alert("❌ Ya existe una solicitud de garantía o devolución para esta factura.");
            return;
        }

        // Si no existe una garantía previa, entonces validamos la fecha de la factura
        return fetch(API_URL, {
            method: "GET",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            }
        })
        .then(response => response.json())
        .then(facturas => {
            const factura = facturas.find(f => f.idFactura === idFactura);

            if (!factura) {
                alert("❌ No se encontró la factura.");
                return;
            }

            const fechaFactura = new Date(factura.fecha);
            const fechaActual = new Date();

            const mesesDiferencia = (fechaActual.getFullYear() - fechaFactura.getFullYear()) * 12 +
                                    (fechaActual.getMonth() - fechaFactura.getMonth());

            if (mesesDiferencia > 3 || (mesesDiferencia === 3 && fechaActual.getDate() > fechaFactura.getDate())) {
                alert("❌ No se puede solicitar garantía. Han pasado más de 3 meses desde la compra.");
                return;
            }

            // Finalmente se realiza la solicitud
            fetch(GARANTIA_URL, {
                method: "POST",
                headers: {
                    "Authorization": token,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    factura: { idFactura: idFactura },
                    estado: "pendiente"
                })
            })
            .then(response => {
                if (!response.ok) throw new Error("Error al registrar la garantía");
                return response.json();
            })
            .then(data => alert("✅ Solicitud de garantía enviada correctamente."))
            .catch(error => console.error("❌ Error al solicitar garantía:", error));
        });
    })
    .catch(error => console.error("❌ Error al validar existencia de garantía:", error));
}




