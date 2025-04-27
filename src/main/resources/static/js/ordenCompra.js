const API_URL = "/api/carrito"; // URL del backend

// 🔹 Cargar el carrito desde el backend
function cargarCarrito() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById("carritoBody");
            tbody.innerHTML = "";
            data.forEach(item => {
                const fila = `
                    <tr>
                        <td>${item.accesorio.nombre}</td>
                        <td>${item.cantidad}</td>
                        <td>${item.accesorio.precio.toFixed(2)}</td>
                        <td>${(item.cantidad * item.accesorio.precio).toFixed(2)}</td>
                        <td>
                            <button class="btn btn-danger btn-sm" onclick="eliminarProducto(${item.accesorio.id})">Eliminar</button>
                        </td>
                    </tr>`;
                tbody.innerHTML += fila;
            });
        })
        .catch(error => console.error("Error al cargar el carrito:", error));
}

// 🔹 Agregar producto al carrito
function agregarProducto() {
    const idAccesorio = parseInt(document.getElementById("idAccesorio").value);
    const cantidad = parseInt(document.getElementById("cantidadProducto").value);

    if (!idAccesorio || cantidad <= 0) {
        alert("Datos inválidos");
        return;
    }

    const producto = {
        accesorio: { id: idAccesorio },
        cantidad: cantidad
    };

    fetch(API_URL + "/agregar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(producto)
    })
    .then(response => response.text())
    .then(() => {
        cargarCarrito();
        document.getElementById("idAccesorio").value = "";
        document.getElementById("cantidadProducto").value = "";
    })
    .catch(error => console.error("Error al agregar producto:", error));
}

// 🔹 Eliminar producto del carrito
function eliminarProducto(id) {
    fetch(API_URL + `/eliminar/${id}`, { method: "DELETE" })
        .then(() => cargarCarrito())
        .catch(error => console.error("Error al eliminar producto:", error));
}

// 🔹 Procesar compra (vaciar carrito)
function procesarCompra() {
    fetch(API_URL + "/procesar", { method: "POST" })
        .then(() => {
            alert("Compra procesada con éxito");
            cargarCarrito();
        })
        .catch(error => console.error("Error al procesar la compra:", error));
}

// 🔹 Cargar el carrito al iniciar
document.addEventListener("DOMContentLoaded", cargarCarrito);