const GARANTIAS_URL = "/api/garantias";
const token = localStorage.getItem("token");

document.addEventListener("DOMContentLoaded", () => {
    if (!token) {
        alert("No estás autenticado.");
        return;
    }

    fetch(GARANTIAS_URL, {
        headers: {
            "Authorization": token
        }
    })
    .then(res => res.json())
    .then(data => procesarPerdidas(data))
    .catch(err => console.error("❌ Error al cargar pérdidas:", err));
});

function procesarPerdidas(garantias) {
    const cuerpo = document.querySelector("#tablaPerdidas tbody");
    cuerpo.innerHTML = "";

    let totalGeneral = 0;

    garantias
        .filter(g => g.estado === "garantia" || g.estado === "devolucion")
        .forEach(g => {
            const producto = g.factura?.producto?.nombre || "Producto desconocido";
            const cantidad = g.factura?.cantidad || 1;
            const subtotal = g.factura?.subtotal || 0;
            const total = g.factura?.total || 0;
            const idGarantia = g.idGarantia;
            const idFactura = g.factura?.idFactura || "N/A";
            const motivo = g.estado;

            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${idGarantia}</td>
                <td>${idFactura}</td>
                <td>${cantidad}</td>
                <td>$${subtotal.toFixed(2)}</td>
                <td>$${total.toFixed(2)}</td>
                <td>${motivo}</td>
            `;

            cuerpo.appendChild(fila);
            totalGeneral += total;
        });

    document.getElementById("totalPerdidas").textContent = totalGeneral.toFixed(2);
}

// 🔁 Conversor de imagen a base64
function convertirImagenABase64(ruta) {
    return new Promise((resolve, reject) => {
        const img = new Image();
        img.crossOrigin = "anonymous";
        img.onload = function () {
            const canvas = document.createElement("canvas");
            canvas.width = this.width;
            canvas.height = this.height;

            const ctx = canvas.getContext("2d");
            ctx.drawImage(this, 0, 0);

            const dataURL = canvas.toDataURL("image/png");
            resolve(dataURL);
        };
        img.onerror = function () {
            console.error("❌ No se pudo cargar la imagen del logo.");
            resolve(null); // opcional: dejarlo pasar sin imagen
        };
        img.src = ruta;
    });
}

document.getElementById("exportarPDF").addEventListener("click", generarReportePerdidasPDF);

function generarReportePerdidasPDF() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    // 🖼 Logo (si quieres uno)
    const imagenRuta = "/img/DriveZone.png"; // Cambia esta ruta a la de tu logo real
    convertirImagenABase64(imagenRuta).then(imagenBase64 => {
        if (imagenBase64) {
            doc.addImage(imagenBase64, "PNG", 15, 10, 30, 30);
        }

        // 📝 Título
        doc.setFontSize(18);
        doc.text("Reporte General de Pérdidas", 105, 20, null, null, "center");

        // 📅 Fecha
        const fechaHoy = new Date().toLocaleDateString();
        doc.setFontSize(11);
        doc.text(`Fecha del reporte: ${fechaHoy}`, 105, 30, null, null, "center");

        // 📊 Extraer datos de la tabla actual
        const filas = [];
        const tabla = document.querySelectorAll("#tablaPerdidas tbody tr");

        tabla.forEach(tr => {
            const celdas = Array.from(tr.querySelectorAll("td")).map(td => td.textContent.trim());
            filas.push(celdas);
        });

        // 🧾 Insertar tabla en el PDF
        doc.autoTable({
            startY: 45,
            head: [["ID Garantía", "ID Factura", "Cantidad", "Subtotal", "Total", "Motivo"]],
            body: filas,
            theme: "grid",
            styles: {
                halign: "center",
                valign: "middle",
                fontSize: 10
            },
            headStyles: {
                fillColor: [220, 53, 69], // Rojo elegante
                textColor: 255,
                fontStyle: "bold"
            }
        });

        // 💵 Total de pérdidas
        const total = document.getElementById("totalPerdidas")?.textContent || "0.00";
        doc.setFontSize(12);
        doc.text(`Total general de pérdidas: $${total}`, 15, doc.lastAutoTable.finalY + 15);

        // 💾 Guardar
        doc.save("Reporte_Perdidas.pdf");
    });
}


