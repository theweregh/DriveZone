document.addEventListener("DOMContentLoaded", async () => {
    const tablaBody = document.querySelector("#tablaInventario tbody");

    const token = localStorage.getItem("token"); // Asegúrate de tenerlo almacenado
    const response = await fetch("/api/accesorio", {
        headers: { Authorization: token }
    });

    const accesorios = await response.json();

    accesorios.forEach(item => {
        const fila = document.createElement("tr");

        // Lógica para estado de stock
        let estado = "";
        let clase = "";

        if (item.stock === 0) {
            estado = "Agotado";
            clase = "agotado";
        } else if (item.stock <= 5) {
            estado = "Stock Crítico";
            clase = "critico";
        } else {
            estado = "Disponible";
        }

        fila.className = clase;
        fila.innerHTML = `
            <td>${item.id}</td>
            <td>${item.nombre}</td>
            <td>${item.stock}</td>
            <td>${estado}</td>
        `;
        tablaBody.appendChild(fila);
    });
});

// 🧾 Exportar a PDF con logo y estilo elegante
document.getElementById("exportarPDF").addEventListener("click", generarReporteInventarioPDF);

function generarReporteInventarioPDF() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    const imagenRuta = "/img/DriveZone.png";
    convertirImagenABase64(imagenRuta).then(imagenBase64 => {
        if (imagenBase64) {
            doc.addImage(imagenBase64, "PNG", 15, 10, 30, 30); // 🖼 Logo bien proporcionado
        }

        // 📝 Título centrado
        doc.setFontSize(18);
        doc.text("Informe de Inventario – DriveZone", 105, 20, null, null, "center");

        // 📅 Fecha
        const fechaHoy = new Date().toLocaleDateString();
        doc.setFontSize(11);
        doc.text(`Fecha del reporte: ${fechaHoy}`, 105, 30, null, null, "center");

        // 📊 Extraer datos de la tabla
        const filas = [];
        const tabla = document.querySelectorAll("#tablaInventario tbody tr");

        tabla.forEach(tr => {
            const celdas = Array.from(tr.querySelectorAll("td")).map(td => td.textContent.trim());
            filas.push(celdas);
        });

        // 🧾 Insertar tabla en el PDF
        doc.autoTable({
            startY: 45,
            head: [["ID", "Nombre", "Stock", "Estado"]],
            body: filas,
            theme: "grid",
            styles: {
                halign: "center",
                valign: "middle",
                fontSize: 10
            },
            headStyles: {
                fillColor: [40, 167, 69], // Verde DriveZone
                textColor: 255,
                fontStyle: "bold"
            }
        });

        // 💾 Guardar
        doc.save("Informe_Inventario.pdf");
    });
}

// 🛠 Convertir imagen a Base64
function convertirImagenABase64(url) {
    return fetch(url)
        .then(res => res.blob())
        .then(blob => new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onloadend = () => resolve(reader.result);
            reader.onerror = reject;
            reader.readAsDataURL(blob);
        }))
        .catch(() => null);
}


