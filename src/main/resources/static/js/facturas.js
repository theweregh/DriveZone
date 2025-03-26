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
    const API_URL = "/api/facturas";

    // Función para cargar las facturas
    function cargarFacturas() {
        $.get(API_URL, function (facturas) {
            const tabla = $("#tabla-facturas");
            tabla.empty(); // Limpiar la tabla antes de agregar datos

            facturas.forEach(factura => {
                const fila = `
                    <tr>
                        <td>${factura.idFactura}</td>
                        <td>${factura.empresaNombre}</td>
                        <td>${factura.nit}</td>
                        <td>${factura.direccion}</td>
                        <td>${factura.metodoPago}</td>
                        <td>${new Date(factura.fecha).toLocaleDateString()}</td>
                        <td>$${factura.subtotal.toFixed(2)}</td>
                        <td>$${factura.descuento.toFixed(2)}</td>
                        <td>$${factura.impuestos.toFixed(2)}</td>
                        <td><strong>$${factura.total.toFixed(2)}</strong></td>
                        <td>
                            <button class="btn btn-success btn-descargar" data-id="${factura.idFactura}">📥 Descargar</button>
                            <button class="btn btn-primary btn-enviar" data-id="${factura.idFactura}">📧 Enviar</button>
                        </td>
                    </tr>
                `;
                tabla.append(fila);
            });

            // Eventos de botones
            $(".btn-descargar").click(descargarFactura);
            $(".btn-enviar").click(enviarFactura);
        }).fail(function () {
            alert("❌ Error al cargar las facturas.");
        });
    }

    // Función para descargar la factura en PDF
    function descargarFactura() {
        const id = $(this).data("id");
        window.open(`${API_URL}/${id}/pdf`, "_blank");
    }

    // Función para enviar la factura por correo
    function enviarFactura() {
        const id = $(this).data("id");
        const email = prompt("📩 Ingresa el correo electrónico:");

        if (email) {
            $.post(`${API_URL}/${id}/enviar-correo?email=${email}`)
                .done(response => alert("✅ Factura enviada con éxito."))
                .fail(error => alert("❌ Error al enviar la factura."));
        }
    }

    // Cargar facturas al inicio
    cargarFacturas();
});
