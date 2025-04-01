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
document.addEventListener("DOMContentLoaded", function () {
    cargarFacturas();

    document.getElementById("exportarPdf").addEventListener("click", function () {
        exportarAPdf();
    });
});

function cargarFacturas() {
    fetch("http://localhost:8080/api/facturas") // Ajusta la URL según tu API
        .then(response => response.json())
        .then(data => {
            let totalGanancias = 0;
            let tablaBody = document.querySelector("#tablaGanancias tbody");
            tablaBody.innerHTML = ""; // Limpiar contenido anterior

            data.forEach(factura => {
                let fila = document.createElement("tr");

                fila.innerHTML = `
                    <td>${factura.idFactura}</td>
                    <td>$${factura.subtotal.toFixed(2)}</td>
                    <td>$${factura.total.toFixed(2)}</td>
                `;

                tablaBody.appendChild(fila);
                totalGanancias += factura.total;
            });

            document.getElementById("totalGanancias").innerText = `$${totalGanancias.toFixed(2)}`;
        })
        .catch(error => console.error("Error cargando facturas:", error));
}

function exportarAPdf() {
    const { jsPDF } = window.jspdf;
    let doc = new jsPDF();
    doc.text("Reporte de Ganancias", 20, 10);

    let filas = [];
    document.querySelectorAll("#tablaGanancias tbody tr").forEach(row => {
        let cols = row.querySelectorAll("td");
        filas.push([
            cols[0].textContent,
            cols[1].textContent,
            cols[2].textContent
        ]);
    });

    // Agregar tabla al PDF
    doc.autoTable({
        head: [["ID Factura", "Subtotal", "Total"]],
        body: filas,
        startY: 20
    });

    let finalY = doc.autoTable.previous.finalY || 30; // Ajuste para evitar error
    let totalGanancias = document.getElementById("totalGanancias").innerText;
    doc.text(`Total de Ganancias: ${totalGanancias}`, 20, finalY + 10);

    doc.save("reporte_ganancias.pdf");
}

