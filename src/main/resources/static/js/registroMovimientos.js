document.addEventListener("DOMContentLoaded", function () {
    cargarHistorial();
});

function cargarHistorial() {
    fetch("/api/historial-accesorios")
        .then(response => response.json())
        .then(data => {
            const tabla = document.querySelector("#historialTabla tbody");
            tabla.innerHTML = "";
            data.forEach(item => {
                const fila = `<tr>
                    <td>${item.idHistorialAccesorio}</td>
                    <td>${item.accion}</td>
                    <td>${item.nombreAccesorio}</td>
                    <td>${item.fecha}</td>
                </tr>`;
                tabla.innerHTML += fila;
            });
        })
        .catch(error => console.error("Error cargando historial:", error));
}

function descargarPDF() {
    fetch("/api/historial-accesorios/descargar-pdf")
        .then(response => response.blob())
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "historial_accesorios.pdf";
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
        })
        .catch(error => console.error("Error descargando PDF:", error));
}
