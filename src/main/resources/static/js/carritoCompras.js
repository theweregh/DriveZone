/*document.addEventListener("DOMContentLoaded", function () {
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
            <td>$${accesorio.precioVenta.toLocaleString("es-CO")}</td>
            <td>
                <input type="number" value="${cantidad}" min="1"
                    onchange="actualizarCantidad(${accesorio.id}, this.value)">
            </td>
            <td>$${subtotal.toLocaleString("es-CO")}</td>
            <td>
                <button onclick="eliminarDelCarrito(${accesorio.id})">❌</button>
            </td>
        `;

        listaCarrito.appendChild(fila);
    });

    // ✅ Corrección: Solo un "$" y "COP" al final
    totalCarrito.innerText = total.toLocaleString("es-CO") + " COP";
}
// 🔹 Validar stock antes de actualizar cantidad
async function actualizarCantidad(id, nuevaCantidad) {
    if (!id || nuevaCantidad <= 0) {
        console.error("ID o cantidad inválida:", id, nuevaCantidad);
        return;
    }

    try {
        // Obtener el carrito para validar el stock disponible
        const carritoResponse = await fetch('/api/carrito', { headers: getHeaders() });
        if (!carritoResponse.ok) throw new Error("Error al obtener el carrito");

        const carrito = await carritoResponse.json();
        const item = carrito.find(producto => producto.accesorio.id === id);

        if (!item) {
            console.error(`El producto con ID ${id} no está en el carrito`);
            return;
        }

        let cantidadFinal = parseInt(nuevaCantidad, 10);

        if (cantidadFinal > item.accesorio.stock) {
            cantidadFinal = item.accesorio.stock; // Ajustar al máximo stock disponible
            alert(`No hay suficiente stock disponible. Se ajustó la cantidad a ${cantidadFinal}`);
        }

        // Actualizar el input en el frontend para reflejar la cantidad corregida
        document.querySelector(`input[onchange="actualizarCantidad(${id}, this.value)"]`).value = cantidadFinal;

        // Si la cantidad ya es la misma, no hacer la petición
        if (cantidadFinal === item.cantidad) return;

        // Si la cantidad es válida, se actualiza el carrito
        const response = await fetch(`/api/carrito/actualizar/${id}`, {
            method: 'PUT',
            headers: getHeaders(),
            body: JSON.stringify({ accesorio: { id }, cantidad: cantidadFinal })
        });

        if (!response.ok) throw new Error(`Error al actualizar cantidad de ID ${id}`);

        console.log(`Cantidad ajustada y actualizada: ${cantidadFinal}`);
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
}*/
document.addEventListener("DOMContentLoaded", function () {
    cargarCarrito();
});

// 🔹 Cargar productos en el carrito
async function cargarCarrito() {
    try {
        const response = await fetch('/api/carrito/obtener', { headers: getHeaders() });
        if (response.status === 204) {
            console.log("El carrito está vacío");
            mostrarCarrito([]);
            return;
        }
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
        if (!item.accesorio) return; // Evita errores si el accesorio es nulo

        const accesorio = item.accesorio;
        const cantidad = item.cantidad || 1;
        const subtotal = accesorio.precioVenta * cantidad;
        total += subtotal;

        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${accesorio.nombre}</td>
            <td>$${accesorio.precioVenta.toLocaleString("es-CO")}</td>
            <td>
                <input type="number" value="${cantidad}" min="1"
                    onchange="actualizarCantidad(${accesorio.id}, this.value)">
            </td>
            <td>$${subtotal.toLocaleString("es-CO")}</td>
            <td>
                <button onclick="eliminarDelCarrito(${accesorio.id})">❌</button>
            </td>
        `;
        listaCarrito.appendChild(fila);
    });

    totalCarrito.innerText = total.toLocaleString("es-CO") + " COP";
}

// 🔹 Actualizar cantidad de un producto en el carrito
async function actualizarCantidad(id, nuevaCantidad) {
    if (!id || nuevaCantidad <= 0) {
        console.error("ID o cantidad inválida:", id, nuevaCantidad);
        return;
    }

    try {
        const response = await fetch(`/api/carrito/actualizar`, {
            method: 'PUT',
            headers: getHeaders(),
            body: JSON.stringify({ accesorioId: id, cantidad: nuevaCantidad })
        });

        if (!response.ok) throw new Error(`Error al actualizar cantidad de ID ${id}`);

        console.log(`Cantidad actualizada para ID ${id}: ${nuevaCantidad}`);
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
async function procesarCompra() {
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
}

document.getElementById("procesar-compra").addEventListener("click", procesarCompra);

// 🔹 Obtener encabezados para las solicitudes
function getHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token || ''
    };
}
