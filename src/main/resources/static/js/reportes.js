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
    fetch("/api/facturas") // Ajusta la URL según tu API
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
    const doc = new jsPDF();

    // 🖼 Logo
    const imagenRuta = "/img/DriveZone.png"; // Ruta del logo

    convertirImagenABase64(imagenRuta).then(imagenBase64 => {
        if (imagenBase64) {
            doc.addImage(imagenBase64, "PNG", 15, 10, 30, 30);
        }

        // 📝 Título
        doc.setFontSize(18);
        doc.text("Reporte de Ganancias", 105, 20, null, null, "center");

        // 📅 Fecha
        const fechaHoy = new Date().toLocaleDateString();
        doc.setFontSize(11);
        doc.text(`Fecha del reporte: ${fechaHoy}`, 105, 30, null, null, "center");

        // 🧾 Tabla
        let filas = [];
        document.querySelectorAll("#tablaGanancias tbody tr").forEach(row => {
            let cols = row.querySelectorAll("td");
            filas.push([
                cols[0].textContent,
                cols[1].textContent,
                cols[2].textContent
            ]);
        });

        doc.autoTable({
            startY: 45,
            head: [["ID Factura", "Subtotal", "Total"]],
            body: filas,
            theme: "grid",
            styles: {
                halign: "center",
                valign: "middle",
                fontSize: 10
            },
            headStyles: {
                fillColor: [40, 167, 69], // Verde
                textColor: 255,
                fontStyle: "bold"
            }
        });

        let finalY = doc.lastAutoTable.finalY || 30;
        let totalGanancias = document.getElementById("totalGanancias").innerText;
        doc.setFontSize(12);
        doc.text(`Total de Ganancias: ${totalGanancias}`, 15, finalY + 15);

        doc.save("Reporte_Ganancias.pdf");
    });
}

// 🧰 Función utilitaria para convertir imagen a Base64
function convertirImagenABase64(url) {
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();
        xhr.onload = function () {
            let reader = new FileReader();
            reader.onloadend = function () {
                resolve(reader.result);
            }
            reader.readAsDataURL(xhr.response);
        };
        xhr.onerror = () => reject("Error al cargar la imagen");
        xhr.open("GET", url);
        xhr.responseType = "blob";
        xhr.send();
    });
}


