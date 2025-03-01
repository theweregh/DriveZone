document.addEventListener("DOMContentLoaded", function () {
    cargarCarrito();
});

// 🔹 Cargar productos en el carrito
async function cargarCarrito() {
    try {
        const response = await fetch('/api/carrito', { headers: getHeaders() });
        if (!response.ok) throw new Error("Error al cargar el carrito");

        const carrito = await response.json();
        console.log("Carrito cargado:", carrito);
        mostrarCarrito(carrito);
    } catch (error) {
        console.error("Error en cargarCarrito:", error);
    }
}

// 🔹 Mostrar productos en la tabla del carrito
function mostrarCarrito(carrito) {
    const listaCarrito = document.getElementById("carrito-lista");
    const totalCarrito = document.getElementById("total-carrito");
    listaCarrito.innerHTML = "";
    let total = 0;

    carrito.forEach(item => {
        const accesorio = item.accesorio;
        const cantidad = item.cantidad || 1;
        const subtotal = accesorio.precioVenta * cantidad;
        total += subtotal;

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${accesorio.nombre}</td>
            <td>$${accesorio.precioVenta.toFixed(2)}</td>
            <td>
                <input type="number" value="${cantidad}" min="1"
                    onchange="actualizarCantidad(${accesorio.id}, this.value)">
            </td>
            <td>$${subtotal.toFixed(2)}</td>
            <td>
                <button onclick="eliminarDelCarrito(${accesorio.id})">❌</button>
            </td>
        `;

        listaCarrito.appendChild(fila);
    });

    totalCarrito.innerText = `$${total.toFixed(2)}`;
}

// 🔹 Actualizar cantidad de un producto en el carrito
async function actualizarCantidad(id, nuevaCantidad) {
    if (!id || nuevaCantidad <= 0) {
        console.error("ID o cantidad inválida:", id, nuevaCantidad);
        return;
    }

    try {
        const response = await fetch(`/api/carrito/actualizar/${id}`, {
            method: 'PUT',
            headers: getHeaders(),
            body: JSON.stringify({ accesorio: { id }, cantidad: parseInt(nuevaCantidad, 10) })
        });

        if (!response.ok) throw new Error(`Error al actualizar cantidad de ID ${id}`);

        console.log(`Cantidad actualizada: ${nuevaCantidad}`);
        cargarCarrito();
    } catch (error) {
        console.error("Error en actualizarCantidad:", error);
    }
}

// 🔹 Eliminar un producto del carrito
async function eliminarDelCarrito(id) {
    if (!id) {
        console.error("Intento de eliminar con ID inválido:", id);
        return;
    }

    try {
        const response = await fetch(`/api/carrito/eliminar/${id}`, {
            method: 'DELETE',
            headers: getHeaders()
        });

        if (!response.ok) throw new Error(`Error al eliminar el accesorio ${id}`);

        console.log(`Accesorio eliminado: ${id}`);
        cargarCarrito();
    } catch (error) {
        console.error("Error en eliminarDelCarrito:", error);
    }
}

// 🔹 Procesar compra
document.getElementById("procesar-compra").addEventListener("click", async function () {
    try {
        const response = await fetch('/api/carrito/procesar', {
            method: 'POST',
            headers: getHeaders()
        });

        if (!response.ok) throw new Error("Error al procesar la compra");

        alert("¡Compra realizada con éxito!");
        cargarCarrito();
    } catch (error) {
        alert("Error al procesar la compra");
        console.error("Error en procesarCompra:", error);
    }
});

// 🔹 Obtener encabezados para las solicitudes
function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token || ''
    };
}